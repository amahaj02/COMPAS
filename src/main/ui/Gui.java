package ui;

import model.Game;
import model.Player;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

import java.util.List;


import static java.awt.Cursor.HAND_CURSOR;

/**
 * The Gui class manages the graphical user interface for the COMPAS application,
 * facilitating user interactions, game navigation, and player progress display.
 * It utilizes modular components and integrates with the Game class for data management,
 * providing an intuitive and visually appealing experience for users.
 */
public class Gui {

    JFrame mainFrame;
    JLayeredPane layeredPane;
    JTable table;
    Game game = new Game();

    /**
     * Constructor for the Gui class.
     * Initializes the UIManager, sets up the main frame, and shows the main menu.
     * Requires: None
     * Modifies: UIManager, mainFrame, layeredPane
     * Effects: Initializes the GUI and shows the main menu.
     */
    public Gui() {
        UIManager.put("Button.disabledText", new ColorUIResource(Color.white));
        showSplashScreen();
        init();
        showMainMenu();
    }

    /**
     * Displays a splash screen with a delay.
     * Requires: None
     * Modifies: None
     * Effects: Shows a splash screen for 3 seconds.
     */
    private void showSplashScreen() {
        JFrame frame = new JFrame();
        frame.setSize(180,180);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        ImageIcon splashIcon = new ImageIcon("src/assets/splash-image.png");
        ImageIcon newImage = new ImageIcon(splashIcon.getImage()
                .getScaledInstance(220,180,Image.SCALE_SMOOTH));
        JLabel splashLabel = new JLabel(newImage);
        frame.add(splashLabel);
        frame.setVisible(true);

        try {
            Thread.sleep(3000); // Replace with your desired delay time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        frame.setVisible(false);
        frame.dispose();
    }

    /**
     * Initializes the main frame of the application.
     * Requires: None
     * Modifies: mainFrame, layeredPane
     * Effects: Sets up the main frame with specified properties.
     */
    private void init() {
        mainFrame = new JFrame();
        mainFrame.setTitle("COMPAS - Computerized Atlas");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setSize(new Dimension(540,600));
        mainFrame.setLocationRelativeTo(null); // open window in the center
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0,0,540,600);
        mainFrame.add(layeredPane);

        ImageIcon icon = new ImageIcon("src/assets/splash-image.png");
        mainFrame.setIconImage(icon.getImage());
        mainFrame.getContentPane().setBackground(new Color(0x253439));
        mainFrame.setVisible(true);
        quitGame();
    }

    /**
     * Displays the main menu in the main frame.
     * Requires: None
     * Modifies: layeredPane
     * Effects: Shows the main menu on the main frame.
     */
    private void showMainMenu() {
        mainFrame.setSize(new Dimension(540,600));
        JPanel panel = new JPanel();
        panel.setBounds(0,0,540,600);
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(0x253439));
        JLabel heading = new JLabel("Main Menu");
        heading.setLocation(170,100);
        heading.setFont(new Font("Verdana", Font.BOLD, 25));
        heading.setForeground(Color.white);
        JPanel menuPanel = createMainMenuButtons();
        panel.add(heading, setGBC(0,0,0,0.0f,0.0f,100,0));
        panel.add(menuPanel, setGBC(1,0,0,1.0f,1.0f,0,100));
        layeredPane.add(panel, Integer.valueOf(0));
    }

    /**
     * Creates and configures buttons for the main menu.
     * Requires: None
     * Modifies: None
     * Effects: Returns a panel with buttons for New Game, Load Game, and Quit Game.
     */
    private JPanel createMainMenuButtons() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setSize(new Dimension(300,100));
        menuPanel.setOpaque(false);
        JButton b1 = new JButton("New Game");
        JButton b2 = new JButton("Load Game");
        JButton b3 = new JButton("Quit Game");
        setButtonProperties(b1);
        setButtonProperties(b2);
        setButtonProperties(b3);
        addButtonListeners(b1, b2, b3);
        menuPanel.add(b1, setGBC(0,100,20,0.0f,0.0f,0,0));
        menuPanel.add(Box.createVerticalStrut(30), setGBC(1,0,0,0.0f,0.0f,0,0));
        menuPanel.add(b2, setGBC(2,100,20,0.0f,0.0f,0,0));
        menuPanel.add(Box.createVerticalStrut(30), setGBC(3,0,0,0.0f,0.0f,0,0));
        menuPanel.add(b3, setGBC(4,100,20,0.0f,0.0f,0,0));

        return menuPanel;
    }

    /**
     * Adds action listeners to the main menu buttons.
     * Requires: None
     * Modifies: None
     * Effects: Adds listeners for New Game, Load Game, and Quit Game buttons.
     */
    private void addButtonListeners(JButton b1, JButton b2, JButton b3) {
        b1.addActionListener(e -> {
            layeredPane.removeAll();
            showSelectPlayers();
        });

        b1.addActionListener(e -> {
            // load
        });

        b3.addActionListener(e -> {
            quitGame();
        });
    }

    /**
     * Quits the game and exits the application.
     * Requires: None
     * Modifies: None
     * Effects: Exits the application.
     */
    private void quitGame() {
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                game.quitGame();
                game.printLogs();
                System.exit(0);
            }
        });
    }

    /**
     * Prompts the user to enter the number of players and initializes player information.
     * Requires: None
     * Modifies: game, layeredPane
     * Effects: Prompts for player information and starts the game.
     */
    private void showSelectPlayers() {
        String input = JOptionPane.showInputDialog("Enter a number:");

        // Convert the string input to an integer
        try {
            int number = Integer.parseInt(input);
            // Now 'number' contains the entered integer
            for (int i = 0; i < number; i++) {
                askPlayerInfo(i + 1);
            }
            startGame();
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid integer
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }

    /**
     * Prompts the user to enter the name of a player and adds the player to the game.
     * Requires: None
     * Modifies: game
     * Effects: Adds a player to the game.
     */
    private void askPlayerInfo(int i) {
        String input = JOptionPane.showInputDialog(null,
                "Player " + i + " Name:", "Enter Player Name", JOptionPane.PLAIN_MESSAGE);
        Player p = new Player(input);
        game.addToLop(p);
    }

    /**
     * Sets up the game interface after player information is entered.
     * Requires: None
     * Modifies: layeredPane
     * Effects: Initializes the game interface.
     */
    private void startGame() {
        mainFrame.setSize(new Dimension(700,700));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBackground(new Color(0x253439));
        mainPanel.setBounds(0,0,680,700);
        mainPanel.add(createNavBar(mainPanel));

        layeredPane.add(mainPanel, Integer.valueOf(0));
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    /**
     * Creates a navigation bar for the game interface.
     * Requires: mainPanel
     * Modifies: None
     * Effects: Returns a panel with navigation bar components.
     */
    public JPanel createNavBar(JPanel mainPanel) {
        JPanel navBar = new JPanel();
        navBar.setPreferredSize(new Dimension(700,5));
        navBar.setBackground(Color.BLACK);
        navBar.setLayout(new BoxLayout(navBar, BoxLayout.X_AXIS));
        navBar.setOpaque(false);

        JLabel activeGrid = createLabelForNav("Current Letter: " + "V");
        JLabel activePlayer = createLabelForNav("Current player: " + "Chintu");

        JButton exitButton = createNavBarButtons("Exit");
        JButton showPlayers = createNavBarButtons("Display Players");

        exitButton.addActionListener(e -> {
            layeredPane.removeAll();
            showMainMenu();
        });

        showPlayers.addActionListener(e -> displayPlayers());

        navBar.add(activeGrid);
        navBar.add(Box.createHorizontalStrut(50));
        navBar.add(activePlayer);
        navBar.add(Box.createHorizontalStrut(50));
        navBar.add(exitButton);
        navBar.add(Box.createHorizontalStrut(50));
        navBar.add(showPlayers);

        return navBar;
    }

    /**
     * Creates navigation bar buttons with specified properties.
     * Requires: None
     * Modifies: None
     * Effects: Returns a button with specified properties.
     */
    private JButton createNavBarButtons(String title) {
        JButton b = new JButton(title);
        setButtonProperties(b);
        b.setFont(new Font("Verdana", Font.BOLD,10));

        return b;
    }

    /**
     * Creates a label for the navigation bar with specified properties.
     * Requires: None
     * Modifies: None
     * Effects: Returns a label with specified properties.
     */
    private JLabel createLabelForNav(String text) {
        JLabel label = new JLabel(text);
        label.setBounds(0,0,20,5);
        label.setFont(new Font("Verdana", Font.PLAIN,15));
        label.setForeground(Color.white);
        return label;
    }

    /**
     * Displays a window showing player progress.
     * Requires: None
     * Modifies: None
     * Effects: Displays a window with a table of player progress.
     */
    private void displayPlayers() {
        JFrame frame = new JFrame();
        frame.setTitle("Player Progress");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(new Dimension(540, 200));
        frame.setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        String[] columnNames = {"Name", "Assigned Letters"};
        List<Player> lopCopy = game.getLop();

        Object[][] playerData = new Object[lopCopy.size()][2];
        for (int i = 0; i < lopCopy.size(); i++) {
            Player player = lopCopy.get(i);
            playerData[i][0] = player.getName();
            if (player.getAssignedLetters().size() < 1) {
                playerData[i][1] = "Not Started Yet";
            } else {
                playerData[i][1] = player.getAssignedLetters();
            }
        }

        addStuffToPanel(mainPanel, playerData, columnNames, frame);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Adds components to the player progress panel.
     * Requires: mainPanel, playerData, columnNames, frame
     * Modifies: None
     * Effects: Adds components to the player progress panel.
     */
    private void addStuffToPanel(JPanel mainPanel, Object[][] playerData, String[] columnNames, JFrame frame) {
        table = createTable(playerData, columnNames);
        mainPanel.add(new JScrollPane(table));
        JPanel panel = new JPanel();
        panel.setBounds(0,0,500,50);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton b1 = new JButton("Add Player");
        JButton b2 = new JButton("Modify Selected Player");

        b1.addActionListener(e -> {
            askPlayerInfo(1);
            frame.dispose();
            displayPlayers();
        });

        b2.addActionListener(e -> {
            if (!table.getSelectionModel().isSelectionEmpty()) {
                modifyPlayerInfo();
                frame.dispose();
                displayPlayers();
            }
        });

        panel.add(b1);
        panel.add(b2);
        mainPanel.add(panel);
    }

    /**
     * Modifies the name of a selected player in the game.
     * Requires: playerName, newName
     * Modifies: game
     * Effects: Modifies the name of a selected player in the game.
     */
    private void modifyPlayerInfo() {
        String playerName = (String)table.getValueAt(table.getSelectedRow(), 0);
        String newName = JOptionPane.showInputDialog(null,
                "Enter new name", "Update Player Name", JOptionPane.PLAIN_MESSAGE);
        game.modifyPlayerFromLop(playerName, newName);
    }

    /**
     * Creates a table for displaying player information.
     * Requires: playerData, columnNames
     * Modifies: None
     * Effects: Returns a table with specified data and column names.
     */
    private JTable createTable(Object[][] playerData, String[] columnNames) {
        JTable table = new JTable(playerData, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return table;
    }

    /**
     * Sets properties for buttons in the UI.
     * Requires: b
     * Modifies: b
     * Effects: Sets properties for the specified button.
     */
    private void setButtonProperties(JButton b) {
        b.setFont(new Font("Verdana", Font.BOLD, 20));
        b.setCursor(Cursor.getPredefinedCursor(HAND_CURSOR));
        b.setBackground(Color.BLACK);
        b.setForeground(Color.white);
        b.setFocusable(false);
    }

    /**
     * Configures GridBagConstraints for layout management.
     * Requires: gy, ipadx, ipady, gwx, gwy, pt, pb
     * Modifies: None
     * Effects: Returns configured GridBagConstraints.
     */
    private GridBagConstraints setGBC(int gy, int ipadx, int ipady, float gwx, float gwy, int pt, int pb) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = gy;
        gbc.ipadx = ipadx;
        gbc.ipady = ipady;
        gbc.weightx = gwx;
        gbc.weighty = gwy;
        gbc.insets = new Insets(pt,0,pb,0);
        return gbc;
    }

}
