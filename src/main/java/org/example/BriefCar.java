package org.example;

public class BriefCar {
    protected int carId;
    protected String vin;
    protected String brand;
    protected String model;

    public BriefCar(int carId, String vin, String brand, String model) {
        setCarId(carId);
        setVin(vin);
        setBrand(brand);
        setModel(model);
    }

    public void setCarId(int carId) {
        CarValidator.validateCarId(carId);
        this.carId = carId;
    }

    public void setVin(String vin) {
        CarValidator.validateVin(vin.toUpperCase());
        this.vin = vin.toUpperCase();
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getCarId() {
        return carId;
    }

    public String getVin() {
        return vin;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    @Override
    public String toString() {
        return "Car {" +
                "carId: " + carId +
                ", vin: " + vin +
                ", brand: " + brand +
                ", model: " + model +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof BriefCar car)) return false;
        return this.vin.equals(car.vin);
    }

    @Override
    public int hashCode() {
        return vin.hashCode();
    }
}