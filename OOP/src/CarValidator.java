import java.time.Year;

public class CarValidator {
    public static void validateCarId(int carId) {
        if (carId < 0) {
            throw new IllegalArgumentException("ID должно быть целым положительным числом или нулём");
        }
    }

    public static void validateVin(String vin) {
        if (vin.length() != 17 || !vin.matches("[A-Z0-9]+")) {
            throw new IllegalArgumentException("VIN номер должен быть длиной в 17 символов и содержать только латинские буквы и цифры");
        }
    }

    public static void validateYear(int year) {
        if (year < 1886 || year > Year.now().getValue()) {
            throw new IllegalArgumentException("Год выпуска автомобиля не может быть " + year);
        }
    }

    public static void validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Цена не может быть меньше или равна нулю");
        }
    }
}