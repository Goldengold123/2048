/*
 * Author: Grace Pu
 * Date: May 30
 * Description: This program simulates a minimal working text version of the puzzle game, 2048. 
 * It serves as the ALPHA PROGRAM of the ICS4U final culminating project.
 * The GamePanel class acts as the main "game loop".
 * It continuously runs the game and calls whatever needs to be called through the run() method.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    // Variable Declaration

    // dimensions of window
    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 600;

    private int BOARD_SIZE = 4;

    private Thread gameThread;
    private Image image;
    private Graphics graphics;

    private Tiles tiles;
    private Stopwatch stopwatch;
    private Text timer;
    private Text score;
    private Text highscore;

    // integer to store game state
    // 0 = menu
    // 1 = game
    // 2 = end
    private int state = 1;

    public GamePanel() {
        tiles = new Tiles(BOARD_SIZE, 25, 160, 90);
        timer = new Text(450, 160, 125, 50, 20, new Color(187, 173, 160), new Color(206, 208, 207),
                new Color(250, 248, 239), "TIMER", 2);
        stopwatch = new Stopwatch();
        score = new Text(450, 240, 125, 50, 20, new Color(187, 173, 160), new Color(206, 208, 207),
                new Color(250, 248, 239), "SCORE", 0);
        highscore = new Text(450, 320, 125, 50, 20, new Color(187, 173, 160), new Color(206, 208, 207),
                new Color(250, 248, 239), "HIGHSCORE", 0);

        this.setFocusable(true); // make everything in this class appear on the screen
        this.addKeyListener(this); // start listening for keyboard input
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

        // thread this class -> reduce lag
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void paint(Graphics g) {
        // we are using "double buffering here" - if we draw images directly onto the
        // screen, it takes time and the human eye can actually notice flashes of lag as
        // each pixel on the screen is drawn one at a time. Instead, we are going to
        // draw images OFF the screen, then simply move the image on screen as needed.
        image = createImage(GAME_WIDTH, GAME_HEIGHT); // draw off screen
        graphics = image.getGraphics();
        draw(graphics);// update the positions of everything on the screen
        g.drawImage(image, 0, 0, this); // move the image on the screen

    }

    // call the draw methods in each class to update positions as things move
    private void draw(Graphics g) {
        if (state == 1)
            drawGame(g);
        else
            drawEnd(g);
    }

    // draws centered text based on string and (x,y) center point
    private void drawCenteredText(Graphics g, String s, int x, int y) {
        int w = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        int h = (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
        int newX = (x - w) / 2;
        int newY = (y - h) / 2;
        g.drawString(s, newX, newY);
    }

    private void drawGame(Graphics g) {
        g.setFont(new Font("Impact", Font.PLAIN, 96));
        drawCenteredText(g, "2048", GAME_WIDTH, 360);
        tiles.draw(g);
        timer.draw(g);
        score.draw(g);
        highscore.draw(g);
    }

    public void drawEnd(Graphics g) {
        g.setFont(new Font("Impact", Font.PLAIN, 96));
        drawCenteredText(g, "End", GAME_WIDTH, 360);
        g.setFont(new Font("Impact", Font.PLAIN, 32));

        // Basic end conditions
        if (tiles.won()) // user has won
            drawCenteredText(g, "Win", GAME_WIDTH, 500);

        else // user has lost
            drawCenteredText(g, "Dead", GAME_WIDTH, 500);

        drawCenteredText(g, "Score " + tiles.getScore(), GAME_WIDTH, 600); // score
    }

    // run() method is what makes the game continue running without end. It calls
    // other methods to move objects, check for collision, and update the screen
    public void run() {
        // the CPU runs our game code too quickly - we need to slow it down! The
        // following lines of code "force" the computer to get stuck in a loop for short
        // intervals between calling other methods to update the screen.
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long now = 0;

        gameScreen(lastTime, amountOfTicks, ns, delta, now);

    }

    // Infinite game loop
    private void gameScreen(long lastTime, double amountOfTicks, double ns, double delta, long now) {
        boolean responded = false; // has user responded (should new board be printed?)

        boolean asked = false; // user has won, have they been asked whether they want to continue or quit?
        Tiles tmpBoard; // temporary board to verify if user has moved or not (copy and compare array)

        // Random initial tile
        tiles.fillRandom(2);

        // Main game loop
        while (state == 1) {
            now = System.nanoTime();
            delta = delta + (now - lastTime) / ns;
            lastTime = now;

            responded = true;
            score.value = tiles.getScore();

            if (tiles.won() && !asked) { // if user has gotten win tile and not asked if they want to quit/continue, ask
                asked = true;
                System.out.println("hmm.");
                responded = false;
                // collect input (must be q or p)
                while (!responded) {
                }
            }
            responded = false;
            tiles.fillRandom((Math.random() < 0.75) ? 2 : 4); // fill board with random tile
            if (!tiles.isAlive()) // check if user still alive (added tile does not kill them)
                state = -1 * Math.abs(state);
            if (state < 0) // if user dead, break out of game loop
                break;

            // User move
            tmpBoard = new Tiles(tiles.getBoard()); // copy array to compare if they actually moved
            while (!responded) {
                responded = !Tiles.sameArray(tiles, tmpBoard); // compare tmpBoard and board to see if actually moved

                stopwatch.update();
                timer.value = stopwatch.getElapsed();
                repaint();
            }
            delta--;
        }
    }

    // if a key is pressed, send to tiles class to process
    @Override
    public void keyPressed(KeyEvent e) {
        tiles.keyPressed(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
