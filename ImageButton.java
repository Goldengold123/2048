/*
 * Author: Grace Pu
 * Date: June 12
 * Description: This program simulates a working version of the puzzle game, 2048. 
 * It serves as the BETA PROGRAM of the ICS4U final culminating project.
 * ImageButton class defines behaviours for the buttons with image icons (e.g. music button).
 * It is a child of Button because the Button class defines behaviours for the buttons in general.
 */

import java.awt.*;

public class ImageButton extends Button {

    // Variable Declaration
    private Image img; // image for when toggle on
    private Image img2; // image for when toggle off
    private boolean toggle; // boolean for toggle

    // constructor creates image button at given location with the images for on/off
    // calls upon button constructor
    public ImageButton(int x, int y, Image i, Image i2) {
        super(x, y, i.getWidth(null), i.getHeight(null));
        img = i;
        img2 = i2;
        toggle = true;
    }

    // method to toggle boolean
    public void toggle() {
        toggle = !toggle;
    }

    public boolean getValue() {
        return toggle;
    }

    // draws the current location of the image button to the screen based on toggle
    public void draw(Graphics g) {
        if (toggle) // on
            g.drawImage(img, x - img.getWidth(null) / 2, y - img.getHeight(null) / 2, null);
        else // off
            g.drawImage(img2, x - img2.getWidth(null) / 2, y - img2.getHeight(null) / 2, null);
    }

}
