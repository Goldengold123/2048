/*
 * Author: Grace Pu
 * Date: May 30
 * Description: This program simulates a working version of the puzzle game, 2048. 
 * It serves as the BETA PROGRAM of the ICS4U final culminating project.
 * The GamePanel class acts as the main "game loop".
 * It continuously runs the game and calls whatever needs to be called through the run() method.
 */

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    // Variable Declaration

    // dimensions of window
    private static final int GAME_WIDTH = 600;
    private static final int GAME_HEIGHT = 600;

    // graphics related variables
    private Thread gameThread;
    private Image image;
    private Graphics graphics;

    // tiles board
    private int BOARD_SIZE = 4;
    private Tiles tiles;

    // stopwatch / timer
    private Text timer;
    private Stopwatch stopwatch;

    // score + high score
    private Text score;
    private Text highscore;
    private int high = 0;

    // buttons on game screen
    private TextButton restart;
    private TextButton menu;

    // buttons on menu screen
    private TextButton play;
    private TextButton instructions;
    private TextButton quit;

    // music
    private Image musicOn;
    private Image musicOff;
    private ImageButton music;
    private Clip musicClip;
    private long musicClipTime = 0;

    // integer to store game state
    // 0 = menu, 1 = game, 2 = end
    private int state = 0;

    private boolean responded = false; // has user responded (should new board be printed?)

    private int ask = 0; // user has won, have they been asked whether they want to continue or quit?
    // ^ 0 = has not won, 1 = asking, 2 = asked

    public GamePanel() {
        // high score file and reader
        File hs;
        Scanner reader;

        // Tiles board
        tiles = new Tiles(BOARD_SIZE, 25, 160, 90);

        // Stopwatch / timer
        timer = new Text(500, 185, "TIMER", 2);
        stopwatch = new Stopwatch();

        // Score
        score = new Text(500, 245, "SCORE", 0);

        // Highscore -- try to read from file, if error then set to 0
        highscore = new Text(600, 240, "HIGHSCORE", 0);
        try {
            hs = new File("highscore.txt");
            reader = new Scanner(hs);
            high = Integer.parseInt(reader.nextLine());
        } catch (Exception e) {
            high = 0;
            updateHighScore();
        }
        highscore.value = high;

        // Restart
        restart = new TextButton(500, 480, "RESTART");

        // Menu
        menu = new TextButton(500, 540, "MENU");

        // Play
        play = new TextButton(GAME_WIDTH / 2, 260, "PLAY");

        // Instructions
        instructions = new TextButton(GAME_WIDTH / 2, 330, "INSTRUCTIONS");

        // Quit
        quit = new TextButton(GAME_WIDTH / 2, 400, "QUIT");

        // Music -- try catch for robustness
        musicOn = new ImageIcon("musicOn.png").getImage();
        musicOff = new ImageIcon("musicOff.png").getImage();

        music = new ImageButton(GAME_WIDTH / 2 - 5, 490, musicOn, musicOff);

        try {
            musicClip = AudioSystem.getClip();
            openSound(musicClip, "music.wav");
            playSound(musicClip);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Mouse Click
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                updateHighScore(); // change high score whenever mouse pressed to keep everything updated
                if (state == 0) { // menu screen -> check menu buttons
                    if (play.checkMouse(e.getX(), e.getY())) {
                        state = 1;
                    } else if (instructions.checkMouse(e.getX(), e.getY())) {
                        state = 2;
                    } else if (quit.checkMouse(e.getX(), e.getY())) {
                        state = -1;
                        System.exit(0);
                    } else if (music.checkMouse(e.getX(), e.getY())) {
                        music.toggle();
                        if (music.getValue())
                            playSound(musicClip);
                        else
                            stopSound(musicClip);
                    }
                } else if (state == 1) { // game screen -> check game buttons
                    if (restart.checkMouse(e.getX(), e.getY())) {
                        restart();
                        state = 1;
                    } else if (menu.checkMouse(e.getX(), e.getY())) {
                        restart();
                        state = 0;
                    }
                } else if (state == 2 || state == 3) { // instructions screen -> check instruction buttons
                    if (menu.checkMouse(e.getX(), e.getY())) {
                        restart();
                        state = 0;
                    }
                }

                // Repaint the panel after mouse click to keep updated
                repaint();
            }
        });

        this.setFocusable(true); // make everything in this class appear on the screen
        this.addKeyListener(this); // start listening for keyboard input
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

        // thread this class -> reduce lag
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Override paint method
    @Override
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
        // Graphics2D antialiasing for smoother text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        // calls upon helper methods (cleaner code)
        if (state == 0)
            drawTitle(g);
        else if (state == 1)
            drawGame(g);
        else if (state == 2)
            drawInstructions(g);
        else if (state == 3)
            drawEnd(g);
    }

    // draws centered text based on string and (x,y) center point
    private void drawCenteredText(Graphics g, String s, int x, int y) {
        int w = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        int h = (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
        int newX = x - w / 2;
        int newY = y - h / 2;
        g.drawString(s, newX, newY);
    }

    // method to draw title screen
    private void drawTitle(Graphics g) {
        g.setFont(new Font("Impact", Font.PLAIN, 120));
        drawCenteredText(g, "2048", GAME_WIDTH / 2, 240);
        highscore.x = 520;
        highscore.y = 120;
        highscore.draw(g);
        play.draw(g);
        instructions.draw(g);
        quit.draw(g);
        music.draw(g);
    }

    // method to draw instructions screen
    private void drawInstructions(Graphics g) {
        String[] lines = {
                "This is a sliding puzzle game where you use the",
                "arrow keys to slide the tiles in the desired direction.",
                "If two tiles with the same number are adjacent to each",
                "other and slide in that direction, they will combine to",
                "form a single tile with double the initial number.",
                "",
                "If you get the tile 2048, you win!",
                "You will also be given the option to exit or continue.",
                "",
                "Good luck!"
        };
        g.setFont(new Font("Impact", Font.PLAIN, 120));
        drawCenteredText(g, "2048", GAME_WIDTH / 2, 240);
        g.setFont(new Font("Impact", Font.PLAIN, 18));
        for (int i = 0; i < lines.length; i++) {
            drawCenteredText(g, lines[i], GAME_WIDTH / 2, 250 + i * 20);
        }
        menu.x = GAME_WIDTH / 2;
        menu.y = 500;
        menu.draw(g);
    }

    // method to draw game screen
    private void drawGame(Graphics g) {
        g.setFont(new Font("Impact", Font.PLAIN, 96));
        drawCenteredText(g, "2048", GAME_WIDTH / 2, 180);
        tiles.draw(g);
        timer.draw(g);
        score.draw(g);
        highscore.x = 500;
        highscore.y = 305;
        highscore.draw(g);
        restart.draw(g);
        menu.x = 500;
        menu.y = 540;
        menu.draw(g);

        if (ask == 1) { // if user has won and needs to be asked to quit/continue
            g.setColor(Color.black); // set color to white

            g.setFont(new Font("Impact", Font.PLAIN, 96));

            drawCenteredText(g, "You won!", GAME_WIDTH / 2, 400);
            g.setFont(new Font("Impact", Font.PLAIN, 18));
            drawCenteredText(g, "Press 'q' for the quit screen and 'p' to continue playing.", GAME_WIDTH / 2, 450);
        }
    }

    // method to draw end screen
    private void drawEnd(Graphics g) {
        g.setFont(new Font("Impact", Font.PLAIN, 120));
        drawCenteredText(g, "2048", GAME_WIDTH / 2, 240);
        g.setFont(new Font("Impact", Font.PLAIN, 32));

        // Basic end conditions
        if (tiles.won()) // user has won
            drawCenteredText(g, "Win", GAME_WIDTH / 2, 250);

        else // user has lost
            drawCenteredText(g, "Dead", GAME_WIDTH / 2, 250);

        drawCenteredText(g, "Score " + tiles.getScore(), GAME_WIDTH / 2, 300); // score

        menu.x = GAME_WIDTH / 2;
        menu.y = 500;
        menu.draw(g);
    }

    // method for opening sound based on path
    private void openSound(Clip sound, String path) {
        try { // try to load the sound
            sound.open(AudioSystem.getAudioInputStream(new File(path)));
        } catch (Exception e) { // exception
            e.printStackTrace();
        }
    }

    // method for playing sound
    private void playSound(Clip sound) {
        sound.setMicrosecondPosition(musicClipTime);
        sound.loop(Clip.LOOP_CONTINUOUSLY);
        sound.start(); // start the sound
    }

    // method for playing sound
    private void stopSound(Clip sound) {
        musicClipTime = sound.getMicrosecondPosition();
        sound.stop(); // start the sound
    }

    // method to update high score
    private void updateHighScore() {
        PrintWriter writer;
        high = Math.max(Math.max(high, highscore.value), tiles.getScore());
        try {
            writer = new PrintWriter("highscore.txt", "UTF-8");
            writer.println(high);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method to restart - game variables, ask variables, update high score
    private void restart() {
        // Reset game variables
        tiles.restart();
        stopwatch.restart();

        responded = false; // has user responded (should new board be printed?)
        ask = 0;

        updateHighScore();

        // Random initial tile
        tiles.fillRandom(1);
    }

    // run() method is what makes the game continue running without end. It calls
    // other methods to move objects, check for collision, and update the screen
    @Override
    public void run() {

        // the CPU runs our game code too quickly - we need to slow it down! The
        // following lines of code "force" the computer to get stuck in a loop for short
        // intervals between calling other methods to update the screen.
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long now = 0;

        while (true) {
            updateHighScore();
            restart();
            // Main game loop
            while (state == 1) {
                now = System.nanoTime();
                delta = delta + (now - lastTime) / ns;
                lastTime = now;

                responded = true;
                if (tiles.won() && ask == 0) { // if user has won and not asked if they want to quit/continue, ask
                    stopwatch.update();
                    stopwatch.override(stopwatch.getElapsed());
                    timer.value = stopwatch.getElapsed();
                    updateHighScore();
                    highscore.value = high;

                    ask = 1;
                    // collect input (must be q or p)
                    while (ask != 2) {
                    }
                }
                responded = false;
                tiles.fillRandom((Math.random() < 0.9) ? 1 : 2); // fill board with random tile
                if (!tiles.isAlive()) // check if user still alive (added tile does not kill them)
                    state = -1 * Math.abs(state);

                // User move
                while (!responded) { // compare tmpBoard and board to see if actually
                                     // moved
                    stopwatch.update();
                    timer.value = stopwatch.getElapsed();

                    score.value = tiles.getScore();
                    updateHighScore();
                    highscore.value = high;

                    repaint();
                }
                delta--;
            }
        }
    }

    // if a key is pressed, send to tiles class to process
    @Override
    public void keyPressed(KeyEvent e) {
        if (state == 1) {
            if (ask == 1) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    ask = 2;
                } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    ask = 2;
                    state = 3;
                }
            } else {
                Tiles tmpBoard; // temporary board to verify if user has moved or not (copy and compare array)
                tmpBoard = new Tiles(tiles.getBoard()); // copy array to compare if they actually moved
                tiles.keyPressed(e);
                responded = !tiles.sameArray(tmpBoard);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
