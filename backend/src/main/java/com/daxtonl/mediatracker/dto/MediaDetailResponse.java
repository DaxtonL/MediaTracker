package com.daxtonl.mediatracker.dto;

import com.daxtonl.mediatracker.model.Media;
import com.daxtonl.mediatracker.model.enums.MediaType;
import com.daxtonl.mediatracker.model.enums.Status;

import java.util.List;

// Everything in MediaResponse, plus the full viewing history
public record MediaDetailResponse(
        Long id,
        String name,
        MediaType type,
        Status status,
        Integer length,
        Integer priority,
        Integer rating,
        int totalProgress,
        String unit,
        List<ViewLogResponse> logs
) {
    public static MediaDetailResponse from(Media media) {
        return new MediaDetailResponse(
                media.getId(),
                media.getName(),
                media.getType(),
                media.getStatus(),
                media.getLength(),
                media.getPriority(),
                media.getRating(),
                media.getTotalViewProgress(),
                media.getType().getIncrement(),
                media.getLogs().stream().map(ViewLogResponse::from).toList());
    }
}
