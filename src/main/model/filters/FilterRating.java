package model.filters;

import model.Media;

// Represents a filter based on a filter's rating
public class FilterRating extends Filter {
    private Boolean greaterThan;
    private Integer amount;

    public FilterRating(Boolean greaterThan, Integer amount) {
        this.greaterThan = greaterThan;
        this.amount = amount;
    }

    @Override
    public Boolean applyFilter(Media m) {
        if (greaterThan) {
            return (m.getRating() >= amount);
        } else {
            return (m.getRating() <= amount);
        }
    }
}