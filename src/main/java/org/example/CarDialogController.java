package org.example;

public class CarDialogController {
    private final ICarDialogView view;
    private final ICarRepository repository;
    private final ICarDialogStrategy strategy;

    public CarDialogController(ICarRepository repository, ICarDialogStrategy strategy) {
        this.repository = repository;
        this.strategy = strategy;

        // Определяем тип view на основе текущего окружения
        this.view = isWebEnvironment() ?
                new CarWebDialogView() :
                new CarDialogView(null, "Car Dialog");

        initializeListeners();
    }

    private boolean isWebEnvironment() {
        // Определение окружения (web/desktop) может быть реализовано различными способами
        // Например, через системные property или конфигурацию
        return System.getProperty("environment", "desktop").equals("web");
    }

    private void initializeListeners() {
        view.addSaveActionListener(() -> strategy.processSave(this));
        view.addCancelActionListener(() -> view.dispose());
    }

    public ICarDialogView getView() {
        return view;
    }

    public ICarRepository getRepository() {
        return repository;
    }

    public void showDialog() {
        view.setVisible(true);
    }
}