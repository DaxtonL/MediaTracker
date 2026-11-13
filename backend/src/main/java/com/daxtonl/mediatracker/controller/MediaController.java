package com.daxtonl.mediatracker.controller;
import java.util.List;

import com.daxtonl.mediatracker.dto.MediaDetailResponse;
import com.daxtonl.mediatracker.dto.MediaRequest;
import com.daxtonl.mediatracker.dto.MediaResponse;
import com.daxtonl.mediatracker.dto.MediaUpdateRequest;
import com.daxtonl.mediatracker.dto.ViewLogRequest;
import com.daxtonl.mediatracker.model.enums.MediaType;
import com.daxtonl.mediatracker.model.enums.Status;
import com.daxtonl.mediatracker.service.MediaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class MediaController {
    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/api/trackers/{trackerId}/media")
    @ResponseStatus(HttpStatus.CREATED)
    public MediaResponse create(@PathVariable Long trackerId,
                                @Valid @RequestBody MediaRequest request) {
        return mediaService.create(trackerId, request);
    }

    @DeleteMapping("/api/media/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        mediaService.delete(id);
    }

    // Optional filters, e.g. /api/trackers/1/media?status=VIEWING&type=GAME&minRating=7
    @GetMapping("/api/trackers/{trackerId}/media")
    public List<MediaResponse> findByTracker(@PathVariable Long trackerId,
                                             @RequestParam(required = false) Status status,
                                             @RequestParam(required = false) MediaType type,
                                             @RequestParam(required = false) @Min(0) @Max(10) Integer minRating,
                                             @RequestParam(required = false) @Min(0) @Max(10) Integer maxRating) {
        return mediaService.findByTracker(trackerId, status, type, minRating, maxRating);
    }

    @GetMapping("/api/media/{id}")
    public MediaDetailResponse findById(@PathVariable Long id) {
        return mediaService.findById(id);
    }

    @PatchMapping("/api/media/{id}")
    public MediaResponse update(@PathVariable Long id,
                                @Valid @RequestBody MediaUpdateRequest request) {
        return mediaService.update(id, request);
    }

    @PostMapping("/api/media/{id}/logs")
    @ResponseStatus(HttpStatus.CREATED)
    public MediaResponse logViewing(@PathVariable Long id,
                                    @Valid @RequestBody ViewLogRequest request) {
        return mediaService.logViewing(id, request);
    }

    @DeleteMapping("/api/media/{id}/logs/latest")
    public MediaResponse removeLatestLog(@PathVariable Long id) {
        return mediaService.removeLatestLog(id);
    }
}
