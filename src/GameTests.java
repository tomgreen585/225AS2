
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
/**
 * JUnit testing
 */
public class GameTests {
	
	/**
	 * tests player initialization
	 */
    @Test
    public void testPlayerInitialization() {
        Player player = new Player("TestPlayer", 0, 0, Color.YELLOW);
        assertEquals("TestPlayer", player.getName());
        assertEquals(0, player.getRow());
        assertEquals(0, player.getCol());
        assertFalse(player.isMyTurn());
    }
    
    /**
     * testing player movement
     */
    @Test
    public void testPlayerMovement() {
        Player player = new Player("TestPlayer", 0, 0, Color.RED);
        player.move(2, 3);
        assertEquals(2, player.getRow());
        assertEquals(3, player.getCol());
    }
    
    /**
     * testing player elimination 
     */
    @Test
    public void testPlayerElimination() {
        Player player = new Player("TestPlayer", 0, 0, Color.RED);
        assertFalse(player.isEliminated());
        player.eliminate();
        assertTrue(player.isEliminated());
    }

    /**
     * testing player turn change
     */
    @Test
    public void testPlayerTurn() {
        Player player = new Player("TestPlayer", 0, 0, Color.RED);
        assertFalse(player.isMyTurn());
        player.setMyTurn(true);
        assertTrue(player.isMyTurn());
    }

    /**
     * testing if player cards are being added
     */
    @Test
    public void testPlayerCards() {
        Player player = new Player("TestPlayer", 0, 0, Color.RED);
        player.addCard("Card1");
        player.addCard("Card2");
        assertEquals(2, player.getCards().size());
    }
    
    /**
     * testing if remaining moves are working
     */
    @Test
    public void testPlayerRemainingMoves() {
        Player player = new Player("TestPlayer", 0, 0, Color.RED);
        player.setMyTurn(true);
        assertEquals(player.getMovesRemaining(), player.getMovesRemaining());
        int decreasedMove = player.getMovesRemaining() - 1;
        player.decrementMovesRemaining();
        assertEquals(decreasedMove, player.getMovesRemaining());
    }

    /**
     * testing if player entry positions work
     */
    @Test
    public void testPlayerEntryPosition() {
        Player player = new Player("TestPlayer", 0, 0, Color.RED);
        player.setEntryPos(2, 3);
        assertEquals(2, player.getEntryRow());
        assertEquals(3, player.getEntryCol());
    }
    
    /**
     * testing player moving into an estate works
     */
    @Test
    public void testPlayerInEstateFlag() {
        Player player = new Player("TestPlayer", 0, 0, Color.RED);
        assertFalse(player.InEstate());
        player.IsInEstate(true);
        assertTrue(player.InEstate());
    }
    
    /**
     * testing if starting screen components work
     */
    @Test
    public void testStartingScreenComponents() {
        // Simulate creating a StartingScreen
        StartingScreen startingScreen = new StartingScreen(null);

        assertNotNull(startingScreen.getComponent(0)); // Title label
        assertTrue(startingScreen.getComponent(0) instanceof JLabel);

        assertNotNull(startingScreen.getComponent(1)); // New Game button
        assertTrue(startingScreen.getComponent(1) instanceof JButton);
    }
    
    /**
     * testing weapon position initialization works
     */
    @Test
    public void testWeaponInitialization() {
        Weapon weapon = new Weapon(3, 4, null);

        assertEquals(3, weapon.getRow());
        assertEquals(4, weapon.getCol());
        assertNull(weapon.getEstate());
    }
}