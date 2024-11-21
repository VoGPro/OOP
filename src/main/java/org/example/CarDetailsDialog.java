package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CarDetailsDialog extends JDialog {
    private final Car car;
    private final JPanel mainPanel;

    public CarDetailsDialog(Frame owner, Car car) {
        super(owner, "Car Details", true);
        this.car = car;

        // Создаем главную панель с отступами
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initializeComponents();

        // Добавляем панель на диалог
        add(mainPanel);

        // Настраиваем размер и положение
        pack();
        setLocationRelativeTo(owner);

        // Добавляем обработчик клавиши Escape для закрытия окна
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
        });
    }

    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Заголовок
        JLabel titleLabel = new JLabel("Car Information");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16));
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 15, 5);
        mainPanel.add(titleLabel, gbc);

        // Основная информация
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.insets = new Insets(5, 5, 5, 5);

        addField("ID:", String.valueOf(car.getCarId()), gbc);
        addField("VIN:", car.getVin(), gbc);
        addField("Brand:", car.getBrand(), gbc);
        addField("Model:", car.getModel(), gbc);
        addField("Year:", String.valueOf(car.getYear()), gbc);
        addField("Price:", String.format("$%.2f", car.getPrice()), gbc);
        addField("Type:", car.getType(), gbc);

        // Кнопка закрытия
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        mainPanel.add(closeButton, gbc);
    }

    private void addField(String label, String value, GridBagConstraints gbc) {
        // Label
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(fieldLabel.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0;
        mainPanel.add(fieldLabel, gbc);

        // Value
        JLabel fieldValue = new JLabel(value);
        gbc.gridx = 1;
        mainPanel.add(fieldValue, gbc);

        gbc.gridy++;
    }
}