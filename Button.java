/*
 * Author: Grace Pu
 * Date: June 11
 * Description: This program simulates a working version of the puzzle game, 2048. 
 * It serves as the BETA PROGRAM of the ICS4U final culminating project.
 * Button class defines behaviours for the buttons.
 * It is a child of Rectangle because it is a rectangle (mainly used in drawing).
 */

import java.awt.*;

public class Button extends Rectangle {

    // constructor creates button at given location with given dimensions
    public Button(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    // method to check if the button is clicked
    public boolean checkMouse(int a, int b) {
        // System.out.println(a + " " + b + " " + x + " " + y + " " + width + " " +
        // height);
        return (x - width / 2 <= a && a <= x + width / 2 && y - height / 2 <= b && b <= y + height / 2);
    }
}
