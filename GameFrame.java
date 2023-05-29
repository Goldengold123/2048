/*
 * Author: Grace Pu
 * Date: May 26
 * 
 * Description: 
 * GameFrame class -> game window
 * Child of JFrame to manage frames
 * Runs constructor in GamePanel class 
 * Bonus Features: 2D Images for Graphics
 */

import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {

    private GamePanel panel;

    public GameFrame() {
        panel = new GamePanel(); // run GamePanel constructor
        this.add(panel);
        this.setTitle("2048"); // set title for frame
        this.setResizable(false); // frame can't change size
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X button will stop program execution
        this.pack();// makes components fit in window - JFrame size adjust accordingly
        this.setVisible(true); // makes window visible to user
        this.setLocationRelativeTo(null);// set window in middle of screen
    }

}