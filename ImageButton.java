import java.awt.*;

public class ImageButton extends Button {
    private Image img;

    // constructor creates ball at given location with given dimensions
    public ImageButton(int x, int y, Image i) {
        super(x, y, i.getWidth(null), i.getHeight(null));
        img = i;
    }

    // called frequently from the GamePanel class
    // draws the current location of the ball to the screen
    public void draw(Graphics g) {
        g.drawImage(img, x - img.getWidth(null) / 2, y - img.getHeight(null) / 2, null);
    }

}
