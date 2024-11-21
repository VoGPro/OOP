package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CarController {
    private final CarView view;
    private final CarTableModel model;

    public CarController(CarView view, CarTableModel model) {
        this.view = view;
        this.model = model;

        initializeView();
        initializeListeners();
        updateNavigationButtons();
    }

    private void initializeView() {
        view.getCarTable().setModel(model);
        // Добавляем слушатель кликов по таблице
        view.getCarTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Двойной клик
                    showCarDetails();
                }
            }
        });

        // Добавляем слушатель клавиш для таблицы
        view.getCarTable().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    showCarDetails();
                }
            }
        });
        updatePageInfo();
        view.setVisible(true);
    }

    private void showCarDetails() {
        int selectedRow = view.getCarTable().getSelectedRow();
        if (selectedRow != -1) {
            Car selectedCar = model.getCarAt(selectedRow);
            CarDetailsDialog dialog = new CarDetailsDialog(view, selectedCar);
            dialog.setVisible(true);
        }
    }

    private void updatePageInfo() {
        int currentPage = model.getCurrentPage() + 1;
        int totalPages = model.getTotalPages();
        view.getPageInfoLabel().setText(String.format("Page: %d/%d", currentPage, totalPages));
    }

    private void updateNavigationButtons() {
        view.getPreviousButton().setEnabled(model.hasPreviousPage());
        view.getNextButton().setEnabled(model.hasNextPage());
        updatePageInfo();
    }

    private void initializeListeners() {
        view.getAddButton().addActionListener(e -> showAddCarDialog());
        view.getEditButton().addActionListener(e -> showEditCarDialog());
        view.getDeleteButton().addActionListener(e -> deleteCar());
        view.getPreviousButton().addActionListener(e -> {
            model.previousPage();
            updateNavigationButtons();
        });
        view.getNextButton().addActionListener(e -> {
            model.nextPage();
            updateNavigationButtons();
        });
        view.getSortComboBox().addActionListener(e -> {
            updateSort();
            updateNavigationButtons();
        });
        view.getApplyFiltersButton().addActionListener(e -> applyMultipleFilters());
        view.getClearFiltersButton().addActionListener(e -> clearFilters());
    }

    private void applyMultipleFilters() {
        try {
            // Начинаем с базового фильтра
            IFilterCriteria filter = new NoFilter();

            // Добавляем фильтр по vin
            String vin = view.getVinFilterField().getText().trim();
            if (!vin.isEmpty()) {
                filter = new VinFilterDecorator(filter, vin);
            }

            // Добавляем фильтр по бренду
            String brand = view.getBrandFilterField().getText().trim();
            if (!brand.isEmpty()) {
                filter = new BrandFilterDecorator(filter, brand);
            }

            // Добавляем фильтр по модели
            String model = view.getModelFilterField().getText().trim();
            if (!model.isEmpty()) {
                filter = new ModelFilterDecorator(filter, model);
            }

            // Применяем комбинированный фильтр
            this.model.setFilter(filter);
            updateNavigationButtons();

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
        // Очищаем все поля фильтров
        view.getVinFilterField().setText("");
        view.getBrandFilterField().setText("");
        view.getModelFilterField().setText("");

        // Сбрасываем фильтр на базовый
        model.setFilter(new NoFilter());
        updateNavigationButtons();
    }

    private void showAddCarDialog() {
        CarDialogController dialogController = new CarDialogController(
                view,
                "Add New Car",
                model.getRepository(),
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

        Car selectedCar = model.getCarAt(selectedRow);

        CarDialogController dialogController = new CarDialogController(
                view,
                "Edit Car",
                model.getRepository(),
                new EditCarStrategy(selectedCar)
        );
        dialogController.getView().setCarData(selectedCar);
        dialogController.showDialog();
    }

    private void deleteCar() {
        int selectedRow = view.getCarTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a car to delete");
            return;
        }

        Car car = model.getCarAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Are you sure you want to delete this car?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            model.getRepository().delete(car.getCarId());
            model.refreshData();
        }
    }

    private void updateSort() {
        String selectedSort = (String) view.getSortComboBox().getSelectedItem();
        if (selectedSort != null) {
            model.setSortField(selectedSort.toLowerCase());
        }
    }

    private void applyFilter() {
        String brandFilter = view.getBrandFilterField().getText();
        if (!brandFilter.isEmpty()) {
            IFilterCriteria filter = new BrandFilterDecorator(new NoFilter(), brandFilter);
            model.setFilter(filter);
        } else {
            model.setFilter(new NoFilter());
        }
    }
}