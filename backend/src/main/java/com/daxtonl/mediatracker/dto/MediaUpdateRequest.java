package com.daxtonl.mediatracker.dto;

import com.daxtonl.mediatracker.model.enums.MediaType;
import com.daxtonl.mediatracker.model.enums.Status;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// Every field is optional: null means "don't change this field"
public record MediaUpdateRequest(
        @Size(min = 1) String name,
        MediaType type,
        Status status,
        @Positive Integer length,
        @Positive Integer priority,
        @Min(0) @Max(10) Integer rating
) {}
