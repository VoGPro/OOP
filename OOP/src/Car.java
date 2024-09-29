public class Car {
    private final int carId;
    private final String brand;
    private final String model;
    private final int year;
    private final double price;
    private final String type;

    private Car(int carId, String brand, String model, int year, double price, String type) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.type = type;
    }
}