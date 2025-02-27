package model.testApp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import model.Media;
import model.MediaTracker;
import model.ViewLog;
import model.enums.MediaType;
import model.enums.Status;
import model.filters.*;

public class TestMediaTracker {
    private MediaTracker testTracker;
    private List<Media> testList;

    private Media m1;
    private Media m2;
    private Media m3;
    private Media m4;
    private Media m5;
    private Media m6;

    private Filter filterStatus = new FilterStatus(Status.HOLD);
    private Filter filterType = new FilterType(MediaType.SHOW);
    private Filter filterRating = new FilterRating(true, 5);
    private Filter filterRatingBelow = new FilterRating(false, 8);

    private List<Filter> filterList;

    @BeforeEach
    public void runBefore() {
        testTracker = new MediaTracker("My media tracker");
        testList = new ArrayList<>();
        filterList = new ArrayList<>();

        m1 = new Media("Neon Genesis Evangelion", MediaType.SHOW, 26, 1);
        m1.logViewing(new ViewLog(null, 12));
        m1.setRating(6);
        m1.setStatus(Status.HOLD);

        m2 = new Media("Shin Megami Tensei V", MediaType.GAME, -1, 3);
        m2.logViewing(new ViewLog(null, 90));
        m2.setRating(4);
        m2.setStatus(Status.FINISHED);

        m3 = new Media("Fullmetal Alchemist (manga)", MediaType.MANGA, 108, -1);
        m3.logViewing(new ViewLog(null, 56));
        m3.setRating(10);
        m3.setStatus(Status.VIEWING);

        m4 = new Media("Soul Eater", MediaType.SHOW, 51, 2);
        m4.setRating(4);
        m4.setStatus(Status.HOLD);

        m5 = new Media("Celeste", MediaType.GAME, -1, -1);
        m5.setStatus(Status.FINISHED);

        m6 = new Media("Blue Box", MediaType.MANGA, -1, 2);
        m6.setStatus(Status.HOLD);
    }

    @Test
    public void testConstructor() {
        assertEquals(testList, testTracker.getFilterMedia(null));
    }

    @Test
    public void testGetFilterMediaNoFilter() {
        assertEquals(testList, testTracker.getFilterMedia(null));
        testList.add(m1);
        testList.add(m2);
        testList.add(m3);
        testList.add(m4);
        testList.add(m5);
        testList.add(m6);
        testTracker.addMedia(m1);
        testTracker.addMedia(m2);
        testTracker.addMedia(m3);
        testTracker.addMedia(m4);
        testTracker.addMedia(m5);
        testTracker.addMedia(m6);
        assertEquals(testList, testTracker.getFilterMedia(null));
        assertEquals(testList, testTracker.getFilterMedia(filterList));
    }

    @Test
    public void testGetFilterMediaFiltersOne() {
        filterList.add(filterType);

        assertEquals(testList, testTracker.getFilterMedia(null));
        testList.add(m1);
        testList.add(m4);
        testTracker.addMedia(m1);
        testTracker.addMedia(m2);
        testTracker.addMedia(m3);
        testTracker.addMedia(m4);
        testTracker.addMedia(m5);
        testTracker.addMedia(m6);
        assertEquals(testList, testTracker.getFilterMedia(filterList));
    }

    @Test
    public void testGetFilterMediaFiltersMultiple() {
        filterList.add(filterType);
        filterList.add(filterStatus);
        filterList.add(filterRating);
        filterList.add(filterRatingBelow);

        assertEquals(testList, testTracker.getFilterMedia(null));
        testList.add(m1);
        testTracker.addMedia(m1);
        testTracker.addMedia(m2);
        testTracker.addMedia(m3);
        testTracker.addMedia(m4);
        testTracker.addMedia(m5);
        testTracker.addMedia(m6);
        assertEquals(testList, testTracker.getFilterMedia(filterList));
    }

    @Test
    public void testAddMedia() {
        assertEquals(testList, testTracker.getFilterMedia(null));

        assertTrue(testTracker.addMedia(m1));
        testList.add(m1);
        assertEquals(testList, testTracker.getFilterMedia(null));

        assertFalse(testTracker.addMedia(m1));
        assertEquals(testList, testTracker.getFilterMedia(null));

        assertTrue(testTracker.addMedia(m2));
        testList.add(m2);
        assertEquals(testList, testTracker.getFilterMedia(null));

        assertTrue(testTracker.addMedia(m3));
        testList.add(m3);
        assertEquals(testList, testTracker.getFilterMedia(null));
    }

    @Test
    public void testRemoveMedia() {
        testList.add(m1);
        testList.add(m2);
        testList.add(m3);
        testTracker.addMedia(m1);
        testTracker.addMedia(m2);
        testTracker.addMedia(m3);
        assertEquals(testList, testTracker.getFilterMedia(null));
        assertFalse(testTracker.removeMedia("Jujutsu Kaisen"));

        assertTrue(testTracker.removeMedia("Neon Genesis Evangelion"));
        testList.remove(0);
        assertEquals(testList, testTracker.getFilterMedia(null));

        assertTrue(testTracker.removeMedia("Fullmetal Alchemist (manga)"));
        testList.remove(1);
        assertEquals(testList, testTracker.getFilterMedia(null));
    }

    @Test
    public void testGetMedia() {
        testTracker.addMedia(m1);
        testTracker.addMedia(m2);
        testTracker.addMedia(m3);
        assertEquals(m1, testTracker.getMedia("Neon Genesis Evangelion"));
        assertEquals(null, testTracker.getMedia("Cowboy Bebop"));
    }
}
