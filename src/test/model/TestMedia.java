package model;

import model.enums.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.*;
import java.util.List;
import java.util.ArrayList;


public class TestMedia {
    private Media testShow;
    private Media testMovie;
    private Media testBook;
    private Media nullMedia;
    private Media m1;
    private Media m2;
    private Media m3;
    private List<ViewLog> testLog;
    private ViewLog view1;
    private ViewLog view2;
    private ViewLog view3;
    
    @BeforeEach
    void runBefore(){
        testShow = new Media("Cowboy Bebop", MediaType.SHOW, 26, 1);
        testMovie = new Media("Whieplash", MediaType.MOVIE, 100, 4);
        testBook = new Media("1984", MediaType.BOOK, 284, -1);
        nullMedia = new Media(null, null, null, null);
        testLog = new ArrayList();
        view1 = new ViewLog(LocalDate.of(2024, Month.MARCH, 13), 5);
        view2 = new ViewLog(LocalDate.of(2024, Month.MARCH, 15), 14);
        view3 = new ViewLog(LocalDate.of(2024, Month.MARCH, 16), 7);

        m1 = new Media("Neon Genesis Evangelion", MediaType.SHOW, 26, 1);
        m2 = new Media("Shin Megami Tensei V", MediaType.GAME, -1, 3);
        m3 = new Media("Fullmetal Alchemist (manga)", MediaType.MANGA, 108, -1);
    }

    @Test
    void testConstructor(){
        assertEquals("Cowboy Bebop", testShow.getName());
        assertEquals(MediaType.SHOW, testShow.getType());
        assertEquals(26, testShow.getLength());
        assertEquals(1, testShow.getPriority());
        assertEquals(testLog, testShow.getLog());
        assertEquals(Status.WAITLIST, testShow.getStatus());
        assertEquals(-1, testShow.getRating());
    }

    @Test
    void testLogViewingOnce(){
        assertEquals(Status.WAITLIST, testShow.getStatus());
        assertEquals(testLog, testShow.getLog());
        ViewLog view1 = new ViewLog(LocalDate.of(2025, Month.JANUARY, 13), 12);
        testLog.add(view1);
        testShow.logViewing(view1);
        assertEquals(LocalDate.of(2025, Month.JANUARY, 13), testShow.getLog().get(0).getDate());
        assertEquals(Status.VIEWING, testShow.getStatus());
        assertEquals(testLog, testShow.getLog());
    }

    @Test
    void testLogViewingMultiple(){
        assertEquals(Status.WAITLIST, testShow.getStatus());
        assertEquals(testLog, testShow.getLog());

        testLog.add(view1);
        testShow.logViewing(view1);
        assertEquals(testLog, testShow.getLog());
        assertEquals(Status.VIEWING, testShow.getStatus());

        testLog.add(view2);
        testShow.logViewing(view2);
        assertEquals(testLog, testShow.getLog());
        assertEquals(Status.VIEWING, testShow.getStatus());

        testLog.add(view3);
        testShow.logViewing(view3);
        assertEquals(testLog, testShow.getLog());
        assertEquals(Status.FINISHED, testShow.getStatus());
    }

    @Test
    void testRemoveLogOnce(){
        assertEquals(testLog, testShow.getLog());

        testLog.add(view1);
        testShow.logViewing(view1);
        assertEquals(testLog, testShow.getLog());

        testLog.remove(0);
        testShow.removeLog();
        assertEquals(testLog, testShow.getLog());
    }

    @Test
    void testRemoveLogMultiple(){
        assertEquals(testLog, testShow.getLog());

        testLog.add(view1);
        testLog.add(view2);
        testLog.add(view3);
        testShow.logViewing(view1);
        testShow.logViewing(view2);
        testShow.logViewing(view3);
        assertEquals(testLog, testShow.getLog());

        testLog.remove(2);
        testShow.removeLog();
        assertEquals(testLog, testShow.getLog());

        testLog.remove(1);
        testShow.removeLog();
        assertEquals(testLog, testShow.getLog());

        testLog.remove(0);
        testShow.removeLog();
        assertEquals(testLog, testShow.getLog());

        testShow.removeLog();
        assertEquals(testLog, testShow.getLog());
    }

    @Test
    void testGetTotalViewProgress(){
        assertEquals(0, testShow.getTotalViewProgess());
        testShow.logViewing(view1);
        assertEquals(5, testShow.getTotalViewProgess());
        testShow.logViewing(view2);
        assertEquals(19, testShow.getTotalViewProgess());
        testShow.logViewing(view3);
        assertEquals(26, testShow.getTotalViewProgess());
    }

    @Test
    void testDisplayMediaList(){
        System.out.println(m1.getStatus().toString());
        m1.logViewing(new ViewLog(null, 12));
        System.out.println(m1.getStatus().toString());
        m1.setStatus(Status.ON_HOLD);
        System.out.println(m1.getStatus().toString());

        m2.logViewing(new ViewLog(null, 90));
        m2.setRating(7);
        m2.setStatus(Status.FINISHED);

        m3.logViewing(new ViewLog(null, 56));
        m3.setStatus(Status.VIEWING);

        String s1 = "Neon Genesis Evangelion | Show | On-hold | 12/26 episodes | Priority: 1 | Rating: N/A";
        String s2 = "Shin Megami Tensei V | Game | Finished | 90/-- hours | Priority: 3 | Rating: 7/10";
        String s3 = "Fullmetal Alchemist (manga) | Manga | Reading | 56/108 chapters | Priority: N/A | Rating: N/A";
        
        assertEquals(s1, m1.displayMediaInfo());
        assertEquals(s2, m2.displayMediaInfo());
        assertEquals(s3, m3.displayMediaInfo());
    }

    @Test
    void testMediaTypeStrings(){
        assertEquals("Waitlist", testMovie.mediaTypeStrings().getViewingVerb());
        assertEquals("Waitlist", testBook.mediaTypeStrings().getViewingVerb());
        assertEquals("Waitlist", nullMedia.mediaTypeStrings().getViewingVerb());
        assertEquals("Waitlist", m1.mediaTypeStrings().getViewingVerb());
        assertEquals("Waitlist", m2.mediaTypeStrings().getViewingVerb());
        assertEquals("Waitlist", m3.mediaTypeStrings().getViewingVerb());

        assertEquals("episodes", m1.mediaTypeStrings().getLengthIncrement());
        assertEquals("pages", testBook.mediaTypeStrings().getLengthIncrement());
        assertEquals("unit", nullMedia.mediaTypeStrings().getLengthIncrement());
        assertEquals("hours", m2.mediaTypeStrings().getLengthIncrement());
        assertEquals("chapters", m3.mediaTypeStrings().getLengthIncrement());
        assertEquals("minutes", testMovie.mediaTypeStrings().getLengthIncrement());

        nullMedia.setStatus(Status.VIEWING);
        testMovie.setStatus(Status.VIEWING);
        testBook.setStatus(Status.VIEWING);
        m1.setStatus(Status.VIEWING);
        m2.setStatus(Status.VIEWING);
        m3.setStatus(Status.VIEWING);

        assertEquals("Watching", testMovie.mediaTypeStrings().getViewingVerb());
        assertEquals("Reading", testBook.mediaTypeStrings().getViewingVerb());
        assertEquals("Viewing", nullMedia.mediaTypeStrings().getViewingVerb());
        assertEquals("Watching", m1.mediaTypeStrings().getViewingVerb());
        assertEquals("Playing", m2.mediaTypeStrings().getViewingVerb());
        assertEquals("Reading", m3.mediaTypeStrings().getViewingVerb());
    }

    @Test
    void testSetName(){
        m3.setName("Wotakoi");
        assertEquals("Wotakoi", m3.getName());
    }

    @Test
    void testSetPriority(){
        m2.setPriority(-1);
        assertEquals(-1, m2.getPriority());
    }

    @Test
    void testSetLength(){
        m2.setLength(21);
        assertEquals(21, m2.getLength());
    }

    @Test
    void testSetType(){
        m1.setType(MediaType.BOOK);
        assertEquals(MediaType.BOOK, m1.getType());
    }
}
