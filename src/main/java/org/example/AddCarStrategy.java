package org.example;

import javax.swing.*;

public class AddCarStrategy implements ICarDialogStrategy {
    @Override
    public void processSave(CarDialogController controller) {
        try {
            Car newCar = new Car(
                    0, // ID will be generated
                    controller.getView().getVinField().getText(),
                    controller.getView().getBrandField().getText(),
                    controller.getView().getModelField().getText(),
                    Integer.parseInt(controller.getView().getYearField().getText()),
                    Double.parseDouble(controller.getView().getPriceField().getText()),
                    controller.getView().getTypeField().getText()
            );

            controller.getRepository().add(newCar);
            controller.getView().dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    controller.getView(),
                    "Error adding car: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}