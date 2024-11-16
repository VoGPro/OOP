package org.example;

public class TypeFilterDecorator extends FilterDecorator {
    private final String type;

    public TypeFilterDecorator(IFilterCriteria component, String type) {
        super(component);
        this.type = type;
    }

    @Override
    public boolean matches(Car car) {
        return component.matches(car) && car.getType().equalsIgnoreCase(type);
    }

    public String getType() {
        return type;
    }
}