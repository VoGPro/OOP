package org.example;

public interface ICarDialogView {
    void setVisible(boolean visible);
    void dispose();
    String getVinField();
    String getBrandField();
    String getModelField();
    String getYearField();
    String getPriceField();
    String getTypeField();
    void setCarData(Car car);
    void clearFields();
    void addSaveActionListener(Runnable action);
    void addCancelActionListener(Runnable action);
    void showError(String message);
    void showSuccess(String message);
}