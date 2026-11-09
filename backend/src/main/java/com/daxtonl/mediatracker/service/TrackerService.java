package com.daxtonl.mediatracker.service;

import com.daxtonl.mediatracker.dto.TrackerRequest;
import com.daxtonl.mediatracker.dto.TrackerResponse;
import com.daxtonl.mediatracker.model.Tracker;
import com.daxtonl.mediatracker.repository.TrackerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TrackerService {
    private final TrackerRepository trackerRepository;

    public TrackerService(TrackerRepository trackerRepository) {
        this.trackerRepository = trackerRepository;
    }

    @Transactional
    public TrackerResponse create(TrackerRequest request) {
        String name = request.name().trim();
        if (trackerRepository.existsByName(name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A tracker named '" + name + "' already exists");
        }
        Tracker saved = trackerRepository.save(new Tracker(name));
        return TrackerResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<TrackerResponse> findAll() {
        return trackerRepository.findAll().stream()
                .map(TrackerResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TrackerResponse findById(Long id) {
        Tracker tracker = trackerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tracker " + id + " not found"));
        return TrackerResponse.from(tracker);
    }
}