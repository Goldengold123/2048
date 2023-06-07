
/* PlayerBall class defines behaviours for the player-controlled ball  

child of Rectangle because that makes it easy to draw and check for collision

In 2D GUI, basically everything is a rectangle even if it doesn't look like it!
*/
import java.awt.*;

public class Tile extends Rectangle {

    public static final int SIZE = 90; // size of tile
    public static final Color[] colours = {
            new Color(205, 193, 180),
            new Color(238, 228, 218),
            new Color(237, 224, 200),
            new Color(242, 177, 121),
            new Color(245, 149, 99),
            new Color(246, 124, 95),
            new Color(246, 94, 59),
            new Color(237, 207, 114),
            new Color(237, 204, 97),
            new Color(237, 200, 80),
            new Color(237, 197, 63),
            new Color(237, 194, 46),
            new Color(60, 58, 50)
    };

    // constructor creates ball at given location with given dimensions
    public Tile() {

    }

    public static void drawCenteredText(Graphics g, String s, int x, int y) {
        int w = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        int h = (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
        int newX = x - w / 2;
        int newY = y + h / 2;

        g.drawString(s, newX, newY);
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public static void draw(Graphics g, int n, int x, int y) {
        g.setColor(colours[(n == 0) ? 0 : Math.min(12, (int) (Math.log10(n) / Math.log10(2)))]);
        g.fillRoundRect(x, y, SIZE, SIZE, SIZE / 3, SIZE / 3);
        g.setColor((n <= 4) ? new Color(119, 110, 101) : new Color(249, 246, 242));
        if (n != 0)
            drawCenteredText(g, n + "", x + SIZE / 2, y + SIZE / 2);
        // ImageIcon icon = new ImageIcon("images/" + n + ".png");
        // ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(SIZE,
        // SIZE, Image.SCALE_DEFAULT));
        // Image scaled = scaledIcon.getImage();
        // g.drawImage(scaled, x, y, null);
    }

}