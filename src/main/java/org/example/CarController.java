package org.example;

import javax.swing.*;
import java.awt.*;

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
        updatePageInfo();
        view.setVisible(true);
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

            // Добавляем фильтр по типу
            String type = view.getTypeFilterField().getText().trim();
            if (!type.isEmpty()) {
                filter = new TypeFilterDecorator(filter, type);
            }

            // Добавляем фильтр по году
            String year = view.getYearFilterField().getText().trim();
            if (!year.isEmpty()) {
                filter = new YearFilterDecorator(filter, year);
            }

            // Добавляем фильтр по цене
            String minPrice = view.getMinPriceField().getText().trim();
            String maxPrice = view.getMaxPriceField().getText().trim();
            if (!minPrice.isEmpty() && !maxPrice.isEmpty()) {
                filter = new PriceRangeFilterDecorator(
                        filter,
                        Double.parseDouble(minPrice),
                        Double.parseDouble(maxPrice)
                );
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
        view.getBrandFilterField().setText("");
        view.getModelFilterField().setText("");
        view.getTypeFilterField().setText("");
        view.getYearFilterField().setText("");
        view.getMinPriceField().setText("");
        view.getMaxPriceField().setText("");

        // Сбрасываем фильтр на базовый
        model.setFilter(new NoFilter());
        updateNavigationButtons();
    }

    private void showAddCarDialog() {
        // Create input dialog
        JDialog dialog = new JDialog(view, "Add New Car", true);
        dialog.setLayout(new GridLayout(8, 2));

        // Create input fields
        JTextField vinField = new JTextField();
        JTextField brandField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField typeField = new JTextField();

        // Add components to dialog
        dialog.add(new JLabel("VIN:"));
        dialog.add(vinField);
        dialog.add(new JLabel("Brand:"));
        dialog.add(brandField);
        dialog.add(new JLabel("Model:"));
        dialog.add(modelField);
        dialog.add(new JLabel("Year:"));
        dialog.add(yearField);
        dialog.add(new JLabel("Price:"));
        dialog.add(priceField);
        dialog.add(new JLabel("Type:"));
        dialog.add(typeField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                Car newCar = new Car(
                        0, // ID will be generated
                        vinField.getText(),
                        brandField.getText(),
                        modelField.getText(),
                        Integer.parseInt(yearField.getText()),
                        Double.parseDouble(priceField.getText()),
                        typeField.getText()
                );

                model.getRepository().add(newCar);
                model.refreshData();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage());
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(saveButton);
        dialog.add(cancelButton);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void showEditCarDialog() {
        int selectedRow = view.getCarTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Please select a car to edit");
            return;
        }

        Car selectedCar = model.getCarAt(selectedRow);

        // Create edit dialog
        JDialog dialog = new JDialog(view, "Edit Car", true);
        dialog.setLayout(new GridLayout(8, 2));

        // Create and populate input fields
        JTextField vinField = new JTextField(selectedCar.getVin());
        JTextField brandField = new JTextField(selectedCar.getBrand());
        JTextField modelField = new JTextField(selectedCar.getModel());
        JTextField yearField = new JTextField(String.valueOf(selectedCar.getYear()));
        JTextField priceField = new JTextField(String.valueOf(selectedCar.getPrice()));
        JTextField typeField = new JTextField(selectedCar.getType());

        // Add components to dialog
        dialog.add(new JLabel("VIN:"));
        dialog.add(vinField);
        dialog.add(new JLabel("Brand:"));
        dialog.add(brandField);
        dialog.add(new JLabel("Model:"));
        dialog.add(modelField);
        dialog.add(new JLabel("Year:"));
        dialog.add(yearField);
        dialog.add(new JLabel("Price:"));
        dialog.add(priceField);
        dialog.add(new JLabel("Type:"));
        dialog.add(typeField);

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                Car updatedCar = new Car(
                        selectedCar.getCarId(),
                        vinField.getText(),
                        brandField.getText(),
                        modelField.getText(),
                        Integer.parseInt(yearField.getText()),
                        Double.parseDouble(priceField.getText()),
                        typeField.getText()
                );

                model.getRepository().update(updatedCar);
                model.refreshData();
                dialog.dispose();
                JOptionPane.showMessageDialog(view, "Car updated successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error updating car: " + ex.getMessage());
            }
        });

        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(saveButton);
        dialog.add(cancelButton);

        // Set dialog properties
        dialog.setSize(300, 300);
        dialog.setLocationRelativeTo(view);
        dialog.setVisible(true);
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