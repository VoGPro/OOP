package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

public class CarController implements IRepositoryObserver {
    private final CarView view;
    private final CarDbRepository model;
    private int currentPage = 0;

    public CarController(CarView view, CarDbRepository model) {
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
    }

    private void initializeView() {
        // Инициализация таблицы
        String[] columnNames = {"vin", "brand", "model"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        view.getCarTable().setModel(tableModel);

        // Добавляем слушатели для просмотра деталей
        view.getCarTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showCarDetails();
                }
            }
        });

        view.getCarTable().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    showCarDetails();
                }
            }
        });

        refreshTableData();
        view.setVisible(true);
    }

    private void initializeListeners() {
        view.getAddButton().addActionListener(e -> showAddCarDialog());
        view.getEditButton().addActionListener(e -> showEditCarDialog());
        view.getDeleteButton().addActionListener(e -> deleteCar());

        view.getPreviousButton().addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                refreshTableData();
                updateNavigationButtons();
            }
        });

        view.getNextButton().addActionListener(e -> {
            if (currentPage < model.getTotalPages() - 1) {
                currentPage++;
                refreshTableData();
                updateNavigationButtons();
            }
        });

        view.getSortComboBox().addActionListener(e -> {
            updateSort();
            refreshTableData();
            updateNavigationButtons();
        });

        view.getApplyFiltersButton().addActionListener(e -> applyMultipleFilters());
        view.getClearFiltersButton().addActionListener(e -> clearFilters());
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
        DefaultTableModel tableModel = (DefaultTableModel) view.getCarTable().getModel();
        tableModel.setRowCount(0);
        for (Car car : cars) {
            tableModel.addRow(new Object[]{
                    car.getVin(),
                    car.getBrand(),
                    car.getModel()
            });
        }
    }

    private void updateNavigationButtons() {
        view.getPreviousButton().setEnabled(currentPage > 0);
        view.getNextButton().setEnabled(currentPage < model.getTotalPages() - 1);
        updatePageInfo();
    }

    private void updatePageInfo() {
        view.getPageInfoLabel().setText(
                String.format("Page: %d/%d", currentPage + 1, model.getTotalPages())
        );
    }

    private void showCarDetails() {
        int selectedRow = view.getCarTable().getSelectedRow();
        if (selectedRow != -1) {
            String vin = (String) view.getCarTable().getValueAt(selectedRow, 0);
            // Находим автомобиль по VIN
            List<Car> cars = model.get_k_n_short_list(
                    currentPage,
                    model.getPageSize(),
                    new VinFilterDecorator(new NoFilter(), vin),
                    model.getCurrentSortField()
            );
            if (!cars.isEmpty()) {
                CarDetailsDialog dialog = new CarDetailsDialog(view, cars.get(0));
                dialog.setVisible(true);
            }
        }
    }

    private void showAddCarDialog() {
        CarDialogController dialogController = new CarDialogController(
                view,
                "Add New Car",
                model,
                new AddCarStrategy()
        );
        dialogController.getView().clearFields();
        dialogController.showDialog();
    }

    private void showEditCarDialog() {
        int selectedRow = view.getCarTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a car to edit");
            return;
        }

        // Получаем VIN выбранной строки
        String vin = (String) view.getCarTable().getValueAt(selectedRow, 0);

        // Находим автомобиль по VIN
        List<Car> cars = model.get_k_n_short_list(
                currentPage,
                model.getPageSize(),
                new VinFilterDecorator(new NoFilter(), vin),
                model.getCurrentSortField()
        );

        if (!cars.isEmpty()) {
            Car selectedCar = cars.get(0);

            CarDialogController dialogController = new CarDialogController(
                    view,
                    "Edit Car",
                    model,
                    new EditCarStrategy(selectedCar)
            );
            dialogController.getView().setCarData(selectedCar);
            dialogController.showDialog();
        } else {
            JOptionPane.showMessageDialog(
                    view,
                    "Error: Car not found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteCar() {
        int selectedRow = view.getCarTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a car to delete");
            return;
        }

        // Получаем VIN выбранной строки
        String vin = (String) view.getCarTable().getValueAt(selectedRow, 0);

        // Находим автомобиль по VIN
        List<Car> cars = model.get_k_n_short_list(
                currentPage,
                model.getPageSize(),
                new VinFilterDecorator(new NoFilter(), vin),
                model.getCurrentSortField()
        );

        if (!cars.isEmpty()) {
            Car carToDelete = cars.get(0);

            int confirm = JOptionPane.showConfirmDialog(
                    view,
                    "Are you sure you want to delete this car?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    model.delete(carToDelete.getCarId());
                    JOptionPane.showMessageDialog(view, "Car deleted successfully");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            view,
                            "Error deleting car: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } else {
            JOptionPane.showMessageDialog(
                    view,
                    "Error: Car not found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void updateSort() {
        String selectedSort = (String) view.getSortComboBox().getSelectedItem();
        if (selectedSort != null) {
            String sortField = switch (selectedSort.toLowerCase()) {
                case "id" -> "car_id";
                case "vin" -> "vin";
                case "brand" -> "brand";
                case "model" -> "model";
                case "year" -> "year";
                case "price" -> "price";
                case "type" -> "type";
                default -> "car_id";
            };
            model.setSortField(sortField);
        }
    }

    private void applyMultipleFilters() {
        try {
            FilterBuilder filterBuilder = new FilterBuilder();

            String vin = view.getVinFilterField().getText().trim();
            if (!vin.isEmpty()) {
                filterBuilder.withVin(vin);
            }

            String brand = view.getBrandFilterField().getText().trim();
            if (!brand.isEmpty()) {
                filterBuilder.withBrand(brand);
            }

            String model = view.getModelFilterField().getText().trim();
            if (!model.isEmpty()) {
                filterBuilder.withModel(model);
            }

            currentPage = 0;
            this.model.setFilter(filterBuilder.build());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Please enter valid numeric values for year and price fields",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Error applying filters: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFilters() {
        currentPage = 0;
        view.getVinFilterField().setText("");
        view.getBrandFilterField().setText("");
        view.getModelFilterField().setText("");

        model.setFilter(new NoFilter());
    }
}