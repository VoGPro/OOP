package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDbRepository implements ICarRepository, IObservableRepository {
    private DbConfig dbConfig;
    private final List<IRepositoryObserver> observers = new ArrayList<>();
    private IFilterCriteria currentFilter = new NoFilter();
    private String currentSortField = "car_id";
    private int pageSize = 10;

    public CarDbRepository() {
        this.dbConfig = dbConfig.getInstance();
    }

    @Override
    public void addObserver(IRepositoryObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IRepositoryObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IRepositoryObserver observer : observers) {
            observer.onRepositoryChanged();
        }
    }

    public void setFilter(IFilterCriteria filter) {
        this.currentFilter = filter;
        notifyObservers();
    }

    public void setSortField(String sortField) {
        this.currentSortField = sortField;
        notifyObservers();
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) get_count(currentFilter) / pageSize);
    }

    @Override
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

    @Override
    public List<Car> get_k_n_short_list(int k, int n, IFilterCriteria filterCriteria, String sortField) {
        if (k < 0 || n <= 0) {
            throw new IllegalArgumentException("k и n должны быть положительными числами");
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM cars WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (filterCriteria instanceof VinFilterDecorator) {
            sql.append(" AND LOWER(vin) LIKE LOWER(?)");
            params.add(((VinFilterDecorator) filterCriteria).getVin());
        }
        if (filterCriteria instanceof YearFilterDecorator) {
            sql.append(" AND year = ?");
            params.add(((YearFilterDecorator) filterCriteria).getYear());
        }
        if (filterCriteria instanceof PriceRangeFilterDecorator) {
            PriceRangeFilterDecorator priceFilter = (PriceRangeFilterDecorator) filterCriteria;
            sql.append(" AND price >= ? AND price <= ?");
            params.add(priceFilter.getMinPrice());
            params.add(priceFilter.getMaxPrice());
        }
        if (filterCriteria instanceof BrandFilterDecorator) {
            sql.append(" AND brand = ?");
            params.add(((BrandFilterDecorator) filterCriteria).getBrand());
        }
        if (filterCriteria instanceof ModelFilterDecorator) {
            sql.append(" AND model = ?");
            params.add(((ModelFilterDecorator) filterCriteria).getModel());
        }
        if (filterCriteria instanceof TypeFilterDecorator) {
            sql.append(" AND type = ?");
            params.add(((TypeFilterDecorator) filterCriteria).getType());
        }

        sql.append(" ORDER BY ").append(sortField);
        sql.append(" LIMIT ? OFFSET ?");
        params.add(n);
        params.add(k * n);

        List<Car> result = new ArrayList<>();
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Car car = extractCarFromResultSet(rs);
                    if (filterCriteria.matches(car)) {
                        result.add(car);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка автомобилей", e);
        }
        return result;
    }

    @Override
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

            notifyObservers();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении автомобиля", e);
        }
    }

    @Override
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

            notifyObservers();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении автомобиля", e);
        }
    }

    @Override
    public void delete(int car_id) {
        String sql = "DELETE FROM cars WHERE car_id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, car_id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalArgumentException("Автомобиль с ID " + car_id + " не найден");
            }

            notifyObservers();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении автомобиля", e);
        }
    }

    @Override
    public int get_count(IFilterCriteria filterCriteria) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM cars WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Добавляем условия фильтрации в SQL
        if (filterCriteria instanceof VinFilterDecorator) {
            sql.append(" AND LOWER(vin) LIKE LOWER(?)");
            params.add(((VinFilterDecorator) filterCriteria).getVin());
        }
        if (filterCriteria instanceof YearFilterDecorator) {
            sql.append(" AND year = ?");
            params.add(((YearFilterDecorator) filterCriteria).getYear());
        }
        if (filterCriteria instanceof BrandFilterDecorator) {
            sql.append(" AND LOWER(brand) = LOWER(?)");
            params.add(((BrandFilterDecorator) filterCriteria).getBrand());
        }

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при подсчете количества автомобилей", e);
        }
        return 0;
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

    public IFilterCriteria getCurrentFilter() {
        return currentFilter;
    }

    public String getCurrentSortField() {
        return currentSortField;
    }

    public int getPageSize() {
        return pageSize;
    }
}