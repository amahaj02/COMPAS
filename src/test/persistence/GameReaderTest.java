package persistence;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameReaderTest {
    private GameWriter writer;

    @BeforeEach
    void setUp() {
        writer = new GameWriter("data/data.json");
    }

    @Test
    void testReadGame() {

        char letter = 'A';
        ArrayList<JSONObject> listOfPlayers = new ArrayList<>();
        List<String> database = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        String activePlayer = "Player1";

        writer.save(letter, listOfPlayers, database, answers, activePlayer);
        GameReader reader = new GameReader("data/data.json");

        try {
            JSONObject data = reader.read();
            assertEquals(activePlayer, data.getString("activePlayer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testReadEmptyGame() {
        GameReader reader = new GameReader("data/emptyGame.json");

        try {
            JSONObject emptyJSONData = reader.read();
            assertTrue(emptyJSONData.isEmpty());
        } catch (IOException e) {
            System.out.println("Couldn't read from file");
        }
    }

}
