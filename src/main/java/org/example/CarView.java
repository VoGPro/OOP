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
    private final JTextField vinFilterField;
    private final JTextField brandFilterField;
    private final JTextField modelFilterField;
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

        // Настраиваем таблицу
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        carTable.setRowHeight(25);
        carTable.setShowGrid(true);
        carTable.setGridColor(Color.LIGHT_GRAY);
        carTable.getTableHeader().setReorderingAllowed(false);

        // Добавляем всплывающую подсказку
        carTable.setToolTipText("Double-click or press Enter to view details");

        String[] sortOptions = {"vin", "brand", "model"};
        sortComboBox = new JComboBox<>(sortOptions);

        // Initialize filter components
        vinFilterField = new JTextField(10);
        brandFilterField = new JTextField(10);
        modelFilterField = new JTextField(10);
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

        // VIN filter
        gbc.gridx = 0; gbc.gridy = 0;
        filterPanel.add(new JLabel("VIN:"), gbc);
        gbc.gridx = 1;
        filterPanel.add(vinFilterField, gbc);

        // Brand filter
        gbc.gridx = 2;
        filterPanel.add(new JLabel("Brand:"), gbc);
        gbc.gridx = 3;
        filterPanel.add(brandFilterField, gbc);

        // Model filter
        gbc.gridx = 0; gbc.gridy = 1;
        filterPanel.add(new JLabel("Model:"), gbc);
        gbc.gridx = 1;
        filterPanel.add(modelFilterField, gbc);

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
    public JTextField getVinFilterField() { return vinFilterField; }
    public JTextField getBrandFilterField() { return brandFilterField; }
    public JTextField getModelFilterField() { return modelFilterField; }
    public JButton getApplyFiltersButton() { return applyFiltersButton; }
    public JButton getClearFiltersButton() { return clearFiltersButton; }
}