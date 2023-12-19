package persistence;

import org.json.JSONObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameWriter {
    private static final int TAB = 4;
    private JSONObject finalObject;
    private String destination;
    private PrintWriter printWriter;

    /**
    * REQUIRES: None.
    * MODIFIES: None.
    * EFFECTS: Initializes the destination path and creates an empty JSONObject.
     */
    public GameWriter(String destination) {
        this.destination = destination;
        this.finalObject = new JSONObject();
    }

    /**
    * REQUIRES: None.
    * MODIFIES: this.printWriter.
    * EFFECTS: Opens a PrintWriter for the specified destination file. If the file does not exist, it will be created.
              May throw a RuntimeException if there are issues opening the file.
     */
    public void open() {
        try {
            printWriter = new PrintWriter(new File(destination));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
    * REQUIRES: None.
    * MODIFIES: this.finalObject, this.printWriter.
    * EFFECTS: Prepares a JSON data structure with the provided information and saves it to the destination file.
    */
    public void save(char letter, ArrayList<JSONObject> listOfPlayers,
                     List<String> database, List<String> answers, String activePlayer) {
        open();
        JSONObject toBeSaved = new JSONObject();
        String letterString = String.valueOf(letter);
        toBeSaved.put("listOfPlayers", listOfPlayers);
        toBeSaved.put("countriesLeft", database);
        toBeSaved.put("countriesAnswered", answers);
        toBeSaved.put("currentLetter", letterString);
        toBeSaved.put("activePlayer", activePlayer);

        finalObject.put("data", toBeSaved);
        printWriter.print(finalObject.toString(TAB));
        printWriter.close();
    }
}

