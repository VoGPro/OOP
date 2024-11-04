package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCarRepository {
    protected List<Car> cars;
    protected final String filePath;
    protected final ObjectMapper objectMapper;

    protected AbstractCarRepository(String filePath, ObjectMapper objectMapper) {
        this.filePath = filePath;
        this.objectMapper = objectMapper;
        this.cars = new ArrayList<>();
        loadFromFile();
    }

    protected abstract void loadFromFile();

    protected abstract void saveToFile();

    public Car getById(int car_id) {
        return cars.stream()
                .filter(car -> car.getCarId() == car_id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Автомобиль с ID " + car_id + " не найден"));
    }

    public List<Car> get_k_n_short_list(int k, int n, String sortField) {
        if (k < 0 || n <= 0) {
            throw new IllegalArgumentException("k и n должны быть положительными числами");
        }

        Comparator<Car> comparator = switch (sortField.toLowerCase()) {
            case "car_id" -> Comparator.comparingInt(Car::getCarId);
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
        saveToFile();
    }

    protected int generateNewCarId() {
        return cars.stream()
                .mapToInt(Car::getCarId)
                .max()
                .orElse(-1) + 1;
    }

    public void update(Car updatedCar) {
        int index = findIndexById(updatedCar.getCarId());
        if (index != -1) {
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

    public int get_count() {
        return cars.size();
    }

    protected int findIndexById(int car_id) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getCarId() == car_id) {
                return i;
            }
        }
        return -1;
    }
}