
/* PlayerBall class defines behaviours for the player-controlled ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
import java.awt.*;

import javax.swing.ImageIcon;

public class Tile extends Rectangle {

    public static final int SIZE = 90; // size of tile

    // constructor creates ball at given location with given dimensions
    public Tile() {

    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public static void draw(Graphics g, int n, int x, int y) {
        // Image img = new ImageIcon(("images/" + n + ".png")).getImage();
        ImageIcon icon = new ImageIcon("images/0.png");
        ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_DEFAULT));
        Image scaled = scaledIcon.getImage();
        g.drawImage(scaled, x, y, null);
    }

}