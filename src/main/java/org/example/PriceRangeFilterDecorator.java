package org.example;

public class PriceRangeFilterDecorator extends FilterDecorator {
    private final double minPrice;
    private final double maxPrice;

    public PriceRangeFilterDecorator(IFilterCriteria component, double minPrice, double maxPrice) {
        super(component);
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public boolean matches(Car car) {
        return component.matches(car) &&
                car.getPrice() >= minPrice &&
                car.getPrice() <= maxPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }
}