package org.example;

public class NoFilter implements IFilterCriteria {
    @Override
    public boolean matches(Car car) {
        return true;
    }
}