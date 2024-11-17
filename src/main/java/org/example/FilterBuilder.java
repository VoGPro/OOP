package org.example;

public class FilterBuilder {
    private IFilterCriteria filter;

    public FilterBuilder() {
        this.filter = new NoFilter();
    }

    public FilterBuilder withBrand(String brand) {
        if (brand != null && !brand.isEmpty()) {
            filter = new BrandFilterDecorator(filter, brand);
        }
        return this;
    }

    public FilterBuilder withModel(String model) {
        if (model != null && !model.isEmpty()) {
            filter = new ModelFilterDecorator(filter, model);
        }
        return this;
    }

    public FilterBuilder withType(String type) {
        if (type != null && !type.isEmpty()) {
            filter = new TypeFilterDecorator(filter, type);
        }
        return this;
    }

    public FilterBuilder withYear(String year) {
        if (year != null && !year.isEmpty()) {
            filter = new YearFilterDecorator(filter, year);
        }
        return this;
    }

    public FilterBuilder withPriceRange(double minPrice, double maxPrice) {
        filter = new PriceRangeFilterDecorator(filter, minPrice, maxPrice);
        return this;
    }

    public IFilterCriteria build() {
        return filter;
    }
}