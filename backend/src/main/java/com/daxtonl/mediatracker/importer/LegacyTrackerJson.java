package com.daxtonl.mediatracker.importer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

// The shape of the files the old Swing app's JsonWriter produced (data/*.json).
// Every value was written as a string, and -1 meant "not set".
@JsonIgnoreProperties(ignoreUnknown = true)
public record LegacyTrackerJson(String name, List<LegacyMediaJson> mediaList) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LegacyMediaJson(
            String name,
            String type,
            String length,
            String status,
            String priority,
            String rating,
            List<LegacyLogJson> log) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LegacyLogJson(String year, String month, String day, String viewProgress) {
    }
}
