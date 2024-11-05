package org.example;

import java.util.List;

public interface ICarRepository {
    Car getById(int car_id);

    List<Car> get_k_n_short_list(int k, int n, String sortField);

    void add(Car car);

    void update(Car car);

    void delete(int car_id);

    int get_count();
}
