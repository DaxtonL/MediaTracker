package com.daxtonl.mediatracker.dto;

import jakarta.validation.constraints.NotBlank;

public record TrackerRequest(@NotBlank String name) {}