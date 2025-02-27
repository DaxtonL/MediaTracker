package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import model.MediaTracker;
import model.Media;

// Represents a JSON reader
// Based on CPSC 210 implementation of READ/WRITE Json files:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String path;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        // stub
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public WorkRoom read() throws IOException {
        // stub
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        // stub
    }

    // EFFECTS: parses a MediaTracker from JSON object and returns it
    private MediaTracker parseMediaTracker(JSONObject jsonObject) {
        // stub
    }

    // MODIFIES: wr
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addMedias(MediaTracker mt, JSONObject jsonObject) {
        // stub
    }

    // MODIFIES: wr
    // EFFECTS: parses thingy from JSON object and adds it to workroom
    private void addMedia(MediaTracker wt, JSONObject jsonObject) {
        // stub
    }
}
