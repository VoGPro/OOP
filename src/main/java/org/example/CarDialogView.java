package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CarDialogView extends JDialog implements ICarDialogView {
    private final JTextField vinField;
    private final JTextField brandField;
    private final JTextField modelField;
    private final JTextField yearField;
    private final JTextField priceField;
    private final JTextField typeField;
    private final JButton saveButton;
    private final JButton cancelButton;
    private final JPanel mainPanel;

    public CarDialogView(Frame owner, String title) {
        super(owner, title, true);

        // Create main panel with padding
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create fields
        vinField = new JTextField(20);
        brandField = new JTextField(20);
        modelField = new JTextField(20);
        yearField = new JTextField(20);
        priceField = new JTextField(20);
        typeField = new JTextField(20);
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        initializeComponents();
        setupKeyboardNavigation();

        // Add panel to dialog
        add(mainPanel);

        // Set dialog properties
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add fields
        addField("VIN:", vinField, gbc, 0);
        addField("Brand:", brandField, gbc, 1);
        addField("Model:", modelField, gbc, 2);
        addField("Year:", yearField, gbc, 3);
        addField("Price:", priceField, gbc, 4);
        addField("Type:", typeField, gbc, 5);

        // Add buttons panel
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, gbc);
    }

    private void addField(String label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(field, gbc);

        // Add focus traversal
        field.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        field.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
    }

    private void setupKeyboardNavigation() {
        // Handle Escape key to close dialog
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(
                e -> dispose(),
                stroke,
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        // Add key listeners for TAB navigation
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) {
                    Component source = (Component) e.getSource();
                    if (e.isShiftDown()) {
                        source.transferFocusBackward();
                    } else {
                        source.transferFocus();
                    }
                    e.consume();
                }
            }
        };

        vinField.addKeyListener(keyAdapter);
        brandField.addKeyListener(keyAdapter);
        modelField.addKeyListener(keyAdapter);
        yearField.addKeyListener(keyAdapter);
        priceField.addKeyListener(keyAdapter);
        typeField.addKeyListener(keyAdapter);
        saveButton.addKeyListener(keyAdapter);
        cancelButton.addKeyListener(keyAdapter);
    }

    // ICarDialogView interface implementation
    @Override
    public String getVinField() {
        return vinField.getText();
    }

    @Override
    public String getBrandField() {
        return brandField.getText();
    }

    @Override
    public String getModelField() {
        return modelField.getText();
    }

    @Override
    public String getYearField() {
        return yearField.getText();
    }

    @Override
    public String getPriceField() {
        return priceField.getText();
    }

    @Override
    public String getTypeField() {
        return typeField.getText();
    }

    @Override
    public void setCarData(Car car) {
        vinField.setText(car.getVin());
        brandField.setText(car.getBrand());
        modelField.setText(car.getModel());
        yearField.setText(String.valueOf(car.getYear()));
        priceField.setText(String.valueOf(car.getPrice()));
        typeField.setText(car.getType());
    }

    @Override
    public void clearFields() {
        vinField.setText("");
        brandField.setText("");
        modelField.setText("");
        yearField.setText("");
        priceField.setText("");
        typeField.setText("");

        // Reset focus to first field
        vinField.requestFocusInWindow();
    }

    @Override
    public void addSaveActionListener(Runnable action) {
        saveButton.addActionListener(e -> action.run());
    }

    @Override
    public void addCancelActionListener(Runnable action) {
        cancelButton.addActionListener(e -> action.run());
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Additional helper methods
    public void setFieldsEnabled(boolean enabled) {
        vinField.setEnabled(enabled);
        brandField.setEnabled(enabled);
        modelField.setEnabled(enabled);
        yearField.setEnabled(enabled);
        priceField.setEnabled(enabled);
        typeField.setEnabled(enabled);
        saveButton.setEnabled(enabled);
        cancelButton.setEnabled(enabled);
    }

    public void setSaveButtonEnabled(boolean enabled) {
        saveButton.setEnabled(enabled);
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }
}