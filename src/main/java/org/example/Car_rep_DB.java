package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Car_rep_DB {
    private final String url;
    private final String username;
    private final String password;

    public Car_rep_DB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    // Получение объекта по CarId
    public Car getById(int carId) {
        String sql = "SELECT * FROM cars WHERE car_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractCarFromResultSet(rs);
            } else {
                throw new IllegalArgumentException("Автомобиль с ID " + carId + " не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении автомобиля", e);
        }
    }

    // Получение отсортированного подсписка
    public List<Car> get_k_n_short_list(int k, int n, String sortField) {
        if (k < 0 || n <= 0) {
            throw new IllegalArgumentException("k и n должны быть положительными числами");
        }

        // Преобразование имени поля из camelCase в snake_case для SQL
        String dbSortField = switch (sortField.toLowerCase()) {
            case "carid" -> "car_id";
            case "vin" -> "vin";
            case "brand" -> "brand";
            case "model" -> "model";
            case "year" -> "year";
            case "price" -> "price";
            case "type" -> "type";
            default -> throw new IllegalArgumentException("Неверное поле для сортировки: " + sortField);
        };

        String sql = "SELECT * FROM cars ORDER BY " + dbSortField + " LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, n);
            stmt.setInt(2, k * n);
            ResultSet rs = stmt.executeQuery();

            List<Car> cars = new ArrayList<>();
            while (rs.next()) {
                cars.add(extractCarFromResultSet(rs));
            }
            return cars;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка автомобилей", e);
        }
    }

    // Добавление нового объекта
    public void add(Car car) {
        String sql = """
            INSERT INTO cars (vin, brand, model, year, price, type)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING car_id
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, car.getVin().toUpperCase());
            stmt.setString(2, car.getBrand());
            stmt.setString(3, car.getModel());
            stmt.setInt(4, car.getYear());
            stmt.setDouble(5, car.getPrice());
            stmt.setString(6, car.getType());
            stmt.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении автомобиля", e);
        }
    }

    // Обновление существующего объекта
    public void update(Car car) {
        String sql = """
            UPDATE cars
            SET vin = ?, brand = ?, model = ?, year = ?, price = ?, type = ?
            WHERE car_id = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, car.getVin().toUpperCase());
            stmt.setString(2, car.getBrand());
            stmt.setString(3, car.getModel());
            stmt.setInt(4, car.getYear());
            stmt.setDouble(5, car.getPrice());
            stmt.setString(6, car.getType());
            stmt.setInt(7, car.getCarId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Автомобиль с ID " + car.getCarId() + " не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении автомобиля", e);
        }
    }

    // Удаление объекта
    public void delete(int carId) {
        String sql = "DELETE FROM cars WHERE car_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Автомобиль с ID " + carId + " не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении автомобиля", e);
        }
    }

    // Получение количества элементов
    public int get_count() {
        String sql = "SELECT COUNT(*) FROM cars";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при подсчете количества автомобилей", e);
        }
    }

    // Вспомогательный метод для создания подключения
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    // Вспомогательный метод для извлечения объекта Car из ResultSet
    private Car extractCarFromResultSet(ResultSet rs) throws SQLException {
        return new Car(
                rs.getInt("car_id"),
                rs.getString("vin"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getInt("year"),
                rs.getDouble("price"),
                rs.getString("type")
        );
    }
}