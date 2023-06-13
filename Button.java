import java.awt.*;

public class Button extends Rectangle {

    // constructor creates ball at given location with given dimensions
    public Button(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public boolean checkMouse(int a, int b) {
        return (x - width / 2 <= a && a <= x + width / 2 && y - width / 2 <= b && b <= y + height / 2);
    }
}
