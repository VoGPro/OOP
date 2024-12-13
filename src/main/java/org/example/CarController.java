package org.example;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CarController implements IRepositoryObserver {
    private final ICarView view;
    private final CarDbRepository model;
    private int currentPage = 0;

    public CarController(ICarView view, CarDbRepository model) {
        this.view = view;
        this.model = model;

        model.addObserver(this);
        initializeView();
        initializeListeners();
        updateNavigationButtons();
    }

    @Override
    public void onRepositoryChanged() {
        refreshTableData();
        updateNavigationButtons();
        view.refreshView();
    }

    private void initializeView() {
        String[] columnNames = {"vin", "brand", "model"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        view.setTableData(tableModel);
        refreshTableData();
        view.setVisible(true);
    }

    private void initializeListeners() {
        view.addActionListener("add", this::showAddCarDialog);
        view.addActionListener("edit", this::showEditCarDialog);
        view.addActionListener("delete", this::deleteCar);
        view.addActionListener("previous", () -> {
            if (currentPage > 0) {
                currentPage--;
                refreshTableData();
                updateNavigationButtons();
            }
        });
        view.addActionListener("next", () -> {
            if (currentPage < model.getTotalPages() - 1) {
                currentPage++;
                refreshTableData();
                updateNavigationButtons();
            }
        });
        view.addActionListener("applyFilters", this::applyMultipleFilters);
        view.addActionListener("clearFilters", this::clearFilters);

        if (view instanceof CarView) {
            CarView carView = (CarView) view;

            // Обработчик сортировки
            carView.getSortComboBox().addActionListener(e -> {
                String selectedSort = (String) carView.getSortComboBox().getSelectedItem();
                updateSort(selectedSort);
            });

            // Обработчик двойного клика по таблице
            carView.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        showCarDetails();
                    }
                }
            });
        }
    }

    private void refreshTableData() {
        List<Car> cars = model.get_k_n_short_list(
                currentPage,
                model.getPageSize(),
                model.getCurrentFilter(),
                model.getCurrentSortField()
        );
        updateTableData(cars);
    }

    private void updateTableData(List<Car> cars) {
        DefaultTableModel tableModel = new DefaultTableModel(
                new String[]{"vin", "brand", "model"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Car car : cars) {
            tableModel.addRow(new Object[]{
                    car.getVin(),
                    car.getBrand(),
                    car.getModel()
            });
        }

        view.setTableData(tableModel);
    }

    private void updateNavigationButtons() {
        boolean hasPrevious = currentPage > 0;
        boolean hasNext = currentPage < model.getTotalPages() - 1;

        view.updateNavigationButtons(hasPrevious, hasNext);
        view.setPageInfo(currentPage, model.getTotalPages());
    }

    private Car findCarByVin(String vin) {
        List<Car> cars = model.get_k_n_short_list(
                0,
                Integer.MAX_VALUE,  // Получаем все записи
                new VinFilterDecorator(new NoFilter(), vin),
                model.getCurrentSortField()
        );
        if (!cars.isEmpty()) {
            return cars.get(0);
        }
        return null;
    }

    private void showCarDetails() {
        int selectedRow = view.getSelectedRow();
        if (selectedRow != -1) {
            String vin = view.getValueAt(selectedRow, 0);
            Car selectedCar = findCarByVin(vin);

            if (selectedCar != null) {
                // В зависимости от типа view показываем соответствующий диалог
                if (view instanceof CarView) {
                    CarDetailsDialog dialog = new CarDetailsDialog((CarView)view, selectedCar);
                    dialog.setVisible(true);
                } else if (view instanceof CarWebView) {
                    CarWebDetailsDialog dialog = new CarWebDetailsDialog(
                            ((CarWebView)view).getDocument(),
                            selectedCar
                    );
                    dialog.show();
                }
            }
        }
    }

    private void showAddCarDialog() {
        CarDialogController dialogController = new CarDialogController(
                model,
                new AddCarStrategy()
        );
        dialogController.getView().clearFields();
        dialogController.showDialog();
    }

    private void showEditCarDialog() {
        int selectedRow = view.getSelectedRow();
        if (selectedRow == -1) {
            view.showMessage("Please select a car to edit", "Warning", MessageType.WARNING);
            return;
        }

        String vin = view.getValueAt(selectedRow, 0);
        Car selectedCar = findCarByVin(vin);

        if (selectedCar != null) {
            CarDialogController dialogController = new CarDialogController(
                    model,
                    new EditCarStrategy(selectedCar)
            );
            dialogController.getView().setCarData(selectedCar);
            dialogController.showDialog();
        } else {
            view.showMessage(
                    "Error: Car not found",
                    "Error",
                    MessageType.ERROR
            );
        }
    }

    private void deleteCar() {
        int selectedRow = view.getSelectedRow();
        if (selectedRow == -1) {
            view.showMessage("Please select a car to delete", "Warning", MessageType.WARNING);
            return;
        }

        String vin = view.getValueAt(selectedRow, 0);
        Car carToDelete = findCarByVin(vin);

        if (carToDelete != null) {
            try {
                model.delete(carToDelete.getCarId());
                view.showMessage("Car deleted successfully", "Success", MessageType.INFO);
            } catch (Exception ex) {
                view.showMessage(
                        "Error deleting car: " + ex.getMessage(),
                        "Error",
                        MessageType.ERROR
                );
            }
        } else {
            view.showMessage(
                    "Error: Car not found",
                    "Error",
                    MessageType.ERROR
            );
        }
    }

    private void applyMultipleFilters() {
        try {
            FilterBuilder filterBuilder = new FilterBuilder();

            String vin = view.getVinFilter();
            if (!vin.isEmpty()) {
                filterBuilder.withVin(vin);
            }

            String brand = view.getBrandFilter();
            if (!brand.isEmpty()) {
                filterBuilder.withBrand(brand);
            }

            String model = view.getModelFilter();
            if (!model.isEmpty()) {
                filterBuilder.withModel(model);
            }

            currentPage = 0;
            this.model.setFilter(filterBuilder.build());

        } catch (NumberFormatException ex) {
            view.showMessage(
                    "Please enter valid numeric values for year and price fields",
                    "Invalid Input",
                    MessageType.ERROR
            );
        } catch (Exception ex) {
            view.showMessage(
                    "Error applying filters: " + ex.getMessage(),
                    "Error",
                    MessageType.ERROR
            );
        }
    }

    private void clearFilters() {
        currentPage = 0;
        view.clearFilters();
        model.setFilter(new NoFilter());
    }

    public void updateSort(String sortField) {
        String dbSortField = switch (sortField.toLowerCase()) {
            case "id" -> "car_id";
            case "vin" -> "vin";
            case "brand" -> "brand";
            case "model" -> "model";
            case "year" -> "year";
            case "price" -> "price";
            case "type" -> "type";
            default -> "car_id";
        };
        model.setSortField(dbSortField);
        refreshTableData();
    }
}