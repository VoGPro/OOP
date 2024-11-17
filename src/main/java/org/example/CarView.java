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
    private final JTextField brandFilterField;
    private final JButton applyFilterButton;
    private final JLabel pageInfoLabel;

    public CarView() {
        setTitle("Car Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Create components
        carTable = new JTable();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");
        pageInfoLabel = new JLabel("Page: 0/0");

        String[] sortOptions = {"car_id", "brand", "model", "year", "price", "type"};
        sortComboBox = new JComboBox<>(sortOptions);

        brandFilterField = new JTextField(15);
        applyFilterButton = new JButton("Apply Filter");

        // Layout
        JPanel controlPanel = new JPanel();
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        controlPanel.add(new JLabel("Sort by:"));
        controlPanel.add(sortComboBox);
        controlPanel.add(new JLabel("Brand filter:"));
        controlPanel.add(brandFilterField);
        controlPanel.add(applyFilterButton);

        JPanel navigationPanel = new JPanel();
        navigationPanel.add(previousButton);
        navigationPanel.add(pageInfoLabel);
        navigationPanel.add(nextButton);

        setLayout(new BorderLayout());
        add(new JScrollPane(carTable), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    public JLabel getPageInfoLabel() { return pageInfoLabel; }

    // Getters for components
    public JTable getCarTable() { return carTable; }
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getPreviousButton() { return previousButton; }
    public JButton getNextButton() { return nextButton; }
    public JComboBox<String> getSortComboBox() { return sortComboBox; }
    public JTextField getBrandFilterField() { return brandFilterField; }
    public JButton getApplyFilterButton() { return applyFilterButton; }
}