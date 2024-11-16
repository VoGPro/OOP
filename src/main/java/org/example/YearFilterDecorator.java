package org.example;

public class YearFilterDecorator extends FilterDecorator {
    private final int year;

    public YearFilterDecorator(IFilterCriteria component, String year) {
        super(component);
        this.year = Integer.parseInt(year);
    }

    @Override
    public boolean matches(Car car) {
        return component.matches(car) && car.getYear() == year;
    }

    public int getYear() {
        return year;
    }
}