import java.awt.*;

public class Text extends Rectangle {
    public String title;
    public double value;
    private int roundness;
    private Color colour;
    private Color titleColour;
    private Color textColour;

    // constructor creates ball at given location with given dimensions
    public Text(int x, int y, int w, int h, int d, Color c, Color tiC, Color teC, String t, int s) {
        super(x, y, w, h);
        roundness = d;
        colour = c;
        titleColour = tiC;
        textColour = teC;
        title = t;
        value = s;
    }

    // draws centered text

    public static void drawCenteredText(Graphics g, String s, int x, int y) {
        int w = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        int h = (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
        int newX = x - w / 2;
        int newY = y + h / 2;

        g.drawString(s, newX, newY);
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        g.setColor(colour);
        g.fillRoundRect(x, y, width, height, roundness, roundness);
        g.setFont(new Font("Impact", Font.PLAIN, 10));
        g.setColor(titleColour);
        drawCenteredText(g, title, x + width / 2, y + height / 5);
        g.setFont(new Font("Impact", Font.PLAIN, 18));
        g.setColor(textColour);
        drawCenteredText(g, value + "", x + width / 2, y + height / 2);
    }
}
