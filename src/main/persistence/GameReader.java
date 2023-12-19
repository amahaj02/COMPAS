package persistence;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameReader {
    private String source;
    private boolean isEmpty;

    /**
    * REQUIRES: None.
    * MODIFIES: None.
    * EFFECTS: Initializes the source field with the specified source path.
     */
    public GameReader(String source) {
        this.source = source;
    }

    /**
    * REQUIRES: None.
    * MODIFIES: this.isEmpty.
    * EFFECTS: Reads JSON data from the source file specified in the source field.
              If the data is empty returns an empty JSON object.
              Otherwise, parses the JSON data, retrieves the "data" object, and returns it as a JSONObject.
              Throws an IOException if there are issues reading the file.
     */
    public JSONObject read() throws IOException {
        String jsonData = readFile(source);
        isEmpty = jsonData.equals("");
        JSONObject jsonObject;
        if (isEmpty) {
            return new JSONObject();
        } else {
            jsonObject = new JSONObject(jsonData);
            JSONObject data = jsonObject.getJSONObject("data");
            return data;
        }
    }

    /**
    * REQUIRES: None.
    * MODIFIES: None.
    * EFFECTS: Reads the content of the file specified by the source path and returns it as a String.
              May throw an IOException if there are issues reading the file.
     */
    public String readFile(String source) throws IOException {
        return new String(Files.readAllBytes(Paths.get(source)));
    }
}
