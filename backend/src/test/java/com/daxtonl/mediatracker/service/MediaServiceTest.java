package com.daxtonl.mediatracker.service;

import com.daxtonl.mediatracker.dto.MediaDetailResponse;
import com.daxtonl.mediatracker.dto.MediaRequest;
import com.daxtonl.mediatracker.dto.MediaResponse;
import com.daxtonl.mediatracker.dto.MediaUpdateRequest;
import com.daxtonl.mediatracker.dto.TrackerRequest;
import com.daxtonl.mediatracker.dto.ViewLogRequest;
import com.daxtonl.mediatracker.model.enums.MediaType;
import com.daxtonl.mediatracker.model.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MediaServiceTest {

    @Autowired
    private TrackerService trackerService;

    @Autowired
    private MediaService mediaService;

    private Long trackerId;
    private Long showId;

    @BeforeEach
    void setUp() {
        trackerId = trackerService.create(new TrackerRequest("Shows")).id();
        showId = mediaService.create(trackerId,
                new MediaRequest("Cowboy Bebop", MediaType.SHOW, 26, 1)).id();
    }

    @Test
    void findByTrackerListsItsMedia() {
        mediaService.create(trackerId, new MediaRequest("Soul Eater", MediaType.SHOW, 51, null));
        assertEquals(2, mediaService.findByTracker(trackerId).size());
    }

    @Test
    void findByIdIncludesLogs() {
        mediaService.logViewing(showId, new ViewLogRequest(LocalDate.of(2026, 11, 1), 5));
        MediaDetailResponse detail = mediaService.findById(showId);
        assertEquals("Cowboy Bebop", detail.name());
        assertEquals(1, detail.logs().size());
        assertEquals(5, detail.logs().get(0).viewProgress());
    }

    @Test
    void findByIdThrows404WhenMissing() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> mediaService.findById(999L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void updateChangesOnlyGivenFields() {
        MediaResponse updated = mediaService.update(showId,
                new MediaUpdateRequest(null, null, Status.HOLD, null, null, 9));
        assertEquals("Cowboy Bebop", updated.name());   // unchanged
        assertEquals(26, updated.length());              // unchanged
        assertEquals(Status.HOLD, updated.status());
        assertEquals(9, updated.rating());
    }

    @Test
    void updateToDuplicateNameThrows409() {
        mediaService.create(trackerId, new MediaRequest("Soul Eater", MediaType.SHOW, 51, null));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> mediaService.update(showId,
                        new MediaUpdateRequest("Soul Eater", null, null, null, null, null)));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void updateKeepingSameNameIsAllowed() {
        MediaResponse updated = mediaService.update(showId,
                new MediaUpdateRequest("Cowboy Bebop", null, null, null, null, 8));
        assertEquals(8, updated.rating());
    }

    @Test
    void loggingUpdatesProgressAndStatus() {
        MediaResponse afterFirst = mediaService.logViewing(showId,
                new ViewLogRequest(LocalDate.of(2026, 11, 1), 20));
        assertEquals(Status.VIEWING, afterFirst.status());
        assertEquals(20, afterFirst.totalProgress());

        MediaResponse afterSecond = mediaService.logViewing(showId,
                new ViewLogRequest(LocalDate.of(2026, 11, 2), 6));   // 20 + 6 = 26 = length
        assertEquals(Status.FINISHED, afterSecond.status());
    }

    @Test
    void removeLatestLogRemovesOnlyTheLastOne() {
        mediaService.logViewing(showId, new ViewLogRequest(LocalDate.of(2026, 11, 1), 5));
        mediaService.logViewing(showId, new ViewLogRequest(LocalDate.of(2026, 11, 2), 3));

        MediaResponse after = mediaService.removeLatestLog(showId);
        assertEquals(5, after.totalProgress());
    }

    @Test
    void removeLatestLogWithNoLogsThrows404() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> mediaService.removeLatestLog(showId));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }
}
