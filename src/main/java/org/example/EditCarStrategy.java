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
                    controller.getView().getVinField().getText(),
                    controller.getView().getBrandField().getText(),
                    controller.getView().getModelField().getText(),
                    Integer.parseInt(controller.getView().getYearField().getText()),
                    Double.parseDouble(controller.getView().getPriceField().getText()),
                    controller.getView().getTypeField().getText()
            );

            controller.getRepository().update(updatedCar);
            controller.getView().dispose();

            JOptionPane.showMessageDialog(
                    controller.getView(),
                    "Car updated successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    controller.getView(),
                    "Error updating car: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}