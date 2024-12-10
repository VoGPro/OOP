package org.example;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CarView extends JFrame {
    private final JTable carTable;
    private JTable rowTable;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton deleteButton;
    private final JButton previousButton;
    private final JButton nextButton;
    private final JComboBox<String> sortComboBox;
    private final JTextField vinFilterField;
    private final JTextField brandFilterField;
    private final JTextField modelFilterField;
    private final JButton applyFiltersButton;
    private final JButton clearFiltersButton;
    private final JLabel pageInfoLabel;

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

        String[] sortOptions = {"vin", "brand", "model"};
        sortComboBox = new JComboBox<>(sortOptions);

        // Initialize filter components
        vinFilterField = new JTextField(10);
        brandFilterField = new JTextField(10);
        modelFilterField = new JTextField(10);
        applyFiltersButton = new JButton("Apply Filters");
        clearFiltersButton = new JButton("Clear Filters");

        // Layout setup
        setupLayout();
    }

    private void setupLayout() {
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
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        filterPanel.add(filterButtonsPanel, gbc);

        // Table setup
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        carTable.setRowHeight(25);
        carTable.setShowGrid(true);
        carTable.setGridColor(Color.LIGHT_GRAY);
        carTable.getTableHeader().setReorderingAllowed(false);

        // Add row numbers
        rowTable = createRowNumberTable(carTable);
        JScrollPane scrollPane = new JScrollPane(carTable);
        scrollPane.setRowHeaderView(rowTable);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowTable.getTableHeader());

        // Navigation panel
        JPanel navigationPanel = new JPanel();
        navigationPanel.add(previousButton);
        navigationPanel.add(pageInfoLabel);
        navigationPanel.add(nextButton);

        // Add all panels to frame
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    public void refreshRowNumbers() {
        rowTable.revalidate();
        rowTable.repaint();
    }

    private JTable createRowNumberTable(JTable mainTable) {
        JTable rowTable = new JTable(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return mainTable.getRowCount();
            }

            @Override
            public int getColumnCount() {
                return 1;
            }

            @Override
            public Object getValueAt(int row, int column) {
                return row + 1;
            }

            @Override
            public String getColumnName(int column) {
                return "№";
            }
        });

        rowTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rowTable.setShowGrid(true);
        rowTable.setGridColor(Color.LIGHT_GRAY);
        rowTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        rowTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        rowTable.setPreferredScrollableViewportSize(new Dimension(50, 0));
        rowTable.setRowHeight(mainTable.getRowHeight());

        rowTable.setBackground(mainTable.getBackground());
        rowTable.setForeground(mainTable.getForeground());

        JTableHeader header = rowTable.getTableHeader();
        header.setBackground(mainTable.getTableHeader().getBackground());
        header.setForeground(mainTable.getTableHeader().getForeground());
        header.setFont(mainTable.getTableHeader().getFont());

        // Синхронизируем выделение строк
        mainTable.getSelectionModel().addListSelectionListener(e -> {
            if (mainTable.getSelectedRow() != -1) {
                rowTable.setRowSelectionInterval(mainTable.getSelectedRow(), mainTable.getSelectedRow());
            }
        });

        return rowTable;
    }

    // Getters
    public JTable getCarTable() { return carTable; }
    public JTable getRowTable() { return rowTable; }
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getPreviousButton() { return previousButton; }
    public JButton getNextButton() { return nextButton; }
    public JComboBox<String> getSortComboBox() { return sortComboBox; }
    public JTextField getVinFilterField() { return vinFilterField; }
    public JTextField getBrandFilterField() { return brandFilterField; }
    public JTextField getModelFilterField() { return modelFilterField; }
    public JButton getApplyFiltersButton() { return applyFiltersButton; }
    public JButton getClearFiltersButton() { return clearFiltersButton; }
    public JLabel getPageInfoLabel() { return pageInfoLabel; }
}