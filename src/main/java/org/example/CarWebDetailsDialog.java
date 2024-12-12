package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CarWebDetailsDialog {
    private final Document document;
    private final Car car;
    private static final String DIALOG_ID = "carDetailsDialog";

    public CarWebDetailsDialog(Document document, Car car) {
        this.document = document;
        this.car = car;
        initializeLayout();
    }

    private void initializeLayout() {
        // Создаем элементы диалога
        Element dialog = document.createElement("div");
        dialog.setAttribute("id", DIALOG_ID);
        dialog.setAttribute("class", "modal");

        Element modalContent = document.createElement("div");
        modalContent.setAttribute("class", "modal-content details-dialog");

        // Заголовок
        Element title = document.createElement("h2");
        title.setTextContent("Car Information");
        modalContent.appendChild(title);

        // Основная информация в таблице
        Element table = document.createElement("table");
        table.setAttribute("class", "details-table");

        // Добавляем поля с информацией
        addField(table, "ID", String.valueOf(car.getCarId()));
        addField(table, "VIN", car.getVin());
        addField(table, "Brand", car.getBrand());
        addField(table, "Model", car.getModel());
        addField(table, "Year", String.valueOf(car.getYear()));
        addField(table, "Price", String.format("$%.2f", car.getPrice()));
        addField(table, "Type", car.getType());

        modalContent.appendChild(table);

        // Кнопка закрытия
        Element buttonContainer = document.createElement("div");
        buttonContainer.setAttribute("class", "button-container");

        Element closeButton = document.createElement("button");
        closeButton.setAttribute("type", "button");
        closeButton.setAttribute("onclick", "closeDetailsDialog()");
        closeButton.setTextContent("Close");

        buttonContainer.appendChild(closeButton);
        modalContent.appendChild(buttonContainer);

        dialog.appendChild(modalContent);

        // Добавляем стили
        addStyles();

        // Добавляем скрипт для работы с диалогом
        addScript();

        // Добавляем диалог в документ
        document.getDocumentElement().appendChild(dialog);
    }

    private void addField(Element table, String label, String value) {
        Element row = document.createElement("tr");

        Element labelCell = document.createElement("td");
        labelCell.setAttribute("class", "label-cell");
        labelCell.setTextContent(label);

        Element valueCell = document.createElement("td");
        valueCell.setAttribute("class", "value-cell");
        valueCell.setTextContent(value);

        row.appendChild(labelCell);
        row.appendChild(valueCell);
        table.appendChild(row);
    }

    private void addStyles() {
        Element style = document.createElement("style");
        style.setTextContent("""
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
            
            .button-container {
                text-align: center;
                margin-top: 20px;
            }
            
            .button-container button {
                padding: 8px 20px;
                background-color: #4CAF50;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 14px;
            }
            
            .button-container button:hover {
                background-color: #45a049;
            }
        """);

        document.getDocumentElement().appendChild(style);
    }

    private void addScript() {
        Element script = document.createElement("script");
        script.setTextContent("""
            function openDetailsDialog() {
                document.getElementById('""" + DIALOG_ID + """
                ').style.display = 'block';
            }
            
            function closeDetailsDialog() {
                document.getElementById('""" + DIALOG_ID + """
                ').style.display = 'none';
            }
            
            // Закрытие по клавише Escape
            document.addEventListener('keydown', function(event) {
                if (event.key === 'Escape') {
                    closeDetailsDialog();
                }
            });
        """);

        document.getDocumentElement().appendChild(script);
    }

    public void show() {
        Element dialog = document.getElementById(DIALOG_ID);
        if (dialog != null) {
            dialog.setAttribute("style", "display: block");
        }
    }

    public void dispose() {
        Element dialog = document.getElementById(DIALOG_ID);
        if (dialog != null) {
            dialog.setAttribute("style", "display: none");
        }
    }
}