package com.daxtonl.mediatracker.dto;

import com.daxtonl.mediatracker.model.Media;
import com.daxtonl.mediatracker.model.enums.MediaType;
import com.daxtonl.mediatracker.model.enums.Status;

public record MediaResponse(
        Long id,
        String name,
        MediaType type,
        Status status,
        Integer length,
        Integer priority,
        Integer rating,
        int totalProgress,
        String unit
) {
    public static MediaResponse from(Media media) {
        return new MediaResponse(
                media.getId(),
                media.getName(),
                media.getType(),
                media.getStatus(),
                media.getLength(),
                media.getPriority(),
                media.getRating(),
                media.getTotalViewProgress(),
                media.getType().getIncrement());
    }
}