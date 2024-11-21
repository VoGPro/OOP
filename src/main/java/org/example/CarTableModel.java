package org.example;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CarTableModel extends AbstractTableModel implements IRepositoryObserver {
    private final String[] columnNames = {"vin", "brand", "model"};
    private List<Car> cars = new ArrayList<>();
    private final ObservableCarRepository repository;
    private IFilterCriteria currentFilter;
    private String currentSortField;
    private int currentPage;
    private final int pageSize;
    private int totalRecords;

    public CarTableModel(ObservableCarRepository repository) {
        this.repository = repository;
        this.currentFilter = new NoFilter();
        this.currentSortField = "vin";
        this.currentPage = 0;
        this.pageSize = 10;

        // Register as observer
        repository.addObserver(this);

        refreshData();
    }

    @Override
    public void onRepositoryChanged() {
        refreshData();
    }

    public void refreshData() {
        cars = repository.get_k_n_short_list(currentPage, pageSize, currentFilter, currentSortField);
        totalRecords = repository.get_count(currentFilter);
        fireTableDataChanged();
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setFilter(IFilterCriteria filter) {
        this.currentFilter = filter;
        currentPage = 0;
        refreshData();
    }

    public void setSortField(String sortField) {
        this.currentSortField = sortField;
        refreshData();
    }

    public void nextPage() {
        if (currentPage < getTotalPages() - 1) {
            currentPage++;
            refreshData();
        }
    }

    public void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            refreshData();
        }
    }

    public boolean hasNextPage() {
        return currentPage < getTotalPages() - 1;
    }

    public boolean hasPreviousPage() {
        return currentPage > 0;
    }

    @Override
    public int getRowCount() {
        return cars.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Car car = cars.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> car.getVin();
            case 1 -> car.getBrand();
            case 2 -> car.getModel();
            default -> null;
        };
    }

    public ICarRepository getRepository() {
        return repository;
    }

    public Car getCarAt(int rowIndex) {
        return cars.get(rowIndex);
    }
}