package org.example;

import java.awt.*;

public class CarDialogController {
    private final CarDialogView view;
    private final ICarRepository repository;
    private final ICarDialogStrategy strategy;

    public CarDialogController(Frame owner, String title, ICarRepository repository, ICarDialogStrategy strategy) {
        this.view = new CarDialogView(owner, title);
        this.repository = repository;
        this.strategy = strategy;

        initializeListeners();
    }

    private void initializeListeners() {
        view.getSaveButton().addActionListener(e -> strategy.processSave(this));
        view.getCancelButton().addActionListener(e -> view.dispose());
    }

    public CarDialogView getView() {
        return view;
    }

    public ICarRepository getRepository() {
        return repository;
    }

    public void showDialog() {
        view.setVisible(true);
    }
}
