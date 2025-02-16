package model.filters;

import model.Media;

// An abtract class of for filters that can be applied to a list of media
public abstract class Filter {
    // EFFECTS: Returns true if media m passes the filter condition
    public abstract Boolean applyFilter(Media m);
}
