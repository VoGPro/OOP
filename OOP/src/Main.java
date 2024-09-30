public class Main {
    public static void main(String[] args) {
        Car car = Car.createFromString("1, Nissan, R34, 2002, 35209.4, Sport");
        System.out.println("Краткая версия: " + car.shortVersion());
        System.out.println("Полная версия: " + car.toString());

        Car car1 = Car.createCar(1, "Nissan", "R34", 2002, 35209.4, "Sport");
        Car car2 = Car.createCar(2, "Mazda", "RX-7", 2002, 37208.3, "Sport");

        if (car1.equals(car)) {
            System.out.println("car1 равен car");
        } else {
            System.out.println("car1 не равен car");
        }

        if (car1.equals(car2)) {
            System.out.println("car1 равен car2");
        } else {
            System.out.println("car1 не равен car2");
        }
    }
}