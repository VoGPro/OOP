package org.example;

import java.util.List;
import java.util.Scanner;

public class Main {
//    public static void main(String[] args) {
//
//        ICarRepository repository = new CarJsonRepository("cars.json");
//
//        // Добавление тестовых данных
//        addSampleCars(repository);
//
//        // Демонстрация различных фильтров
//        demonstrateFilters(repository);
//    }
//
    private static void addSampleCars(ICarRepository repository) {
        try {
            // Добавление тестовых автомобилей
            Car car0 = new Car(0, "1HGBH41JXMN109186", "Nissan", "R34", 2002, 35209.4, "Sport");
            Car car1 = new Car(1, "WVWZZZ1KZAM985632", "Volkswagen", "Golf", 2020, 25000.0, "Hatchback");
            Car car2 = new Car(2, "WAUZZZ8V9LA123456", "Audi", "A3", 2021, 35000.0, "Sedan");
            Car car3 = new Car(3, "1HGBH41JXMN109107", "Mazda", "RX-7", 2002, 37208.3, "Sport");
            Car car4 = new Car(4, "1HMTS41JXMN109175", "Nissan", "R35", 2024, 122985.5, "Sport");

            repository.add(car0);
            repository.add(car1);
            repository.add(car2);
            repository.add(car3);
            repository.add(car4);

            System.out.println("=== Добавлено автомобилей ===");
            System.out.println("Общее количество: " + repository.get_count(new NoFilter()));
            System.out.println();
        } catch (Exception e) {
            System.err.println("Ошибка при добавлении тестовых данных: " + e.getMessage());
        }
    }

    private static void demonstrateFilters(ICarRepository repository) {
        try {
            System.out.println("=== Демонстрация фильтров ===\n");

            // 1. Фильтр по году
            demonstrateYearFilter(repository);

            // 2. Фильтр по бренду
            demonstrateBrandFilter(repository);

            // 3. Фильтр по типу
            demonstrateTypeFilter(repository);

            // 4. Комбинированный фильтр (год + бренд)
            demonstrateCombinedFilter(repository);

            // 5. Фильтр по ценовому диапазону
            demonstratePriceRangeFilter(repository);

        } catch (Exception e) {
            System.err.println("Ошибка при демонстрации фильтров: " + e.getMessage());
        }
    }

    private static void demonstrateYearFilter(ICarRepository repository) {
        System.out.println("1. Поиск автомобилей 2002 года:");
        IFilterCriteria yearFilter = new YearFilterDecorator(new NoFilter(), "2002");
        printFilteredCars(repository, yearFilter, "brand");
    }

    private static void demonstrateBrandFilter(ICarRepository repository) {
        System.out.println("\n2. Поиск автомобилей марки Nissan:");
        IFilterCriteria brandFilter = new BrandFilterDecorator(new NoFilter(), "Nissan");
        printFilteredCars(repository, brandFilter, "year");
    }

    private static void demonstrateTypeFilter(ICarRepository repository) {
        System.out.println("\n3. Поиск спортивных автомобилей:");
        IFilterCriteria typeFilter = new TypeFilterDecorator(new NoFilter(), "Sport");
        printFilteredCars(repository, typeFilter, "brand");
    }

    private static void demonstrateCombinedFilter(ICarRepository repository) {
        System.out.println("\n4. Поиск автомобилей Nissan 2002 года:");
        IFilterCriteria yearFilter = new YearFilterDecorator(new NoFilter(), "2002");
        IFilterCriteria combinedFilter = new BrandFilterDecorator(yearFilter, "Nissan");
        printFilteredCars(repository, combinedFilter, "model");
    }

    private static void demonstratePriceRangeFilter(ICarRepository repository) {
        System.out.println("\n5. Поиск автомобилей в ценовом диапазоне 30000-40000:");
        IFilterCriteria priceFilter = new PriceRangeFilterDecorator(new NoFilter(), 30000, 40000);
        printFilteredCars(repository, priceFilter, "price");
    }

    private static void printFilteredCars(ICarRepository repository, IFilterCriteria filter, String sortField) {
        try {
            List<Car> cars = repository.get_k_n_short_list(0, 10, filter, sortField);
            int totalCount = repository.get_count(filter);

            System.out.println("Найдено автомобилей: " + totalCount);
            System.out.println("Результаты поиска:");

            if (cars.isEmpty()) {
                System.out.println("Нет автомобилей, соответствующих критериям поиска");
            } else {
                cars.forEach(car -> System.out.println(formatCarInfo(car)));
            }
            System.out.println("----------------------------------------");
        } catch (Exception e) {
            System.err.println("Ошибка при выводе результатов: " + e.getMessage());
        }
    }

    private static String formatCarInfo(Car car) {
        return String.format("%-8s | %-10s | %-15s | %4d | %10.2f | %-10s",
                car.getCarId(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getPrice(),
                car.getType()
        );
    }




//        Scanner in = new Scanner(System.in);
//        System.out.print("Работать с автомобилями в:\n" +
//                "1 - JSON файле\n" +
//                "2 - YAML файле\n" +
//                "3 - Базе данных\n" +
//                "Выбор: ");
//        int choice = in.nextInt();
//        in.close();
//
//        // Выбор стратегии
//        ICarStrategy repository;
//        switch(choice){
//            case 1:
//                repository = new Car_rep_json("cars.json");
//                break;
//            case 2:
//                repository = new Car_rep_yaml("cars.yaml");
//                break;
//            case 3:
//                repository = new Car_rep_DB();
//            default:
//                throw new IllegalArgumentException("Неверный ввод: " + choice);
//        }




    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print("Работать с автомобилями в:\n" +
                "1 - JSON файле\n" +
                "2 - YAML файле\n" +
                "3 - Базе данных\n" +
                "Выбор: ");
        int choice = in.nextInt();
        in.close();

        // Выбор стратегии
        ICarRepository repository;
        CarFileRepository carFileRepository;
        switch(choice){
            case 1:
                carFileRepository = new CarFileRepository("cars.json", new CarJsonRepository());
                repository = new CarFileRepositoryAdapter(carFileRepository);
                break;
            case 2:
                carFileRepository = new CarFileRepository("cars.yaml", new CarYamlRepository());
                repository = new CarFileRepositoryAdapter(carFileRepository);
                break;
            case 3:
                repository = new CarDbRepository();
            default:
                throw new IllegalArgumentException("Неверный ввод: " + choice);
        }

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

        try {
            car1.setVin("WAUZZZ8V9LA123456"); // Пытаемся установить VIN второго автомобиля
            repository.update(car1); // Выбросит исключение
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        IFilterCriteria noFilter = new NoFilter();
        System.out.println("Добавлено автомобилей. Текущее количество: " + repository.get_count(noFilter));

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
        IFilterCriteria filter = new PriceRangeFilterDecorator(new NoFilter(), 30000, 40000);
        // List<Car> sortedCars = repository.get_k_n_short_list(0, 5, "price");
        List<Car> sortedCars = repository.get_k_n_short_list(0, 5, filter, "price");
        System.out.println("\nТоп-5 автомобилей по цене:");
        sortedCars.forEach(System.out::println);

        // Получить следующие 5 автомобилей (k=0, n=5), отсортированных по году
//        sortedCars = repository.get_k_n_short_list(0, 5, "year");
//        System.out.println("\nПервая страница автомобилей, отсортированных по году:");
//        sortedCars.forEach(System.out::println);

        // 5. Удаление автомобиля
        try {
            repository.delete(car2.getCarId());
            System.out.println("Автомобиль удален. Текущее количество: " + repository.get_count(noFilter));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при удалении: " + e.getMessage());
        }
    }
}