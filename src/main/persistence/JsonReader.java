package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import model.MediaTracker;
import model.ViewLog;
import model.enums.MediaType;
import model.Media;
import model.enums.Status;
import java.time.*;

// Represents a JSON reader
// Based on CPSC 210 implementation of READ/WRITE Json files:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String path;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String path) {
        this.path = path;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public MediaTracker read() throws IOException {
        String jsonData = readFile(path);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMediaTracker(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses a MediaTracker from JSON object and returns it
    private MediaTracker parseMediaTracker(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        MediaTracker wr = new MediaTracker(name);
        addMedias(wr, jsonObject);
        return wr;
    }

    // MODIFIES: wr
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addMedias(MediaTracker mt, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("mediaList");
        for (Object json : jsonArray) {
            JSONObject nextMedia = (JSONObject) json;
            addMedia(mt, nextMedia);
        }
    }

    // MODIFIES: wr
    // EFFECTS: parses thingy from JSON object and adds it to workroom
    private void addMedia(MediaTracker wt, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        MediaType type = MediaType.valueOf(jsonObject.getString("type"));
        Integer length = Integer.parseInt(jsonObject.getString("length"));
        Integer priority = Integer.parseInt(jsonObject.getString("priority"));

        Media m = new Media(name, type, length, priority);
        Status status = Status.valueOf(jsonObject.getString("status"));
        Integer rating = Integer.parseInt(jsonObject.getString("rating"));
        m.setStatus(status);
        m.setRating(rating);
        JSONArray jsonArray = jsonObject.getJSONArray("log");
        for (Object json : jsonArray) {
            JSONObject thisJson = (JSONObject) json;
            Integer year = Integer.parseInt(thisJson.getString("year"));
            Month month = Month.valueOf(thisJson.getString("month"));
            Integer day = Integer.parseInt(thisJson.getString("day"));
            LocalDate d = LocalDate.of(year, month, day);
            Integer viewProgress = Integer.parseInt(thisJson.getString("viewProgress"));
            ViewLog viewLog = new ViewLog(d, viewProgress);
            m.logViewing(viewLog);
        }        
        wt.addMedia(m);
    }
}
