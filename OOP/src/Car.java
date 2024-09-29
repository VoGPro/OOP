import java.time.Year;

public class Car {
    private final int carId;
    private final String brand;
    private final String model;
    private final int year;
    private final double price;
    private final String type;

    public Car(int carId, String brand, String model, int year, double price, String type) {
        validateCarId(carId);
        validateYear(year);
        validatePrice(price);
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.type = type;
    }

    private static void validateCarId(int carId) {
        if (carId <= 0) {
            throw new IllegalArgumentException("ID должно быть целым положительным числом");
        }
    }

    private static void validateYear(int year) {
        if (year < 1886 || year > Year.now().getValue()) {
            throw new IllegalArgumentException("Год выпуска автомобиля не может быть " + year);
        }
    }

    private static void validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Цена не может быть меньше или равна нулю");
        }
    }

    public int getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
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
                "carId: " + carId +
                ", brand: " + brand +
                ", model: " + model +
                ", year: " + year +
                ", price: " + price +
                ", type: " + type +
                '}';
    }
}