package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CarFileRepository {
    private List<Car> cars;
    private final String filePath;
    private final ObjectMapper objectMapper;

    public CarFileRepository(String filePath, IFileStrategy fileStrategy) {
        this.filePath = filePath;
        this.objectMapper = fileStrategy.createObjectMapper();
        this.cars = new ArrayList<>();
        loadFromFile();
    }

    void loadFromFile() {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                cars = objectMapper.readValue(file, new TypeReference<List<Car>>() {});
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при чтении файла", e);
            }
        }
    }

    void saveToFile() {
        try {
            objectMapper.writeValue(new File(filePath), cars);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при записи в файл", e);
        }
    }

    public Car getById(int car_id) {
        return cars.stream()
                .filter(car -> car.getCarId() == car_id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Автомобиль с ID " + car_id + " не найден"));
    }

    public List<Car> get_k_n_short_list(int k, int n, IFilterCriteria filterCriteria, String sortField) {
        if (k < 0 || n <= 0) {
            throw new IllegalArgumentException("k и n должны быть положительными числами");
        }

        Comparator<Car> comparator = switch (sortField.toLowerCase()) {
            case "carid" -> Comparator.comparingInt(Car::getCarId);
            case "brand" -> Comparator.comparing(Car::getBrand);
            case "model" -> Comparator.comparing(Car::getModel);
            case "year" -> Comparator.comparingInt(Car::getYear);
            case "price" -> Comparator.comparingDouble(Car::getPrice);
            case "type" -> Comparator.comparing(Car::getType);
            case "vin" -> Comparator.comparing(Car::getVin);
            default -> throw new IllegalArgumentException("Неверное поле для сортировки: " + sortField);
        };

        List<Car> filteredList = cars.stream()
                .filter(filterCriteria::matches)
                .sorted(comparator)
                .collect(Collectors.toList());

        int startIndex = k * n;
        if (startIndex >= filteredList.size()) {
            return new ArrayList<>();
        }

        int endIndex = Math.min(startIndex + n, filteredList.size());
        return filteredList.subList(startIndex, endIndex);
    }

    public void add(Car car) {
        boolean vinExists = cars.stream()
                .anyMatch(existingCar -> existingCar.getVin().equalsIgnoreCase(car.getVin()));

        if (vinExists) {
            throw new IllegalArgumentException(
                    String.format("Автомобиль с VIN номером %s уже существует в базе", car.getVin())
            );
        }

        int newCarId = generateNewCarId();
        car.setCarId(newCarId);
        cars.add(car);
        saveToFile();
    }

    private int generateNewCarId() {
        return cars.stream()
                .mapToInt(Car::getCarId)
                .max()
                .orElse(-1) + 1;
    }

    public void update(Car updatedCar) {
        int index = findIndexById(updatedCar.getCarId());
        if (index != -1) {

            boolean vinExists = cars.stream()
                    .anyMatch(existingCar ->
                            existingCar.getCarId() != updatedCar.getCarId() && // Исключаем текущий автомобиль из проверки
                                    existingCar.getVin().equalsIgnoreCase(updatedCar.getVin())
                    );

            if (vinExists) {
                throw new IllegalArgumentException(
                        String.format("Автомобиль с VIN номером %s уже существует в базе", updatedCar.getVin())
                );
            }

            cars.set(index, updatedCar);
            saveToFile();
        } else {
            throw new IllegalArgumentException("Автомобиль с ID " + updatedCar.getCarId() + " не найден");
        }
    }

    public void delete(int car_id) {
        int index = findIndexById(car_id);
        if (index != -1) {
            cars.remove(index);
            saveToFile();
        } else {
            throw new IllegalArgumentException("Автомобиль с ID " + car_id + " не найден");
        }
    }

    public int get_count(IFilterCriteria filterCriteria) {
        return (int) cars.stream()
                .filter(filterCriteria::matches)
                .count();
    }

    private int findIndexById(int car_id) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getCarId() == car_id) {
                return i;
            }
        }
        return -1;
    }
}