## ER-диаграмма

```mermaid
erDiagram
    CLIENT ||--o{ RENTAL : "обращается"
    CAR ||--o{ RENTAL : "принимает"

    CLIENT {
        int client_id PK
        string last_name
        string first_name
        string middle_name
        string address
        string phone_number
    }

    CAR {
        int car_id PK
        string vin
        string brand
        string model
        int year
        decimal price
        string type
    }

    RENTAL {
        int rental_id PK
        int client_id FK
        int car_id FK
        date delivery_date
        date return_date
        decimal total_cost
    }
```

## Описание таблиц

1. **CLIENT (Клиент)**:
   - client_id: уникальный идентификатор клиента (первичный ключ)
   - last_name, first_name, middle_name: ФИО клиента
   - address: адрес
   - phone_number: номер телефона

2. **CAR (Автомобиль)**:
   - car_id: уникальный идентификатор автомобиля (первичный ключ)
   - vin: серийный номер автомобиля
   - brand: марка автомобиля
   - model: модель автомобиля
   - year: год выпуска автомобиля
   - price: цена автомобиля
   - type: тип автомобиля

3. **RENTAL (Аренда автомобиля)**:
   - rental_id: уникальный идентификатор аренды (первичный ключ)
   - client_id: внешний ключ, связывающий с таблицей CLIENT
   - car_id: внешний ключ, связывающий с таблицей CAR
   - delivery_date: дата выдачи автомобиля
   - return_date: дата возврата автомобиля
   - total_cost: общая стоимость аренды

## Выбранная сущность для дальнейшей работы
**Таблица CAR - Автомобиль**

## Диаграмма классов
```mermaid
classDiagram

    class BriefCar {
        -int carId
        -String brand
        -String model
        +BriefCar(int, String, String)
        +static createFromString(String) BriefCar
        +static validateCarId(int)
        +getCarId() int
        +getBrand() String
        +getModel() String
        +toString() String
        +equals(Object) boolean
    }

    class Car {
        -int year
        -double price
        -String type
        +Car(int, String, String, int, double, String)
        +static createFromString(String) Car
        +static validateYear(int)
        +static validatePrice(double)
        +getInitials() String
        +getYear() int
        +getPrice() double
        +getType() String
        +toString() String
        +equals(Object) boolean
}

    class CarValidator {
        +static validateCarId(int)
        +static validateVin(String)
        +static validateYear(int)
        +static validatePrice(double)
    }

    BriefCar <|-- Car : extends
    BriefCar ..> CarValidator : uses
    Car ..> CarValidator : uses
```
