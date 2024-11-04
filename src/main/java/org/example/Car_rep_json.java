package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Car_rep_json extends AbstractCarRepository {
    public Car_rep_json(String jsonFilePath) {
        super(jsonFilePath, new ObjectMapper());
    }

    @Override
    protected void loadFromFile() {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                cars = objectMapper.readValue(file, new TypeReference<List<Car>>() {});
            } catch (IOException e) {
                throw new RuntimeException("Ошибка при чтении JSON файла", e);
            }
        }
    }

    @Override
    protected void saveToFile() {
        try {
            objectMapper.writeValue(new File(filePath), cars);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при записи в JSON файл", e);
        }
    }
}