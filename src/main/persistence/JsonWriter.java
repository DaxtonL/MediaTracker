package persistence;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes a JSON representation of a media tracker to file
// Based on CPSC 210 implementation of READ/WRITE Json files:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriter {
    private PrintWriter writer;
    private String path;

    // EFFECTS: creates a new Json writer that will write to the specified path
    public JsonWriter(String path) {
        // stub
    }

    // MODIFIES: this
    // EFFECTS: Opens writer; throws FileNotFoundException if file at path cannot be found
    public void open() throws FileNotFoundException {
        // stub
    }

    // MODIFIES: this
    // EFFECTS: Writes the writeable object to file
    public void write(Writeable wr) {
        // stub
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {
        // stub
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile() {
        // stub
    }
}
