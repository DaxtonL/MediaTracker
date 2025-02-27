package model.testPersistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import model.Media;
import model.enums.MediaType;
import model.enums.Status;

public class JsonTest {
    protected void checkMedia(String name, MediaType type, int length, 
            Status status, int priority, int rating, int viewProgress, Media m) {
        assertEquals(name, m.getName());
        assertEquals(type, m.getType());
        assertEquals(length, m.getLength());
        assertEquals(status, m.getStatus());
        assertEquals(priority, m.getPriority());
        assertEquals(rating, m.getRating());
        assertEquals(viewProgress, m.getTotalViewProgess());
    }
}
