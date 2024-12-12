package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;

public class WebEnvironmentInitializer {
    private Document document;
    private Map<String, Runnable> eventListeners = new HashMap<>();

    public WebEnvironmentInitializer() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.newDocument();
            initializeLayout();
            addStyles();
            setupEventListeners();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize web environment", e);
        }
    }

    private void initializeLayout() {
        Element html = document.createElement("html");
        Element head = document.createElement("head");
        Element body = document.createElement("body");

        html.appendChild(head);
        html.appendChild(body);
        document.appendChild(html);

        // Main container
        Element app = document.createElement("div");
        app.setAttribute("id", "app");

        // Controls section
        Element controls = document.createElement("div");
        controls.setAttribute("id", "controls");
        controls.setAttribute("class", "controls-section");

        // Add button
        Element addButton = document.createElement("button");
        addButton.setAttribute("id", "addBtn");
        addButton.setTextContent("Add");
        controls.appendChild(addButton);

        // Sort select
        Element sortLabel = document.createElement("label");
        sortLabel.setTextContent("Sort by: ");
        Element sortSelect = document.createElement("select");
        sortSelect.setAttribute("id", "sortSelect");
        String[] sortOptions = {"vin", "brand", "model"};
        for (String option : sortOptions) {
            Element optElement = document.createElement("option");
            optElement.setAttribute("value", option);
            optElement.setTextContent(option);
            sortSelect.appendChild(optElement);
        }
        controls.appendChild(sortLabel);
        controls.appendChild(sortSelect);

        // Filters section
        Element filters = document.createElement("div");
        filters.setAttribute("id", "filters");
        filters.setAttribute("class", "filters-section");

        // Add filter fields
        String[] filterFields = {"vin", "brand", "model"};
        for (String field : filterFields) {
            Element inputGroup = document.createElement("div");
            inputGroup.setAttribute("class", "input-group");

            Element label = document.createElement("label");
            label.setTextContent(field.substring(0, 1).toUpperCase() + field.substring(1) + ": ");

            Element input = document.createElement("input");
            input.setAttribute("id", field + "Filter");
            input.setAttribute("type", "text");

            inputGroup.appendChild(label);
            inputGroup.appendChild(input);
            filters.appendChild(inputGroup);
        }

        Element filterButtons = document.createElement("div");
        filterButtons.setAttribute("class", "filter-buttons");

        Element applyButton = document.createElement("button");
        applyButton.setAttribute("id", "applyFiltersBtn");
        applyButton.setTextContent("Apply Filters");

        Element clearButton = document.createElement("button");
        clearButton.setAttribute("id", "clearFiltersBtn");
        clearButton.setTextContent("Clear Filters");

        filterButtons.appendChild(applyButton);
        filterButtons.appendChild(clearButton);
        filters.appendChild(filterButtons);

        // Table section
        Element table = document.createElement("table");
        table.setAttribute("id", "carTable");
        table.setAttribute("class", "car-table");

        // Navigation section
        Element navigation = document.createElement("div");
        navigation.setAttribute("id", "navigation");
        navigation.setAttribute("class", "navigation-section");

        Element prevButton = document.createElement("button");
        prevButton.setAttribute("id", "prevBtn");
        prevButton.setTextContent("Previous");

        Element pageInfo = document.createElement("span");
        pageInfo.setAttribute("id", "pageInfo");

        Element nextButton = document.createElement("button");
        nextButton.setAttribute("id", "nextBtn");
        nextButton.setTextContent("Next");

        navigation.appendChild(prevButton);
        navigation.appendChild(pageInfo);
        navigation.appendChild(nextButton);

        // Add all sections to app
        app.appendChild(controls);
        app.appendChild(filters);
        app.appendChild(table);
        app.appendChild(navigation);

        // Add dialogs
        app.appendChild(createDialogs());

        body.appendChild(app);
    }

    private Element createDialogs() {
        Element dialogsContainer = document.createElement("div");

        // Car dialog for add/edit
        Element carDialog = document.createElement("div");
        carDialog.setAttribute("id", "carDialog");
        carDialog.setAttribute("class", "modal");

        Element dialogContent = document.createElement("div");
        dialogContent.setAttribute("class", "modal-content");

        Element title = document.createElement("h2");
        title.setAttribute("id", "dialogTitle");
        title.setTextContent("Add New Car");

        Element form = document.createElement("form");
        form.setAttribute("id", "carForm");

        // Form fields
        String[][] fields = {
                {"vin", "VIN", "text"},
                {"brand", "Brand", "text"},
                {"model", "Model", "text"},
                {"year", "Year", "number"},
                {"price", "Price", "number"},
                {"type", "Type", "text"}
        };

        for (String[] field : fields) {
            Element formGroup = document.createElement("div");
            formGroup.setAttribute("class", "form-group");

            Element label = document.createElement("label");
            label.setAttribute("for", field[0]);
            label.setTextContent(field[1] + ":");

            Element input = document.createElement("input");
            input.setAttribute("id", field[0]);
            input.setAttribute("type", field[2]);
            if (field[2].equals("number")) {
                if (field[0].equals("price")) {
                    input.setAttribute("step", "0.01");
                }
            }
            input.setAttribute("required", "true");

            formGroup.appendChild(label);
            formGroup.appendChild(input);
            form.appendChild(formGroup);
        }

        // Form buttons
        Element buttonContainer = document.createElement("div");
        buttonContainer.setAttribute("class", "button-container");

        Element saveButton = document.createElement("button");
        saveButton.setAttribute("type", "submit");
        saveButton.setTextContent("Save");

        Element cancelButton = document.createElement("button");
        cancelButton.setAttribute("type", "button");
        cancelButton.setAttribute("onclick", "closeCarDialog()");
        cancelButton.setTextContent("Cancel");

        buttonContainer.appendChild(saveButton);
        buttonContainer.appendChild(cancelButton);
        form.appendChild(buttonContainer);

        dialogContent.appendChild(title);
        dialogContent.appendChild(form);
        carDialog.appendChild(dialogContent);
        dialogsContainer.appendChild(carDialog);

        // Car details dialog
        Element detailsDialog = document.createElement("div");
        detailsDialog.setAttribute("id", "carDetailsDialog");
        detailsDialog.setAttribute("class", "modal");
        dialogsContainer.appendChild(detailsDialog);

        return dialogsContainer;
    }

    private void addStyles() {
        Element style = document.createElement("style");
        style.setTextContent("""
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }
            
            body {
                font-family: Arial, sans-serif;
                line-height: 1.6;
                padding: 20px;
            }
            
            #app {
                max-width: 1200px;
                margin: 0 auto;
            }
            
            .controls-section {
                margin-bottom: 20px;
                display: flex;
                align-items: center;
                gap: 15px;
            }
            
            .filters-section {
                background: #f5f5f5;
                padding: 15px;
                border-radius: 4px;
                margin-bottom: 20px;
            }
            
            .input-group {
                display: inline-block;
                margin-right: 15px;
            }
            
            .input-group label {
                margin-right: 5px;
            }
            
            .filter-buttons {
                margin-top: 10px;
            }
            
            .car-table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
            }
            
            .car-table th, .car-table td {
                border: 1px solid #ddd;
                padding: 12px;
                text-align: left;
            }
            
            .car-table th {
                background-color: #f4f4f4;
                font-weight: bold;
            }
            
            .car-table tr:nth-child(even) {
                background-color: #f9f9f9;
            }
            
            .car-table tr:hover {
                background-color: #f5f5f5;
                cursor: pointer;
            }
            
            .navigation-section {
                display: flex;
                justify-content: center;
                align-items: center;
                gap: 15px;
            }
            
            button {
                padding: 8px 15px;
                background-color: #4CAF50;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 14px;
            }
            
            button:disabled {
                background-color: #cccccc;
                cursor: not-allowed;
            }
            
            button:hover:not(:disabled) {
                background-color: #45a049;
            }
            
            input, select {
                padding: 8px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 14px;
            }
            
            .modal {
                display: none;
                position: fixed;
                z-index: 1;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.4);
            }
            
            .modal-content {
                background-color: #fefefe;
                margin: 15% auto;
                padding: 20px;
                border: 1px solid #888;
                width: 80%;
                max-width: 500px;
                border-radius: 5px;
            }
            
            .form-group {
                margin-bottom: 15px;
            }
            
            .form-group label {
                display: block;
                margin-bottom: 5px;
                font-weight: bold;
            }
            
            .form-group input {
                width: 100%;
            }
            
            .button-container {
                margin-top: 20px;
                text-align: right;
            }
            
            .button-container button {
                margin-left: 10px;
            }
            
            .button-container button[type="button"] {
                background-color: #f44336;
            }
            
            .details-dialog {
                max-width: 600px !important;
            }
            
            .details-table {
                width: 100%;
                border-collapse: collapse;
                margin: 20px 0;
            }
            
            .details-table tr {
                border-bottom: 1px solid #eee;
            }
            
            .details-table tr:last-child {
                border-bottom: none;
            }
            
            .details-table td {
                padding: 12px 8px;
            }
            
            .label-cell {
                font-weight: bold;
                width: 120px;
                color: #666;
            }
            
            .value-cell {
                color: #333;
            }
            
            .validation-error {
                border-color: #f44336 !important;
            }
            
            .error-message {
                color: #f44336;
                font-size: 12px;
                margin-top: 5px;
            }
            
            #pageInfo {
                font-weight: bold;
            }
        """);
        document.getDocumentElement().appendChild(style);
    }

    private void setupEventListeners() {
        Element scriptElement = document.createElement("script");
        scriptElement.setTextContent("""
            // Глобальные функции для работы с диалогами
            function openCarDialog() {
                document.getElementById('carDialog').style.display = 'block';
            }
            
            function closeCarDialog() {
                document.getElementById('carDialog').style.display = 'none';
            }
            
            function openDetailsDialog() {
                document.getElementById('carDetailsDialog').style.display = 'block';
            }
            
            function closeDetailsDialog() {
                document.getElementById('carDetailsDialog').style.display = 'none';
            }
            
            // Глобальное событие для закрытия по Escape
            document.addEventListener('keydown', function(event) {
                if (event.key === 'Escape') {
                    closeCarDialog();
                    closeDetailsDialog();
                }
            });
            
            // Глобальные переменные для состояния приложения
            let currentPage = 0;
            let totalPages = 1;
            
            // Инициализация при загрузке страницы
            document.addEventListener('DOMContentLoaded', function() {
                loadCars();
                setupEventHandlers();
            });
            
            // Загрузка списка автомобилей
            async function loadCars(page = 0) {
                try {
                    const params = new URLSearchParams({
                        page: page,
                        sort: document.getElementById('sortSelect').value
                    });
                    
                    // Добавляем фильтры
                    const vinFilter = document.getElementById('vinFilter').value;
                    const brandFilter = document.getElementById('brandFilter').value;
                    const modelFilter = document.getElementById('modelFilter').value;
                    
                    if (vinFilter) params.append('vin', vinFilter);
                    if (brandFilter) params.append('brand', brandFilter);
                    if (modelFilter) params.append('model', modelFilter);
                    
                    const response = await fetch(`/api/cars?${params}`);
                    const data = await response.json();
                    
                    if (response.ok) {
                        updateTable(data.cars);
                        currentPage = data.currentPage;
                        totalPages = data.totalPages;
                        updatePagination();
                    } else {
                        showError(data.message);
                    }
                } catch (error) {
                    showError('Error loading cars: ' + error.message);
                }
            }
            
            // Обновление таблицы
            function updateTable(cars) {
                const table = document.getElementById('carTable');
                table.innerHTML = '';
                
                // Создаем заголовок
                const header = document.createElement('tr');
                ['VIN', 'Brand', 'Model', 'Actions'].forEach(text => {
                    const th = document.createElement('th');
                    th.textContent = text;
                    header.appendChild(th);
                });
                table.appendChild(header);
                
                // Добавляем данные
                cars.forEach(car => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${car.vin}</td>
                        <td>${car.brand}</td>
                        <td>${car.model}</td>
                        <td>
                            <button onclick="editCar(${car.carId})">Edit</button>
                            <button onclick="deleteCar(${car.carId})" style="background-color: #f44336;">Delete</button>
                        </td>
                    `;
                    
                    // Добавляем обработчик двойного клика для показа деталей
                    row.addEventListener('dblclick', () => showCarDetails(car.carId));
                    table.appendChild(row);
                });
            }
            
            // Показ деталей автомобиля
            async function showCarDetails(carId) {
                try {
                    const response = await fetch(`/api/cars?id=${carId}`);
                    const car = await response.json();
                    
                    if (response.ok) {
                        // Обновляем содержимое существующего диалога
                        const dialog = document.getElementById('carDetailsDialog');
                        
                        dialog.innerHTML = `
                            <div class="modal-content details-dialog">
                                <h2>Car Information</h2>
                                <table class="details-table">
                                    <tr><td class="label-cell">ID:</td><td class="value-cell">${car.carId}</td></tr>
                                    <tr><td class="label-cell">VIN:</td><td class="value-cell">${car.vin}</td></tr>
                                    <tr><td class="label-cell">Brand:</td><td class="value-cell">${car.brand}</td></tr>
                                    <tr><td class="label-cell">Model:</td><td class="value-cell">${car.model}</td></tr>
                                    <tr><td class="label-cell">Year:</td><td class="value-cell">${car.year}</td></tr>
                                    <tr><td class="label-cell">Price:</td><td class="value-cell">${car.price.toFixed(2)}</td></tr>
                                    <tr><td class="label-cell">Type:</td><td class="value-cell">${car.type}</td></tr>
                                </table>
                                <div class="button-container">
                                    <button onclick="closeDetailsDialog()">Close</button>
                                </div>
                            </div>
                        `;
                        
                        openDetailsDialog();
                    } else {
                        showError(car.message);
                    }
                } catch (error) {
                    showError('Error loading car details: ' + error.message);
                }
            }
            
            // Обновление пагинации
            function updatePagination() {
                document.getElementById('pageInfo').textContent = 
                    `Page: ${currentPage + 1}/${totalPages}`;
                
                document.getElementById('prevBtn').disabled = currentPage <= 0;
                document.getElementById('nextBtn').disabled = currentPage >= totalPages - 1;
            }
            
            // Настройка обработчиков событий
            function setupEventHandlers() {
                // Пагинация
                document.getElementById('prevBtn').onclick = () => {
                    if (currentPage > 0) {
                        loadCars(currentPage - 1);
                    }
                };
                
                document.getElementById('nextBtn').onclick = () => {
                    if (currentPage < totalPages - 1) {
                        loadCars(currentPage + 1);
                    }
                };
                
                // Сортировка
                document.getElementById('sortSelect').onchange = () => {
                    loadCars(currentPage);
                };
                
                // Фильтры
                document.getElementById('applyFiltersBtn').onclick = () => {
                    loadCars(0);
                };
                
                document.getElementById('clearFiltersBtn').onclick = () => {
                    document.getElementById('vinFilter').value = '';
                    document.getElementById('brandFilter').value = '';
                    document.getElementById('modelFilter').value = '';
                    loadCars(0);
                };
                
                // Добавление нового автомобиля
                document.getElementById('addBtn').onclick = () => {
                    openCarDialog();
                };
                
                // Обработка формы
                document.getElementById('carForm').onsubmit = async function(e) {
                    e.preventDefault();
                    
                    if (!validateCarForm()) {
                        return;
                    }
                    
                    const formData = {
                        vin: document.getElementById('vin').value.toUpperCase(),
                        brand: document.getElementById('brand').value,
                        model: document.getElementById('model').value,
                        year: parseInt(document.getElementById('year').value),
                        price: parseFloat(document.getElementById('price').value),
                        type: document.getElementById('type').value
                    };
                    
                    const mode = this.dataset.mode;
                    
                    try {
                        if (mode === 'edit') {
                            formData.carId = parseInt(this.dataset.carId);
                            await updateCar(formData);
                        } else {
                            await addCar(formData);
                        }
                        
                        closeCarDialog();
                        loadCars(currentPage);
                    } catch (error) {
                        showError('Error saving car: ' + error.message);
                    }
                };
            }
            
            // Валидация формы
            function validateCarForm() {
                clearValidationErrors();
                let isValid = true;
                
                // VIN validation
                const vin = document.getElementById('vin').value;
                if (!/^[A-Z0-9]{17}$/.test(vin.toUpperCase())) {
                    showValidationError('vin', 'VIN must be exactly 17 characters (letters and numbers)');
                    isValid = false;
                }
                
                // Year validation
                const year = parseInt(document.getElementById('year').value);
                const currentYear = new Date().getFullYear();
                if (isNaN(year) || year < 1886 || year > currentYear) {
                    showValidationError('year', `Year must be between 1886 and ${currentYear}`);
                    isValid = false;
                }
                
                // Price validation
                const price = parseFloat(document.getElementById('price').value);
                if (isNaN(price) || price <= 0) {
                    showValidationError('price', 'Price must be greater than 0');
                    isValid = false;
                }
                
                return isValid;
            }
            
            // Очистка ошибок валидации
            function clearValidationErrors() {
                const inputs = document.querySelectorAll('#carForm input');
                inputs.forEach(input => {
                    input.classList.remove('validation-error');
                    const errorMessage = input.parentElement.querySelector('.error-message');
                    if (errorMessage) {
                        errorMessage.remove();
                    }
                });
            }
            
            // Показ ошибки валидации
            function showValidationError(inputId, message) {
                const input = document.getElementById(inputId);
                input.classList.add('validation-error');
                
                const errorMessage = document.createElement('div');
                errorMessage.className = 'error-message';
                errorMessage.textContent = message;
                
                input.parentElement.appendChild(errorMessage);
            }
            
            // CRUD операции
            async function addCar(carData) {
                const response = await fetch('/api/cars', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(carData)
                });
                
                const data = await response.json();
                if (!response.ok) {
                    throw new Error(data.message);
                }
                
                showSuccess('Car added successfully');
            }
            
            async function editCar(carId) {
                try {
                    const response = await fetch(`/api/cars?id=${carId}`);
                    const car = await response.json();
                    
                    if (response.ok) {
                        document.getElementById('vin').value = car.vin;
                        document.getElementById('brand').value = car.brand;
                        document.getElementById('model').value = car.model;
                        document.getElementById('year').value = car.year;
                        document.getElementById('price').value = car.price;
                        document.getElementById('type').value = car.type;
                        
                        const form = document.getElementById('carForm');
                        form.dataset.mode = 'edit';
                        form.dataset.carId = car.carId;
                        
                        document.getElementById('dialogTitle').textContent = 'Edit Car';
                        openCarDialog();
                    } else {
                        showError(data.message);
                    }
                } catch (error) {
                    showError('Error loading car details: ' + error.message);
                }
            }
            
            async function updateCar(carData) {
                const response = await fetch('/api/cars', {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(carData)
                });
                
                const data = await response.json();
                if (!response.ok) {
                    throw new Error(data.message);
                }
                
                showSuccess('Car updated successfully');
            }
            
            async function deleteCar(carId) {
                if (confirm('Are you sure you want to delete this car?')) {
                    try {
                        const response = await fetch(`/api/cars?id=${carId}`, {
                            method: 'DELETE'
                        });
                        
                        const data = await response.json();
                        if (response.ok) {
                            showSuccess(data.message);
                            loadCars(currentPage);
                        } else {
                            showError(data.message);
                        }
                    } catch (error) {
                        showError('Error deleting car: ' + error.message);
                    }
                }
            }
            
            // Вспомогательные функции
            function showSuccess(message) {
                alert('Success: ' + message);
            }
            
            function showError(message) {
                alert('Error: ' + message);
            }
        """);
        document.getDocumentElement().appendChild(scriptElement);
    }

    public void registerEventHandler(String type, Runnable handler) {
        eventListeners.put(type, handler);
    }

    public Document getDocument() {
        return document;
    }
}