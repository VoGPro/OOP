public class Main {
    public static void main(String[] args) {
        Car car0 = new Car(1, "1hgbh41jxmn109186", "Nissan", "R34", 2002, 35209.4, "Sport");
        Car car1 = Car.createFromString("1, 1hgbh41jxmn109186, Nissan, R34, 2002, 35209.4, Sport");
    }
}