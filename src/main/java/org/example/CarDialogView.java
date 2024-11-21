package org.example;

import javax.swing.*;
import java.awt.*;

public class CarDialogView extends JDialog {
    private final JTextField vinField;
    private final JTextField brandField;
    private final JTextField modelField;
    private final JTextField yearField;
    private final JTextField priceField;
    private final JTextField typeField;
    private final JButton saveButton;
    private final JButton cancelButton;

    public CarDialogView(Frame owner, String title) {
        super(owner, title, true);

        // Create fields
        vinField = new JTextField(20);
        brandField = new JTextField(20);
        modelField = new JTextField(20);
        yearField = new JTextField(20);
        priceField = new JTextField(20);
        typeField = new JTextField(20);
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        // Layout
        setLayout(new BorderLayout());

        // Fields panel
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        addField(fieldsPanel, "VIN:", vinField, gbc, 0);
        addField(fieldsPanel, "Brand:", brandField, gbc, 1);
        addField(fieldsPanel, "Model:", modelField, gbc, 2);
        addField(fieldsPanel, "Year:", yearField, gbc, 3);
        addField(fieldsPanel, "Price:", priceField, gbc, 4);
        addField(fieldsPanel, "Type:", typeField, gbc, 5);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        add(fieldsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Dialog properties
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    private void addField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    // Getters
    public JTextField getVinField() { return vinField; }
    public JTextField getBrandField() { return brandField; }
    public JTextField getModelField() { return modelField; }
    public JTextField getYearField() { return yearField; }
    public JTextField getPriceField() { return priceField; }
    public JTextField getTypeField() { return typeField; }
    public JButton getSaveButton() { return saveButton; }
    public JButton getCancelButton() { return cancelButton; }

    // Helper methods
    public void setCarData(Car car) {
        vinField.setText(car.getVin());
        brandField.setText(car.getBrand());
        modelField.setText(car.getModel());
        yearField.setText(String.valueOf(car.getYear()));
        priceField.setText(String.valueOf(car.getPrice()));
        typeField.setText(car.getType());
    }

    public void clearFields() {
        vinField.setText("");
        brandField.setText("");
        modelField.setText("");
        yearField.setText("");
        priceField.setText("");
        typeField.setText("");
    }
}