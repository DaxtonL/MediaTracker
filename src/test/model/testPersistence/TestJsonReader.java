package model.testPersistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import persistence.JsonReader;
import model.Media;
import model.MediaTracker;
import model.enums.MediaType;
import model.enums.Status;

public class TestJsonReader extends JsonTest {
    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            MediaTracker mt = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyMediaTracker.json");
        try {
            MediaTracker mt = reader.read();
            assertEquals("My media tracker", mt.getName());
            assertEquals(0, mt.getFilterMedia(new ArrayList<>()).size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralMediaTracker.json");
        try {
            MediaTracker mt = reader.read();
            assertEquals("My media tracker", mt.getName());
            List<Media> media = mt.getFilterMedia(new ArrayList<>());
            assertEquals(2, media.size());
            checkMedia("Celeste", MediaType.GAME, -1, Status.VIEWING, 2, -1, 99, media.get(0));
            checkMedia("Cowboy Bebop", MediaType.SHOW, 26, Status.FINISHED, -1, 9, 26, media.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
