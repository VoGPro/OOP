package org.example;

public class BrandFilterDecorator extends FilterDecorator {
    private final String brand;

    public BrandFilterDecorator(IFilterCriteria component, String brand) {
        super(component);
        this.brand = brand;
    }

    @Override
    public boolean matches(Car car) {
        return component.matches(car) && car.getBrand().equalsIgnoreCase(brand);
    }

    public String getBrand() {
        return brand;
    }
}