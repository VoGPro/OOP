package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CarsApiHandler implements HttpHandler {
    private final CarDbRepository repository;
    private final ObjectMapper objectMapper;

    public CarsApiHandler(CarDbRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String response = "";
        int statusCode = 200;

        try {
            switch (method) {
                case "GET":
                    response = handleGet(exchange);
                    break;
                case "POST":
                    response = handlePost(exchange);
                    break;
                case "PUT":
                    response = handlePut(exchange);
                    break;
                case "DELETE":
                    response = handleDelete(exchange);
                    break;
                default:
                    statusCode = 405;
                    response = createErrorResponse("Method not allowed");
            }
        } catch (Exception e) {
            statusCode = 500;
            response = createErrorResponse(e.getMessage());
        }

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        byte[] responseBytes = response.getBytes();
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.getResponseBody().close();
    }

    private String handleGet(HttpExchange exchange) throws Exception {
        Map<String, String> params = parseQueryString(exchange.getRequestURI().getQuery());

        // Если запрашивается конкретный автомобиль
        if (params.containsKey("id")) {
            int carId = Integer.parseInt(params.get("id"));
            Car car = repository.getById(carId);
            return objectMapper.writeValueAsString(car);
        }

        // Иначе возвращаем список с пагинацией
        int page = Integer.parseInt(params.getOrDefault("page", "0"));
        String sortField = params.getOrDefault("sort", "car_id");

        FilterBuilder filterBuilder = new FilterBuilder();
        if (params.containsKey("vin")) {
            filterBuilder.withVin(params.get("vin"));
        }
        if (params.containsKey("brand")) {
            filterBuilder.withBrand(params.get("brand"));
        }
        if (params.containsKey("model")) {
            filterBuilder.withModel(params.get("model"));
        }

        IFilterCriteria filter = filterBuilder.build();
        List<Car> cars = repository.get_k_n_short_list(page, repository.getPageSize(), filter, sortField);

        Map<String, Object> response = new HashMap<>();
        response.put("cars", cars);
        response.put("totalPages", repository.getTotalPages());
        response.put("currentPage", page);

        return objectMapper.writeValueAsString(response);
    }

    private String handlePost(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Car car = objectMapper.readValue(requestBody, Car.class);
        repository.add(car);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Car added successfully");

        return objectMapper.writeValueAsString(response);
    }

    private String handlePut(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Car car = objectMapper.readValue(requestBody, Car.class);
        repository.update(car);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Car updated successfully");

        return objectMapper.writeValueAsString(response);
    }

    private String handleDelete(HttpExchange exchange) throws IOException {
        Map<String, String> params = parseQueryString(exchange.getRequestURI().getQuery());
        int carId = Integer.parseInt(params.get("id"));
        repository.delete(carId);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Car deleted successfully");

        return objectMapper.writeValueAsString(response);
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    private Map<String, String> parseQueryString(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    params.put(entry[0], entry[1]);
                } else {
                    params.put(entry[0], "");
                }
            }
        }
        return params;
    }

    private String createErrorResponse(String message) throws IOException {
        Map<String, String> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return objectMapper.writeValueAsString(response);
    }
}