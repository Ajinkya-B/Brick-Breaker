
/**
 * ICS4U0 Computer Science, Grade 12
 *
 * modified     20201106
 * date         20201102
 * @filename	SetPaddle.java
 * @author      Group 2 (Ajinkya, Abdul Hadi jehanzeb)
 * @version     1.0
 */


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle; 


// =======================================================================================
// For checking the collision we used the java Rectangle class which creates a rectangular 
// bounding(a.k.a hitbox) around the components like paddble, brick, and ball.
// =======================================================================================

public class SetPaddle extends Rectangle { 
    
    // creates a rectangle to act as the paddle with all its properties(x cor, y cor, width, height)
    public SetPaddle(int x, int y, int width, int height) {
        super(x, y, width, height); 
    } 
    
    
    // paints the paddle with all the rectangle properties given
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(this.x, this.y, this.width, this.height);
    }
}
