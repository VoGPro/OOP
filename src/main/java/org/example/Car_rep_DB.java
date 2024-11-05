package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Car_rep_DB implements ICarStrategy {
    private DbConfig dbConfig;

    public Car_rep_DB() {
        this.dbConfig = dbConfig.getInstance();
    }

    public Car getById(int car_id) {
        String sql = "SELECT * FROM cars WHERE car_id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, car_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractCarFromResultSet(rs);
            } else {
                throw new IllegalArgumentException("Автомобиль с ID " + car_id + " не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении автомобиля", e);
        }
    }

    public List<Car> get_k_n_short_list(int k, int n, String sortField) {
        if (k < 0 || n <= 0) {
            throw new IllegalArgumentException("k и n должны быть положительными числами");
        }

        sortField = sortField.toLowerCase();
        List<String> carFields = List.of("car_id", "vin", "brand", "model", "year", "price", "type");
        if (!carFields.contains(sortField)) {
            throw new IllegalArgumentException("Неверное поле для сортировки: " + sortField);
        }

        String sql = "SELECT * FROM cars ORDER BY " + sortField + " LIMIT ? OFFSET ?";

        try (Connection conn = dbConfig.getConnection();
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

    public void add(Car car) {
        String sql = """
            INSERT INTO cars (vin, brand, model, year, price, type)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING car_id
            """;

        try (Connection conn = dbConfig.getConnection();
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

    public void update(Car car) {
        String sql = """
            UPDATE cars
            SET vin = ?, brand = ?, model = ?, year = ?, price = ?, type = ?
            WHERE car_id = ?
            """;

        try (Connection conn = dbConfig.getConnection();
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

    public void delete(int car_id) {
        String sql = "DELETE FROM cars WHERE car_id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, car_id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Автомобиль с ID " + car_id + " не найден");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении автомобиля", e);
        }
    }

    public int get_count() {
        String sql = "SELECT COUNT(*) FROM cars";

        try (Connection conn = dbConfig.getConnection();
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