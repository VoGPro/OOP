package org.example;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;

public class CarView extends JFrame implements ICarView {
    private final JTable carTable;
    private JTable rowTable;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton deleteButton;
    private final JButton previousButton;
    private final JButton nextButton;
    private final JButton applyFiltersButton;
    private final JButton clearFiltersButton;
    private final JComboBox<String> sortComboBox;
    private final JTextField vinFilterField;
    private final JTextField brandFilterField;
    private final JTextField modelFilterField;
    private final JLabel pageInfoLabel;

    public CarView() {
        // Настройка основного окна
        setTitle("Car Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);

        // Инициализация компонентов
        carTable = new JTable();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");
        pageInfoLabel = new JLabel("Page: 0/0");

        String[] sortOptions = {"vin", "brand", "model"};
        sortComboBox = new JComboBox<>(sortOptions);

        // Поля фильтров
        vinFilterField = new JTextField(10);
        brandFilterField = new JTextField(10);
        modelFilterField = new JTextField(10);
        applyFiltersButton = new JButton("Apply Filters");
        clearFiltersButton = new JButton("Clear Filters");

        // Настройка компонентов и layout
        setupLayout();
        setupTable();
    }

    private void setupTable() {
        // Настройка таблицы
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        carTable.setRowHeight(25);
        carTable.setShowGrid(true);
        carTable.setGridColor(Color.LIGHT_GRAY);
        carTable.getTableHeader().setReorderingAllowed(false);

        // Добавление номеров строк
        rowTable = createRowNumberTable(carTable);
        JScrollPane scrollPane = new JScrollPane(carTable);
        scrollPane.setRowHeaderView(rowTable);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowTable.getTableHeader());

        // Добавляем в центр окна
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupLayout() {
        // Верхняя панель управления
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        controlPanel.add(new JLabel("Sort by:"));
        controlPanel.add(sortComboBox);

        // Панель фильтров
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Добавление полей фильтров
        gbc.gridx = 0; gbc.gridy = 0;
        filterPanel.add(new JLabel("VIN:"), gbc);
        gbc.gridx = 1;
        filterPanel.add(vinFilterField, gbc);

        gbc.gridx = 2;
        filterPanel.add(new JLabel("Brand:"), gbc);
        gbc.gridx = 3;
        filterPanel.add(brandFilterField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        filterPanel.add(new JLabel("Model:"), gbc);
        gbc.gridx = 1;
        filterPanel.add(modelFilterField, gbc);

        // Кнопки фильтров
        JPanel filterButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        filterButtonsPanel.add(applyFiltersButton);
        filterButtonsPanel.add(clearFiltersButton);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        filterPanel.add(filterButtonsPanel, gbc);

        // Панель навигации
        JPanel navigationPanel = new JPanel();
        navigationPanel.add(previousButton);
        navigationPanel.add(pageInfoLabel);
        navigationPanel.add(nextButton);

        // Компоновка всех панелей
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(navigationPanel, BorderLayout.SOUTH);
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

        // Настройка внешнего вида
        rowTable.setBackground(mainTable.getBackground());
        rowTable.setForeground(mainTable.getForeground());

        JTableHeader header = rowTable.getTableHeader();
        header.setBackground(mainTable.getTableHeader().getBackground());
        header.setForeground(mainTable.getTableHeader().getForeground());
        header.setFont(mainTable.getTableHeader().getFont());

        // Синхронизация выделения строк
        mainTable.getSelectionModel().addListSelectionListener(e -> {
            if (mainTable.getSelectedRow() != -1) {
                rowTable.setRowSelectionInterval(mainTable.getSelectedRow(), mainTable.getSelectedRow());
            }
        });

        return rowTable;
    }

    // Реализация методов интерфейса ICarView

    @Override
    public void refreshView() {
        refreshRowNumbers();
        revalidate();
        repaint();
    }

    public void refreshRowNumbers() {
        rowTable.revalidate();
        rowTable.repaint();
    }

    @Override
    public void showMessage(String message, String title, MessageType type) {
        int messageType = switch(type) {
            case ERROR -> JOptionPane.ERROR_MESSAGE;
            case INFO -> JOptionPane.INFORMATION_MESSAGE;
            case WARNING -> JOptionPane.WARNING_MESSAGE;
        };
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    @Override
    public void setPageInfo(int currentPage, int totalPages) {
        pageInfoLabel.setText(String.format("Page: %d/%d", currentPage + 1, totalPages));
    }

    @Override
    public void updateNavigationButtons(boolean hasPrevious, boolean hasNext) {
        previousButton.setEnabled(hasPrevious);
        nextButton.setEnabled(hasNext);
    }

    @Override
    public String getVinFilter() {
        return vinFilterField.getText().trim();
    }

    @Override
    public String getBrandFilter() {
        return brandFilterField.getText().trim();
    }

    @Override
    public String getModelFilter() {
        return modelFilterField.getText().trim();
    }

    @Override
    public void clearFilters() {
        vinFilterField.setText("");
        brandFilterField.setText("");
        modelFilterField.setText("");
    }

    @Override
    public void setTableData(TableModel model) {
        carTable.setModel(model);
        refreshRowNumbers();
    }

    @Override
    public int getSelectedRow() {
        return carTable.getSelectedRow();
    }

    @Override
    public String getValueAt(int row, int column) {
        return carTable.getValueAt(row, column).toString();
    }

    @Override
    public void addActionListener(String actionName, Runnable action) {
        switch(actionName) {
            case "add" -> addButton.addActionListener(e -> action.run());
            case "edit" -> editButton.addActionListener(e -> action.run());
            case "delete" -> deleteButton.addActionListener(e -> action.run());
            case "previous" -> previousButton.addActionListener(e -> action.run());
            case "next" -> nextButton.addActionListener(e -> action.run());
            case "applyFilters" -> applyFiltersButton.addActionListener(e -> action.run());
            case "clearFilters" -> clearFiltersButton.addActionListener(e -> action.run());
        }
    }

    // Дополнительные методы для работы с сортировкой
    public JComboBox<String> getSortComboBox() {
        return sortComboBox;
    }

    public void setColumnSortable(int column, boolean sortable) {
        carTable.getTableHeader().getColumnModel().getColumn(column).setResizable(sortable);
    }
}