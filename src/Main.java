
/**
 * ICS4U0 Computer Science, Grade 12
 *
 * modified     20201110
 * date         20201103
 * @filename	Main.java
 * @author      Group 2 (Ajinkya, Abdul Hadi jehanzeb)
 * @version     1.0
 */


import java.awt.GridLayout; 
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Main extends JFrame {
	
    public static int width = 1060;
    public static int height = 500; 

    public static int gameDelay = 16;
    
    public static BrickBreakerScreen gameScreen;
    public static MusicPlayer musicPlayer;


    public static void main(String[] args) {

        
        // referencing: https://stackoverflow.com/questions/8073823/forcing-java-to-refresh-the-java-swing-gui/8076959#8076959
        SwingUtilities.invokeLater(() -> {
            // Setting up the JFrame
            JFrame frame = new JFrame();
            frame.setTitle("Brick Breaker");
            frame.setSize(width, height);
            frame.setResizable(false);
            frame.getContentPane().setLayout(new GridLayout());
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            
            // play background music using musicPlayer class functionalities
            musicPlayer = new MusicPlayer();
            musicPlayer.playBackgroundAudio();
            
            
            try {
                // adding game screen to JFrame
                gameScreen = new BrickBreakerScreen(width-10, height);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            frame.add(gameScreen);
            gameScreen.runEvents();
            gameScreen.repaintScreen(gameDelay);
        });
    }
}

