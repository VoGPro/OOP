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

## Диаграмма классов автомобиля
```mermaid
classDiagram

    class BriefCar {
        #int car_id
        #String vin
        #String brand
        #String model
        +BriefCar(int, String, String, String)
        +setCarId(int)
        +setVin(String)
        +setBrand(String)
        +setModel(String)
        +getCarId() int
        +getVin() String
        +getBrand() String
        +getModel() String
        +toString() String
        +equals(Object) boolean
        +hashCode() int
    }

    class Car {
        -int year
        -double price
        -String type
        +Car(int, String, String, String, int, double, String)
        +setYear(int)
        +setPrice(double)
        +setType(String)
        +getYear() int
        +getPrice() double
        +getType() String
        +toString() String
        +static createFromString(String) Car
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

## Диаграмма классов БД и файлов
```mermaid
classDiagram

    class ICarStrategy {
        <<interface>>
        +getById(int) Car
        +get_k_n_short_list(int, int, String) List~Car~
        +add(Car)
        +update(Car)
        +delete(int)
        +get_count() int
    }

    class AbstractCarRepository {
        #List~Car~ cars
        #String filePath
        #ObjectMapper objectMapper
        #AbstractCarRepository(String, ObjectMapper)
        #abstract loadFromFile()
        #abstract saveToFile()
        +getById(int) Car
        +get_k_n_short_list(int, int, String) List~Car~
        +add(Car)
        +update(Car)
        +delete(int)
        +get_count() int
        #generateNewCarId() int
        #findIndexById(int) int
    }

    class Car_rep_json {
        +Car_rep_json(String)
        #loadFromFile()
        #saveToFile()
    }

    class Car_rep_yaml {
        +Car_rep_yaml(String)
        #loadFromFile()
        #saveToFile()
    }

    class Car_rep_DB {
        -DbConfig dbConfig
        +Car_rep_DB()
        +getById(int) Car
        +get_k_n_short_list(int, int, String) List~Car~
        +add(Car)
        +update(Car)
        +delete(int)
        +get_count() int
        -extractCarFromResultSet(ResultSet) Car
    }

    class DbConfig {
        -static DbConfig instance
        -static String CONFIG_FILE
        -Properties properties
        -DbConfig()
        +static getInstance() DbConfig
        +getConnection() Connection
    }

    ICarStrategy <|.. AbstractCarRepository : implements
    ICarStrategy <|.. Car_rep_DB : implements
    AbstractCarRepository <|-- Car_rep_json : extends
    AbstractCarRepository <|-- Car_rep_yaml : extends
    Car_rep_DB --> DbConfig : uses
```
