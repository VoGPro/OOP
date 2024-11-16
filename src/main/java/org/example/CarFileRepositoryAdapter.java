package org.example;

import java.util.List;

public class CarFileRepositoryAdapter implements ICarRepository {
    CarFileRepository carFileRepository;

    public CarFileRepositoryAdapter(CarFileRepository carFileRepository) {
        this.carFileRepository = carFileRepository;
    }

    public Car getById(int car_id) {
        return carFileRepository.getById(car_id);
    }

    public List<Car> get_k_n_short_list(int k, int n, IFilterCriteria filterCriteria, String sortField) {
        return carFileRepository.get_k_n_short_list(k, n, filterCriteria, sortField);
    }

    public void add(Car car) {
        carFileRepository.add(car);
    }

    public void update(Car car) {
        carFileRepository.update(car);
    }

    public void delete(int car_id) {
        carFileRepository.delete(car_id);
    }

    public int get_count(IFilterCriteria filterCriteria) {
        return carFileRepository.get_count(filterCriteria);
    }
}
