public class Main {
    public static void main(String[] args) {
        Car car = Car.createFromString("1, Nissan, R34, 2002, 35209.4, Sport");
        System.out.printf(car.toString());
    }
}