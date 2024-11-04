package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DbConfig {
    private static final String CONFIG_FILE = "src/main/resources/database.properties";
    private final Properties properties;

    public DbConfig() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке конфигурации БД", e);
        }
    }

    public String getUrl() {
        return properties.getProperty("db.url");
    }

    public String getUser() {
        return properties.getProperty("db.user");
    }

    public String getPassword() {
        return properties.getProperty("db.password");
    }
}