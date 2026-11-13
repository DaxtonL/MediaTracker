package com.daxtonl.mediatracker.importer;

import com.daxtonl.mediatracker.importer.JsonTrackerImporter.Result;
import com.daxtonl.mediatracker.model.Media;
import com.daxtonl.mediatracker.model.Tracker;
import com.daxtonl.mediatracker.model.enums.MediaType;
import com.daxtonl.mediatracker.model.enums.Status;
import com.daxtonl.mediatracker.repository.MediaRepository;
import com.daxtonl.mediatracker.repository.TrackerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Ported from the old TestJsonReader, plus the cases the database adds.
@SpringBootTest
@Transactional
class JsonTrackerImporterTest {

    @Autowired
    private JsonTrackerImporter importer;

    @Autowired
    private TrackerRepository trackerRepository;

    @Autowired
    private MediaRepository mediaRepository;

    private static Path fixture(String name) throws Exception {
        return Path.of(JsonTrackerImporterTest.class.getResource("/import/" + name).toURI());
    }

    private Tracker tracker(String name) {
        return trackerRepository.findByName(name).orElseThrow();
    }

    private Media media(Tracker tracker, String name) {
        return mediaRepository.findByTrackerId(tracker.getId()).stream()
                .filter(m -> m.getName().equals(name))
                .findFirst().orElseThrow();
    }

    @Test
    void importsEmptyTracker() throws Exception {
        assertEquals(Result.IMPORTED, importer.importFile(fixture("empty.json")));

        Tracker t = tracker("My media tracker");
        assertTrue(mediaRepository.findByTrackerId(t.getId()).isEmpty());
    }

    @Test
    void importsGeneralTracker() throws Exception {
        assertEquals(Result.IMPORTED, importer.importFile(fixture("general.json")));

        Tracker t = tracker("My media tracker");
        assertEquals(2, mediaRepository.findByTrackerId(t.getId()).size());

        Media celeste = media(t, "Celeste");
        assertEquals(MediaType.GAME, celeste.getType());
        assertEquals(Status.VIEWING, celeste.getStatus());
        assertNull(celeste.getLength());     // -1 in the file
        assertEquals(2, celeste.getPriority());
        assertNull(celeste.getRating());     // -1 in the file
        assertEquals(1, celeste.getLogs().size());
        assertEquals(LocalDate.of(2025, 1, 13), celeste.getLogs().get(0).getViewedOn());
        assertEquals(99, celeste.getTotalViewProgress());

        Media bebop = media(t, "Cowboy Bebop");
        assertEquals(MediaType.SHOW, bebop.getType());
        assertEquals(Status.FINISHED, bebop.getStatus());
        assertEquals(26, bebop.getLength());
        assertNull(bebop.getPriority());
        assertEquals(9, bebop.getRating());
    }

    @Test
    void keepsSavedStatusEvenWithLogs() throws Exception {
        importer.importFile(fixture("edge-cases.json"));

        // logViewing() would set VIEWING; the file says DROPPED
        Media berserk = media(tracker("Edge cases"), "Berserk");
        assertEquals(Status.DROPPED, berserk.getStatus());
        assertEquals(40, berserk.getTotalViewProgress());
    }

    @Test
    void skipsDuplicateMediaNamesInOneFile() throws Exception {
        importer.importFile(fixture("edge-cases.json"));

        List<Media> media = mediaRepository.findByTrackerId(tracker("Edge cases").getId());
        assertEquals(1, media.size());   // the first "Berserk" wins
        assertEquals(Status.DROPPED, media.get(0).getStatus());
    }

    @Test
    void skipsTrackerThatAlreadyExists() throws Exception {
        assertEquals(Result.IMPORTED, importer.importFile(fixture("general.json")));
        assertEquals(Result.SKIPPED_EXISTS, importer.importFile(fixture("general.json")));

        assertEquals(1, trackerRepository.count());
        assertEquals(2, mediaRepository.findByTrackerId(tracker("My media tracker").getId()).size());
    }

    @Test
    void invalidFileSavesNothing() throws Exception {
        assertThrows(IllegalArgumentException.class,
                () -> importer.importFile(fixture("invalid-type.json")));

        assertTrue(trackerRepository.findByName("Broken").isEmpty());
    }
}
