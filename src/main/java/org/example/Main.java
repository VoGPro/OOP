package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Car_rep_DB repository = new Car_rep_DB();

        // 1. Добавление новых автомобилей
        Car car0 = new Car(0, "1hgbh41jxmn109186", "Nissan", "R34", 2002, 35209.4, "Sport");
        Car car1 = new Car(1, "WVWZZZ1KZAM985632", "Volkswagen", "Golf", 2020, 25000.0, "Hatchback");
        Car car2 = new Car(2, "WAUZZZ8V9LA123456", "Audi", "A3", 2021, 35000.0, "Sedan");
        Car car3 = Car.createFromString("3, 1hgbh41jxmn109107, Mazda, RX-7, 2002, 37208.3, Sport");
        Car car4 = new Car(4, "1hmts41jxmn109175", "Nissan", "R35", 2024, 122985.5, "Sport");
        repository.add(car0);
        repository.add(car1);
        repository.add(car2);
        repository.add(car3);
        repository.add(car4);
        System.out.println("Добавлено автомобилей. Текущее количество: " + repository.get_count());

        // 2. Получение автомобиля по ID
        try {
            Car foundCar = repository.getById(car1.getCarId());
            System.out.println("Найден автомобиль: " + foundCar);
        } catch (IllegalArgumentException e) {
            System.out.println("Автомобиль не найден: " + e.getMessage());
        }

        // 3. Обновление информации об автомобиле
        try {
            Car carToUpdate = repository.getById(car1.getCarId());
            carToUpdate.setPrice(26000.0);
            repository.update(carToUpdate);
            System.out.println("Автомобиль обновлен: " + carToUpdate);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при обновлении: " + e.getMessage());
        }

        // 4. Получение отсортированного подсписка
        // Получить первые 5 автомобилей (k=0, n=5), отсортированных по цене
        List<Car> sortedCars = repository.get_k_n_short_list(0, 5, "price");
        System.out.println("\nТоп-5 автомобилей по цене:");
        sortedCars.forEach(System.out::println);

        // Получить следующие 5 автомобилей (k=0, n=5), отсортированных по году
        sortedCars = repository.get_k_n_short_list(0, 5, "year");
        System.out.println("\nПервая страница автомобилей, отсортированных по году:");
        sortedCars.forEach(System.out::println);

        // 5. Удаление автомобиля
        try {
            repository.delete(car2.getCarId());
            System.out.println("Автомобиль удален. Текущее количество: " + repository.get_count());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при удалении: " + e.getMessage());
        }
    }
}