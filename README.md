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
    class ICarRepository {
        <<interface>>
        +getById(car_id: int): Car
        +get_k_n_short_list(k: int, n: int, filterCriteria: IFilterCriteria, sortField: String): List~Car~
        +add(car: Car): void
        +update(car: Car): void
        +delete(car_id: int): void
        +get_count(filterCriteria: IFilterCriteria): int
    }

    class CarFileRepository {
        #cars: List~Car~
        #filePath: String
        #objectMapper: ObjectMapper
        -loadFromFile(): void
        -saveToFile(): void
        +getById(car_id: int): Car
        +get_k_n_short_list(k: int, n: int, filterCriteria: IFilterCriteria, sortField: String): List~Car~
        +add(car: Car): void
        +update(car: Car): void
        +delete(car_id: int): void
        +get_count(filterCriteria: IFilterCriteria): int
        -generateNewCarId(): int
        -findIndexById(car_id: int): int
    }

    class CarFileRepositoryAdapter {
        -carFileRepository: CarFileRepository
        +CarFileRepositoryAdapter(CarFileRepository)
        +getById(car_id: int): Car
        +get_k_n_short_list(k: int, n: int, filterCriteria: IFilterCriteria, sortField: String): List~Car~
        +add(car: Car): void
        +update(car: Car): void
        +delete(car_id: int): void
        +get_count(filterCriteria: IFilterCriteria): int
    }

    class IFileStrategy {
        <<interface>>
        +createObjectMapper(): ObjectMapper
    }

    class CarJsonRepository {
        +createObjectMapper(): ObjectMapper
    }

    class CarYamlRepository {
        +createObjectMapper(): ObjectMapper
    }

    class IFilterCriteria {
        <<interface>>
        +matches(car: Car): boolean
    }

    class FilterDecorator {
        <<abstract>>
        #component: IFilterCriteria
        +FilterDecorator(IFilterCriteria)
        +matches(car: Car): boolean
    }

    class BrandFilterDecorator {
        -brand: String
        +BrandFilterDecorator(IFilterCriteria)
        +matches(car: Car): boolean
        +getBrand(): String
    }

    class ModelFilterDecorator {
        -model: String
        +ModelFilterDecorator(IFilterCriteria)
        +matches(car: Car): boolean
        +getModel(): String
    }

    class YearFilterDecorator {
        -year: int
        +YearFilterDecorator(IFilterCriteria)
        +matches(car: Car): boolean
        +getYear(): int
    }

    class PriceRangeFilterDecorator {
        -minPrice: double
        -maxPrice: double
        +PriceRangeFilterDecorator(IFilterCriteria)
        +matches(car: Car): boolean
        +getMinPrice(): double
        +getMaxPrice(): double
    }

    class TypeFilterDecorator {
        -type: String
        +TypeFilterDecorator(IFilterCriteria)
        +matches(car: Car): boolean
        +getType(): String
    }

    class CarDbRepository {
        -dbConfig: DbConfig
        +CarDbRepository()
        +getById(car_id: int): Car
        +get_k_n_short_list(k: int, n: int, filterCriteria: IFilterCriteria, sortField: String): List~Car~
        +add(car: Car): void
        +update(car: Car): void
        +delete(car_id: int): void
        +get_count(filterCriteria: IFilterCriteria): int
        -extractCarFromResultSet(rs: ResultSet): Car
    }

    class DbConfig {
        -instance: DbConfig
        -CONFIG_FILE: String
        -properties: Properties
        -DbConfig()
        +getInstance(): DbConfig
        +getConnection(): Connection
    }

    ICarRepository <|.. CarDbRepository : implements
    ICarRepository <|.. CarFileRepositoryAdapter : implements
    CarFileRepositoryAdapter --> CarFileRepository : uses
    CarFileRepository --> IFileStrategy : uses
    IFileStrategy <|.. CarJsonRepository : implements
    IFileStrategy <|.. CarYamlRepository : implements
    CarDbRepository --> DbConfig : uses
    CarDbRepository --> IFilterCriteria : uses
    CarFileRepository --> IFilterCriteria : uses
    
    IFilterCriteria <|.. FilterDecorator : implements
    FilterDecorator <|-- ModelFilterDecorator : extends
    FilterDecorator <|-- PriceRangeFilterDecorator : extends
    FilterDecorator <|-- TypeFilterDecorator : extends
    FilterDecorator <|-- YearFilterDecorator : extends
    FilterDecorator <|-- BrandFilterDecorator : extends
```
