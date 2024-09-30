import java.time.Year;
import java.util.Objects;

public class Car extends BriefCar {
    private final int year;
    private final double price;
    private final String type;

    public Car(int carId, String brand, String model, int year, double price, String type) {
        super(carId, brand, model);
        validateYear(year);
        validatePrice(price);
        this.year = year;
        this.price = price;
        this.type = type;
    }

    public static Car createFromString(String carString) {
        String[] parts = carString.split(",");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Неверный тип данных. Ожидается строка, разделённая 6 запятыми.");
        }
        try {
            int carId = Integer.parseInt(parts[0].trim());
            String brand = parts[1].trim();
            String model = parts[2].trim();
            int year = Integer.parseInt(parts[3].trim());
            double price = Double.parseDouble(parts[4].trim());
            String type = parts[5].trim();
            return new Car(carId, brand, model, year, price, type);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный формат данных в строке", e);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return carId == car.carId &&
                Objects.equals(brand, car.brand) &&
                Objects.equals(model, car.model) &&
                year == car.year &&
                price == car.price &&
                Objects.equals(type, car.type);
    }
}