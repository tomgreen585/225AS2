import java.awt.Color;
import java.util.*;
/**
 * Game logic is held this class
 * Initializes player and board
 * Switches turns
 * Generates Cards 
 * Generates Killer info
 * Overall game logic for Hobby Detectives 
 */
public class Game {
    private Board board;                // hold board
    public static List<Player>players;  // list of player obj
    private int currentPlayerIndex = 0; // Initialize with the first player
    // lists used to make cards
    private List<String> weaponsList = Arrays.asList("Broom", "Scissors", "Knife", "Shovel", "iPad");
    private List<String> playerNames = Arrays.asList("Lucilla", "Bert", "Percy", "Malina");
    private List<String> estatesLists = Arrays.asList("Manic Manor", "Haunted House", "Calamity Castle", "Peril Palace", "Visitation Villa");
    private List<String> killerInfo = new ArrayList<String>();  // 3 items: player, estate, weapon
    List<Estate> estateCards = new ArrayList<>();               // list of the estate enums (To access each)

    /**
     * all game functionality should come out of this class
     * whatever happens in the game logic through this is displayed on the GamePanel
     * game logic
     */
    public Game() {
        //create players and add to list
        players = new ArrayList<>();
        players.add(new Player("Lucilla", 1, 11, Color.RED));   // Player 1
        players.add(new Player("Bert", 9, 1, Color.BLUE));      // Player 2
        players.add(new Player("Malina", 14, 22, Color.YELLOW));// Player 3
        players.add(new Player("Percy", 22, 10, Color.GREEN));  // Player 4
        
        
        generateKiller();   // needs to be done first to then remove later in makeCards()
        makeCards();        // give cards to players and estates
        initBoardClass();   //initialise board
        players.get(currentPlayerIndex).setMyTurn(true);    //getting the players turns
    }

    /**
     * Initialize the board
     */
    private void initBoardClass() {
        board = new Board(this, players);
    }

    /**
     * gets current player position
     * @return - returns player index as integer
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * gets current player
     * @return - returns current player
     */
    public static Player getCurrentPlayer(){
        for (Player player : players){
            if (player.isMyTurn()){
                return player;              // return current player
            }
        }
        return null;
    }
    
    /**
     * handle moving to next players turn 
     */
    void switchTurns() {
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.setMyTurn(false);                     // Set the current player's turn to false
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (players.get(currentPlayerIndex).isEliminated());
        players.get(currentPlayerIndex).setMyTurn(true);    // Set the new current player's turn to true
    }
    
    /**
     * gets board
     * @return - returns board object
     */
    public Board getBoard() {
        return board;
    }

    /**
     * passes it a list and get a random item
     * @param <T>
     * @param list - A generic list
     * @return - a random item from generic list
     */
    public static <T> T getRandomItem(List<T> list) {   // get one random item from list
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
    
    /**
     * creates murderer list
     */
    public void generateKiller(){   // make killer info
        Player player = getRandomItem(players);
        String estate = getRandomItem(estatesLists);
        String weapon = getRandomItem(weaponsList);
        Collections.addAll(killerInfo, player.getName(), estate, weapon); // add all to killerInfo
    }
    
    /**
     * return list of killers info
     * @return - returns the killer infomation as list
     */
    public List<String> getKillerInfo() {
        return killerInfo;
    }
    
    /**
     * return list of estate names
     * @return - list of estates as strings
     */
    public List<String> getEstateLists(){
        return estatesLists;
    }
    
    /**
     * return list of weapons names
     * @return - list of weapons, as strings
     */
    public List<String> getWeaponsList(){
        return weaponsList;
    }
    
    /**
     * return list of player names
     * @return - list of players, as strings
     */
    public List<String> getPlayerList(){
        return playerNames;
    }
    
    /**
     * return list of players objects
     * @return - list of payer objects
     */
    public List<Player> getPlayers() {
        return players;
    }
    
    /**
     * return list of estate objects
     * @return - list of estate objects
     */
    public List<Estate> getEstates() {
        return estateCards;
    }
    
    /** 
     * find if the solve attempt is correct or not
     * @param playerGuess - what player the player has guessed 
     * @param weaponGuess - what weapon the player has guessed
     * @param estateGuess - what estate the player has guessed
     * @return - returns weather the player has gotten the solve attempt correct or not
     */
    public boolean handleSolveAttempt(String playerGuess, String weaponGuess, String estateGuess) {
        if (killerInfo.contains(playerGuess) && killerInfo.contains(weaponGuess) && killerInfo.contains(estateGuess)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * generic method to randomize list eg weapons allocation
     * @param <K> - generic method
     * @param list - takes in a list
     * @return - returns the list, but randomised
     */
    public static <K> List<K> getRandomList(List<K> list) {
        Random rand = new Random();
        List<K> clonedList = new ArrayList<>(list);             // Clone the list to avoid modifying the original one
        List<K> newList = new ArrayList<>();
        int totalItems = list.size();                           // Default size for the random list
        for (int i = 0; i < totalItems; i++) {
            int randomIndex = rand.nextInt(clonedList.size());  // Generate a random index between 0 and size
            newList.add(clonedList.get(randomIndex));           // Add element to the new list
            clonedList.remove(randomIndex);                     // Remove selected element from the cloned list (no duplicates)
        }
        return newList;
    }
    
    /**
     * make the cards and give them out
     */
    public void makeCards(){
        
        List<String> allCards = new ArrayList<String>(); // randomise list so the players dont get predictable cards
        // make lists random so new cards each game
        List<String> weaponListRand = getRandomList(weaponsList);
        List<String> playerNamesRand = getRandomList(playerNames);
        List<String> estatesListRand = getRandomList(estatesLists);
        
        // add random weapons to estates
        estateCards.addAll(java.util.Arrays.asList(Estate.values()));
        for (int i = 0; i < weaponListRand.size(); i++){
            estateCards.get(i).addWeaponCard(weaponListRand.get(i));    // this is an adder in the estate enum
        }
        // add all random lists to all cards
        allCards.addAll(weaponListRand);
        allCards.addAll(playerNamesRand);
        allCards.addAll(estatesListRand);
        allCards.removeAll(killerInfo);
        
        // give cards to players
        int index = 0;
        for(String card : allCards) {
            players.get(index).addCard(card);
            index = (index + 1) % players.size();
        }
    }
    
    /**
     * remove player coords for when they are removed from the game
     * @param player
     */
    public void removePlayerCoordinates(Player player) {
        player.setRow(-1); // Set row to an invalid value to hide the player
        player.setCol(-1); // Set col to an invalid value to hide the player
    }
    
    // remove player after failed solve attempt 
    public void eliminateCurrentPlayer() {
        Player currentPlayer = getCurrentPlayer();
        int row = currentPlayer.getRow();
        int col = currentPlayer.getCol();
        board.setTileColor(row, col, Color.WHITE);
    
        currentPlayer.eliminate(); // Eliminate the player from the game
        
        //switchTurns(); // Switch turns to the next player
        board.repaint();
    }
    
    /**
     * is player still playing or have they been removed
     * @param player - takes in a player
     * @return - returns whether the player is playing as boolean
     */
    public boolean isPlayerParticipating(Player player) {
        return players.contains(player);    // Assuming players is the list of all players in the game
    }
}
