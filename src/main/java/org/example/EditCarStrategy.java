package org.example;

import javax.swing.*;

public class EditCarStrategy implements ICarDialogStrategy {
    private final Car carToEdit;

    public EditCarStrategy(Car car) {
        this.carToEdit = car;
    }

    @Override
    public void processSave(CarDialogController controller) {
        try {
            Car updatedCar = new Car(
                    carToEdit.getCarId(),
                    controller.getView().getVinField(),
                    controller.getView().getBrandField(),
                    controller.getView().getModelField(),
                    Integer.parseInt(controller.getView().getYearField()),
                    Double.parseDouble(controller.getView().getPriceField()),
                    controller.getView().getTypeField()
            );

            controller.getRepository().update(updatedCar);
            controller.getView().dispose();

            // Показываем сообщение об успехе в зависимости от типа view
            if (controller.getView() instanceof CarDialogView) {
                javax.swing.JOptionPane.showMessageDialog(
                        (CarDialogView)controller.getView(),
                        "Car updated successfully",
                        "Success",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                controller.getView().showSuccess("Car updated successfully");
            }

        } catch (Exception ex) {
            showError(controller, "Error updating car: " + ex.getMessage());
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