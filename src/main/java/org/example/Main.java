package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ICarRepository baseRepository = new CarDbRepository(); // или CarFileRepositoryAdapter

            // Wrap with observable decorator
            ObservableCarRepository observableRepository = new ObservableCarRepository(baseRepository);

            // Create MVC components
            CarTableModel model = new CarTableModel(observableRepository);
            CarView view = new CarView();
            CarController controller = new CarController(view, model);
        });
    }
}