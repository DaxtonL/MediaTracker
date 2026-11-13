package com.daxtonl.mediatracker.importer;

import com.daxtonl.mediatracker.importer.LegacyTrackerJson.LegacyLogJson;
import com.daxtonl.mediatracker.importer.LegacyTrackerJson.LegacyMediaJson;
import com.daxtonl.mediatracker.model.Media;
import com.daxtonl.mediatracker.model.Tracker;
import com.daxtonl.mediatracker.model.ViewLog;
import com.daxtonl.mediatracker.model.enums.MediaType;
import com.daxtonl.mediatracker.model.enums.Status;
import com.daxtonl.mediatracker.repository.TrackerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Replaces the old JsonReader: reads one Swing-era tracker file and saves it to the database.
@Service
public class JsonTrackerImporter {
    private static final Logger log = LoggerFactory.getLogger(JsonTrackerImporter.class);

    private final TrackerRepository trackerRepository;
    private final ObjectMapper objectMapper;

    public JsonTrackerImporter(TrackerRepository trackerRepository, ObjectMapper objectMapper) {
        this.trackerRepository = trackerRepository;
        this.objectMapper = objectMapper;
    }

    public enum Result { IMPORTED, SKIPPED_EXISTS }

    // One transaction per file: if anything in the file is bad, none of it is saved.
    @Transactional
    public Result importFile(Path file) {
        LegacyTrackerJson json = objectMapper.readValue(file.toFile(), LegacyTrackerJson.class);
        String trackerName = required(json.name(), "tracker name").trim();

        // Tracker names are unique. Skipping means re-running the import is safe.
        if (trackerRepository.existsByName(trackerName)) {
            log.info("Skipping {}: tracker '{}' already exists", file.getFileName(), trackerName);
            return Result.SKIPPED_EXISTS;
        }

        Tracker tracker = new Tracker(trackerName);
        Set<String> seenNames = new HashSet<>();
        for (LegacyMediaJson m : orEmpty(json.mediaList())) {
            String name = required(m.name(), "media name").trim();
            if (!seenNames.add(name)) {
                log.warn("Skipping duplicate media '{}' in {}", name, file.getFileName());
                continue;
            }
            tracker.addMedia(toMedia(name, m));
        }

        trackerRepository.save(tracker);   // cascades to media and logs
        log.info("Imported tracker '{}' with {} media from {}",
                trackerName, tracker.getMediaList().size(), file.getFileName());
        return Result.IMPORTED;
    }

    private Media toMedia(String name, LegacyMediaJson m) {
        Media media = new Media(name,
                MediaType.valueOf(required(m.type(), "type")),
                optionalInt(m.length()),
                optionalInt(m.priority()));
        media.setRating(optionalInt(m.rating()));

        for (LegacyLogJson l : orEmpty(m.log())) {
            LocalDate date = LocalDate.of(
                    Integer.parseInt(required(l.year(), "log year")),
                    Month.valueOf(required(l.month(), "log month")),
                    Integer.parseInt(required(l.day(), "log day")));
            media.logViewing(new ViewLog(date, Integer.parseInt(required(l.viewProgress(), "viewProgress"))));
        }

        // logViewing() recalculates the status, so restore the saved one afterwards
        // (e.g. media you dropped or put on hold after logging some views).
        media.setStatus(m.status() == null ? Status.WAITLIST : Status.valueOf(m.status()));
        return media;
    }

    // The old app wrote -1 for "not set"; the new model uses null.
    private static Integer optionalInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        int n = Integer.parseInt(value.trim());
        return n == -1 ? null : n;
    }

    private static String required(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing " + field);
        }
        return value;
    }

    private static <T> List<T> orEmpty(List<T> list) {
        return list == null ? List.of() : list;
    }
}
