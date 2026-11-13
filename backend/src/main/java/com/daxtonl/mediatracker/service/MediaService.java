package com.daxtonl.mediatracker.service;

import java.util.List;
import com.daxtonl.mediatracker.dto.MediaDetailResponse;
import com.daxtonl.mediatracker.dto.MediaRequest;
import com.daxtonl.mediatracker.dto.MediaResponse;
import com.daxtonl.mediatracker.dto.MediaUpdateRequest;
import com.daxtonl.mediatracker.dto.ViewLogRequest;
import com.daxtonl.mediatracker.model.Media;
import com.daxtonl.mediatracker.model.Tracker;
import com.daxtonl.mediatracker.model.ViewLog;
import com.daxtonl.mediatracker.model.enums.MediaType;
import com.daxtonl.mediatracker.model.enums.Status;
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

    @Transactional(readOnly = true)
    public List<MediaResponse> findByTracker(Long trackerId) {
        return findByTracker(trackerId, null, null, null, null);
    }

    // Lists a tracker's media, keeping only media that pass every given filter.
    // Any filter left null is ignored.
    @Transactional(readOnly = true)
    public List<MediaResponse> findByTracker(Long trackerId, Status status, MediaType type,
                                             Integer minRating, Integer maxRating) {
        if (!trackerRepository.existsById(trackerId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Tracker " + trackerId + " not found");
        }
        if (minRating != null && maxRating != null && minRating > maxRating) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "minRating cannot be greater than maxRating");
        }
        return mediaRepository.findFiltered(trackerId, status, type, minRating, maxRating).stream()
                .map(MediaResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MediaDetailResponse findById(Long id) {
        return MediaDetailResponse.from(getOrThrow(id));
    }

    @Transactional
    public MediaResponse update(Long id, MediaUpdateRequest request) {
        Media media = getOrThrow(id);

        if (request.name() != null) {
            String name = request.name().trim();
            if (name.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be blank");
            }
            // Only check for duplicates if the name is actually changing
            if (!name.equals(media.getName())
                    && mediaRepository.existsByTrackerIdAndName(media.getTracker().getId(), name)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "'" + name + "' is already in this tracker");
            }
            media.setName(name);
        }
        if (request.type() != null) media.setType(request.type());
        if (request.status() != null) media.setStatus(request.status());
        if (request.length() != null) media.setLength(request.length());
        if (request.priority() != null) media.setPriority(request.priority());
        if (request.rating() != null) media.setRating(request.rating());

        // No save() needed: Hibernate saves changes to loaded entities when the transaction ends
        return MediaResponse.from(media);
    }

    @Transactional
    public MediaResponse logViewing(Long id, ViewLogRequest request) {
        Media media = getOrThrow(id);
        media.logViewing(new ViewLog(request.viewedOn(), request.viewProgress()));
        // No save() needed: the new log is saved through the cascade when the transaction ends
        return MediaResponse.from(media);
    }

    @Transactional
    public MediaResponse removeLatestLog(Long id) {
        Media media = getOrThrow(id);
        if (media.getLogs().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Media " + id + " has no logs to remove");
        }
        media.removeLatestLog();
        return MediaResponse.from(media);
    }
}
