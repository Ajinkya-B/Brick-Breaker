
/**
 * ICS4U0 Computer Science, Grade 12
 *
 * modified     20201110
 * date         20201105
 * @filename	BrickBreakerLogic.java
 * @author      Group 2 (Ajinkya, Abdul Hadi jehanzeb)
 * @version     1.0
 */


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;


public class BrickBreakerLogic {

    // Window properties
    public int windowWidth;
    public int windowHeight;
    public BrickBreakerScreen gameScreen;
    
    
    // Ball properties
    SetBall ball;
    public int ballLength = 10;
    public double ballXSpeed;
    public double ballYSpeed;
    public double speedMultiplier;

    
    // Paddle properties
    SetPaddle paddle;
    public int paddleYOffset = 100;
    public int paddleWidth = 80;
    public int paddleHeight = 10;
    public Color paddleColor = Color.WHITE;

    
    // Brick properties
    ArrayList<SetBrick> bricks;
    public int brickWidth;
    public int brickHeight;
    public int numBricks = 43;

    
    // Aimphase properties
    public boolean aimPhase = true;
    public Point aimPoint;
    double dirX;
    double dirY;
    double aimLineX;
    double aimLineY;

    boolean bottomCollision = false;
    
    int lives;
    int score;
    int level;
    
    public static MusicPlayer musicPlayer;
    GetLocalHighscore localHighscore;
    
    
    BrickBreakerLogic(int windowWidth, int windowHeight, BrickBreakerScreen gameScreen) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.gameScreen = gameScreen;
        
        musicPlayer = new MusicPlayer();
        localHighscore = new GetLocalHighscore();

        this.paddleWidth = 100;
        this.paddleYOffset = 150;

        //            new Paddle(x, y, width, length);
        this.paddle = new SetPaddle(windowWidth/2 - paddleWidth/2, windowHeight - paddleYOffset, paddleWidth, paddleHeight);
        
        //          new Ball(x, y, length, xSpeed, ySpeed);
        this.ball = new SetBall(paddle.getCenterX() - ballLength/2, paddle.getCenterY() - paddle.height/2 - ballLength, ballLength, ballXSpeed, ballYSpeed);

        brickWidth = 70;
        brickHeight = brickWidth / 2;

        this.aimPoint = new Point(0, 0);
        setDefaults();  
    }

    
    public final void setDefaults() {
        this.lives = 3;
        this.speedMultiplier = 6;
        this.score = 0;
        this.level = 1;
        this.paddle.width = paddleWidth;
        paddle.width = (int)paddleWidth;
        createBricks(numBricks);
    }

   
    // start movement of the ball
    public void runGame() throws IOException {
        if (!aimPhase) {
            int collisionInt = hasCollision();
            moveBall(collisionInt);
        }
    }

    
    // ===================================================
    // paints all the game screen components in the window
    // ===================================================
    
    // Main paint
    public void paintGameScreen(Graphics g) {
        paddle.paintComponent(g);
        paintBricks(g);
        ball.paintComponent(g);
        paintAimPoint(g);
        paintLives(g);
        paintScore(g);
        paintLevel(g);
    }

    
    public void paintBricks(Graphics g) {
        for (int i = 0; i < bricks.size(); i++) {
            SetBrick brick = bricks.get(i);
            brick.paintComponent(g);
        }
    }
    
    
    // referance: https://stackoverflow.com/a/19466282
    public void paintAimPoint(Graphics g) {
        if (aimPhase) {
            this.dirX = aimPoint.x - ball.getCenterX();
            this.dirY = aimPoint.y - ball.getCenterY();
            double dirLen = Math.sqrt(dirX*dirX + dirY*dirY);
            this.dirX = dirX / dirLen;
            this.dirY = dirY / dirLen;
            this.aimLineX = this.dirX * 90;
            this.aimLineY = this.dirY * 90;
            g.drawLine((int)(ball.getCenterX()), (int)(ball.getCenterY()), (int)(ball.getCenterX() + this.aimLineX), (int)(ball.getCenterY() + this.aimLineY));
        }
    }
    
    
    public void paintLives(Graphics g) {
        int xOffset = 30;
        int yOffset = windowHeight - 70;
        g.setColor(new Color(211, 117, 86));
        for (int i = 0; i < this.lives; i++) {
            g.fillOval(xOffset, yOffset, ballLength, ballLength);
            xOffset += ballLength * 2;
        }
    }
    

    public void paintScore(Graphics g) {
        String scoreString = "Score: " + this.score;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Agency FB", Font.BOLD, 20));
        g.drawString(scoreString, 110, windowHeight - 58);
    }
    
    
    public void paintLevel(Graphics g){
        String levelString = "Level " + this.level;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Agency FB", Font.BOLD, 24));
        g.drawString(levelString, windowWidth - 100, windowHeight - 58);
    }
    
    
    // ==============================================================
    // set x & y speed for the ball depending on the aim phase values
    // ==============================================================
    public void handleMouseClickEvent(MouseEvent e) {
        if (aimPhase && gameScreen.gameScreen) {
            aimPhase = false;
            ball.xSpeed = this.dirX*speedMultiplier;
            ball.ySpeed = this.dirY*speedMultiplier;
        }
    }
    
    
    // =======================================================
    // tracking mouse x and setting it to paddle's x cordinate
    // =======================================================
    public void handleMouseMoveEvent(MouseEvent e) {
        if(!gameScreen.paused){
            if (aimPhase) {
            aimPoint.setLocation(e.getX(), e.getY()); // using mouse adapter class to find x and y cor of the mouse
            
            }else {
                int x = e.getX();

                // left side of window collision/bounds
                if (x > windowWidth - paddle.width/2){
                    x = windowWidth - paddle.width/2;
                }
                // right side of window collision/bounds
                if (x < paddle.width/2){
                    x = paddle.width/2;
                }

                x -= paddle.width/2;
                paddle.setLocation(x, paddle.y);
            }
        }
    }
    
    
    // ==============================================================
    // Creates a new Associative array with default brick properties
    // ==============================================================
    public void createBricks(int n) {
        bricks = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            bricks.add(buildBrick(bricks));
        }
    }
    
    
    // ==========================
    // Building individual bricks
    // ==========================
    public SetBrick buildBrick(ArrayList<SetBrick> bricks) {
        int topX;
        int topY;
        boolean broken = false;
        
        if (bricks.isEmpty()) {
            topX = 0;
            topY = 0;
        }
        else {
            SetBrick lastBrick = bricks.get(bricks.size() - 1);

            if (lastBrick.x + 2*(lastBrick.width) >= this.windowWidth) {
                topX = this.brickWidth;
                topY = lastBrick.y + this.brickHeight;
            }
            else {
                topX = this.brickWidth + lastBrick.x + this.brickWidth; 
                topY = lastBrick.y +1;
            }
        }
        return new SetBrick(topX, topY, brickWidth, brickHeight, broken);
    }

    
    // ===================================================================================
    // Changes ball x & y spped depending on where the ball intersectis with other objects
    // ===================================================================================
    public void moveBall(int collisionInt) throws IOException {
        if (collisionInt == 1) {
            ball.ySpeed = -ball.ySpeed;
        }
        if (collisionInt == 3) {
            if (this.bottomCollision) {
                ball.ySpeed = -ball.ySpeed;
            
            }else{
                lives -= 1;

                if (lives == 0) {
                    endGame();
                }else {
                    this.aimPhase = true;
                    setDefaultLocation();
                }
                
            }
        }
        else if (collisionInt == 2) {
            ball.xSpeed = -ball.xSpeed;
        }
        ball.lastX = ball.x;
        ball.lastY = ball.y;

        ball.setFrame(ball.x + ball.xSpeed, ball.y - ball.ySpeed, ball.length, ball.length);
    }

    
    // ===============================================
    // Checking collisions of all dirrerent components
    //
    // Returns 0 if no collision
    //         1 if horizontal collision
    //         2 if vertical collision
    //         3 if(ball) collides with bottom
    // ===============================================

    // Main collison check 
    public int hasCollision() {
        int brickCollisionInt = brickCollision(this.bricks);
        
        if (brickCollisionInt != 0) {
            return brickCollisionInt;
        }	
        if (paddleCollision() != 0) {
            return 1;
        }
        return windowCollision();	
    }

   
    // paddle collision check
    public int paddleCollision() {
        if (ball.intersects(paddle)) {
            ball.y = paddle.y - paddle.height; // to make sure ball doesn't glitch on to the paddle
            musicPlayer.playCollisionAudio();
            return 1;
        }
        return 0;
    }

     
    // game window collision check
    public int windowCollision() {
        // top collision
        if (ball.x < 0) {
            ball.x = 0;
            musicPlayer.playCollisionAudio();
            return 2;
        }
        // right collision
        else if (ball.x + ball.length > this.windowWidth) {
            musicPlayer.playCollisionAudio();
            return 2;
        }
        // left collision
        else if (ball.y < 0) {
            ball.y = 0;
            musicPlayer.playCollisionAudio();
            return 1;
        }else if (ball.y + ball.length > this.windowHeight) {
            musicPlayer.bottomCollisionAudio();
            return 3;
        }else{
            return 0;
        } 
    }
    
    
    // brick collision check
    public int brickCollision(ArrayList<SetBrick> bricks) {
        for (int i = 0; i < bricks.size(); i++) {
            SetBrick brick = bricks.get(i);
            if (!brick.broken && ball.intersects(brick)) {
                brick.broken = true;
                this.score += 10; // scorekeeping
                checkGameOver();
                int myint = brickCollisionType(brick);
                return myint;
            }
        }
        return 0;
    }

    
    public int brickCollisionType(SetBrick brick) {
        
        // referencing: https://gamedev.stackexchange.com/questions/24078/which-side-was-hit/24091#24091
        // using the concept "Minkowski Sum", also known as "Dilation"
        double checkX = (0.5 * (ball.length + brick.height)) * (ball.getCenterX() - brick.getCenterX());
        double checkY = (0.5 * (ball.length + brick.width)) * (ball.getCenterY() - brick.getCenterY());
        

        if (checkY > checkX) {
            if (checkY > -checkX) { 
                //collision at the top
                musicPlayer.playCollisionAudio();
                return 1;
            }else { 
                // collision on the left
                musicPlayer.playCollisionAudio();
                return 2;
            }
            
        }else {
            if (checkY > -checkX) { 
                // collision on the right
                musicPlayer.playCollisionAudio();
                return 2;
            }else { 
                // collision at the bottom
                musicPlayer.playCollisionAudio();
                return 1;
            }
        }
    }


    public void checkGameOver() {
        
        // goes through the brick object in the array and checks if all the bricks are broken
        for (int i = 0; i < bricks.size(); i++) {
            if (!bricks.get(i).broken){
                return; // ends the method here if a brick is not broken
            }
        }
        
        musicPlayer.winAudio();
        resetGame();
    }
    
    
    public void resetGame(){
        createBricks(numBricks);
        setDefaultLocation();
        this.aimPhase = true;
        speedMultiplier ++;
        level ++;
    }

    
    public void endGame() throws IOException {
        
        // scorekeeping --> checks for highscore
        
        this.gameScreen.score = this.score;
        
        if (this.gameScreen.highscore < this.score) {
            this.gameScreen.highscore = this.score;
            localHighscore.writeHighscore(this.score);
        }
        
        
        this.gameScreen.setEndScreenButtons();
        this.gameScreen.gameScreen = false;
        this.gameScreen.endScreen = true;
        this.aimPhase = true;
        setDefaults();
        setDefaultLocation();
    }
    
    
    public void setDefaultLocation() {
        this.paddle.setLocation(windowWidth/2 - paddleWidth/2, windowHeight - paddleYOffset);
        this.ball.setFrame(paddle.getCenterX() - ballLength/2, paddle.getCenterY() - paddle.height/2 - ballLength, ball.length, ball.length);
    }

}
