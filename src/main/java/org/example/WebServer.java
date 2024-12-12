package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.w3c.dom.Document;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class WebServer {
    private final HttpServer server;
    private final CarController carController;
    private final WebEnvironmentInitializer webInit;
    private final CarDbRepository model;

    public WebServer(int port) throws IOException {
        // Создаем HTTP сервер
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newFixedThreadPool(10));

        // Инициализация компонентов
        this.webInit = new WebEnvironmentInitializer();
        this.model = new CarDbRepository();
        ICarView view = new CarWebView();
        this.carController = new CarController(view, model);

        // Регистрация обработчиков
        setupHandlers();
    }

    private void setupHandlers() {
        // Обработчик главной страницы
        server.createContext("/", new MainHandler());

        // Обработчик API для работы с автомобилями
        server.createContext("/api/cars", new CarsApiHandler(model));

        // Обработчик для статических ресурсов (если потребуются)
        server.createContext("/static", new StaticHandler());
    }

    public void start() {
        server.start();
        System.out.println("Server started on port " + server.getAddress().getPort());
        System.out.println("Open http://localhost:" + server.getAddress().getPort() + " in your browser");
    }

    private class MainHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                // Преобразуем DOM документ в строку HTML
                String response = convertDocumentToString(webInit.getDocument());

                // Устанавливаем заголовки
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");

                // Отправляем ответ
                byte[] responseBytes = response.getBytes("UTF-8");
                exchange.sendResponseHeaders(200, responseBytes.length);
                exchange.getResponseBody().write(responseBytes);
            } catch (Exception e) {
                String error = "Error: " + e.getMessage();
                exchange.sendResponseHeaders(500, error.length());
                exchange.getResponseBody().write(error.getBytes());
            } finally {
                exchange.getResponseBody().close();
            }
        }
    }

    private class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            path = path.substring("/static".length());

            try {
                // Здесь можно добавить логику для отдачи статических файлов
                // Например, CSS, JavaScript, изображений и т.д.
                String response = ""; // Заглушка
                exchange.getResponseHeaders().set("Content-Type", getContentType(path));
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
            } catch (Exception e) {
                String error = "Error serving static file: " + e.getMessage();
                exchange.sendResponseHeaders(404, error.length());
                exchange.getResponseBody().write(error.getBytes());
            } finally {
                exchange.getResponseBody().close();
            }
        }

        private String getContentType(String path) {
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".png")) return "image/png";
            if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
            return "text/plain";
        }
    }

    private String convertDocumentToString(Document doc) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        return writer.getBuffer().toString();
    }

    public static void main(String[] args) {
        try {
            // Устанавливаем режим веб-окружения
            System.setProperty("environment", "web");

            // Запускаем сервер на порту 8080
            WebServer server = new WebServer(8080);
            server.start();
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}