package org.example;

import javax.swing.*;
import java.awt.*;

public class CarView extends JFrame {
    private final JTable carTable;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton deleteButton;
    private final JButton previousButton;
    private final JButton nextButton;
    private final JComboBox<String> sortComboBox;
    private final JLabel pageInfoLabel;

    // Фильтры
    private final JTextField brandFilterField;
    private final JTextField modelFilterField;
    private final JTextField typeFilterField;
    private final JTextField yearFilterField;
    private final JTextField minPriceField;
    private final JTextField maxPriceField;
    private final JButton applyFiltersButton;
    private final JButton clearFiltersButton;

    public CarView() {
        setTitle("Car Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);

        // Create basic components
        carTable = new JTable();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");
        pageInfoLabel = new JLabel("Page: 0/0");

        String[] sortOptions = {"car_id", "brand", "model", "year", "price", "type"};
        sortComboBox = new JComboBox<>(sortOptions);

        // Initialize filter components
        brandFilterField = new JTextField(10);
        modelFilterField = new JTextField(10);
        typeFilterField = new JTextField(10);
        yearFilterField = new JTextField(10);
        minPriceField = new JTextField(10);
        maxPriceField = new JTextField(10);
        applyFiltersButton = new JButton("Apply Filters");
        clearFiltersButton = new JButton("Clear Filters");

        // Layout
        setLayout(new BorderLayout());

        // Top control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        controlPanel.add(new JLabel("Sort by:"));
        controlPanel.add(sortComboBox);

        // Filter panel
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Brand filter
        gbc.gridx = 0; gbc.gridy = 0;
        filterPanel.add(new JLabel("Brand:"), gbc);
        gbc.gridx = 1;
        filterPanel.add(brandFilterField, gbc);

        // Model filter
        gbc.gridx = 2;
        filterPanel.add(new JLabel("Model:"), gbc);
        gbc.gridx = 3;
        filterPanel.add(modelFilterField, gbc);

        // Type filter
        gbc.gridx = 0; gbc.gridy = 1;
        filterPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        filterPanel.add(typeFilterField, gbc);

        // Year filter
        gbc.gridx = 2;
        filterPanel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 3;
        filterPanel.add(yearFilterField, gbc);

        // Price range filter
        gbc.gridx = 0; gbc.gridy = 2;
        filterPanel.add(new JLabel("Min Price:"), gbc);
        gbc.gridx = 1;
        filterPanel.add(minPriceField, gbc);
        gbc.gridx = 2;
        filterPanel.add(new JLabel("Max Price:"), gbc);
        gbc.gridx = 3;
        filterPanel.add(maxPriceField, gbc);

        // Filter buttons
        JPanel filterButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filterButtonsPanel.add(applyFiltersButton);
        filterButtonsPanel.add(clearFiltersButton);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 4;
        filterPanel.add(filterButtonsPanel, gbc);

        // Navigation panel
        JPanel navigationPanel = new JPanel();
        navigationPanel.add(previousButton);
        navigationPanel.add(pageInfoLabel);
        navigationPanel.add(nextButton);

        // Add all panels to frame
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(carTable), BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    // Getters for all components
    public JTable getCarTable() { return carTable; }
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getPreviousButton() { return previousButton; }
    public JButton getNextButton() { return nextButton; }
    public JComboBox<String> getSortComboBox() { return sortComboBox; }
    public JLabel getPageInfoLabel() { return pageInfoLabel; }

    // Getters for filter components
    public JTextField getBrandFilterField() { return brandFilterField; }
    public JTextField getModelFilterField() { return modelFilterField; }
    public JTextField getTypeFilterField() { return typeFilterField; }
    public JTextField getYearFilterField() { return yearFilterField; }
    public JTextField getMinPriceField() { return minPriceField; }
    public JTextField getMaxPriceField() { return maxPriceField; }
    public JButton getApplyFiltersButton() { return applyFiltersButton; }
    public JButton getClearFiltersButton() { return clearFiltersButton; }
}