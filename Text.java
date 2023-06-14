/*
 * Author: Grace Pu
 * Date: June 9
 * Description: This program simulates a working version of the puzzle game, 2048. 
 * It serves as the BETA PROGRAM of the ICS4U final culminating project.
 * Text class defines behaviours for the text objects with varying values.
 * It is a child of Rectangle because it is a rectangle (mainly used in drawing).
 */

import java.awt.*;

public class Text extends Rectangle {

    // Variable Declaration
    public String title;
    public int value;
    private static int roundness = 20;
    private static Color colour = new Color(187, 173, 160); // background
    private static Color titleColour = new Color(206, 208, 207); // title text
    private static Color textColour = new Color(250, 248, 239); // value text

    // constructor creates text at given location with given title and value text
    public Text(int x, int y, String t, int s) {
        super(x, y, 125, 50);
        title = t;
        value = s;
    }

    // draws centered text about point (x, y)
    private void drawCenteredText(Graphics g, String s, int x, int y) {
        int w = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        int h = (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
        int newX = x - width / 2 - w / 2;
        int newY = y - height / 2 + h / 2;

        g.drawString(s, newX, newY);
    }

    // draws the current location of the text box to the screen
    public void draw(Graphics g) {
        // background
        g.setColor(colour);
        g.fillRoundRect(x - width / 2, y - height / 2, width, height, roundness, roundness);

        // title
        g.setFont(new Font("Impact", Font.PLAIN, 10));
        g.setColor(titleColour);
        drawCenteredText(g, title, x + width / 2, y + height / 5);

        // value
        g.setFont(new Font("Impact", Font.PLAIN, 18));
        g.setColor(textColour);
        drawCenteredText(g, "" + value, x + width / 2, y + height / 2);
    }
}
