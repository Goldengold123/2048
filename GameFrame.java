/*
 * Author: Grace Pu
 * Date: May 30
 * Description: This program simulates a working version of the puzzle game, 2048. 
 * It serves as the FINAL PROGRAM of the ICS4U final culminating project.
 * GameFrame class establishes the frame (window) for the game.
 * It is a child of JFrame because JFrame manages frames.
 * Runs the constructor in GamePanel class.
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
        this.setBackground(Color.white);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X button will stop program execution
        this.pack();// makes components fit in window
        this.setVisible(true); // makes window visible to user
        this.setLocationRelativeTo(null);// set window in middle of screen
    }

}