package org.example;

public interface IObservableRepository {
    void addObserver(IRepositoryObserver observer);
    void removeObserver(IRepositoryObserver observer);
    void notifyObservers();
}
