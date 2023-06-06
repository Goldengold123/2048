import java.awt.Color;

public enum GameState {
    MENU("Main Menu", new Color(120, 0, 0)),
    PLAY("Playing!", new Color(0, 120, 0)),
    GAMEOVER("Game Over!", new Color(255, 0, 0)),
    WIN("Winner!", new Color(0, 255, 0));

    public final String state;

    public final Color colour;

    GameState(String s, Color c) {
        this.state = s;
        this.colour = c;
    }
}
