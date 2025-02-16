package model;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import model.enums.MediaType;
import model.enums.Status;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import model.filters.*;

public class TestMediaList {
    private MediaTracker testTracker;
    private List<Media> testList;

    private Media m1;
    private Media m2;
    private Media m3;
    private Media m4;
    private Media m5;
    private Media m6;

    private Filter filterStatus = new FilterStatus(Status.ON_HOLD);
    private Filter filterType = new FilterType(MediaType.SHOW);
    private List<Filter> filterList;

    @BeforeEach
    void runBefore(){
        testTracker = new MediaTracker();
        testList = new ArrayList<>();
        filterList = new ArrayList<>();

        m1 = new Media("Neon Genesis Evangelion", MediaType.SHOW, 26, 1);
        m1.logViewing(new ViewLog(null, 12));
        m1.setStatus(Status.ON_HOLD);

        m2 = new Media("Shin Megami Tensei V", MediaType.GAME, -1, 3);
        m2.logViewing(new ViewLog(null, 90));
        m2.setRating(7);
        m2.setStatus(Status.FINISHED);

        m3 = new Media("Fullmetal Alchemist (manga)", MediaType.MANGA, 108, -1);
        m3.logViewing(new ViewLog(null, 56));
        m3.setStatus(Status.VIEWING);

        m4 = new Media("Soul Eater", MediaType.SHOW, 51, 2);
        m1.setStatus(Status.WAITLIST);

        m5= new Media("Celeste", MediaType.GAME, -1, -1);
        m5.setStatus(Status.FINISHED);

        m6 = new Media("Blue Box", MediaType.MANGA, -1, 2);
        m6.setStatus(Status.ON_HOLD);
    }

    @Test
    void testConstructor(){
        assertEquals(testList, testTracker.getFilterMedia(null));
    }

    @Test
    void testGetFilterMediaNoFilter(){
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
    }

    @Test
    void testGetFilterMediaFiltersOne(){
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
    void testGetFilterMediaFiltersMultiple(){
        filterList.add(filterType);
        filterList.add(filterStatus);

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
    void testDisplayMediaList(){
        List<String> output = new ArrayList<>();
        testTracker.addMedia(m1);
        testTracker.addMedia(m2);
        testTracker.addMedia(m3);
        private String s1 = "Neon Genesis Evangelion | Show | On-Hold | 12/26 episodes | Priority: 1 | Rating: N/A";
        private String s2 = "Shin Megami Tensei V | Game | Finished | 90/-- hours | Priority: 3 | Rating: 7/10";
        private String s3 = "Fullmetal Alchemist (manga) | Manga | Reading | 56/108 chapters | Priority: N/A | Rating: N/A";
        output.add(s1);
        output.add(s2);
        output.add(s3);
        assertEquals(output, testTracker.displayMediaList());
    }

    @Test
    void testAddMedia(){
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
    void testRemoveMedia(){
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
}
