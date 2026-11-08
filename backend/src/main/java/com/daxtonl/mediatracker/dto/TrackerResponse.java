package com.daxtonl.mediatracker.dto;

import com.daxtonl.mediatracker.model.Tracker;

public record TrackerResponse(Long id, String name, int mediaCount) {

    public static TrackerResponse from(Tracker tracker) {
        return new TrackerResponse(
                tracker.getId(),
                tracker.getName(),
                tracker.getMediaList().size());
    }
}