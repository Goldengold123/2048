/*
 * Author: Grace Pu
 * Date: May 30
 * Description: This program simulates a working version of the puzzle game, 2048. 
 * It serves as the FINAL PROGRAM of the ICS4U final culminating project.
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

    // background colour of pop-up messages
    private static final Color messageColor = new Color(238, 198, 202, 210);

    // size of board
    private static final int BOARD_SIZE = 4;

    // mouse point, x coord, y coord
    private Point mouse;
    private int mX;
    private int mY;

    // graphics related variables
    private Thread gameThread;
    private Image image;
    private Graphics graphics;

    // tiles board
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
    private Image musicOnMouse;
    private Image musicOff;
    private Image musicOffMouse;
    private ImageButton music;
    private Clip musicClip;
    private long musicClipTime = 0;

    // integer to store game state
    // 0 = menu, 1 = game, 2 = instructions, 3 = end
    private int state = 0;

    private boolean responded = false; // has user responded (should new board be printed?)

    // 0 = has not won, 1 = asking, 2 = asked
    private int ask = 0; // user has won, have they been asked whether they want to continue or quit?

    private boolean toggleControlHint = false; // should contorl hints pop-up be displayed

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
        musicOn = new ImageIcon("media/musicOn.png").getImage();
        musicOnMouse = new ImageIcon("media/musicOnMouse.png").getImage();
        musicOff = new ImageIcon("media/musicOff.png").getImage();
        musicOffMouse = new ImageIcon("media/musicOffMouse.png").getImage();

        music = new ImageButton(GAME_WIDTH / 2 - 5, 490, musicOn, musicOnMouse, musicOff, musicOffMouse);

        try {
            musicClip = AudioSystem.getClip();
            openSound(musicClip, "media/music.wav");
            playSound(musicClip);
        } catch (Exception e) {
            System.out.println("Music-related Error! (Check if the file is in the media folder)");
        }

        // Mouse Click
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (ask != 1) { // prevent mouse actions when user being asked to continue or quit
                    updateHighScore(); // change high score whenever mouse pressed to keep everything updated
                    if (state == 0) { // menu screen -> check menu buttons
                        if (play.checkMouse(e.getX(), e.getY())) { // play button
                            state = 1;
                        } else if (instructions.checkMouse(e.getX(), e.getY())) { // instructions button
                            state = 2;
                        } else if (quit.checkMouse(e.getX(), e.getY())) { // quit button
                            state = -1;
                            System.exit(0);
                        } else if (music.checkMouse(e.getX(), e.getY())) { // toggle music
                            music.toggle();
                            if (music.getValue())
                                playSound(musicClip);
                            else
                                stopSound(musicClip);
                        }
                    } else if (state == 1) { // game screen -> check game buttons
                        if (restart.checkMouse(e.getX(), e.getY())) { // restart button
                            restart();
                            state = 1;
                        } else if (menu.checkMouse(e.getX(), e.getY())) { // menu button
                            restart();
                            state = 0;
                        }
                    } else if (state == 2 || state == 3) { // instructions screen -> check instruction button
                        if (menu.checkMouse(e.getX(), e.getY())) { // menu button
                            restart();
                            state = 0;
                        }
                    }

                    // Repaint the panel after mouse click to keep updated
                    repaint();
                }
            }
        });

        this.setFocusable(true); // make everything in this class appear on the screen
        this.addKeyListener(this); // start listening for keyboard input
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));

        // thread this class -> reduce lag
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Paint method
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
        // Graphics2D antialiasing for smoother and clearer text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        // get mouse x and y coords
        mX = (int) mouse.getX();
        mY = (int) mouse.getY();

        // calls upon helper methods (cleaner code)
        if (state == 0) // menu screen
            drawTitle(g, mX, mY);
        else if (state == 1) // game screen
            drawGame(g, mX, mY);
        else if (state == 2) // instructions screen
            drawInstructions(g, mX, mY);
        else if (state == 3) // end screen
            drawEnd(g, mX, mY);
    }

    // draws centered text based on string and (x,y) center point
    private void drawCenteredText(Graphics g, String s, int x, int y) {
        int w = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        int h = (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
        int newX = x - w / 2;
        int newY = y - h / 2;

        g.drawString(s, newX, newY);
    }

    // draws pop-up message rectangle based on colour, (x,y) center, width, height
    private void drawMessageRectangle(Graphics g, Color c, int x, int y, int w, int h) {
        g.setColor(c);
        g.fillRoundRect(x - 50 - w / 2, y - 50 - h / 2, w + 100, h + 100, 20, 20);
    }

    // method to draw title screen (title, buttons)
    private void drawTitle(Graphics g, int mX, int mY) {
        g.setFont(new Font("Impact", Font.PLAIN, 120));
        drawCenteredText(g, "2048", GAME_WIDTH / 2, 240);

        highscore.x = 520;
        highscore.y = 120;
        highscore.draw(g);

        play.draw(g, mX, mY);
        instructions.draw(g, mX, mY);
        quit.draw(g, mX, mY);
        music.draw(g, mX, mY);
    }

    // method to draw instructions screen (title, instructions, button)
    private void drawInstructions(Graphics g, int mX, int mY) {
        // array to store instructions text lines
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
        menu.draw(g, mX, mY);
    }

    // method to draw game screen (title, game board, buttons, possible pop-ups)
    private void drawGame(Graphics g, int mX, int mY) {
        g.setFont(new Font("Impact", Font.PLAIN, 96));
        drawCenteredText(g, "2048", GAME_WIDTH / 2, 180);

        tiles.draw(g);
        timer.draw(g);
        score.draw(g);

        highscore.x = 500;
        highscore.y = 305;
        highscore.draw(g);

        restart.draw(g, mX, mY);

        menu.x = 500;
        menu.y = 540;
        menu.draw(g, mX, mY);

        if (ask == 1) { // if user has won and needs to be asked to quit/continue
            drawMessageRectangle(g, messageColor, GAME_WIDTH / 2, 350, 350, 120);

            g.setColor(Color.black); // set color to black
            g.setFont(new Font("Impact", Font.PLAIN, 96));
            drawCenteredText(g, "You won!", GAME_WIDTH / 2, 400);

            g.setFont(new Font("Impact", Font.PLAIN, 18));
            drawCenteredText(g, "Press Q for the quit screen and P to continue playing.", GAME_WIDTH / 2, 450);
        }

        if (toggleControlHint) { // pop-up control hints display
            drawMessageRectangle(g, messageColor, GAME_WIDTH / 2, 360, 420, 150);

            g.setColor(Color.black); // set color to black
            g.setFont(new Font("Impact", Font.PLAIN, 96));
            drawCenteredText(g, "Control Hints", GAME_WIDTH / 2, 400);

            g.setFont(new Font("Impact", Font.PLAIN, 18));
            drawCenteredText(g, "Arrow keys or WASD to slide tiles", GAME_WIDTH / 2, 450);

            g.setColor(Color.gray);
            drawCenteredText(g, "CHEAT - 9 to generate 1024 in top left corner", GAME_WIDTH / 2, 480);
        }

    }

    // method to draw end screen (title, result, score, highest tile, time, button)
    private void drawEnd(Graphics g, int mX, int mY) {
        g.setFont(new Font("Impact", Font.PLAIN, 120));
        drawCenteredText(g, "2048", GAME_WIDTH / 2, 240);

        g.setFont(new Font("Impact", Font.PLAIN, 32));
        // End conditions
        if (tiles.won()) { // user has won
            drawCenteredText(g, "Congratulations!", GAME_WIDTH / 2, 250);
            drawCenteredText(g, "You reached the tile 2048!", GAME_WIDTH / 2, 300);
        } else { // user has lost
            drawCenteredText(g, "Tragic.", GAME_WIDTH / 2, 250);
            drawCenteredText(g, "You did not reach the tile 2048...", GAME_WIDTH / 2, 300);
        }

        drawCenteredText(g, "Final Score: " + tiles.getScore(), GAME_WIDTH / 2, 350); // score
        drawCenteredText(g, "Highest Tile Attained: " + tiles.getMaxTile(), GAME_WIDTH / 2, 400); // score
        drawCenteredText(g, "Time Taken: " + timer.value + " seconds", GAME_WIDTH / 2, 450);

        menu.x = GAME_WIDTH / 2;
        menu.y = 500;
        menu.draw(g, mX, mY);
    }

    // method for opening sound based on path, try catch for robustness
    private void openSound(Clip sound, String path) {
        try { // try to load the sound
            sound.open(AudioSystem.getAudioInputStream(new File(path)));
        } catch (Exception e) { // exception
            System.out.println("Sound-related Error!");
        }
    }

    // method for playing sound
    private void playSound(Clip sound) {
        sound.setMicrosecondPosition(musicClipTime); // set time position
        sound.loop(Clip.LOOP_CONTINUOUSLY); // loop
        sound.start(); // start the sound
    }

    // method for playing sound
    private void stopSound(Clip sound) {
        musicClipTime = sound.getMicrosecondPosition(); // save time position
        sound.stop(); // stop the sound
    }

    // method to update high score, try catch for robustness
    private void updateHighScore() {
        PrintWriter writer;

        high = Math.max(Math.max(high, highscore.value), tiles.getScore());

        try {
            writer = new PrintWriter("highscore.txt", "UTF-8");
            writer.println(high);
            writer.close();
        } catch (Exception e) {
            System.out.println("High Score File-related Error!");
        }
    }

    // method to restart - game variables, ask variables, update high score
    private void restart() {
        // Reset game variables
        tiles.restart(); // calls upon method in tiles class
        stopwatch.restart(); // calls upon method in stopwatch class

        responded = false; // user has not responded
        ask = 0; // user has not been asked if they want to continue/quit

        updateHighScore(); // method to updateHighScore() -- reset to 0

        // Random initial tile
        tiles.fillRandom(1); // fill in a random tile
    }

    // method to subtract 2 points, used to get mouse position
    private Point subtract(Point a, Point b) {
        return new Point((int) (a.getX() - b.getX()), (int) (a.getY() - b.getY()));
    }

    // method to get mouse position, if error then set to (0,0) for robustness
    // subtract absolute mouse position and game window position
    @Override
    public Point getMousePosition() {
        Point p;
        try {
            p = subtract(MouseInfo.getPointerInfo().getLocation(), getLocationOnScreen());
        } catch (Exception e) {
            p = new Point(0, 0);
        }
        return p;
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

        restart();

        // Main game loop
        while (true) {
            now = System.nanoTime();
            delta = delta + (now - lastTime) / ns;
            lastTime = now;

            updateHighScore(); // update high score (display and variables)

            // Menu Screen -> restart variables
            if (state == 0) {
                restart();
            }

            // Game Screen
            if (state == 1) {
                // if user won and has not been asked if they want to quit/continue, ask
                if (tiles.won() && ask == 0) {
                    // stopwatch/timer values saved and overriden
                    stopwatch.update();
                    stopwatch.override();
                    timer.value = stopwatch.getElapsed();

                    // high score value saved
                    updateHighScore();
                    highscore.value = high;

                    // set ask variable to 1/asking
                    ask = 1;

                    // collect input (must be q or p), while loop until ask variable is 2/asked
                    while (ask != 2) {
                    }
                }

                responded = false; // user has not responded with arrow keys -> dont update

                tiles.fillRandom((Math.random() < 0.9) ? 1 : 2); // fill board with random tile
                                                                 // 90% value of 2, 10% value of 4

                if (!tiles.isAlive()) {// check if user still alive (added tile), if not update accordingly
                    state = 3;
                    continue;
                }

                // User move
                while (!responded) { // compare tmpBoard and board to see if actually moved
                    // update stopwatch values
                    stopwatch.update();
                    timer.value = stopwatch.getElapsed();

                    // update score and high scorevalues
                    score.value = tiles.getScore();
                    updateHighScore();
                    highscore.value = high;

                    // Mouse position for mouseover effects for buttons
                    mouse = getMousePosition();
                    repaint();
                }

                if (!tiles.isAlive()) {// check if user still alive (after moving)
                    state = 3;
                    continue;
                }
            }

            // End Screen -> override stopwatch (stop)
            if (state == 3) {
                stopwatch.override();
            }

            // Mouse position for mouseover effects
            mouse = getMousePosition();
            repaint();

            delta--;
        }
    }

    // Override method to manage key presses
    @Override
    public void keyPressed(KeyEvent e) {
        // Only applies when user at game screen
        if (state == 1) {
            if (ask == 1) { // if asking, check for P and Q
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    ask = 2;
                    stopwatch.override();
                } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    ask = 2;
                    state = 3;
                }
            } else { // if not checking, send to tiles class to process, verify if user really moved
                Tiles tmpBoard; // temporary board to verify if user has moved or not (copy and compare array)
                tmpBoard = new Tiles(tiles.getBoard()); // copy array to compare if they actually moved
                tiles.keyPressed(e); // send to tiles class to process
                responded = ((!tiles.sameArray(tmpBoard))); // set responded accordingly
            }
        }

        // If H pressed, display control hint pop-up regardless of in game screen or not
        if (e.getKeyCode() == KeyEvent.VK_H)
            toggleControlHint = true;

        // Repaint
        repaint();
    }

    // Override method
    @Override
    public void keyTyped(KeyEvent e) {

    }

    // Override method for when key released
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_H) // H released -> hide control hint pop-up
            toggleControlHint = false;
    }

}
