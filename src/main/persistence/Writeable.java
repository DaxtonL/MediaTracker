package persistence;

import org.json.JSONObject;

// Based on CPSC 210 implementation of READ/WRITE Json files:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public interface Writeable {

    // Effects returns the object as a writeable json file
    JSONObject toJson();
}
