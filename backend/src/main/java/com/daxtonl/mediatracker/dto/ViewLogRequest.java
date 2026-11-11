package com.daxtonl.mediatracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ViewLogRequest(
        @NotNull @PastOrPresent LocalDate viewedOn,
        @NotNull @Positive Integer viewProgress
) {}
