package org.example;

import java.util.ArrayList;
import java.util.List;

public class ObservableCarRepository implements ICarRepository, IObservableRepository {
    private final ICarRepository repository;
    private final List<IRepositoryObserver> observers = new ArrayList<>();

    public ObservableCarRepository(ICarRepository repository) {
        this.repository = repository;
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

    @Override
    public Car getById(int car_id) {
        return repository.getById(car_id);
    }

    @Override
    public List<Car> get_k_n_short_list(int k, int n, IFilterCriteria filterCriteria, String sortField) {
        return repository.get_k_n_short_list(k, n, filterCriteria, sortField);
    }

    @Override
    public void add(Car car) {
        repository.add(car);
        notifyObservers();
    }

    @Override
    public void update(Car car) {
        repository.update(car);
        notifyObservers();
    }

    @Override
    public void delete(int car_id) {
        repository.delete(car_id);
        notifyObservers();
    }

    @Override
    public int get_count(IFilterCriteria filterCriteria) {
        return repository.get_count(filterCriteria);
    }
}