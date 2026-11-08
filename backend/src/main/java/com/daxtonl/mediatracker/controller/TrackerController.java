package com.daxtonl.mediatracker.controller;

import com.daxtonl.mediatracker.dto.TrackerRequest;
import com.daxtonl.mediatracker.dto.TrackerResponse;
import com.daxtonl.mediatracker.service.TrackerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trackers")
public class TrackerController {
    private final TrackerService trackerService;

    public TrackerController(TrackerService trackerService) {
        this.trackerService = trackerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrackerResponse create(@Valid @RequestBody TrackerRequest request) {
        return trackerService.create(request);
    }

    @GetMapping
    public List<TrackerResponse> findAll() {
        return trackerService.findAll();
    }
}