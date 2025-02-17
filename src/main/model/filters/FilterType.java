package model.filters;

import model.enums.MediaType;
import model.Media;

// Represents a filter based on a media's type
public class FilterType extends Filter {
    private MediaType type;

    public FilterType(MediaType type) {
        this.type = type;
    }

    public Boolean applyFilter(Media m) {
        return (m.getType().equals(type));
    }
}
