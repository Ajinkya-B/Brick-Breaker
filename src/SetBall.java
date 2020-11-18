
/**
 * ICS4U0 Computer Science, Grade 12
 *
 * modified     20201107
 * date         20201102
 * @filename	SetBall.java
 * @author      Group 2 (Ajinkya, Abdul Hadi jehanzeb)
 * @version     1.0
 */


import java.awt.Color; 
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;


// =======================================================================================
// For checking the collision we used the java Rectangle class which creates a rectangular 
// bounding(a.k.a hitbox) around the components like paddble, brick, and ball.
// =======================================================================================


public class SetBall extends Ellipse2D.Double {
	
    public double lastX;
    public double lastY;

    // ball size
    public double length;  


    public double xSpeed;
    public double ySpeed;


    // creates a rectangle to act as the ball with all its properties(x cor, y cor, width, height)
    SetBall(double x, double y, double length, double xSpeed, double ySpeed) {
        super(x, y, length, length);

        this.length = length;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    } 

    public void paintComponent(Graphics g) {
        g.setColor(new Color(211, 117, 86));
        g.fillOval((int)(x), (int)(y), (int)(this.length), (int)(this.length));
    }

}
