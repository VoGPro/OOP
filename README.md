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

## Диаграмма классов MVC
```mermaid
classDiagram
    class IObservableRepository {
        <<interface>>
        +addObserver(observer: IRepositoryObserver): void
        +removeObserver(observer: IRepositoryObserver): void
        +notifyObservers(): void
    }

    class ICarRepository {
        <<interface>>
        +getById(car_id: int): Car
        +get_k_n_short_list(k: int, n: int, filterCriteria: IFilterCriteria, sortField: String): List~Car~
        +add(car: Car): void
        +update(car: Car): void
        +delete(car_id: int): void
        +get_count(filterCriteria: IFilterCriteria): int
    }

    class IRepositoryObserver {
        <<interface>>
        +onRepositoryChanged(): void
    }

    class CarDbRepository {
        -dbConfig: DbConfig
        -observers: List~IRepositoryObserver~
        +CarDbRepository()
        +addObserver(observer: IRepositoryObserver): void
        +removeObserver(observer: IRepositoryObserver): void
        +notifyObservers(): void
        +getById(car_id: int): Car
        +get_k_n_short_list(k: int, n: int, filterCriteria: IFilterCriteria, sortField: String): List~Car~
        +add(car: Car): void
        +update(car: Car): void
        +delete(car_id: int): void
        +get_count(filterCriteria: IFilterCriteria): int
    }

    class CarView {
        -carTable: JTable
        -addButton: JButton
        -editButton: JButton
        -deleteButton: JButton
        -filterFields: Map~String, JTextField~
        +CarView()
        +getCarTable(): JTable
        +getAddButton(): JButton
        +getEditButton(): JButton
        +getDeleteButton(): JButton
        +getFilterFields(): Map~String, JTextField~
    }

    class CarController {
        -view: CarView
        -model: CarDbRepository
        +CarController(view: CarView, model: CarDbRepository)
        -initializeView(): void
        -initializeListeners(): void
        -onRepositoryChanged(): void
        -showAddCarDialog(): void
        -showEditCarDialog(): void
        -showCarDetails(): void
        -updateSort(): void
        -applyFilters(): void
    }

    class ICarDialogStrategy {
        <<interface>>
        +processSave(controller: CarDialogController): void
    }

    class AddCarStrategy {
        +processSave(controller: CarDialogController): void
    }

    class EditCarStrategy {
        -carToEdit: Car
        +EditCarStrategy(car: Car)
        +processSave(controller: CarDialogController): void
    }

    class CarDialogView {
        -vinField: JTextField
        -brandField: JTextField
        -modelField: JTextField
        -yearField: JTextField
        -priceField: JTextField
        -typeField: JTextField
        -saveButton: JButton
        -cancelButton: JButton
        +CarDialogView(owner: Frame, title: String)
        +getVinField(): JTextField
        +getBrandField(): JTextField
        +getModelField(): JTextField
        +getYearField(): JTextField
        +getPriceField(): JTextField
        +getTypeField(): JTextField
        +getSaveButton(): JButton
        +getCancelButton(): JButton
        +setCarData(car: Car): void
        +clearFields(): void
    }

    class CarDialogController {
        -view: CarDialogView
        -repository: ICarRepository
        -strategy: ICarDialogStrategy
        +CarDialogController(owner: Frame, title: String, repository: ICarRepository, strategy: ICarDialogStrategy)
        +getView(): CarDialogView
        +getRepository(): ICarRepository
        +showDialog(): void
    }

    class CarDetailsDialog {
        -car: Car
        -mainPanel: JPanel
        +CarDetailsDialog(owner: Frame, car: Car)
        -initializeComponents(): void
        -addField(label: String, value: String, gbc: GridBagConstraints): void
    }

    ICarRepository <|.. CarDbRepository : implements
    IObservableRepository <|.. CarDbRepository : implements
    IRepositoryObserver <|.. CarController : implements
    CarDbRepository --> IRepositoryObserver : notifies
    CarController --> CarView : uses
    CarController --> CarDbRepository : uses
    CarController --> CarDialogController : creates
    CarController --> CarDetailsDialog : creates
    CarDialogController --> CarDialogView : uses
    CarDialogController --> CarDbRepository : uses
    CarDialogController --> ICarDialogStrategy : uses
    ICarDialogStrategy <|.. AddCarStrategy : implements
    ICarDialogStrategy <|.. EditCarStrategy : implements
```

## Диаграмма последовательности MVC
### Запуск приложения
```mermaid
sequenceDiagram
    actor User
    participant Main
    participant View as CarView
    participant Controller as CarController
    participant Model as CarDbRepository

    Note over User,Model: Запуск приложения
    User->>Main: Запуск приложения
    Main->>Model: new CarDbRepository()
    Main->>View: new CarView()
    Main->>Controller: new CarController(view, model)
    Controller->>Model: addObserver(this)
    Controller->>View: setModel(tableModel)
    Controller->>Model: get_k_n_short_list(currentPage, pageSize, filter, sort)
    Model-->>Controller: List<Car>
    Controller->>View: updateTableData(cars)
    Controller->>View: addActionListeners()
    Controller->>View: setVisible(true)
    View-->>User: показать данные
```

### Просмотр деталей автомобиля
```mermaid
sequenceDiagram
    actor User
    participant View as CarView
    participant Controller as CarController
    participant Model as CarDbRepository
    participant DetailsDialog as CarDetailsDialog

    Note over User,DetailsDialog: Просмотр деталей автомобиля
    User->>Controller: Двойной клик по строке
    Controller->>View: getSelectedRow()
    View-->>Controller: selectedRow
    Controller->>View: getValueAt(selectedRow, 0)
    View-->>Controller: value
    Controller->>Model: get_k_n_short_list(currentPage, pageSize, filter, sort)
    Model-->>Controller: List<Car>
    Controller->>DetailsDialog: new CarDetailsDialog(view, car)
    DetailsDialog->>DetailsDialog: initializeComponents()
    View->>DetailsDialog: setVisible(true)
    DetailsDialog-->>User: показ деталей
    User->>DetailsDialog: нажатие кнопки "Close"
    DetailsDialog->>DetailsDialog: dispose()
    DetailsDialog-->>User: закрытие диалогового окна
```

### Добавление автомобиля
```mermaid
sequenceDiagram
    actor User
    participant View as CarView
    participant Controller as CarController
    participant Model as CarDbRepository
    participant DialogController as CarDialogController
    participant DialogView as CarDialogView
    participant AddStrategy as AddCarStrategy

    Note over User,AddStrategy: Добавление автомобиля
    User->>Controller: нажатие кнопки "Add"
    Controller->>DialogController: new CarDialogController(view, "Add New Car", model, new AddCarStrategy())
    DialogController->>DialogView: new CarDialogView(owner, title)
    DialogController->>AddStrategy: new AddCarStrategy()
    DialogController->>DialogView: addActionListeners()
    DialogController->>DialogView: clearFields()
    DialogController->>DialogView: setVisible(true)
    
    Note over Model,AddStrategy: Сохранение нового автомобиля
    User->>DialogController: нажатие кнопки "Save"
    DialogController->>AddStrategy: processSave(controller)
    AddStrategy->>DialogView: getFields()
    DialogView-->>AddStrategy: fieldValues
    AddStrategy->>Model: add(newCar)
    Model->>Model: executeInsertSQL()
    Model->>Controller: notifyObservers()
    Controller->>Model: get_k_n_short_list()
    Model-->>Controller: List<Car>
    Controller->>View: updateTableData(cars)
    AddStrategy->>DialogView: dispose()
    View-->>User: отображение новых данных
```

### Редактирование автомобиля
```mermaid
sequenceDiagram
    actor User
    participant View as CarView
    participant Controller as CarController
    participant Model as CarDbRepository
    participant DialogController as CarDialogController
    participant DialogView as CarDialogView
    participant EditStrategy as EditCarStrategy

    Note over User,EditStrategy: Редактирование автомобиля
    User->>Controller: нажатие кнопки "Edit"
    Controller->>View: getSelectedRow()
    View-->>Controller: selectedRow
    Controller->>View: getValueAt(selectedRow, 0)
    View-->>Controller: value
    Controller->>Model: get_k_n_short_list(currentPage, pageSize, vinFilter, sort)
    Model-->>Controller: List<Car>
    Controller->>DialogController: new CarDialogController(view, "Edit Car", model, new EditCarStrategy(car))
    DialogController->>DialogView: new CarDialogView(owner, title)
    DialogController->>EditStrategy: new EditCarStrategy(car)
    DialogController->>DialogView: addActionListeners()
    DialogController->>DialogView: setCarData(car)
    DialogController->>DialogView: setVisible(true)

    Note over Model,EditStrategy: Сохранение изменений
    User->>DialogController: нажатие кнопки "Save"
    DialogController->>EditStrategy: processSave(controller)
    EditStrategy->>DialogView: getFields()
    DialogView-->>EditStrategy: fieldValues
    EditStrategy->>Model: update(updatedCar)
    Model->>Model: executeUpdateSQL()
    Model->>Controller: notifyObservers()
    Controller->>Model: get_k_n_short_list()
    Model-->>Controller: List<Car>
    Controller->>View: updateTableData(cars)
    EditStrategy->>DialogView: dispose()
    View-->>User: отображение новых данных
```

### Удаление автомобиля
```mermaid
sequenceDiagram
    actor User
    participant View as CarView
    participant Controller as CarController
    participant Model as CarDbRepository

    Note over User,Model: Удаление автомобиля
    User->>Controller: нажатие кнопки "Delete"
    Controller->>View: getSelectedRow()
    View-->>Controller: selectedRow
    Controller->>View: getValueAt(selectedRow, 0)
    View-->>Controller: value
    Controller->>Model: get_k_n_short_list(currentPage, pageSize, vinFilter, sort)
    Model-->>Controller: List<Car>
    Controller->>View: showConfirmDialog()
    View-->>Controller: confirmed
    Controller->>Model: delete(carId)
    Model->>Model: executeDeleteSQL()
    Model->>Controller: notifyObservers()
    Controller->>Model: get_k_n_short_list()
    Model-->>Controller: List<Car>
    Controller->>View: updateTableData(cars)
    View-->>User: отображение новых данных
```

### Паттерн Наблюдатель
```mermaid
sequenceDiagram
    participant View as CarView
    participant Controller as CarController
    participant Repository as CarDbRepository
    
    Note over Controller,Repository: Регистрация наблюдателя
    Controller->>Repository: addObserver(this)
    Repository->>Repository: observers.add(observer)
    
    Note over View,Repository: Добавление автомобиля    
    View->>Controller: click "Add"
    Controller->>Repository: add(newCar)
    Repository->>Repository: executeInsertSQL()
    Repository->>Controller: notifyObservers()
    Controller->>Repository: get_k_n_short_list()
    Repository-->>Controller: List<Car>
    Controller->>View: updateTableData(cars)
    
    Note over View,Repository: Редактирование автомобиля
    View->>Controller: click "Edit"
    Controller->>Repository: update(car)
    Repository->>Repository: executeUpdateSQL()
    Repository->>Controller: notifyObservers()
    Controller->>Repository: get_k_n_short_list()
    Repository-->>Controller: List<Car>
    Controller->>View: updateTableData(cars)
    
    Note over View,Repository: Удаление автомобиля
    View->>Controller: click "Delete"
    Controller->>Repository: delete(carId)
    Repository->>Repository: executeDeleteSQL()
    Repository->>Controller: notifyObservers()
    Controller->>Repository: get_k_n_short_list()
    Repository-->>Controller: List<Car>
    Controller->>View: updateTableData(cars)
```