import java.time.Year;
import java.util.Objects;

public class BriefCar implements ICar {
    protected final int carId;
    protected final String brand;
    protected final String model;

    public BriefCar(int carId, String brand, String model) {
        validateCarId(carId);
        this.carId = carId;
        this.brand = brand;
        this.model = model;
    }

    public static BriefCar createFromString(String carString) {
        String[] parts = carString.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Неверный тип данных. Ожидается строка, разделённая 3 запятыми.");
        }
        try {
            int carId = Integer.parseInt(parts[0].trim());
            String brand = parts[1].trim();
            String model = parts[2].trim();
            return new BriefCar(carId, brand, model);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный формат данных в строке", e);
        }
    }

    private static void validateCarId(int carId) {
        if (carId <= 0) {
            throw new IllegalArgumentException("ID должно быть целым положительным числом");
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

    @Override
    public String toString() {
        return "Car {" +
                "carId: " + carId +
                ", brand: " + brand +
                ", model: " + model +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BriefCar car = (BriefCar) o;
        return carId == car.carId &&
                Objects.equals(brand, car.brand) &&
                Objects.equals(model, car.model);
    }
}