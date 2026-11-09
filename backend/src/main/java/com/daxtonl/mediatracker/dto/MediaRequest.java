package com.daxtonl.mediatracker.dto;

import com.daxtonl.mediatracker.model.enums.MediaType;
import jakarta.validation.constraints.*;

public record MediaRequest(
        @NotBlank String name,
        @NotNull MediaType type,
        @Positive Integer length,     // null allowed = unknown
        @Positive Integer priority    // null allowed = none
) {}
