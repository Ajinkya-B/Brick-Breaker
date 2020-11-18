
/**
 * ICS4U0 Computer Science, Grade 12
 *
 * modified     20201106
 * date         20201102
 * @filename	SetBrick.java
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


public class SetBrick extends Rectangle {
	
    public boolean broken;

    // creates a rectangle to act an individual brick with all its properties(x cor, y cor, width, height, brick state)
    SetBrick(int x, int y, int width, int height, boolean broken) {
        super(x, y, width, height);

        this.broken = broken;
        
    } 

    
    // paint the brick to screen only if it's not broken
    public void paintComponent(Graphics g) {
        if (!this.broken) {
            
            g.setColor(new Color(211, 117, 86));
            
            g.fillRect(x, y, width-1, height-1);
        }
    }
}
