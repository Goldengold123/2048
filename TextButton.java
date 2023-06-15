/*
 * Author: Grace Pu
 * Date: June 12
 * Description: This program simulates a working version of the puzzle game, 2048. 
 * It serves as the BETA PROGRAM of the ICS4U final culminating project.
 * TextButton class defines behaviours for the buttons with text labels (e.g. play, menu buttons).
 * It is a child of Button because the Button class defines behaviours for the buttons in general.
 */

import java.awt.*;

public class TextButton extends Button {

    // Variable Declaration
    private String text;
    private static int roundness = 20;
    public static Color colour = new Color(187, 173, 160); // background
    public static Color colourMouseOver = new Color(166, 147, 130); // background mouseOver
    private static Color textColour = new Color(250, 248, 239); // text

    // constructor creates text button at given location with given text
    // calls upon button constructor
    public TextButton(int x, int y, String t) {
        super(x, y, 125, 50);
        text = t;
    }

    // draws centered text about point (x, y)
    private void drawCenteredText(Graphics g, String s, int x, int y) {
        int w = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        int h = (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
        int newX = x - width / 2 - w / 2;
        int newY = y - height / 2 + h / 2;

        g.drawString(s, newX, newY);
    }

    // draws the current location of the text button to the screen
    public void draw(Graphics g, int mX, int mY) {
        // background
        if (checkMouse(mX, mY))
            g.setColor(colourMouseOver);
        else
            g.setColor(colour);
        g.fillRoundRect(x - width / 2, y - height / 2, width, height, roundness, roundness);

        // text
        g.setFont(new Font("Impact", Font.PLAIN, 18));
        g.setColor(textColour);
        drawCenteredText(g, text, x + width / 2, y + height / 2);
    }
}
