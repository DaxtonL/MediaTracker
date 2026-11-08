package com.daxtonl.mediatracker.model;

import com.daxtonl.mediatracker.model.enums.MediaType;
import com.daxtonl.mediatracker.model.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MediaTest {
    private Media testShow;
    private Media m2;
    private List<ViewLog> testLog;
    private ViewLog view1;
    private ViewLog view2;
    private ViewLog view3;

    @BeforeEach
    void runBefore() {
        testShow = new Media("Cowboy Bebop", MediaType.SHOW, 26, 1);
        m2 = new Media("Shin Megami Tensei V", MediaType.GAME, null, 3);
        testLog = new ArrayList<>();
        view1 = new ViewLog(LocalDate.of(2024, Month.MARCH, 13), 5);
        view2 = new ViewLog(LocalDate.of(2024, Month.MARCH, 15), 14);
        view3 = new ViewLog(LocalDate.of(2024, Month.MARCH, 16), 7);
    }

    @Test
    void testConstructor() {
        assertEquals("Cowboy Bebop", testShow.getName());
        assertEquals(MediaType.SHOW, testShow.getType());
        assertEquals(26, testShow.getLength());
        assertEquals(1, testShow.getPriority());
        assertEquals(testLog, testShow.getLogs());
        assertEquals(Status.WAITLIST, testShow.getStatus());
        assertNull(testShow.getRating());
    }

    @Test
    void testLogViewingOnce() {
        ViewLog view = new ViewLog(LocalDate.of(2025, Month.JANUARY, 13), 12);
        testLog.add(view);
        testShow.logViewing(view);
        assertEquals(LocalDate.of(2025, Month.JANUARY, 13), testShow.getLogs().get(0).getViewedOn());
        assertEquals(Status.VIEWING, testShow.getStatus());
        assertEquals(testLog, testShow.getLogs());
    }

    @Test
    void testLogViewingMultiple() {
        testLog.add(view1);
        testShow.logViewing(view1);
        assertEquals(testLog, testShow.getLogs());
        assertEquals(Status.VIEWING, testShow.getStatus());

        testLog.add(view2);
        testShow.logViewing(view2);
        assertEquals(testLog, testShow.getLogs());
        assertEquals(Status.VIEWING, testShow.getStatus());

        testLog.add(view3);
        testShow.logViewing(view3);   // 5 + 14 + 7 = 26 = length
        assertEquals(testLog, testShow.getLogs());
        assertEquals(Status.FINISHED, testShow.getStatus());
    }

    @Test
    void testLogViewingUnknownLengthNeverFinishes() {
        m2.logViewing(new ViewLog(LocalDate.of(2024, Month.MARCH, 13), 500));
        assertEquals(Status.VIEWING, m2.getStatus());
    }

    @Test
    void testRemoveLogOnce() {
        testLog.add(view1);
        testShow.logViewing(view1);
        assertEquals(testLog, testShow.getLogs());

        testLog.remove(0);
        testShow.removeLatestLog();
        assertEquals(testLog, testShow.getLogs());
    }

    @Test
    void testRemoveLogMultiple() {
        testLog.add(view1);
        testLog.add(view2);
        testLog.add(view3);
        testShow.logViewing(view1);
        testShow.logViewing(view2);
        testShow.logViewing(view3);
        assertEquals(testLog, testShow.getLogs());

        testLog.remove(2);
        testShow.removeLatestLog();
        assertEquals(testLog, testShow.getLogs());

        testLog.remove(1);
        testShow.removeLatestLog();
        assertEquals(testLog, testShow.getLogs());

        testLog.remove(0);
        testShow.removeLatestLog();
        assertEquals(testLog, testShow.getLogs());

        testShow.removeLatestLog();   // removing from an empty log does nothing
        assertEquals(testLog, testShow.getLogs());
    }

    @Test
    void testGetTotalViewProgress() {
        assertEquals(0, testShow.getTotalViewProgress());
        testShow.logViewing(view1);
        assertEquals(5, testShow.getTotalViewProgress());
        testShow.logViewing(view2);
        assertEquals(19, testShow.getTotalViewProgress());
        testShow.logViewing(view3);
        assertEquals(26, testShow.getTotalViewProgress());
    }

    @Test
    void testSetters() {
        m2.setName("Wotakoi");
        m2.setPriority(null);
        m2.setLength(21);
        m2.setType(MediaType.BOOK);
        m2.setRating(7);
        m2.setStatus(Status.HOLD);

        assertEquals("Wotakoi", m2.getName());
        assertNull(m2.getPriority());
        assertEquals(21, m2.getLength());
        assertEquals(MediaType.BOOK, m2.getType());
        assertEquals(7, m2.getRating());
        assertEquals(Status.HOLD, m2.getStatus());
    }
}