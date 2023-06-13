import java.awt.*;

public class ImageButton extends Button {
    private Image img;
    private Image img2;
    private boolean toggle;

    // constructor creates ball at given location with given dimensions
    public ImageButton(int x, int y, Image i, Image i2) {
        super(x, y, i.getWidth(null), i.getHeight(null));
        img = i;
        img2 = i2;
        toggle = true;
    }

    public void toggle() {
        toggle = !toggle;
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        if (toggle)
            g.drawImage(img, x - img.getWidth(null) / 2, y - img.getHeight(null) / 2, null);
        else
            g.drawImage(img2, x - img2.getWidth(null) / 2, y - img2.getHeight(null) / 2, null);
    }

}
