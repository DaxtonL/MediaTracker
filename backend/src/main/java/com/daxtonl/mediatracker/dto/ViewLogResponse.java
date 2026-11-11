package com.daxtonl.mediatracker.dto;

import com.daxtonl.mediatracker.model.ViewLog;

import java.time.LocalDate;

public record ViewLogResponse(Long id, LocalDate viewedOn, int viewProgress) {

    public static ViewLogResponse from(ViewLog log) {
        return new ViewLogResponse(log.getId(), log.getViewedOn(), log.getViewProgress());
    }
}
