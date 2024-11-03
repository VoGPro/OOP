package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Car_rep_yaml {
    private List<Car> cars;
    private final String yamlFilePath;
    private final ObjectMapper objectMapper;

    public Car_rep_yaml(String yamlFilePath) {
        this.yamlFilePath = yamlFilePath;
        this.objectMapper = new ObjectMapper(new YAMLFactory());
        this.cars = new ArrayList<>();
        loadFromYaml();
    }

    private void loadFromYaml() {
        File file = new File(yamlFilePath);
        if (file.exists()) {
            try {
                cars = objectMapper.readValue(file, new TypeReference<List<Car>>() {});
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при чтении YAML файла", e);
            }
        }
    }

    public void saveToYaml() {
        try {
            objectMapper.writeValue(new File(yamlFilePath), cars);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при записи в YAML файл", e);
        }
    }

    public Car getById(int carId) {
        return cars.stream()
                .filter(car -> car.getCarId() == carId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Автомобиль с ID " + carId + " не найден"));
    }

    public List<Car> get_k_n_short_list(int k, int n, String sortField) {
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

        List<Car> sortedList = cars.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        int startIndex = k * n;
        if (startIndex >= sortedList.size()) {
            return new ArrayList<>();
        }

        int endIndex = Math.min(startIndex + n, sortedList.size());
        return sortedList.subList(startIndex, endIndex);
    }

    public void add(Car car) {
        int newCarId = generateNewCarId();
        car.setCarId(newCarId);
        cars.add(car);
        saveToYaml();
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
            cars.set(index, updatedCar);
            saveToYaml();
        } else {
            throw new IllegalArgumentException("Автомобиль с ID " + updatedCar.getCarId() + " не найден");
        }
    }

    public void delete(int carId) {
        int index = findIndexById(carId);
        if (index != -1) {
            cars.remove(index);
            saveToYaml();
        } else {
            throw new IllegalArgumentException("Автомобиль с ID " + carId + " не найден");
        }
    }

    public int get_count() {
        return cars.size();
    }

    private int findIndexById(int carId) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getCarId() == carId) {
                return i;
            }
        }
        return -1;
    }
}