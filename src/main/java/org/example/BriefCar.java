package org.example;

public class BriefCar {
    protected int car_id;
    protected String vin;
    protected String brand;
    protected String model;

    public BriefCar(int car_id, String vin, String brand, String model) {
        setCarId(car_id);
        setVin(vin);
        setBrand(brand);
        setModel(model);
    }

    public void setCarId(int car_id) {
        CarValidator.validateCarId(car_id);
        this.car_id = car_id;
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
        return car_id;
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
                "car_id: " + car_id +
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