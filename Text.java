import java.awt.*;

public class Text extends Rectangle {
    public String title;
    public int value;
    private static int roundness = 20;
    private static Color colour = new Color(187, 173, 160);
    private static Color titleColour = new Color(206, 208, 207);
    private static Color textColour = new Color(250, 248, 239);

    // constructor creates ball at given location with given dimensions
    public Text(int x, int y, String t, int s) {
        super(x, y, 125, 50);
        title = t;
        value = s;
    }

    // draws centered text

    private void drawCenteredText(Graphics g, String s, int x, int y) {
        int w = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        int h = (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
        int newX = x - width / 2 - w / 2;
        int newY = y - height / 2 + h / 2;

        g.drawString(s, newX, newY);
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        g.setColor(colour);
        g.fillRoundRect(x - width / 2, y - height / 2, width, height, roundness, roundness);
        g.setFont(new Font("Impact", Font.PLAIN, 10));
        g.setColor(titleColour);
        drawCenteredText(g, title, x + width / 2, y + height / 5);
        g.setFont(new Font("Impact", Font.PLAIN, 18));
        g.setColor(textColour);
        drawCenteredText(g, "" + value, x + width / 2, y + height / 2);
    }
}
