package org.example;

import java.util.List;

public class CarRepositoryAdapter implements ICarRepository {
    private final AbstractCarRepository abstractCarRepository;

    public CarRepositoryAdapter(AbstractCarRepository abstractCarRepository) {
        this.abstractCarRepository = abstractCarRepository;
    }

    public Car getById(int car_id) {
        return abstractCarRepository.getById(car_id);
    }

    public List<Car> get_k_n_short_list(int k, int n, String sortField) {
        return abstractCarRepository.get_k_n_short_list(k, n, sortField);
    }

    public void add(Car car) {
        abstractCarRepository.add(car);
    }

    public void update(Car car) {
        abstractCarRepository.update(car);
    }

    public void delete(int car_id) {
        abstractCarRepository.delete(car_id);
    }

    public int get_count() {
        return abstractCarRepository.get_count();
    }
}
