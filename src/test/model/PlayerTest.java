package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testConstructor() {
        Player player = new Player("Aarav");
        assertEquals("Aarav", player.getName());
    }

    @Test
    public void testAssignLetter() {
        Player player = new Player("Alice");

        // Assign a letter to the player
        player.assignLetter('A');

        // Check if the letter was added to the assignedLetters list
        assertTrue(player.getAssignedLetters().contains('A'));
    }

    @Test
    public void testAssignMultipleLetters() {
        Player player = new Player("Bob");

        // Assign multiple letters to the player
        player.assignLetter('B');
        player.assignLetter('C');
        player.assignLetter('D');

        // Check if all letters were added to the assignedLetters list
        assertTrue(player.getAssignedLetters().contains('B'));
        assertTrue(player.getAssignedLetters().contains('C'));
        assertTrue(player.getAssignedLetters().contains('D'));
    }

    @Test
    public void testAssignDuplicateLetter() {
        Player player = new Player("Charlie");

        // Assign a letter to the player
        player.assignLetter('E');

        // Try to assign the same letter again
        player.assignLetter('E');

        // Check that the letter is not duplicated in the assignedLetters list
        assertEquals(1, player.getAssignedLetters().size());
    }

    @Test
    public void testIncrementLettersAssigned() {
        Player player = new Player("Aarav");
        player.incrementLettersAssigned();
        assertEquals(1, player.getLettersAssigned());
    }

    @Test
    public void testToJson() {
        Player player = new Player("Alice");
        player.assignLetter('A');
        player.assignLetter('B');

        JSONObject playerJson = player.toJson();

        JSONArray lettersAssigned = playerJson.getJSONArray("lettersAssigned");

        char letterA = 'A';
        char letterB = 'B';

        assertTrue(lettersAssigned.toList().contains(letterA));
        assertTrue(lettersAssigned.toList().contains(letterB));
    }

    @Test
    public void testToJsonEmpty() {
        Player player = new Player("Bob");

        JSONObject playerJson = player.toJson();

        assertEquals("Bob", playerJson.getString("name"));
        assertTrue(playerJson.getJSONArray("lettersAssigned").isEmpty());
    }

    @Test
    public void testToJsonMultipleLetters() {
        Player player = new Player("Carol");
        player.assignLetter('C');
        player.assignLetter('D');
        player.assignLetter('C');

        JSONObject playerJson = player.toJson();

        assertEquals("Carol", playerJson.getString("name"));
        JSONArray lettersAssigned = playerJson.getJSONArray("lettersAssigned");

        char letterC = 'C';
        char letterD = 'D';

        assertTrue(lettersAssigned.toList().contains(letterC));
        assertTrue(lettersAssigned.toList().contains(letterD));
    }
}
