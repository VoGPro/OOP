package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;

public class CarWebDialogView implements ICarDialogView {
    private final Document document;
    private Map<String, Runnable> actionListeners = new HashMap<>();

    public CarWebDialogView() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.newDocument();
            initializeLayout();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize web dialog view", e);
        }
    }

    private void initializeLayout() {
        // Create root elements
        Element html = document.createElement("html");
        Element body = document.createElement("body");

        // Create dialog container
        Element dialog = document.createElement("div");
        dialog.setAttribute("id", "dialog");
        dialog.setAttribute("class", "modal");

        // Create dialog content
        Element modalContent = document.createElement("div");
        modalContent.setAttribute("class", "modal-content");

        // Add title
        Element title = document.createElement("h2");
        title.setTextContent("Car Details");
        modalContent.appendChild(title);

        // Create form
        Element form = document.createElement("form");
        form.setAttribute("id", "carForm");

        // Add form fields
        addFormField(form, "vinField", "VIN");
        addFormField(form, "brandField", "Brand");
        addFormField(form, "modelField", "Model");
        addFormField(form, "yearField", "Year", "number");
        addFormField(form, "priceField", "Price", "number");
        addFormField(form, "typeField", "Type");

        // Add buttons
        Element buttonsDiv = document.createElement("div");
        buttonsDiv.setAttribute("class", "buttons");

        Element saveButton = document.createElement("button");
        saveButton.setAttribute("id", "saveBtn");
        saveButton.setAttribute("type", "button");
        saveButton.setTextContent("Save");

        Element cancelButton = document.createElement("button");
        cancelButton.setAttribute("id", "cancelBtn");
        cancelButton.setAttribute("type", "button");
        cancelButton.setTextContent("Cancel");

        buttonsDiv.appendChild(saveButton);
        buttonsDiv.appendChild(cancelButton);
        form.appendChild(buttonsDiv);

        // Assemble the dialog
        modalContent.appendChild(form);
        dialog.appendChild(modalContent);
        body.appendChild(dialog);
        html.appendChild(body);
        document.appendChild(html);
    }

    private void addFormField(Element form, String id, String label, String type) {
        Element div = document.createElement("div");

        Element labelElement = document.createElement("label");
        labelElement.setTextContent(label + ":");

        Element input = document.createElement("input");
        input.setAttribute("id", id);
        if (type != null) {
            input.setAttribute("type", type);
            if (type.equals("number") && id.equals("priceField")) {
                input.setAttribute("step", "0.01");
            }
        }

        div.appendChild(labelElement);
        div.appendChild(input);
        form.appendChild(div);
    }

    private void addFormField(Element form, String id, String label) {
        addFormField(form, id, label, "text");
    }

    @Override
    public void setVisible(boolean visible) {
        Element dialog = document.getElementById("dialog");
        if (dialog != null) {
            dialog.setAttribute("style", "display: " + (visible ? "block" : "none"));
        }
    }

    @Override
    public void dispose() {
        setVisible(false);
    }

    @Override
    public String getVinField() {
        return getFieldValue("vinField");
    }

    @Override
    public String getBrandField() {
        return getFieldValue("brandField");
    }

    @Override
    public String getModelField() {
        return getFieldValue("modelField");
    }

    @Override
    public String getYearField() {
        return getFieldValue("yearField");
    }

    @Override
    public String getPriceField() {
        return getFieldValue("priceField");
    }

    @Override
    public String getTypeField() {
        return getFieldValue("typeField");
    }

    private String getFieldValue(String fieldId) {
        Element field = document.getElementById(fieldId);
        return field != null ? field.getAttribute("value") : "";
    }

    @Override
    public void setCarData(Car car) {
        setFieldValue("vinField", car.getVin());
        setFieldValue("brandField", car.getBrand());
        setFieldValue("modelField", car.getModel());
        setFieldValue("yearField", String.valueOf(car.getYear()));
        setFieldValue("priceField", String.valueOf(car.getPrice()));
        setFieldValue("typeField", car.getType());
    }

    private void setFieldValue(String fieldId, String value) {
        Element field = document.getElementById(fieldId);
        if (field != null) {
            field.setAttribute("value", value);
        }
    }

    @Override
    public void clearFields() {
        String[] fields = {"vinField", "brandField", "modelField",
                "yearField", "priceField", "typeField"};
        for (String fieldId : fields) {
            setFieldValue(fieldId, "");
        }
    }

    @Override
    public void addSaveActionListener(Runnable action) {
        Element saveBtn = document.getElementById("saveBtn");
        if (saveBtn != null) {
            actionListeners.put("save", action);
            saveBtn.setAttribute("onclick",
                    "javascript:window.dispatchEvent(new CustomEvent('dialogAction', {detail: 'save'}))");
        }
    }

    @Override
    public void addCancelActionListener(Runnable action) {
        Element cancelBtn = document.getElementById("cancelBtn");
        if (cancelBtn != null) {
            actionListeners.put("cancel", action);
            cancelBtn.setAttribute("onclick",
                    "javascript:window.dispatchEvent(new CustomEvent('dialogAction', {detail: 'cancel'}))");
        }
    }

    @Override
    public void showError(String message) {
        Element alert = document.createElement("script");
        alert.setTextContent(String.format("alert('Error: %s')", message));
        document.getDocumentElement().appendChild(alert);
    }

    @Override
    public void showSuccess(String message) {
        Element alert = document.createElement("script");
        alert.setTextContent(String.format("alert('Success: %s')", message));
        document.getDocumentElement().appendChild(alert);
    }
}