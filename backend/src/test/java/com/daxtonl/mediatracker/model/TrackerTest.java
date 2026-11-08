package com.daxtonl.mediatracker.model;

import com.daxtonl.mediatracker.model.enums.MediaType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrackerTest {
    @Test
    void testConstructor() {
        Tracker t = new Tracker("My media tracker");
        assertEquals("My media tracker", t.getName());
        assertTrue(t.getMediaList().isEmpty());
    }

    @Test
    void testAddMediaLinksBothSides() {
        Tracker t = new Tracker("My media tracker");
        Media m1 = new Media("Neon Genesis Evangelion", MediaType.SHOW, 26, 1);
        Media m2 = new Media("Celeste", MediaType.GAME, null, null);

        t.addMedia(m1);
        t.addMedia(m2);

        assertEquals(List.of(m1, m2), t.getMediaList());
        assertSame(t, m1.getTracker());   // the media points back to its tracker
    }
}