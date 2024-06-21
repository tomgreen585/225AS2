import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * handles logic for the player when moving around the board
 * also contains some methods that should go in the game
 */
public class Player {
    // player variables
    private int row;            // player position
    private int col;
    private int entryRow;       // used for tp to estates
    private int entryCol;
    private Color color;        // player square colour
    private int movesRemaining; // current movesRemaining
    private List<String> playerCards = new ArrayList<String> ();    // cards player was delt
    private String name;        
    
    // players turn infomation
    private boolean isMyTurn;
    private boolean inEstate = false;
    private boolean eliminated = false;
    
    /**
     * constructor
     * @param Name - name of player
     * @param row - starting row of player
     * @param col - starting col of player
     * @param color - color of player
     */
    public Player(String Name, int row, int col, Color color) { // construct and initialise players
        this.row = row; 
        this.col = col;
        this.color = color;
        this.isMyTurn = false;
        this.name = Name;
        this.movesRemaining = 0;     //moves remaining for player starts at 0
    }
    
    //dice method for player
    private void rollDice(){
        Random random = new Random();
        //movesRemaining = random.nextInt(6) + 1;
        
//        FOR TESTING
      movesRemaining = 100; 
    }
    
    /**
     * decrease remaining moves of player
     */
    public void decrementMovesRemaining() {
        if (movesRemaining > 0) {
            movesRemaining--;
        }
    }
    
    
    // GETTERS:
    /**
     * getter for player turn
     * @return - true if is players turn
     */
    public boolean isMyTurn() {
        return isMyTurn;
    }
    
    /**
     * return players cards
     * @return - list of players cards, as strings
     */
    public List<String> getCards(){
        return playerCards;
    }
    
    /**
     * return players name
     * @return - player name
     */
    public String getName(){
        return name;
    }
    
    /**
     * return string of players name and their cards
     * @return - player name and cards
     */
    public String printCards() {    
        return "Player name: " + name + " Player cards: " + playerCards;
    }
    
    /**
     * return remaining moves
     * @return - number of moves remaining, integer
     */
    public int getMovesRemaining() {
        return movesRemaining;
    }
    
    /**
     * color of each player
     * @return - players color
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * getter for row
     * @return - players row
     */
    public int getRow() {
        return row;
    }

    /**
     * getter for col
     * @return - players col
     */
    public int getCol() {
        return col;
    }
    
    /**
     * checks whether player is in an estate
     * @return - true if player is in an estate
     */
    public boolean InEstate() {
        return inEstate;
    }
    
    /**
     * has the player been eliminated 
     * @return - true if player has been eliminated
     */
    public boolean isEliminated(){
        return eliminated;
    }
    
    /**
     * return entry col after tp
     * @return - entry col
     */
    public int getEntryCol() {
        return this.entryCol;
    }
    
    /**
     * return entry row after tp
     * @return - entry row
     */
    public int getEntryRow() {
        return this.entryRow;
    }
    
    
    
    // SETTERS
    /**
     * set player turn flag
     * @param myTurn - changes if is / isnt players turn
     */
    public void setMyTurn(boolean myTurn) {
        this.isMyTurn = myTurn;
        if(myTurn){
            rollDice();
        }
    }
    
    /**
     * movement for players (update position)
     * @param newRow - players new row
     * @param newCol - players new col
     */
    public void move(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
        decrementMovesRemaining();
    }
    
    /**
     * add to players cards
     * @param a - card in question, as string
     */
    public void addCard(String a){
        playerCards.add(a);
    }
    
    /**
     * reset total moves for next player
     */
    public void resetMovesRemaining() {
        movesRemaining = 0;
    }
    
    /**
     * setter for row
     * @param newRow - the new row 
     */
    public void setRow(int newRow) {
        this.row = newRow;
    }

    /**
     * setter for col
     * @param newCol - the new col
     */
    public void setCol(int newCol) {
        this.col = newCol;
    }
    
    /**
     * changes weather a player is in an estate or not
     * @param b - true if they are, false if not
     */
    public void IsInEstate(boolean b) {
        this.inEstate = b;
    }
    
    /**
     * eliminate a player
     */
    public void eliminate(){
        eliminated = true;
    }
    
    /**
     * set entry pos for tp to a estate
     * @param r - row of entry 
     * @param c - col of entry
     */
    public void setEntryPos(int r, int c) {
        this.entryRow = r;
        this.entryCol = c;
    }
}
