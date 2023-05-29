/*
 * Author: Grace Pu
 * Date: May 21
 * 
 * Description: 
 * GamePanel class runs the game and controls some of the events (e.g. game loop)
 * Child of JPanel because JPanel contains methods for drawing to the screen
 * Implements Runnable interface to use threading
 * Implements KeyListener interface to listen for keyboard input
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    // VARIABLE DECLARATION

    // dimensions of window
    private static final int GAME_WIDTH = 700;
    private static final int GAME_HEIGHT = 600;

    // variables for drawing screen
    private Thread gameThread;
    private Image image;
    private Graphics graphics;

    // constructor - initialize game objects
    public GamePanel() {

        this.setFocusable(true); // make everything in this class appear on the screen
        this.addKeyListener(this); // start listening for keyboard input
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT)); // preferred window size

        // thread this class
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Override paint method in java.awt library
    @Override
    public void paint(Graphics g) {
        // use double buffering
        image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
        graphics = image.getGraphics();
        draw(graphics);// update the positions of everything on the screen
        g.drawImage(image, 0, 0, this); // move the image on the screen

    }

    // call the draw methods in each class to update positions as things move
    private void draw(Graphics g) {
        // antialiasing text so it appears smoother
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    }

    // run method keeps game running
    // call methods to move objects, check collision, update screen
    public void run() {
        // tick speed
        long lastTime = System.nanoTime();
        double amountOfTicks = 60; // tick speed is 60
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long now;

        while (true) { // infinite game loop
            now = System.nanoTime();
            delta = delta + (now - lastTime) / ns;
            lastTime = now;

            // only move objects around and update screen if enough time has passed
            if (delta >= 1) {
                repaint();
                delta--;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

}