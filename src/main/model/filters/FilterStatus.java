package model.filters;

import model.enums.Status;
import model.Media;

public class FilterStatus extends Filter {
    private Status status;
    
    public FilterStatus(Status status){
        this.status = status;
    }

    @Override
    public Boolean applyFilter(Media m){
        return (m.getStatus().equals(status));
    }
}
