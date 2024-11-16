package org.example;

public abstract class FilterDecorator implements IFilterCriteria {
    protected IFilterCriteria component;

    public FilterDecorator(IFilterCriteria component) {
        this.component = component;
    }

    @Override
    public boolean matches(Car car) {
        return component.matches(car);
    }
}