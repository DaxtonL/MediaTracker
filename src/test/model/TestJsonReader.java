package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import persistence.JsonReader;

import model.enums.Status;

public class TestJsonReader extends JsonTest {
    private MediaType game = new MediaType("game", "playing", "hours");
    private MediaType show = new MediaType("show", "watching", "episodes");


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
            List<Media> thingies = mt.getFilterMedia(new ArrayList<>());
            assertEquals(2, thingies.size());
            checkMedia("Celeste", game, -1, Status.VIEWING, 2, 2, 99, thingies.get(0));
            checkMedia("Cowboy Bebop", show, 26, Status.FINISHED, -1, 9, 26, thingies.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
