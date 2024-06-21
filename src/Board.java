import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * displays the board for the game
 * also handles the logic from the player when on the board
 */
public class Board extends JPanel {
    private Game game;              // game obj
    private List<Player> players;    // list of players
    
    // Lists and maps to store game location values
    private List<Point> doorLocations = new ArrayList<>();
    private List<Point> weaponLocations = new ArrayList<>();
    private Map<Point, Estate> doorEstateMap = new HashMap<>();
    private Map<Point, Estate> weaponEstateMap = new HashMap<>();

    //rows and cols initialise
    final static int ROWS = 24;
    final static int COLS = 24;

    //cell width and height variable (not sure if this is needed for even fucken correct but we move)
    public static final int CELL_H = GameGUI.GAME_WIDTH_HEIGHT / ROWS;
    public static final int CELL_W = GameGUI.GAME_WIDTH_HEIGHT / COLS;

    //colors for board and estates
    private final Color WHITE_COLOR = Color.WHITE;
    private final Color BORDER_COLOR = Color.BLACK;

    //2d array
    private JPanel[][] boardCells = new JPanel[ROWS][COLS];
    private Player[][] playerGrid = new Player[ROWS][COLS]; // New 2D array
    private boolean[][] doorSquares = new boolean[ROWS][COLS];
    
    /**
     * CONSTRUCTOR for the board class, takes in the game and a list of players, playing the game
     * @param game - current game that will be ran on the board
     * @param players - list of players
     */
    public Board(Game game, List<Player> players) {
        //instance of game and players
        this.game = game;
        this.players = players;

        //setup board
        setLayout(new GridLayout(ROWS, COLS));
        initialize();   // set up all estates and positions

        // Set focus to the panel
        setFocusable(true);
        requestFocus();
    }

    //INITIALISE METHODS
    private void initialize() {
        drawBoard();            //draws 24x24 board with estates and weapons
        initialisePlayers();    // places players on the board
    }

    /**
     * Draws the board 24 x 24
     * Draws estates, deadspots, doors and weapons
     */
    public void drawBoard(){
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                boardCells[row][col] = new JPanel();            // default set cells to white and change later
                boardCells[row][col].setBackground(WHITE_COLOR);
                boardCells[row][col].setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
                
                // Haunted House
                if (col >= 2 && col <= 6 && row >= 2 && row <= 6) {
                    boardCells[row][col].setBackground(Color.BLACK);
                    boardCells[row][col].setBorder(BorderFactory.createLineBorder(Color.RED));
                }

                // Manic Manor
                if (col >= 17 && col <= 21 && row >= 2 && row <= 6) {
                    boardCells[row][col].setBackground(Color.BLACK);
                    boardCells[row][col].setBorder(BorderFactory.createLineBorder(Color.GREEN));
                }

                // Calamity Castle
                if (col >= 2 && col <= 6 && row >= 17 && row <= 21) {
                    boardCells[row][col].setBackground(Color.BLACK);
                    boardCells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLUE));
                }

                // Peril Palace
                if (col >= 17 && col <= 21 && row >= 17 && row <= 21) {
                    boardCells[row][col].setBackground(Color.BLACK);
                    boardCells[row][col].setBorder(BorderFactory.createLineBorder(Color.YELLOW));
                }

                // Visitation Villa
                if (col >= 9 && col <= 14 && row >= 10 && row <= 13) {
                    boardCells[row][col].setBackground(Color.BLACK);
                    boardCells[row][col].setBorder(BorderFactory.createLineBorder(Color.ORANGE));
                }

                // Deadspots
                if ((col >= 5 && col <= 6 && row >= 11 && row <= 12) ||
                        (col >= 17 && col <= 18 && row >= 11 && row <= 12) ||
                        (col >= 11 && col <= 12 && row >= 5 && row <= 6) ||
                        (col >= 11 && col <= 12 && row >= 17 && row <= 18)) {
                    boardCells[row][col].setBackground(Color.BLACK);
                    boardCells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
                
                // adds doors
                if ((col == 5 && row == 7) || (col == 7 && row == 3) ||
                        (col == 16 && row == 5) || (col == 20 && row == 7) ||
                        (col == 18 && row == 16) || (col == 16 && row == 20) ||
                        (col == 3 && row == 16) || (col == 7 && row == 18) ||
                        (col == 12 && row == 9) || (col == 11 && row == 14) ||
                        (col == 8 && row == 12) || (col == 15 && row == 11)) {
                    doorSquares[row][col] = true;
                    // Add the door location to the list
                    Point doorLocation = new Point(row, col);
                    doorLocations.add(doorLocation);
                    // Associate the door location with its corresponding estate
                    Estate estate = getEstateForDoorLocation(row, col);
                    doorEstateMap.put(doorLocation, estate);
                }
                
                if ((col == 5 && row == 6) || (col == 6 && row == 3) ||
                        (col == 17 && row == 5) || (col == 20 && row == 6) ||
                        (col == 18 && row == 17) || (col == 17 && row == 20) ||
                        (col == 3 && row == 17) || (col == 6 && row == 18) ||
                        (col == 12 && row == 10) || (col == 11 && row == 13) ||
                        (col == 9 && row == 12) || (col == 14 && row == 11)) {
                    boardCells[row][col].setBackground(Color.GRAY);
                    boardCells[row][col].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                }
                
                // Draw all weapons
                if((col == 10 && row == 11) || (col == 3 && row == 3) ||
                    (col == 3 && row == 20) || (col == 20 && row == 20) ||
                    (col == 20 && row == 3)){
                    boardCells[row][col].setBackground(Color.PINK);     // draw weapons as pink cells
                    boardCells[row][col].setBorder(BorderFactory.createLineBorder(Color.PINK));
                    Point weaponLocation = new Point(row, col);         // make a location for weapon
                    weaponLocations.add(weaponLocation);                // store weapons locations
                    //associate with estate
                    Estate estate = getEstateForWeaponLocation(row,col);
                    weaponEstateMap.put(weaponLocation, estate);    
                }
                add(boardCells[row][col]);  // add the cell with its info to boardCells[][]
            }
        }
    }
    
    /**
     * list of door locations
     * @return - list of door locations as points
     */
    public List<Point> getDoorLocations() {
        return doorLocations;
    }

    /**
     * return list of weapon locations
     * @return - list of weapon points
     */
    public List<Point> getWeaponLocations(){
        return weaponLocations;
    }

    /**
     * getter if player is on door
     * @param player - a player that we want to check
     * @return - if player is on, returns true
     */
    public boolean isPlayerOnDoor(Player player) {
        int playerRow = player.getRow();
        int playerCol = player.getCol();
        for (Point doorLocation : doorLocations) {  // loop through door locations and check for a match
            int doorRow = (int) doorLocation.getX();
            int doorCol = (int) doorLocation.getY();
            if (playerRow == doorRow && playerCol == doorCol) {return true;}
        }
        return false;
    }

    /**
     * get doors estate from location
     * @param row - row of door
     * @param col - col of door
     * @return - returns the estate of the door
     */
    public Estate getEstateForDoorLocation(int row, int col) {
        if (col == 5 && row == 7 || col == 7 && row == 3) {
            return Estate.HAUNTED_HOUSE;
        } else if (col == 16 && row == 5 || col == 20 && row == 7) {
            return Estate.MANIC_MANOR;
        } else if (col == 3 && row == 16 || col == 7 && row == 18) {
            return Estate.CALAMITY_CASTLE;
        } else if (col == 18 && row == 16 || col == 16 && row == 20) {
            return Estate.PERIL_PALACE;
        } else if (col == 12 && row == 9 || col == 11 && row == 14 || col == 8 && row == 12 || col == 15 && row == 11) {
            return Estate.VISITATION_VILLA;
        }
        return null; // Return null if the location is not a door
    }
    
    /**
     * get weapons estate from location
     * @param row - row of weapon
     * @param col - col of weapon
     * @return - returns the estate of the weapon
     */
    public Estate getEstateForWeaponLocation(int row, int col){
        if(col == 10 && row == 11){
            return Estate.VISITATION_VILLA;
        } else if(col == 3 && row == 3){
            return Estate.HAUNTED_HOUSE;
        } else if(col == 20 && row == 3){
            return Estate.MANIC_MANOR;
        } else if(row == 20 && col == 20){
            return Estate.PERIL_PALACE;
        } else if(col == 3 && row == 20){
            return Estate.CALAMITY_CASTLE;
        }
        return null;
    }
    
    /**
     * get estate door map with location given player
     * @param player - player in question
     * @return - the estate based off the map
     */
    public Estate getEstateForPlayer(Player player) {
        int row = player.getEntryRow();
        int col = player.getEntryCol();
        for (Point doorLocation : doorLocations) {
            if (doorSquares[row][col] && doorLocation.equals(new Point(row, col))) {
                return doorEstateMap.get(doorLocation);
            }
        }
        return null; // Return null if the player is not in an estate
    }
    
    /**
     * get door location given player
     * @param player - player in question
     * @return - the point of the door location
     */
    public Point getEntryDoorLocation(Player player) {
        int row = player.getEntryRow();
        int col = player.getEntryCol();
        if (doorSquares[row][col]) {
            for (Point doorLocation : doorLocations) {
                if (doorLocation.equals(new Point(row, col))) {
                    return doorLocation;
                }
            }
        }
        return null; // Return null if the player is not in front of a door
    }
    
    
    /**
     * Redraws all the players inside the estate every time someone LEAVES
     * @param e - the estate in question
     */
    public void drawEstatesPlayers(Estate e) {
        List<Player> players = e.getPlayersInEstate();
        if (e.getName() == "Haunted House") {
            boardCells[3][4].setBackground(Color.BLACK);
            boardCells[3][4].setBorder(BorderFactory.createLineBorder(Color.RED));
            boardCells[4][5].setBackground(Color.BLACK);
            boardCells[4][5].setBorder(BorderFactory.createLineBorder(Color.RED));
            boardCells[5][4].setBackground(Color.BLACK);
            boardCells[5][4].setBorder(BorderFactory.createLineBorder(Color.RED));
            boardCells[4][3].setBackground(Color.BLACK);
            boardCells[4][3].setBorder(BorderFactory.createLineBorder(Color.RED));
            
            for (int i = 0; i < players.size(); i++) {
                if (i == 0) {
                    boardCells[3][4].setBackground(players.get(i).getColor());
                    boardCells[3][4].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(3, 4);
                } else if (i == 1) {
                    boardCells[4][5].setBackground(players.get(i).getColor());
                    boardCells[4][5].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(4, 5);
                } else if (i == 2) {
                    boardCells[5][4].setBackground(players.get(i).getColor());
                    boardCells[5][4].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(5, 4);
                } else if (i == 3) {
                    boardCells[4][3].setBackground(players.get(i).getColor());
                    boardCells[4][3].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(4, 3);
                }
            }
        } else if (e.getName() == "Manic Manor") {
            boardCells[3][19].setBackground(Color.BLACK);
            boardCells[3][19].setBorder(BorderFactory.createLineBorder(Color.GREEN));
            boardCells[4][20].setBackground(Color.BLACK);
            boardCells[4][20].setBorder(BorderFactory.createLineBorder(Color.GREEN));
            boardCells[5][19].setBackground(Color.BLACK);
            boardCells[5][19].setBorder(BorderFactory.createLineBorder(Color.GREEN));
            boardCells[4][18].setBackground(Color.BLACK);
            boardCells[4][18].setBorder(BorderFactory.createLineBorder(Color.GREEN));
            
            for (int i = 0; i < players.size(); i++) {
                if (i == 0) {
                    boardCells[3][19].setBackground(players.get(i).getColor());
                    boardCells[3][19].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(3, 19);
                } else if (i == 1) {
                    boardCells[4][20].setBackground(players.get(i).getColor());
                    boardCells[4][20].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(4, 20);
                } else if (i == 2) {
                    boardCells[5][19].setBackground(players.get(i).getColor());
                    boardCells[5][19].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(5, 19);
                } else if (i == 3) {
                    boardCells[5][19].setBackground(players.get(i).getColor());
                    boardCells[5][19].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(5, 19);
                }
            }
        } else if (e.getName() == "Calamity Castle") {
            boardCells[18][4].setBackground(Color.BLACK);
            boardCells[18][4].setBorder(BorderFactory.createLineBorder(Color.BLUE));
            boardCells[19][5].setBackground(Color.BLACK);
            boardCells[19][5].setBorder(BorderFactory.createLineBorder(Color.BLUE));
            boardCells[20][4].setBackground(Color.BLACK);
            boardCells[20][4].setBorder(BorderFactory.createLineBorder(Color.BLUE));
            boardCells[19][3].setBackground(Color.BLACK);
            boardCells[19][3].setBorder(BorderFactory.createLineBorder(Color.BLUE));
            
            for (int i = 0; i < players.size(); i++) {
                if (i == 0) {
                    boardCells[18][4].setBackground(players.get(i).getColor());
                    boardCells[18][4].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(18, 4);
                } else if (i == 1) {
                    boardCells[19][5].setBackground(players.get(i).getColor());
                    boardCells[19][5].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(19, 5);
                } else if (i == 2) {
                    boardCells[20][4].setBackground(players.get(i).getColor());
                    boardCells[20][4].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(20, 4);
                } else if (i == 3) {
                    boardCells[19][3].setBackground(players.get(i).getColor());
                    boardCells[19][3].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(19, 3);
                }
            }
        } else if (e.getName() == "Peril Palace") {
            boardCells[18][19].setBackground(Color.BLACK);
            boardCells[18][19].setBorder(BorderFactory.createLineBorder(Color.YELLOW));
            boardCells[19][20].setBackground(Color.BLACK);
            boardCells[19][20].setBorder(BorderFactory.createLineBorder(Color.YELLOW));
            boardCells[20][19].setBackground(Color.BLACK);
            boardCells[20][19].setBorder(BorderFactory.createLineBorder(Color.YELLOW));
            boardCells[19][18].setBackground(Color.BLACK);
            boardCells[19][18].setBorder(BorderFactory.createLineBorder(Color.YELLOW));
            
            for (int i = 0; i < players.size(); i++) {
                if (i == 0) {
                    boardCells[18][19].setBackground(players.get(i).getColor());
                    boardCells[18][19].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(18, 19);
                } else if (i == 1) {
                    boardCells[19][20].setBackground(players.get(i).getColor());
                    boardCells[19][20].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(19, 20);
                } else if (i == 2) {
                    boardCells[20][19].setBackground(players.get(i).getColor());
                    boardCells[20][19].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(20, 19);
                } else if (i == 3) {
                    boardCells[19][18].setBackground(players.get(i).getColor());
                    boardCells[19][18].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(19, 18);
                }
            }
        } else if (e.getName() == "Visitation Villa") {
            boardCells[11][11].setBackground(Color.BLACK);
            boardCells[11][11].setBorder(BorderFactory.createLineBorder(Color.ORANGE));
            boardCells[11][12].setBackground(Color.BLACK);
            boardCells[11][12].setBorder(BorderFactory.createLineBorder(Color.ORANGE));
            boardCells[12][11].setBackground(Color.BLACK);
            boardCells[12][11].setBorder(BorderFactory.createLineBorder(Color.ORANGE));
            boardCells[12][12].setBackground(Color.BLACK);
            boardCells[12][12].setBorder(BorderFactory.createLineBorder(Color.ORANGE));
            
            for (int i = 0; i < players.size(); i++) {
                if (i == 0) {
                    boardCells[11][11].setBackground(players.get(i).getColor());
                    boardCells[11][11].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(11, 11);
                } else if (i == 1) {
                    boardCells[11][12].setBackground(players.get(i).getColor());
                    boardCells[11][12].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(11, 12);
                } else if (i == 2) {
                    boardCells[12][11].setBackground(players.get(i).getColor());
                    boardCells[12][11].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(12, 11);
                } else if (i == 3) {
                    boardCells[12][12].setBackground(players.get(i).getColor());
                    boardCells[12][12].setBorder(BorderFactory.createLineBorder(players.get(i).getColor()));
                    players.get(i).move(12, 12);
                }
            }
        } 
    }

    private boolean isValidPosition(int row, int col) {
    	return row >= 0 && row < ROWS && col >= 0 && col < COLS && boardCells[row][col].getBackground() == Color.WHITE;
    }

    //places players on the board and sets color
    private void initialisePlayers() {
        // get player obj
        Player lucilla = players.get(0);
        Player bert = players.get(1);
        Player Malina = players.get(2);
        Player Percy = players.get(3);
        
        // add players to 2d array 
        playerGrid[1][11] = lucilla; 
        playerGrid[9][1] = bert;     
        playerGrid[22][10] = Percy;   
        playerGrid[14][22] = Malina;    
        
        // Draw players to board
        boardCells[1][11].setBackground(lucilla.getColor()); // Set Lucilla's color on the cell
        boardCells[9][1].setBackground(bert.getColor());    // Set Bert's color on the cell
        boardCells[22][10].setBackground(Percy.getColor());    // Set Phil's color on the cell
        boardCells[14][22].setBackground(Malina.getColor());    // Set Alex's color on the cell
    }

    /**
     * get player on grid -> used for GamePanel
     * @return - player on the grid
     */
    public Player[][] getPlayerGrid() {
        return playerGrid;
    }

    /**
     * cell at grid -> used for GamePanel
     * @return - cell on the grid
     */
    public JPanel[][] getBoardCells() {
        return boardCells;
    }
    
    /**
     * set cell color
     * @param row - row in question
     * @param col - col in question
     * @param color - color we are changing the cell to
     */
    public void setTileColor(int row, int col, Color color){
        boardCells[row][col].setBackground(color);
        repaint();
    }
}
