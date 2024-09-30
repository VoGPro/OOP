public class Main {
    public static void main(String[] args) {
//        Car car = Car.createFromString("1, Nissan, R34, 2002, 35209.4, Sport");
//        System.out.println("Краткая версия: " + car.shortVersion());
//        System.out.println("Полная версия: " + car);
//
//        Car car1 = new Car(1, "Nissan", "R34", 2002, 35209.4, "Sport");
//        Car car2 = new Car(2, "Mazda", "RX-7", 2002, 37208.3, "Sport");
//
//        if (car1.equals(car)) {
//            System.out.println("car1 равен car");
//        } else {
//            System.out.println("car1 не равен car");
//        }
//
//        if (car1.equals(car2)) {
//            System.out.println("car1 равен car2");
//        } else {
//            System.out.println("car1 не равен car2");
//        }

        BriefCar car = BriefCar.createFromString("1, Nissan, R34");
        System.out.println(car);

        BriefCar car1 = new BriefCar(1, "Nissan", "R34");
        BriefCar car2 = new BriefCar(2, "Mazda", "RX-7");

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