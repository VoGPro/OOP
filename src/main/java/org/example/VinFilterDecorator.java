package org.example;

public class VinFilterDecorator extends FilterDecorator {
    private final String vin;

    public VinFilterDecorator(IFilterCriteria component, String vin) {
        super(component);
        this.vin = vin;
    }

    @Override
    public boolean matches(Car car) {
        return component.matches(car) && car.getVin().toLowerCase().contains(vin.toLowerCase());
    }

    public String getVin() {
        return vin;
    }
}