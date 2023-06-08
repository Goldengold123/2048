
/* PlayerBall class defines behaviours for the player-controlled ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
import java.awt.*;

public class Text extends Rectangle {

    public double value;
    private int roundness;
    private Color colour;

    // constructor creates ball at given location with given dimensions
    public Text(int x, int y, int w, int h, int d, Color c) {
        super(x, y, w, h);
        roundness = d;
        colour = c;
    }

    public static void drawCenteredYText(Graphics g, String s, int x, int y) {
        int h = (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
        int newY = y + h / 2;
        g.drawString(s, x, newY);
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        g.setColor(colour);
        g.fillRoundRect(x, y, width, height, roundness, roundness);
        drawCenteredYText(g, value + "", x + roundness, y + height / 2);
    }

}