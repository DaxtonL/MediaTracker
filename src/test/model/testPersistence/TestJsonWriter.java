package model.testPersistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import org.junit.Test;

import model.Media;
import model.MediaTracker;
import model.enums.MediaType;
import model.enums.Status;
import persistence.JsonReader;
import persistence.JsonWriter;

public class TestJsonWriter extends JsonTest {

    @Test
    public void testWriterInvalidFile() {
        try {
            MediaTracker mt = new MediaTracker("My work room");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testWriterEmptyWorkroom() {
        try {
            MediaTracker mt = new MediaTracker("My work room");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyMediaTracker.json");
            writer.open();
            writer.write(mt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyMediaTracker.json");
            mt = reader.read();
            assertEquals("My work room", mt.getName());
            assertEquals(0, mt.getFilterMedia(new ArrayList<>()).size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testWriterGeneralWorkroom() {
        try {
            MediaTracker mt = new MediaTracker("My work room");
            mt.addMedia(new Media("Celeste", MediaType.GAME, -1, 2));
            mt.addMedia(new Media("Whiplash", MediaType.MOVIE, 120, -1));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(mt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            mt = reader.read();
            assertEquals("My work room", mt.getName());
            List<Media> thingies = mt.getFilterMedia(new ArrayList<>());
            assertEquals(2, thingies.size());
            checkMedia("Celeste", MediaType.GAME, -1, Status.WAITLIST, 2, -1, 0, thingies.get(0));
            checkMedia("Whiplash", MediaType.MOVIE, 120, Status.WAITLIST, -1, -1, 0, thingies.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
