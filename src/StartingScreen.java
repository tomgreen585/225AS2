import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
/**
 * Represents the screen when main is run
 * implements button to start game using JPanel
 */
public class StartingScreen extends JPanel {
	/**
	 * Constructor of StartingScreen
	 * @param startGameListener - takes in Actionlistener 
	 */
    public StartingScreen(ActionListener startGameListener) {
        setLayout(new BorderLayout());
        // print game title 
        JLabel titleLabel = new JLabel("Welcome to Hobby Detectives", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        // add button to begin the game
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(startGameListener);
        add(newGameButton, BorderLayout.CENTER);
    }
}
