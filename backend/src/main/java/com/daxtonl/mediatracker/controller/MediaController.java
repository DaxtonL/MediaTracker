package com.daxtonl.mediatracker.controller;
import java.util.List;

import com.daxtonl.mediatracker.dto.MediaRequest;
import com.daxtonl.mediatracker.dto.MediaResponse;
import com.daxtonl.mediatracker.service.MediaService;
import jakarta.validation.Valid;
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

    @GetMapping("/api/trackes/{trackerId}/media")
    public List<MediaResponse> findByTracker(@PathVariable Long trackerId) {
        return mediaService.findByTracker(trackerId);
    }
}