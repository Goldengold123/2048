/*
 * Author: Grace Pu
 * Date: May 30
 * Description: This program simulates a minimal working text version of the puzzle game, 2048. 
 * It serves as the ALPHA PROGRAM of the ICS4U final culminating project.
 * In the final product, the GameFrame class should establish the frame (window) for the game.
 */

public class GameFrame {

    private GamePanel panel;

    public GameFrame() {
        panel = new GamePanel(); // run GamePanel constructor
        panel.run();
    }

}