package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.GameReader;
import persistence.GameWriter;

import java.io.IOException;
import java.util.*;

/**
 * The `Game` class represents the core logic and management of the game.
 * It handles player management, gameplay, and game-related data, such as the list of players,
 * the active player, the current letter, and game constants.
 */

public class Game {
    // will have a list of players
    // will assign a letter from "GLOBE" to each player on miss

    private List<Player> lop;
    private int activePlayerIndex;
    private Player activePlayer;
    private char currentLetter;
    private String answer;
    char loadLetter;
    String loadPlayer;
    private int command;
    private Scanner input;
    Random random = new Random();

    private static final String JSON_PATH = "./data/data.json";

    public static final String START_WORD = "atlas";
    public static final List<String> DATABASE = new ArrayList<>();
    private static List<String> ANSWERS = new ArrayList<>();

    public static final String RESET = "\u001B[0m";
    public static final String BLUE = "\u001B[34m";

    // Creates a new game instance with an empty list of players and initializes the game.
    public Game() {
        lop = new ArrayList<>();
        EventLog.getInstance().logEvent(new Event("Game started."));
        //init();
    }

    /**
     * Requires: None.
     * Modifies: None.
     * Effects: Initializes the database of place names.
     */
    public void createData() {
        String[] countries = {
                "singapore", "sri lanka", "spain", "switzerland", "south africa",
                "south sudan", "sweden", "estonia", "england", "egypt", "afghanistan",
                "azerbaijan", "america", "australia", "austria", "albania", "armenia",
                "algeria", "new zealand", "netherlands", "nepal", "namibia", "denmark",
                "dominican republic", "djibouti", "dominica", "latvia", "lebanon",
                "luxembourg", "libya", "laos", "kuwait", "kenya", "kazakhstan", "kyrgyzstan",
                "costa rica", "chile", "cameroon", "chad", "india", "italy", "iran", "iraq",
                "ghana", "germany", "georgia", "guinea", "greece", "qatar", "turkey",
                "yemen", "lebanon"
        };

        DATABASE.addAll(Arrays.asList(countries));
    }

    /**
     * Requires: None.
     * Modifies: None.
     * Effects: Initializes the game input scanner and creates place name data.
     */
    public void init() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        showMainMenu();
    }

    /**
     * Requires: None.
     * Modifies: None.
     * Effects: Displays the main menu options and handles user input.
     */
    public void showMainMenu() {
        System.out.println("1 --> Start Game");
        System.out.println("2 --> Quit Game");
        System.out.println("3 --> Load Game");
        command = input.nextInt();

        handleMainMenu();
    }

    /**
     * Adds a player to the List of Players (lop).
     * param p The player to be added.
     * requires The input player (p) is not null.
     * modifies The List of Players (lop).
     * effects Adds the specified player to the List of Players.
     */
    public void addToLop(Player p) {
        lop.add(p);
        EventLog.getInstance().logEvent(new Event(p.getName() + " added as a player."));
    }

    /**
     * Retrieves the List of Players (lop).
     * requires None.
     * modifies None.
     * effects Returns the List of Players containing player objects.
     * return The List of Players.
     */
    public List<Player> getLop() {
        EventLog.getInstance().logEvent(new Event("Displayed all players."));
        return lop;
    }

    /**
     * Modifies the name of a player in the List of Players (lop).
     * param name The current name of the player to be modified.
     * param newName The new name to set for the player.
     * requires Both name and newName are not null.
     * modifies The name of the specified player in the List of Players (lop).
     * effects Updates the name of the specified player to the new name.
     */
    public void modifyPlayerFromLop(String name, String newName) {
        for (Player p: lop) {
            if (p.getName().toLowerCase().equals(name.toLowerCase())) {
                EventLog.getInstance().logEvent(new Event("Modified " + p.getName() + " to " + newName + "."));
                p.setName(newName);
            }
        }
    }

    /**
     * Requires: None.
     * Modifies: None.
     * Effects: Resets game-related variables.
     */
    public void resetVars() {
        lop = new ArrayList<>();
        ANSWERS = new ArrayList<>();
    }

    /**
     * Requires: None.
     * Modifies: None.
     * Effects: Handles user choices from the main menu.
     */
    public void handleMainMenu() {
        if (command == 1) {
            showStartMenu();
            showMainMenu();
        } else if (command == 2) {
            System.out.println("Thank you for playing!");
            System.exit(0);
        } else if (command == 3) {
            handleLoad();
            startGame(true);
        } else {
            System.out.println("Invalid Entry!");
            showMainMenu();
        }
    }

    /**
    * REQUIRES: None.
    * MODIFIES: loadLetter, loadPlayer, DATABASE, ANSWERS, lop.
    * EFFECTS: Reads game data from a JSON file and loads it into the game. If the JSON data is not empty, it updates
      the game state with the loaded data, including the current letter, active player, remaining countries, answered
      countries, and list of players. If the JSON data is empty, it displays a message and returns to the main menu.
     */
    public void handleLoad() {
        GameReader reader = new GameReader(JSON_PATH);
        try {
            JSONObject data = reader.read();
            if (!data.isEmpty()) {
                loadLetter = data.getString("currentLetter").charAt(0);
                loadPlayer = data.getString("activePlayer");
                for (int i = 0; i < data.getJSONArray("countriesLeft").length(); i++) {
                    DATABASE.add(data.getJSONArray("countriesLeft").getString(i));
                }
                for (int i = 0; i < data.getJSONArray("countriesAnswered").length(); i++) {
                    ANSWERS.add(data.getJSONArray("countriesAnswered").getString(i));
                }
                for (int i = 0; i < data.getJSONArray("listOfPlayers").length(); i++) {
                    lop.add(parsePlayer(data.getJSONArray("listOfPlayers").getJSONObject(i)));
                }
                System.out.println("Data Loaded");
            } else {
                System.out.println("No data to load!");
                showMainMenu();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
    * REQUIRES: A valid JSON object representing a player with a "name" field and an array "lettersAssigned."
    * MODIFIES: Creates a new Player object and assigns letters to it.
    * EFFECTS: Parses the JSON object to create a Player with the specified name and assigned letters. Returns the
       Player object.
    */
    private Player parsePlayer(JSONObject json) {
        Player p = new Player(json.getString("name"));
        JSONArray lettersAssigned = json.getJSONArray("lettersAssigned");
        for (int i = 0; i < lettersAssigned.length(); i++) {
            p.assignLetter(lettersAssigned.getString(i).toCharArray()[0]);
        }

        return p;
    }

    /**
     * Requires: None.
     * Modifies: command.
     * Effects: Displays the start menu options and handles user input.
     */
    public void showStartMenu() {
        createData();
        resetVars();
        System.out.println("Enter number of players (1 - 5)");
        command = input.nextInt();

        handleStartMenu();
    }

    /**
     * Requires: The number of players 'nop' should be between 1 and 5.
     * Modifies: Initializes the list of players by calling 'addPlayers' if 'nop' is valid.
     * Effects: Handles user choices from the start menu and adds players if the input is valid.
     */
    public void handleStartMenu() {
        if (command >= 1 && command <= 5) {
            addPlayers(command);
        } else {
            System.out.println("Invalid Entry!");
            showStartMenu();
        }

    }

    /**
     * Requires: The number of players 'nop' should be between 1 and 5.
     * Modifies: Initializes the list of players.
     * Effects: Allows users to add player names, then displays the game status and starts the game.
     */
    public void addPlayers(int nop) {
        for (int i = 0; i <= nop - 1; i++) {
            System.out.println("\nEnter name for Player " + (i + 1) + " : ");
            lop.add(new Player(input.next()));
        }
        gameStatus(nop);
        startGame(false);
    }

    /**
     * Requires: The number of players 'nop' should be between 1 and 5.
     * Modifies: None.
     * Effects: Displays the game status, including player names and assigned letters.
     */
    public void gameStatus(int nop) {
        System.out.println("\nGame Status :");
        for (int i = 0; i <= nop - 1; i++) {
            System.out.println("\nPlayer " + (i + 1) + " : " + lop.get(i).getName());
            List<Character> assignedList = lop.get(i).getAssignedLetters();
            String letters = "";
            for (int j = 0; j < "LOSER".length(); j++) {
                if (assignedList.contains("LOSER".charAt(j))) {
                    letters = letters.concat(BLUE);
                    letters = letters.concat(assignedList.get(j).toString());
                    letters = letters.concat(RESET);
                } else {
                    letters = letters + "LOSER".charAt(j);
                }

            }
            if (letters.length() == 0) {
                System.out.println("No letters assigned yet.");
            } else {
                System.out.println("Letters Assigned : " + letters);
            }
        }
    }

    /**
     * Requires: None.
     * Modifies: Initializes the game with a randomly selected active player and a starting letter if not loaded.
                 If loaded, sets the current letter and active player based on loaded data.
     * Effects: Initiates the game and displays information about the starting player and letter.
     */
    public void startGame(boolean loaded) {
        if (loaded) {
            currentLetter = loadLetter;
            for (Player p : lop) {
                if (p.getName().equals(loadPlayer)) {
                    activePlayer = p;
                }
            }
        } else {
            currentLetter = START_WORD.charAt(random.nextInt(START_WORD.length()));
            activePlayerIndex = random.nextInt(lop.size());
            activePlayer = lop.get(activePlayerIndex);

        }
        System.out.println("LET THE GAME BEGIN!");
        handleGame();
    }

    /**
     * Requires: None.
     * Modifies: None.
     * Effects: Handles the game logic, player interactions, and user choices during the game.
     */
    public void handleGame() {
        System.out.println("\nThe letter is : " + currentLetter);
        System.out.println("The player is : " + activePlayer.getName());
        System.out.println("\nType 'quit' to quit to the Main Menu.");
        System.out.println("\nType 'status' to display Game Status.");
        System.out.println("\nAnswer : ");
        answer = input.next().toLowerCase();

        if (answer.equals("quit")) {
            handleSave();
            resetVars();
            showMainMenu();
        } else if (answer.equals("status")) {
            gameStatus(lop.size());
            handleGame();
        } else {
            checkCorrect();
        }

    }

    /**
     * Requires: None.
     * Modifies: None.
     * Effects: Checks if a player should be eliminated, removes them if needed,
     *          and returns the number of remaining players.
     */
    public int handleElimination() {
        Iterator<Player> iterator = lop.iterator();
        while (iterator.hasNext()) {
            Player p = iterator.next();
            if (p.getLettersAssigned() == 5) {
                iterator.remove();
                System.out.println("Eliminated: " + p.getName());
                if (lop.size() < 2) {
                    System.out.println(lop.get(0).getName() + " has won the game!");
                    return 0;
                }
            }
        }
        return lop.size();
    }

    /**
     * Requires: None.
     * Modifies: Modifies the 'ANSWERS' list and handles player eliminations.
     * Effects: Checks if the answer is correct, validates it, and assigns letters to players accordingly.
     */
    public void checkCorrect() {
        if (answer.charAt(0) == currentLetter) {
            if (DATABASE.contains(answer) && !(ANSWERS.contains(answer))) {
                currentLetter = answer.charAt(answer.length() - 1);
                System.out.println("CORRECT!");
                ANSWERS.add(answer);
                DATABASE.remove(answer);
            } else if (DATABASE.contains(answer) && (ANSWERS.contains(answer))) {
                System.out.println("Wrong Answer: Already Answered");
                assignLetterToPlayer(activePlayer);
            } else {
                System.out.println("Wrong Answer: Invalid Country");
                assignLetterToPlayer(activePlayer);
            }
        } else {
            System.out.println("Wrong Answer: Country doesn't start with given letter");
            assignLetterToPlayer(activePlayer);
        }


        changePlayer(handleElimination());
    }

    /**
     * Requires: None.
     * Modifies: Modifies the 'lettersAssigned' field, assigns a letter to the player,
     *           and increments their letters assigned count.
     * Effects: Assigns a letter from "LOSER" to a player and increases their count of assigned letters.
     */
    public void assignLetterToPlayer(Player p) {
        String loserLetters = "LOSER";
        char letterToAssign = loserLetters.charAt(p.getLettersAssigned());
        p.assignLetter(letterToAssign);
        System.out.println("Letter '" + letterToAssign + "' assigned to " + p.getName());
        p.incrementLettersAssigned();
    }

    /**
     * Requires: The number of remaining players 'size' should be greater than 0.
     * Modifies: Changes the active player for the next turn.
     * Effects: Changes the active player for the next turn and continues the game.
     */
    public void changePlayer(int size) {
        if (size != 0) {
            if (activePlayerIndex == size - 1) {
                activePlayerIndex = 0;
            } else {
                activePlayerIndex++;
            }

            activePlayer = lop.get(activePlayerIndex);
            handleGame();
        }
    }

    /**
    * REQUIRES: None.
    * MODIFIES: command.
    * EFFECTS: Displays a message asking if the user wants to save the game and handles the user's choice. If the user
      chooses to save the game (1), it calls the saveGame method. If the user chooses not to save (2),
      it returns to the main menu by calling the showMainMenu method.
    */
    public void handleSave() {
        System.out.println("Do you want to save the game?\n");
        System.out.println("1 --> Yes");
        System.out.println("2 --> No");
        command = input.nextInt();
        if (command == 1) {
            saveGame();
        } else if (command == 2) {
            showMainMenu();
        }
    }

    /**
    * REQUIRES: None.
    * MODIFIES: DATABASE, ANSWERS, currentLetter, activePlayer.
    * EFFECTS: Saves the current game state to a JSON file at the specified path. The game state includes the list
      of players' information, the database, answers, current letter, and the name of the active player.
    */
    public void saveGame() {
        ArrayList<JSONObject> listOfPlayers = new ArrayList<>();
        for (Player p : lop) {
            listOfPlayers.add(p.toJson());
        }
        GameWriter gameWriter = new GameWriter(JSON_PATH);
        gameWriter.save(currentLetter, listOfPlayers, DATABASE, ANSWERS, activePlayer.getName());
        System.out.println("GAME SAVED!");
    }

    /**
     * REQUIRES: None.
     * MODIFIES: this
     * EFFECTS: prints all the logs in the events
     */
    public void printLogs() {
        for (Event next: EventLog.getInstance()) {
            System.out.println(next.getDate() + " : " + next.getDescription());
        }
    }

    public void quitGame() {
        EventLog.getInstance().logEvent(new Event("Exited the game."));

    }

}