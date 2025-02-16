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
    private List<ViewLog> testLog;
    private ViewLog view1;
    private ViewLog view2;
    private ViewLog view3;
    
    @BeforeEach
    void runBefore(){
        testShow = new Media("Cowboy Bebop", MediaType.SHOW, 26, 1);
        testLog = new ArrayList();
        view1 = new ViewLog(LocalDate.of(2024, Month.MARCH, 13), 5);
        view2 = new ViewLog(LocalDate.of(2024, Month.MARCH, 15), 14);
        view3 = new ViewLog(LocalDate.of(2024, Month.MARCH, 16), 7);
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
        testShow.removeLog(0);
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
        testShow.removeLog(2);
        assertEquals(testLog, testShow.getLog());

        testLog.remove(0);
        testShow.removeLog(0);
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
}
