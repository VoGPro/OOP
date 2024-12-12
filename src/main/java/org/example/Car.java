package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Car extends BriefCar {
    private int year;
    private double price;
    private String type;

    @JsonCreator
    public Car(@JsonProperty("carId") int car_id,
               @JsonProperty("vin") String vin,
               @JsonProperty("brand") String brand,
               @JsonProperty("model") String model,
               @JsonProperty("year") int year,
               @JsonProperty("price") double price,
               @JsonProperty("type") String type) {
        super(car_id, vin, brand, model);
        setYear(year);
        setPrice(price);
        setType(type);
    }

    public static Car createFromString(String carString) {
        String[] parts = carString.split(",");
        if (parts.length != 7) {
            throw new IllegalArgumentException("Неверный тип данных. Ожидается строка, разделённая 7 запятыми.");
        }
        try {
            int car_id = Integer.parseInt(parts[0].trim());
            String vin = parts[1].trim();
            String brand = parts[2].trim();
            String model = parts[3].trim();
            int year = Integer.parseInt(parts[4].trim());
            double price = Double.parseDouble(parts[5].trim());
            String type = parts[6].trim();
            return new Car(car_id, vin, brand, model, year, price, type);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный формат данных в строке", e);
        }
    }

    public void setYear(int year) {
        CarValidator.validateYear(year);
        this.year = year;
    }

    public void setPrice(double price) {
        CarValidator.validatePrice(price);
        this.price = price;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Car {" +
                "car_id: " + car_id +
                ", brand: " + brand +
                ", model: " + model +
                ", year: " + year +
                ", price: " + price +
                ", type: " + type +
                '}';
    }
}