package org.example;

public class ModelFilterDecorator extends FilterDecorator {
    private final String model;

    public ModelFilterDecorator(IFilterCriteria component, String model) {
        super(component);
        this.model = model;
    }

    @Override
    public boolean matches(Car car) {
        return component.matches(car) && car.getModel().equalsIgnoreCase(model);
    }

    public String getModel() {
        return model;
    }
}