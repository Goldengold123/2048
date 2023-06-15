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
    private Image imgOn; // image for when toggle on
    private Image imgOnMouse; // image for when toggle on
    private Image imgOff; // image for when toggle off
    private Image imgOffMouse; // image for when toggle off
    private boolean toggle; // boolean for toggle

    // constructor creates image button at given location with the images for on/off
    // calls upon button constructor
    public ImageButton(int x, int y, Image on, Image onMouse, Image off, Image offMouse) {
        super(x, y, on.getWidth(null), on.getHeight(null));
        imgOn = on;
        imgOnMouse = onMouse;
        imgOff = off;
        imgOffMouse = offMouse;
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
    public void draw(Graphics g, int mX, int mY) {
        if (toggle) { // on
            if (checkMouse(mX, mY))
                g.drawImage(imgOnMouse, x - imgOnMouse.getWidth(null) / 2, y - imgOnMouse.getHeight(null) / 2, null);
            else
                g.drawImage(imgOn, x - imgOn.getWidth(null) / 2, y - imgOn.getHeight(null) / 2, null);
        } else { // off
            if (checkMouse(mX, mY))
                g.drawImage(imgOffMouse, x - imgOffMouse.getWidth(null) / 2, y - imgOffMouse.getHeight(null) / 2, null);
            else
                g.drawImage(imgOff, x - imgOff.getWidth(null) / 2, y - imgOff.getHeight(null) / 2, null);
        }
    }

}
