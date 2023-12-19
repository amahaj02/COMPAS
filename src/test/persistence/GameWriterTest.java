package persistence;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameWriterTest {
    private GameReader reader;

    @BeforeEach
    void setUp() {
        reader = new GameReader("data/data.json");
    }

    @Test
    void testSaveGame() throws IOException {

        char letter = 'A';
        ArrayList<JSONObject> listOfPlayers = new ArrayList<>();
        List<String> database = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        String activePlayer = "Player1";

        GameWriter writer = new GameWriter("data/data.json");

        writer.save(letter, listOfPlayers, database, answers, activePlayer);


        JSONObject data = reader.read();

        assertEquals(activePlayer, data.getString("activePlayer"));
        assertEquals(letter, data.getString("currentLetter").charAt(0));
        assertEquals(activePlayer, data.getString("activePlayer"));
        assertTrue(data.getJSONArray("countriesLeft").similar(new JSONArray(database)));
        assertTrue(data.getJSONArray("countriesAnswered").similar(new JSONArray(answers)));
        assertTrue(data.getJSONArray("listOfPlayers").similar(new JSONArray(listOfPlayers)));
    }

    @Test
    void testFileNotFound() {
        GameWriter invalidWriter = new GameWriter("doesnotexist/notThere.json");

        assertThrows(RuntimeException.class, invalidWriter::open);
    }

}
