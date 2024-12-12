package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.swing.table.TableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;

public class CarWebView implements ICarView {
    private final Document document;
    private TableModel tableModel;
    private Map<String, Runnable> actionListeners = new HashMap<>();

    public CarWebView() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.newDocument();
            initializeLayout();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize web view", e);
        }
    }

    private void initializeLayout() {
        Element html = document.createElement("html");
        Element body = document.createElement("body");

        Element app = document.createElement("div");
        app.setAttribute("id", "app");

        // Controls section
        Element controls = document.createElement("div");
        controls.setAttribute("id", "controls");
        addButton(controls, "addBtn", "Add");
        addButton(controls, "editBtn", "Edit");
        addButton(controls, "deleteBtn", "Delete");

        // Filters section
        Element filters = document.createElement("div");
        filters.setAttribute("id", "filters");
        addInput(filters, "vinFilter", "VIN");
        addInput(filters, "brandFilter", "Brand");
        addInput(filters, "modelFilter", "Model");
        addButton(filters, "applyFiltersBtn", "Apply");
        addButton(filters, "clearFiltersBtn", "Clear");

        // Table section
        Element table = document.createElement("table");
        table.setAttribute("id", "carTable");

        // Navigation section
        Element navigation = document.createElement("div");
        navigation.setAttribute("id", "navigation");
        addButton(navigation, "prevBtn", "Previous");
        Element pageInfo = document.createElement("span");
        pageInfo.setAttribute("id", "pageInfo");
        addButton(navigation, "nextBtn", "Next");

        navigation.appendChild(pageInfo);

        app.appendChild(controls);
        app.appendChild(filters);
        app.appendChild(table);
        app.appendChild(navigation);
        body.appendChild(app);
        html.appendChild(body);
        document.appendChild(html);
    }

    private void addButton(Element parent, String id, String text) {
        Element button = document.createElement("button");
        button.setAttribute("id", id);
        button.setTextContent(text);
        parent.appendChild(button);
    }

    private void addInput(Element parent, String id, String placeholder) {
        Element input = document.createElement("input");
        input.setAttribute("id", id);
        input.setAttribute("placeholder", placeholder);
        parent.appendChild(input);
    }

    @Override
    public void setVisible(boolean visible) {
        Element app = document.getElementById("app");
        if (app != null) {
            app.setAttribute("style", "display: " + (visible ? "block" : "none"));
        }
    }

    @Override
    public void refreshView() {
        Element table = document.getElementById("carTable");
        if (table != null) {
            // Clear existing content
            while (table.hasChildNodes()) {
                table.removeChild(table.getFirstChild());
            }

            // Add headers
            Element header = document.createElement("tr");
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                Element th = document.createElement("th");
                th.setTextContent(tableModel.getColumnName(i));
                header.appendChild(th);
            }
            table.appendChild(header);

            // Add data rows
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                Element tr = document.createElement("tr");
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    Element td = document.createElement("td");
                    td.setTextContent(String.valueOf(tableModel.getValueAt(row, col)));
                    tr.appendChild(td);
                }
                table.appendChild(tr);
            }
        }
    }

    @Override
    public void showMessage(String message, String title, MessageType type) {
        Element alert = document.createElement("script");
        alert.setTextContent(String.format("alert('%s: %s')", title, message));
        document.getDocumentElement().appendChild(alert);
    }

    @Override
    public void setPageInfo(int currentPage, int totalPages) {
        Element pageInfo = document.getElementById("pageInfo");
        if (pageInfo != null) {
            pageInfo.setTextContent(String.format("Page: %d/%d", currentPage + 1, totalPages));
        }
    }

    @Override
    public void updateNavigationButtons(boolean hasPrevious, boolean hasNext) {
        Element prevBtn = document.getElementById("prevBtn");
        Element nextBtn = document.getElementById("nextBtn");

        if (prevBtn != null) {
            prevBtn.setAttribute("disabled", String.valueOf(!hasPrevious));
        }
        if (nextBtn != null) {
            nextBtn.setAttribute("disabled", String.valueOf(!hasNext));
        }
    }

    @Override
    public String getVinFilter() {
        Element input = document.getElementById("vinFilter");
        return input != null ? input.getAttribute("value") : "";
    }

    @Override
    public String getBrandFilter() {
        Element input = document.getElementById("brandFilter");
        return input != null ? input.getAttribute("value") : "";
    }

    @Override
    public String getModelFilter() {
        Element input = document.getElementById("modelFilter");
        return input != null ? input.getAttribute("value") : "";
    }

    @Override
    public void clearFilters() {
        Element vinFilter = document.getElementById("vinFilter");
        Element brandFilter = document.getElementById("brandFilter");
        Element modelFilter = document.getElementById("modelFilter");

        if (vinFilter != null) vinFilter.setAttribute("value", "");
        if (brandFilter != null) brandFilter.setAttribute("value", "");
        if (modelFilter != null) modelFilter.setAttribute("value", "");
    }

    @Override
    public void setTableData(TableModel model) {
        this.tableModel = model;
        refreshView();
    }

    @Override
    public int getSelectedRow() {
        Element table = document.getElementById("carTable");
        if (table != null) {
            Element selectedRow = findFirstElementByClass(table, "selected");
            if (selectedRow != null) {
                return Integer.parseInt(selectedRow.getAttribute("data-row-index"));
            }
        }
        return -1;
    }

    private Element findFirstElementByClass(Element parent, String className) {
        // Simple implementation to find element by class
        if (parent.getAttribute("class") != null &&
                parent.getAttribute("class").contains(className)) {
            return parent;
        }

        Element[] children = new Element[parent.getChildNodes().getLength()];
        for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
            if (parent.getChildNodes().item(i) instanceof Element) {
                Element child = (Element) parent.getChildNodes().item(i);
                Element found = findFirstElementByClass(child, className);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    @Override
    public String getValueAt(int row, int column) {
        return tableModel.getValueAt(row, column).toString();
    }

    @Override
    public void addActionListener(String actionName, Runnable action) {
        actionListeners.put(actionName, action);
        String elementId = switch(actionName) {
            case "add" -> "addBtn";
            case "edit" -> "editBtn";
            case "delete" -> "deleteBtn";
            case "previous" -> "prevBtn";
            case "next" -> "nextBtn";
            case "applyFilters" -> "applyFiltersBtn";
            case "clearFilters" -> "clearFiltersBtn";
            default -> throw new IllegalArgumentException("Unknown action: " + actionName);
        };

        Element element = document.getElementById(elementId);
        if (element != null) {
            // В реальном веб-приложении здесь бы добавлялся JavaScript обработчик
            element.setAttribute("onclick",
                    String.format("javascript:window.dispatchEvent(new CustomEvent('action', {detail: '%s'}))", actionName));
        }
    }

    public Document getDocument() {
        return document;
    }
}