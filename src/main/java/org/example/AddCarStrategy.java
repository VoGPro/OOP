package org.example;

import javax.swing.*;

public class AddCarStrategy implements ICarDialogStrategy {
    @Override
    public void processSave(CarDialogController controller) {
        try {
            Car newCar = new Car(
                    0, // ID will be generated
                    controller.getView().getVinField(),
                    controller.getView().getBrandField(),
                    controller.getView().getModelField(),
                    Integer.parseInt(controller.getView().getYearField()),
                    Double.parseDouble(controller.getView().getPriceField()),
                    controller.getView().getTypeField()
            );

            controller.getRepository().add(newCar);
            controller.getView().dispose();

            // Показываем сообщение об успехе в зависимости от типа view
            if (controller.getView() instanceof CarDialogView) {
                javax.swing.JOptionPane.showMessageDialog(
                        (CarDialogView)controller.getView(),
                        "Car added successfully",
                        "Success",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                controller.getView().showSuccess("Car added successfully");
            }

        } catch (Exception ex) {
            showError(controller, "Error adding car: " + ex.getMessage());
        }
    }

    private void showError(CarDialogController controller, String message) {
        if (controller.getView() instanceof CarDialogView) {
            javax.swing.JOptionPane.showMessageDialog(
                    (CarDialogView)controller.getView(),
                    message,
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        } else {
            controller.getView().showError(message);
        }
    }
}