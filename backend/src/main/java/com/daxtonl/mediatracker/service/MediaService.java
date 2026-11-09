package com.daxtonl.mediatracker.service;

import java.util.List;
import com.daxtonl.mediatracker.dto.MediaRequest;
import com.daxtonl.mediatracker.dto.MediaResponse;
import com.daxtonl.mediatracker.model.Media;
import com.daxtonl.mediatracker.model.Tracker;
import com.daxtonl.mediatracker.repository.MediaRepository;
import com.daxtonl.mediatracker.repository.TrackerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MediaService {
    private final MediaRepository mediaRepository;
    private final TrackerRepository trackerRepository;

    public MediaService(MediaRepository mediaRepository, TrackerRepository trackerRepository) {
        this.mediaRepository = mediaRepository;
        this.trackerRepository = trackerRepository;
    }

    private Media getOrThrow(Long id) {
        return mediaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Media " + id + " not found"));
    }

    @Transactional
    public MediaResponse create(Long trackerId, MediaRequest request) {
        // 1. Load: the tracker must exist
        Tracker tracker = trackerRepository.findById(trackerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tracker " + trackerId + " not found"));

        // 2. Check rules: no duplicate names within a tracker
        String name = request.name().trim();
        if (mediaRepository.existsByTrackerIdAndName(trackerId, name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "'" + name + "' is already in this tracker");
        }

        // 3. Do the work: create the media and attach it to the tracker
        Media media = new Media(name, request.type(), request.length(), request.priority());
        tracker.addMedia(media);
        Media saved = mediaRepository.save(media);

        // 4. Return a DTO
        return MediaResponse.from(saved);
    }

    @Transactional 
    public void delete(Long id) {
        mediaRepository.delete(getOrThrow(id));
    }

    // MediaService
    @Transactional(readOnly = true)
    public List<MediaResponse> findByTracker(Long trackerId) {
        if (!trackerRepository.existsById(trackerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Tracker " + trackerId + " not found");
        }
        return mediaRepository.findByTrackerId(trackerId).stream()
                .map(MediaResponse::from)
                .toList();
    }
}