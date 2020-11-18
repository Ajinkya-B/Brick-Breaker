
/**
 * ICS4U0 Computer Science, Grade 12
 *
 * modified     20201110
 * date         20201105
 * @filename	BrickBreakerScreen.java
 * @author      Group 2 (Ajinkya, Abdul Hadi jehanzeb)
 * @version     1.0
 */


import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent; 
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


// ==============================================================================
// This class displays and swaps between intro screen, game screen and end screen.
// Insted of using multiple JFrames we simply toggled on/off certain components on the screen.
// ==============================================================================

public class BrickBreakerScreen extends JPanel {
	
    public int height;
    public int width;

    public int ballDelay = 10;

    public int highscore;
    public int score;

    public boolean gameScreen = false;
    public boolean endScreen = false;
    public boolean paused = false;
    
    private final ImageIcon logo;
    private final ImageIcon gameOverIcon;
    private final ImageIcon scoreIcon;
    private final ImageIcon sonicIcon;
    private final ImageIcon marioIcon;
    private final JLabel label1;
    private final JLabel label2;
    private final JLabel label3;
    private final JLabel label4;    
    private final JLabel label5;

    Button play;
    Button quit;
    Button ok;

    Graphics g;
    
    BrickBreakerLogic gameLogic;
    Timer eventsTimer;
    GetLocalHighscore localHighscore;


    // Sets up all the components in the start and end screen
    public BrickBreakerScreen(int width, int height) throws MalformedURLException {

        // set up the widow size and properties
        this.width = width;
        this.height = height;
        this.setBackground(new Color(12, 36, 49));
        this.gameLogic = new BrickBreakerLogic(width, height, this);
        
        this.localHighscore = new GetLocalHighscore();
        this.localHighscore.readHighscore();
        this.highscore = this.localHighscore.localHighscore;

        
        // ========================================================================================
        // Setting up mouse events(using Mouse Adapter class) --> mouse position to move the paddle
        //                                                        mouse click to start the game
        // ========================================================================================
        
        MouseAdapter mouseAdapter = new MouseAdapter() {
            
            @Override
            public void mouseMoved(MouseEvent e) {
                gameLogic.handleMouseMoveEvent(e);
            }
            
            // mouse click --> get out of the aimphase and start moving the ball
            @Override
            public void mouseClicked(MouseEvent e) {
                gameLogic.handleMouseClickEvent(e);
            }
        };
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);

        this.setFocusable(true);
        this.requestFocus();


        // ====================================================================
        // adding commands(using keyListener class) --> escape to end the game 
        //                                              space to pause the game
        // ====================================================================

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                // ESC --> Exit game screen(not the actual game)
                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    try {
                        if(gameScreen){
                            gameLogic.endGame();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(BrickBreakerScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // Space --> pause the game
                if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
                    // pause/continue depending on the value of paused
                    if (paused) {
                        runEvents();
                    }
                    else {
                        stopEvents();
                    }
                    paused = !paused;
                }
            }
        });

        this.setLayout(null);

        
        //===================
        // Create Play Button
        //===================
        play = new Button("PLAY");
        play.setFont(new Font("Agency FB", Font.BOLD, 20));
        play.setBounds(width/2 - 110, 320, 70, 50);
        play.setBackground(new Color(211, 117, 86));
        play.setForeground(new Color(225, 218, 210));
        play.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
               playBtnMouseClicked(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                playBtnMouseEntered(e);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                playBtnMouseExited(e);
            }
        });
        this.add(play);

        
        //===================
        // Create Quit Button
        //===================
        quit = new Button("QUIT");
        quit.setFont(new Font("Agency FB", Font.BOLD, 20));
        quit.setBounds(width/2 + 40, 320, 70, 50);
        quit.setBackground(new Color(211, 117, 86));
        quit.setForeground(new Color(225, 218, 210));
        quit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0); // close the game
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                quitBtnMouseEntered(e);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                quitBtnMouseExited(e);
            }
        });
        this.add(quit);
        
               
        //=================
        // Create OK Button
        //=================
        ok = new Button("OK");
        ok.setFont(new Font("Agency FB", Font.BOLD, 20));
        ok.setBounds(this.width/2-50, 320, 100, 50);
        ok.setBackground(new Color(211, 117, 86));
        ok.setForeground(new Color(225, 218, 210));
        ok.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
               proceedBtnMouseClicked(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                proceedBtnMouseEntered(e);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                proceedBtnMouseExited(e);
            }
        });
        ok.setVisible(false);
        this.add(ok);
        
        
        // ==================
        // Brick Breaker Logo
        // ==================
        logo = new ImageIcon(".\\BrickBreakerLogo.png"); // image created using photoshop
        label1 = new JLabel(logo);
        label1.setBounds(this.width/2-150, -10, 300, 151);
        this.add(label1);
        
        
        // ==============
        // Game Over Logo
        // ==============
        gameOverIcon = new ImageIcon(".\\Gameover.png"); // image created using photoshop
        label2 = new JLabel(gameOverIcon);
        label2.setBounds(this.width/2-170, 0, 340, 111);
        label2.setVisible(false);
        this.add(label2);
        
        
        // ==========
        // Score Logo
        // ==========
        scoreIcon = new ImageIcon(".\\Score.png"); // image created using photoshop
        label3 = new JLabel(scoreIcon);
        label3.setBounds(this.width/2-75, 120, 150, 60);
        label3.setVisible(false);
        this.add(label3);
        
        
        // =====================
        // Sonic gif
        // referance: https://i.gifer.com/origin/00/004670b5114c8dbdaeeb5b26b8c65bb4_w200.gif
        // =====================
        URL sonicUrl = new URL("https://i.gifer.com/origin/00/004670b5114c8dbdaeeb5b26b8c65bb4_w200.gif");
        sonicIcon = new ImageIcon(sonicUrl);
        label4 = new JLabel(sonicIcon);
        label4.setBounds(50, 60, 200, 331);
        label4.setVisible(false);
        this.add(label4);
        
        
        // =====================
        // Mario gif
        // referance: https://pa1.narvii.com/7128/448b387f956e7bc797bac0d37bc36ba87ce6e293r1-256-256_hq.gif
        // =====================
        URL marioUrl = new URL("https://pa1.narvii.com/7128/448b387f956e7bc797bac0d37bc36ba87ce6e293r1-256-256_hq.gif");
        marioIcon = new ImageIcon(marioUrl);
        label5 = new JLabel(marioIcon);
        label5.setBounds(this.width-294, 150, 256, 256);
        label5.setVisible(false);
        this.add(label5);
        
        
        this.setVisible(true);
    }
    
    
    // required method --> repaint/refresh the Graphics
    @Override
    public void paintComponent(Graphics g) {
        this.g = g;
        super.paintComponent(g); // clear the previous paint
        
        
        // painting specific components depending on selected screen
        if (gameScreen) {
            gameLogic.paintGameScreen(g);
            
        }else if(endScreen){
            paintEndScreen(g);
            
        }else {
            paintIntroScreen(g);
        }
    }
    
    
    // =================
    // Intro screen text
    // =================
    public void paintIntroScreen(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Agency FB", Font.PLAIN, 20));
        CenteredString(g, "Created by VMSO Group 2", 150);


        // instructions
        g.setFont(new Font("Agency FB", Font.PLAIN, 17));
        CenteredString(g, "Use the mouse to aim where you want the ball to shoot!", 190);
        CenteredString(g, "Once you're ready, simply left click and start playing!", 220);
        CenteredString(g, "Press 'ESC' to exit the game and 'SPACE' to pause the game.", 250);
        CenteredString(g, "You have 3 lives to try and get the highest score possible!", 280);


        // highscore display
        g.setFont(new Font("Agency FB", Font.BOLD, 27));
        String scoreString = "Highscore: " + this.highscore;
        g.drawString(scoreString, 30, height - 75); 
    } 
    
    
    // ===============
    // End screen text
    // ===============
    public void paintEndScreen(Graphics g){
        g.setColor(Color.WHITE);
        
        // user score display
        g.setFont(new Font("Agency FB", Font.BOLD, 87));
        CenteredString(g, Integer.toString(this.score), 260);
    }
    
    
    //================================================
    // Toggle button and image display for each screen
    //================================================
    
    public void setIntroScreenButtons() {
        this.play.setVisible(true);
        this.quit.setVisible(true);
        this.label1.setVisible(true);
        this.label2.setVisible(false);
        this.label3.setVisible(false);
        this.label4.setVisible(false);
        this.label5.setVisible(false);
        this.ok.setVisible(false);
    }
    
    
    public void setGameScreenButtons(){
        this.play.setVisible(false);
        this.quit.setVisible(false);
        this.label1.setVisible(false);
        this.label2.setVisible(false);
        this.label3.setVisible(false);
        this.label4.setVisible(false);
        this.label5.setVisible(false);
        this.ok.setVisible(false);
    }

    
    public void setEndScreenButtons() {
        this.play.setVisible(false);
        this.quit.setVisible(false);
        this.label1.setVisible(false);
        this.label2.setVisible(true);
        this.label3.setVisible(true);
        this.label4.setVisible(true);
        this.label5.setVisible(true);
        this.ok.setVisible(true);
    }
    
    
    // ==========================
    // Centers the Graphic String
    // referance: https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java/27740330#27740330
    // ==========================
    public void CenteredString(Graphics g, String myString, int y) {
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D rect2D = fm.getStringBounds(myString, g);
        int x = (width - (int)(rect2D.getWidth())) / 2; // finds the x cor for centered string
        g.drawString(myString, x, y);
    }

    
    // ========================
    // Refresh/Re-paints screen
    // ========================
    public void repaintScreen(int delay) {
        
        ActionListener taskPerformer = (ActionEvent evt) -> {
            repaint(); // java method used to update the screen
        };
        new Timer(delay, taskPerformer).start();
    }

    
    // ========================================================================================
    // Runs the logic chnages the cordinates for each component(i.e ball) every 10 milliseconds
    // ========================================================================================
    public void runEvents() {
        ActionListener taskPerformer = (ActionEvent evt) -> {
            try {
                gameLogic.runGame();
            } catch (IOException ex) {
                Logger.getLogger(BrickBreakerScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        eventsTimer = new Timer(ballDelay, taskPerformer);
        eventsTimer.start();
    }

    
    // Stops the logic/event timer --> nothing gets updated
    public void stopEvents() {
        eventsTimer.stop(); 
    }

    
    //====================
    // Play Button Methods
    //====================
    
    private void playBtnMouseClicked(MouseEvent evt) {
        setGameScreenButtons();
        gameScreen = true;
        if (paused) {
            runEvents();
            paused = false;
        }
    }
    
    // Animations
    
    // Increases the text size and moves the button up
    private void playBtnMouseEntered(MouseEvent evt){
        play.setFont(new Font("Agency FB", Font.BOLD, 22));
        play.setBounds(width/2 - 110, 319, 70, 50);
    }
    
    
    // sets is button properties back to default
    private void playBtnMouseExited(MouseEvent evt){
        play.setFont(new Font("Agency FB", Font.BOLD, 20));
        play.setBounds(width/2 - 110, 320, 70, 50);
    }
    
    
    //====================
    // Quit Button Methods
    //====================
    
    // Animations
    
    // Increases the text size and moves the button up
    private void quitBtnMouseEntered(MouseEvent evt){
        quit.setFont(new Font("Agency FB", Font.BOLD, 22));
        quit.setBounds(width/2 + 40, 319, 70, 50);
    }
    
    
    // sets is button properties back to default
    private void quitBtnMouseExited(MouseEvent evt){
        quit.setFont(new Font("Agency FB", Font.BOLD, 20));
        quit.setBounds(width/2 + 40, 320, 70, 50);
    }
    
    
    //=======================
    // Proceed Button Methods
    //=======================
    
    private void proceedBtnMouseClicked(MouseEvent evt) {
        setIntroScreenButtons();
        endScreen = false;
        gameScreen = false;
    }

    // Animations
    
    // Increases the text size and moves the button up
    private void proceedBtnMouseEntered(MouseEvent evt){
        ok.setFont(new Font("Agency FB", Font.BOLD, 22));
        ok.setBounds(this.width/2-50, 319, 100, 50);
    }
    
    
    // sets is button properties back to default
    private void proceedBtnMouseExited(MouseEvent evt){
        ok.setFont(new Font("Agency FB", Font.BOLD, 20));
        ok.setBounds(this.width/2-50, 320, 100, 50);
    }
}
