package model;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The Player class represents a player in the word game. Each player has a name
 * and a list of letters assigned to them during the game. Players take turns
 * guessing words that start with a specific letter, and they are eliminated if
 * they make incorrect guesses.
 */

public class Player {
    private String name;
    private int lettersAssigned;
    private List<Character> assignedLetters;


    public Player(String name) {
        this.name = name;
        this.lettersAssigned = 0;
        this.assignedLetters = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Requires: None.
     * Modifies: None.
     * Effects: Returns the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Requires: None.
     * Modifies: None.
     * Effects: Returns the number of letters assigned to the player.
     */
    public int getLettersAssigned() {
        return lettersAssigned;
    }

    /**
     * Requires: The character 'letter' to be assigned is not already in the 'assignedLetters' list.
     * Modifies: The 'assignedLetters' list if the 'letter' is successfully assigned.
     * Effects: Assigns the provided 'letter' to the player if it's not already assigned.
     */
    public void assignLetter(char letter) {
        if (!assignedLetters.contains(letter)) {
            assignedLetters.add(letter);
        }
    }

    /**
     * Requires: None.
     * Modifies: None.
     * Effects: Returns the list of letters assigned to the player.
     */
    public List<Character> getAssignedLetters() {
        return assignedLetters;
    }

    /**
     * Requires: None.
     * Modifies: The 'lettersAssigned' field, incrementing it by 1.
     * Effects: Increases the count of letters assigned to the player.
     */
    public void incrementLettersAssigned() {
        this.lettersAssigned++;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("lettersAssigned", assignedLetters);
        return jsonObject;
    }
}
