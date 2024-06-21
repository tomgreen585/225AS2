import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

/**
 * Creates instance of board to be displayed with amount of moves for player
 * Implements informationWindow to display information using JScrollPane and JLabel
 * Implements controlWindow that provides a list of buttons for character choices and movements using JButton
 * Logic for players movement for board and estates
 * Implements player guessing using JComboBox and JButton
 * Implements refutation logic and process using JDialog
 * Implements solve attempt logic and process using JDialog and JButton
 * Display winning and loosing windows for game using JFrame and JLabel
 */
public class GamePanel extends JPanel {
    private Game game;
    private static JFrame informationFrame;     // Add this line as a member variable
    private static JFrame controlsFrame;        // Add this line as a member variable

    private JPanel movesPanel;                  // Add Panels
    private JPanel buttonPanel = new JPanel();
    
    private JButton enterEstateButton;          // Add buttons
    private JButton exitEstateButton;
    private JButton guessButton;
    private JButton solveButton;
    private JButton endTurnButton;
    private JButton moveLeftButton;
    private JButton moveRightButton;
    private JButton moveUpButton;
    private JButton moveDownButton;
    
    private JLabel movesLabel;                  // Add label
    private String currentEstateName = "";      // store esate name for info window
    private String currentWeaponString = "";    // store esate weapon for info window
    private int[] incorrectCount = {0};         // counter loss after all refutations
    private boolean isPlayerTurnOngoing = false;
    Estate currentEstate;                       // estate obj 
    private List<String> refuteInfo = new ArrayList<String>();  // each item is a players name and their cards and guess

    //constructor for game panel
    //game panel displays whatever is happening within the game
    //whatever is happening on the board
    //creates seperate panel for player movement (and eventually options)
    //handles player movement using functionality from other classes (mainly player, game and board)
    //sets layout of how it is presented in relation with the GUI
    
    /**
     * constructor for GamePanel
     * @param game - the current game instance
     */
    public GamePanel(Game game) {
        this.game = game;                               //creates instance of game
        setLayout(new BorderLayout());                  //sets layout of game panel
        Board board = game.getBoard();                  //creates instance of board
        add(board, BorderLayout.CENTER);                //where board is positioned on the panel
        movesPanel = new JPanel();                      //creates instance of how many moves player has left 
        movesLabel = new JLabel("Moves Remaining: ");   //text displayed
        movesPanel.add(movesLabel);                     //adds panel from method(below!!!)
        add(movesPanel, BorderLayout.NORTH);            //where this is displayed on the panel(above the board)
        setFocusable(true);
        requestFocus();
    }
    
    /**
     * close all the frames for end of game
     */
    public static void closeAllFrames() {     
        informationFrame.dispose();
        controlsFrame.dispose();
        GameGUI.closeFrame(); 
    }
    
    /**
     * close the frames for new game
     */
    public static void closeInfoContrlFrames() {     
        informationFrame.dispose();
        controlsFrame.dispose();
    }

    /**
     * updates the moves of the player
     * @param movesRemaining - moves remaining for player
     */
    public void updateMovesLabel(int movesRemaining){
        movesLabel.setText("Moves remaining: " + movesRemaining);
    }
    
    /**
     * update info window
     */
    public void updateInformationLabel() {
        showInformationWindow();    // Update the information window with the current player's info
    }
    
  /**
   * clear info window
   */
    public void clearInformationPanel() {
        if (informationFrame != null) {
            informationFrame.dispose();
            informationFrame = null; // Reset the frame reference
        }
    }
    
    /**
     * Display Info window 
     */
    public void showInformationWindow() {
        if (informationFrame != null) {informationFrame.dispose();}  // Dispose of the previous frame if it exists
        
        //make info window
        informationFrame = new JFrame("Information");
        informationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        informationFrame.setBounds(GameGUI.GAME_WIDTH_HEIGHT + 5, 150, 500, 600); // Adjust the size as needed
        // get player to get cards
        Player currentPlayer = game.getCurrentPlayer();
        List<String> playerCards = currentPlayer.getCards();
    
        StringBuilder infoText = new StringBuilder("Player Cards (" + currentPlayer.getName() + "):\n");
        for (String card : playerCards) {infoText.append(card).append("\n");}       // add player cards to string for printing
        
        // Display estate info when player is inside
        if (currentPlayer.InEstate()){
            Estate estate = game.getBoard().getEstateForDoorLocation(currentPlayer.getEntryRow(), currentPlayer.getEntryCol());
            currentEstateName = estate.getName();
            currentWeaponString = estate.getWeaponString();
            infoText.append("\nPlayer entering estate:\n");
            infoText.append("Estate: ").append(currentEstateName).append("\n");
            infoText.append("Weapon: ").append(currentWeaponString).append("\n\n");
        }
        
        infoText.append("Refutation Information:                                                                     \n");
        
        for (String item : refuteInfo) {infoText.append(item).append("\n");}    // put new info on another line
    
        JTextArea infoTextArea = new JTextArea(infoText.toString());
        infoTextArea.setEditable(false);
    
        // Increase the font size
        Font textFont = new Font("Arial", Font.PLAIN, 18); // You can adjust the font and size
        infoTextArea.setFont(textFont);

        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        informationFrame.getContentPane().add(scrollPane);
    
        informationFrame.pack();
        informationFrame.setVisible(true);
    }

    /**
     * creates control window (called in the GUI class in the start game method)
     */
    public void showControlsWindow() {
        //frame displayed when game is started
        controlsFrame = new JFrame("Controls");
        controlsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        controlsFrame.setBounds(GameGUI.GAME_WIDTH_HEIGHT + 5, 0, 100, 50);

        //layout of the buttons on the frames
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2));

        //LEFT BUTTON (moves player left)
        moveLeftButton = new JButton("Move Left");
        moveLeftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCurrentPlayer("left");
            }
        });

        //RIGHT BUTTON (moves player right)
        moveRightButton = new JButton("Move Right");
        moveRightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCurrentPlayer("right");
            }
        });

        //UP BUTTON (moves player up)
        moveUpButton = new JButton("Move Up");
        moveUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCurrentPlayer("up");
                //closeAllFrames();
            }
        });

        //DOWN BUTTON (moves player down)
        moveDownButton = new JButton("Move Down");
        moveDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveCurrentPlayer("down");
            }
        });
    
        //Enter Estate Button (moves player into Estate)
        enterEstateButton = new JButton("Enter Estate");
        enterEstateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterEstate();
            }
        });
        
        //Exit Estate Button (moves player out of Estate)
        exitEstateButton = new JButton("Exit Estate");
        exitEstateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitEstate();
            }
        });
        
        //GUESS BUTTON
        guessButton = new JButton("Guess");
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(game.getCurrentPlayer().InEstate()){ //maybe Game
                    showGuessDialog(game, controlsFrame, buttonPanel);
                    guessButton.setEnabled(false);
                    updateControlsState();
                }
            }
        });
        //SOLVE BUTTON 
        solveButton = new JButton("Solve Attempt");
        solveButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                showSolveAttemptDialog(game, controlsFrame);
            }
        });
        //END TURN BUTTON (after the player has looked at the refute cards they can end turn)
        endTurnButton = new JButton("End Turn");
        endTurnButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                currentEstateName = "";
                currentWeaponString = "";
                endTurnInitiate(game);
                isPlayerTurnOngoing = false; // Turn is ending
                guessButton.setEnabled(true);
                updateControlsState();
            }
        });

        //adds button to the frame
        buttonPanel.add(moveLeftButton);
        buttonPanel.add(moveRightButton);
        buttonPanel.add(moveUpButton);
        buttonPanel.add(moveDownButton);
        buttonPanel.add(enterEstateButton);     //estate show
        buttonPanel.add(exitEstateButton);      //estate exit
        buttonPanel.add(guessButton);           //guess added to button display
        buttonPanel.add(solveButton);           //solveAttempt
        buttonPanel.add(endTurnButton);         //end turn
        controlsFrame.add(buttonPanel);         //adds button panel to the frame
        controlsFrame.pack();
        controlsFrame.setVisible(true);         //sets it visible
    }
    
    // grey out all buttons when all refutes have been done
    private void updateControlsState() {
        boolean controlsEnabled = !isPlayerTurnOngoing;
        moveLeftButton.setEnabled(controlsEnabled);
        moveRightButton.setEnabled(controlsEnabled);
        moveUpButton.setEnabled(controlsEnabled);
        moveDownButton.setEnabled(controlsEnabled);
        enterEstateButton.setEnabled(controlsEnabled);
        exitEstateButton.setEnabled(controlsEnabled);
        solveButton.setEnabled(controlsEnabled);
    }
    
    //player movement method
    //takes direction as a string
    //checks if player can move in that direction through COLOR!!!
    //if player can move in that direction, moves player
    //if player cannot move in that direction, does not move player
    //if player has no moves remaining, switches turns
    private void moveCurrentPlayer(String direction) {
        Player currentPlayer = game.getCurrentPlayer(); // get current player
        if (currentPlayer != null && currentPlayer.isMyTurn() && currentPlayer.getMovesRemaining() > 0 && !currentPlayer.InEstate()) {
            int newRow = currentPlayer.getRow();        // get players position
            int newCol = currentPlayer.getCol();
            switch (direction) {    // change direction
                case "left":
                    newCol--;
                    break;
                case "right":
                    newCol++;
                    break;
                case "up":
                    newRow--;
                    break;
                case "down":
                    newRow++;
                    break; 
            }

            //grid of board used from the Board class
            Player[][] playerGrid = game.getBoard().getPlayerGrid();
            JPanel[][] cellGrid = game.getBoard().getBoardCells();

            //checks if player can move in that direction using color as well
            if (newRow >= 0 && newRow < Board.ROWS && newCol >= 0 && newCol < Board.COLS
                    && playerGrid[newRow][newCol] == null
                    && cellGrid[newRow][newCol].getBackground() == Color.WHITE) {
                //current row and col of the player
                int currentRow = currentPlayer.getRow();
                int currentCol = currentPlayer.getCol();

                //sets the current row and col to null
                playerGrid[currentRow][currentCol] = null;
                //sets the new row and col to the current player
                playerGrid[newRow][newCol] = currentPlayer;

                //sets row and col
                currentPlayer.setRow(newRow);
                currentPlayer.setCol(newCol);

                //sets previous cell to white and new cell to player color
                //needs some fixing when on colored (available movement) cells
                game.getBoard().getBoardCells()[currentRow][currentCol].setBackground(Color.WHITE);
                game.getBoard().getBoardCells()[newRow][newCol].setBackground(currentPlayer.getColor());

                currentPlayer.move(newRow, newCol);

                //if player has no moves remaining, switches turns
                //as well as updates the movement table to amount of turns of next player
                //and decremements the current when move is taken
                if (currentPlayer.getMovesRemaining() == 0) {
                    game.switchTurns();
                    currentPlayer.resetMovesRemaining();
                    updateMovesLabel(currentPlayer.getMovesRemaining());
                    updateInformationLabel();
                } else {
                    updateMovesLabel(currentPlayer.getMovesRemaining());
                }
            }
        }
    }
    
    // player move into estate 
    private void enterEstate() {
        Player currentPlayer = Game.getCurrentPlayer();
        if (currentPlayer != null) {
            int row = currentPlayer.getRow();
            int col = currentPlayer.getCol();
            // Check if the current position is a door square
            if (game.getBoard().isPlayerOnDoor(currentPlayer)) {
                
                Estate estate = game.getBoard().getEstateForDoorLocation(row, col); // Get the corresponding estate of the door
                List<Player> playersInEstate = estate.getPlayersInEstate();         // get players in current estate
                currentEstateName = estate.getName();                               // update current estate info            
                currentWeaponString = estate.getWeaponString();                     // update current estate info 
                currentPlayer.IsInEstate(true);                                     // the player is now in an estate 
                // Gets rid of the player on the board
                JPanel[][] cells = game.getBoard().getBoardCells();                   
                cells[row][col].setBackground(Color.WHITE);
                cells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                game.getBoard().getPlayerGrid()[row][col] = null;
                
                currentPlayer.setEntryPos(row, col);                                // Adds players entry location
                playersInEstate.add(currentPlayer);
                game.getBoard().drawEstatesPlayers(estate);                         // Draws the player onto the estate
                updateInformationLabel();                                           // Update the information window with the new estate information
            }
        }
    }
    
    
    // player exit estate 
    private void exitEstate() {
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer != null && currentPlayer.InEstate()) {
            Estate estate = game.getBoard().getEstateForPlayer(currentPlayer);  // find and remove player from state 
            estate.getPlayersInEstate().remove(currentPlayer);
            if (estate != null) {
                Point entryDoorLocation = game.getBoard().getEntryDoorLocation(currentPlayer);  // Get the door location the player entered through
                if (entryDoorLocation != null) { 
                        int entryRow = entryDoorLocation.y; // Update player position to the entry door location
                        int entryCol = entryDoorLocation.x;
                        
                        JPanel[][] cells = game.getBoard().getBoardCells();
					
        		// ints to stored exiting positions 
        		int newRow = currentPlayer.getEntryRow();
        		int newCol = currentPlayer.getEntryCol();
        		
        		// loop through players to check if door is blocked
        		for (Player p : game.getPlayers()) {
        			int x = p.getCol();
        			int y = p.getRow();
        			
        			// check if position is occupied
        			if (x == currentPlayer.getEntryCol() && y == currentPlayer.getEntryRow()) {
        				// Places player on the next available position
        				if (cells[y][x+1].getBackground() == Color.WHITE) {
        					newRow = y;
        					newCol = x+1;
        				} else if (cells[y][x-1].getBackground() == Color.WHITE) {
        					newRow = y;
        					newCol = x-1;
        				} else if (cells[y+1][x].getBackground() == Color.WHITE) {
        					newRow = y+1;
        					newCol = x;
        				} else if (cells[y-1][x].getBackground() == Color.WHITE) {
        					newRow = y-1;
        					newCol = x;
        				}
        			}
        		}
        		
        		// Update the player grid and cell appearance at the entry door location
        		currentPlayer.move(newRow, newCol);
        		game.getBoard().getPlayerGrid()[newRow][newCol] = currentPlayer;
        		cells[newRow][newCol].setBackground(currentPlayer.getColor());
        
        		game.getBoard().drawEstatesPlayers(estate);
        		currentPlayer.IsInEstate(false);
        		// Show the enter estate button again
        		enterEstateButton.setVisible(true);
                }
            }
        }
    }
    
    //GUESSING METHODS
    private void showGuessDialog(Game game, JFrame frame, JPanel buttonPanel){
        // make dialog window
        JDialog guessDialog = new JDialog(frame, "Make a Guess", true);
        guessDialog.setSize(300,200);
        guessDialog.setLayout(new GridLayout(4,2));
       
        //Create JComboBoxes for player cards, weapon cards, and estates
        JComboBox<String> playerCardsComboBox = new JComboBox<>(game.getPlayerList().toArray(new String[0]));
        JComboBox<String> weaponCardsComboBox = new JComboBox<>(game.getWeaponsList().toArray(new String[0]));

        guessDialog.add(new JLabel("Select Player Card: "));    // player selects items
        guessDialog.add(playerCardsComboBox);
        guessDialog.add(new JLabel("Select Weapon Card: "));
        guessDialog.add(weaponCardsComboBox);

        JButton submitButton = new JButton("Submit");           // submit to save chnages
        submitButton.addActionListener(new ActionListener(){ 
            @Override
            public void actionPerformed(ActionEvent e){
                // read in guess
                String playerGuess = playerCardsComboBox.getSelectedItem().toString();
                String weaponGuess = weaponCardsComboBox.getSelectedItem().toString();
                String estateGuess = game.getBoard().getEstateForPlayer(Game.getCurrentPlayer()).getName();
                guessDialog.dispose();                              // close dialog window
                Player currentPlayer = Game.getCurrentPlayer();
                tpPlayer(currentPlayer, playerGuess, estateGuess);  // move player to estate 
                refuteInfo.add(Game.getCurrentPlayer().getName() + " Guessed: " + playerGuess + ", " + weaponGuess + ", " + estateGuess);  // add guess info to refuteInfo to be printed
                updateInformationLabel();                           // update info panel with guess info from refuteInfo
                isPlayerTurnOngoing = true;                         // player is still playing while the other show cards
                refutationProcess(game, playerGuess, weaponGuess, estateGuess, frame);  // start refute process
                refuteInfo.clear();                                 // clear refute action logs
            }
        });
        guessDialog.add(submitButton);
        guessDialog.setVisible(true);   // show dialog button
    }
    
    // Move player to estate (when a guess happens)
    private void tpPlayer(Player currentPlayer, String playerName, String estate) {
        List<Player> players = game.getPlayers();
        List<Estate> estates = game.getEstates();
        JPanel[][] cells = game.getBoard().getBoardCells();
        Player movingPlayer = null;
        Estate destEstate = null;
        
        // Finds the corresponding player
        for (Player p : players) {
            if (p.getName() == playerName) {
                movingPlayer = p;
            }
        }
        // Finds the corresponding estate
        for (Estate e : estates) {
            if (e.getName().equals(estate)) {
                destEstate = e;
                break; // Once found, exit the loop
            }
        }
        // player is out or cant find estate
        if (movingPlayer == null || movingPlayer.isEliminated() || destEstate == null) {
            return; // Exit the method
        }
        // get player pos
        int playerX = movingPlayer.getCol();
        int playerY = movingPlayer.getRow();
        if (!movingPlayer.InEstate()) { // if the player is not already in a state 
            cells[playerY][playerX].setBackground(Color.WHITE);
            cells[playerY][playerX].setBackground(Color.WHITE);
        } else {
            Estate fromEstate = null;
            for (Estate e : game.getEstates()) {
                if (e.getPlayersInEstate().contains(movingPlayer)) {
                    fromEstate = e;
                }
            }
            // move player and redraw
            fromEstate.getPlayersInEstate().remove(movingPlayer);
            game.getBoard().drawEstatesPlayers(fromEstate);
        }
        destEstate.getPlayersInEstate().add(movingPlayer);  // add the player to the dest estate
        game.getBoard().drawEstatesPlayers(destEstate);     // redraw
        movingPlayer.setEntryPos(currentPlayer.getEntryRow(), currentPlayer.getEntryCol()); // set moving players entry door to same as current player
        movingPlayer.IsInEstate(true);  // Update estate status
    }
    
    
  //refutation process when guess happens
    private void refutationProcess(Game game, String playerGuess, String weaponGuess, String estateGuess, JFrame frame) {
        List<Player> players = Game.players; //gain list of players             
        Player guessingPlayer = Game.getCurrentPlayer();   //current player making guess 
        clearInformationPanel(); //clear panel so current player info not shown
    
        for (Player refutingPlayer : players) {     
            if (refutingPlayer != guessingPlayer) {
                List<String> refutingPlayerCards = refutingPlayer.getCards();
                JDialog refutationDialog = new JDialog(frame, "Refutation", true); //create dialog for refutation
                refutationDialog.setSize(300, 200);
                refutationDialog.setLayout(new GridLayout(refutingPlayerCards.size() + 1, 2));
                
                // Check if the refuting player has matching cards
                boolean hasMatchingCard = false;
                for (String card : refutingPlayerCards) { 
                    if (card.equals(playerGuess) || card.equals(weaponGuess) || card.equals(estateGuess)) {
                        hasMatchingCard = true; // If the refuting player has a matching card, set the flag to true
                        break;
                    }
                }
    
                if (hasMatchingCard) { // If the refuting player has a matching card
                    // Add buttons for matched cards
                    for (String card : refutingPlayerCards) {
                        if (card.equals(playerGuess) || card.equals(weaponGuess) || card.equals(estateGuess)) {
                            JButton cardButton = new JButton(card); // Create a button for the refute card(s)
                            cardButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    refuteInfo.add("\n" + refutingPlayer.getName() + " refutes with: " + card); //show refutation card in dialog
                                    refutationDialog.dispose();
                                }
                            });
                            refutationDialog.add(cardButton); // Add the button to the dialog
                        }
                    }
                    refutationDialog.setVisible(true);
                } else { //if refuting player does not have matching card
                    JButton doNotHaveButton = new JButton("Do Not Have Card"); //create button for no refute cards
                    doNotHaveButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            refuteInfo.add("\n" + refutingPlayer.getName() + " does not have any of the cards."); //show no refute card in dialog
                            refutationDialog.dispose();
                        }
                    });
                    refutationDialog.add(doNotHaveButton); //add buttton to dialog
                    refutationDialog.setVisible(true);
                }
            }
        }
        updateInformationLabel();
    }

    // end the players turn and clear refute log on screen and list
    private void endTurnInitiate(Game game){
        game.switchTurns();
        updateInformationLabel();
        refuteInfo.clear();
    }

    //SOLVE ATTEMPT METHODS
    private void showSolveAttemptDialog(Game game, JFrame frame) {
        // Make solve attempt window
        JDialog solveDialog = new JDialog(frame, "Solve Attempt", true);
        solveDialog.setSize(300, 200);
        solveDialog.setLayout(new GridLayout(4, 2));
        // drop down options to select guess
        JComboBox<String> playerCardsComboBox = new JComboBox<>(game.getPlayerList().toArray(new String[0]));
        JComboBox<String> weaponCardsComboBox = new JComboBox<>(game.getWeaponsList().toArray(new String[0]));
        JComboBox<String> estateCardsComboBox = new JComboBox<>(game.getEstateLists().toArray(new String[0]));
        
        solveDialog.add(new JLabel("Select Player Card: "));
        solveDialog.add(playerCardsComboBox);
        solveDialog.add(new JLabel("Select Weapon Card: "));
        solveDialog.add(weaponCardsComboBox);
        solveDialog.add(new JLabel("Select Estate Card: "));
        solveDialog.add(estateCardsComboBox);
    
        JButton submitButton = new JButton("Submit");   // button to save solve attempt
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // read in guess
                String playerGuess = playerCardsComboBox.getSelectedItem().toString();
                String weaponGuess = weaponCardsComboBox.getSelectedItem().toString();
                String estateGuess = estateCardsComboBox.getSelectedItem().toString();
                boolean isCorrectSolve = game.handleSolveAttempt(playerGuess, weaponGuess, estateGuess);    // was the guess correct (win or remove player)
                if (isCorrectSolve) {   // attempt was correct player wins game
                    solveDialog.dispose();
                    displayWinFrame();  // Call a new method to display the "YOU WON" frame
                } else {                // attempt was wrong, remove player
                    solveDialog.dispose();
                    displayIncorrectFrame(game.getCurrentPlayer()); // Display the "Incorrect" frame
                    incorrectCount[0]++;            // Increment the incorrect count. 
                    if (incorrectCount[0] >= 4) {   // all players have guessed worng, everyone looses
                        displayGameLostFrame();     // Call method to display the "Game Lost - Killer Won" frame
                    }
                }
            }
        });
        solveDialog.add(submitButton);
        solveDialog.setVisible(true);
    }
    
    // when game is won, show winning screen
    private void displayWinFrame() {
        closeAllFrames();   // close all frames 
        // make winning window
        JFrame winFrame = new JFrame("Congratulations!");
        winFrame.setSize(300, 200);
        winFrame.setLayout(new BorderLayout());
        JLabel messageLabel = new JLabel("YOU WON!");
        winFrame.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton exitGameButton = new JButton("Exit Game");  // button to exit game 
        exitGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        buttonPanel.add(exitGameButton);
        winFrame.add(buttonPanel, BorderLayout.SOUTH);
        winFrame.setVisible(true);
    }

    // You loose frame (for when the player looses)
    private void displayIncorrectFrame(Player currentPlayer) {
        JFrame incorrectFrame = new JFrame("Incorrect!");
        incorrectFrame.setSize(300, 200);
        incorrectFrame.setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel("Incorrect! You are now only refute.");
        incorrectFrame.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton nextButton = new JButton("Next");

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.eliminateCurrentPlayer();
                incorrectFrame.dispose();
                game.switchTurns();
                updateInformationLabel();
                
            }
        });
        buttonPanel.add(nextButton);
        incorrectFrame.add(buttonPanel, BorderLayout.SOUTH);
        incorrectFrame.setVisible(true);
    }

    // game lost frame (when everyone is out)
    private void displayGameLostFrame() {
        // Make frame
        JFrame gameLostFrame = new JFrame("Game Lost");
        gameLostFrame.setSize(300, 200);
        gameLostFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        JLabel messageLabel = new JLabel("Game Lost - Killer Won");
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application when the "Close" button is clicked
            }
        });
        // make panel and add Items to display
        JPanel panel = new JPanel();                   
        panel.setLayout(new BorderLayout());
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);
        gameLostFrame.add(panel);
        gameLostFrame.setVisible(true);
    }
}