package model.testPersistence;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import model.Media;
import model.MediaTracker;
import model.ViewLog;
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
            Media m1 = new Media("Celeste", MediaType.GAME, -1, 2);
            Media m2 = new Media("Whiplash", MediaType.MOVIE, 120, -1);
            m1.logViewing(new ViewLog(LocalDate.of(2025, Month.FEBRUARY, 28), 80));
            m2.logViewing(new ViewLog(LocalDate.of(2025, Month.MARCH, 13), 120));
            mt.addMedia(m1);
            mt.addMedia(m2);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralMediaTracker.json");
            writer.open();
            writer.write(mt);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralMediaTracker.json");
            mt = reader.read();
            assertEquals("My work room", mt.getName());
            List<Media> thingies = mt.getFilterMedia(new ArrayList<>());
            assertEquals(2, thingies.size());
            checkMedia("Celeste", MediaType.GAME, -1, Status.VIEWING, 2, -1, 80, thingies.get(0));
            checkMedia("Whiplash", MediaType.MOVIE, 120, Status.FINISHED, -1, -1, 120, thingies.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
