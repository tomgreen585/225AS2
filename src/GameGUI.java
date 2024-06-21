import javax.swing.*;
/**
 * Acts as the main method for the game
 * Creates frame for the startingScreen, characterChoice, game/board
 * Creates JMenuBarf for starting new game or exiting both new JOptionPanes
 * Logic for closing frames
 * Initiates character starting screen
 * Initiates character selection screen
 * Initiates start game screen
 * Creates game panels for window and information window
 */
public class GameGUI {
    private static JFrame frame;        // game window frame
    private JPanel currentPanel;        // current window being showen
    private Game game;                  // game instance
    private GamePanel gamePanel;        // the panel containing the game
    
    private Boolean closeWindowFlag = false;    // flag to allow closing of windows

    //initial game height identifiers
    public static final int GAME_WIDTH_HEIGHT = 600;    // set window size
    
    /**
     * constructor for the GameGUI
     * creates the frame 
     * creates the menuBar
     * creates the gameMenu
     * creates the newGame menu item
     * starts by showing the start screen
     */
    public GameGUI() {
        //initial frame of game
        frame = new JFrame("Hobby Detectives");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GAME_WIDTH_HEIGHT, GAME_WIDTH_HEIGHT);

        // Create and set up the menu bar
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        // Create the 'Game' menu
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);

        // Create the 'New Game' menu item
        JMenuItem newGameMenuItem = new JMenuItem("New Game");
        newGameMenuItem.addActionListener(e -> showConfirmationDialog("Start a new game?", this::showStartScreen));
        gameMenu.add(newGameMenuItem);

        // Create the 'Exit' menu item
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> showConfirmationDialog("Exit the game?", this::exitGame));
        gameMenu.add(exitMenuItem);

        showStartScreen();
    }
    
    // display message such as "Start a new game?" 
    private void showConfirmationDialog(String message, Runnable action) {
        int response = JOptionPane.showConfirmDialog(frame, message, "Confirmation", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            if(closeWindowFlag){GamePanel.closeInfoContrlFrames(); }
            action.run();
        }
    }
    
    /**
     * close main frame
     */
    public static void closeFrame(){
        frame.dispose();
    }
    
    // Panel to start game
    private void showStartScreen() {
        //initial frame starting screen
        currentPanel = new StartingScreen(e -> showCharacterSelectionScreen());

        frame.getContentPane().removeAll();
        frame.getContentPane().add(currentPanel);

        frame.revalidate();
        frame.repaint();
    }
    
    // players can select their players
    private void showCharacterSelectionScreen() {
        currentPanel = new CharacterSelectionScreen(e -> startGame());
        frame.getContentPane().removeAll();
        frame.getContentPane().add(currentPanel);
        frame.revalidate();
        frame.repaint(); 
    }
    
    // Begin the game
    private void startGame() {
            closeWindowFlag = true;  // you can close the frames
            //create instance of game
            game = new Game();
            
            //create instance of game panel
            gamePanel = new GamePanel(game); // Create the game panel with the initialized game instance
            frame.getContentPane().removeAll();
            frame.getContentPane().add(gamePanel);
        
            //controls panel
            gamePanel.showControlsWindow();
            gamePanel.showInformationWindow();

            frame.revalidate();
            frame.repaint();   
    }
    
    /**
     * display frame
     */
    public void display() {
        frame.setVisible(true);
    }

    /**
     * return frame for display use in other classes
     * @return 
     */
    public JFrame getFrame() {
        return frame;
    } 
    
    /**
     * Exit game frame
     */
    private void exitGame() {
        frame.dispose();
    }

    /**
     * main method, used to run and start the program
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameGUI gameGUI = new GameGUI();
            gameGUI.display();
        });
    }
}