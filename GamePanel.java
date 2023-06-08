/*
 * Author: Grace Pu
 * Date: May 30
 * Description: This program simulates a minimal working text version of the puzzle game, 2048. 
 * It serves as the ALPHA PROGRAM of the ICS4U final culminating project.
 * The GamePanel class acts as the main "game loop".
 * It continuously runs the game and calls whatever needs to be called through the run() method.
 */

import java.util.Scanner;

public class GamePanel {

    // Variable Declaration
    private int BOARD_SIZE = 4;

    // Constructor is empty for now since it is a minimal text-based version.
    public GamePanel() {
    }

    // Infinite game loop
    public void run() {

        // Variable Declaration
        Scanner in = new Scanner(System.in); // scanner for input
        boolean alive = true; // is user alive
        boolean moved = false; // has user moved (should new board be printed?)
        boolean asked = false; // user has won, have they been asked whether they want to continue or quit?
        Tiles board = new Tiles(BOARD_SIZE);
        Tiles tmpBoard; // temporary board to verify if user has moved or not (copy and compare array)
        char m; // user input character

        // Random initial tile
        board.fillRandom(2);

        // Main game loop
        while (alive) {
            if (board.won() && !asked) { // if user has gotten win tile and not asked if they want to quit/continue, ask
                asked = true;
                System.out.println("You reached " + board.getScore() + "! Enter q to quit or p to continue playing.");
                in.nextLine();
                // collect input (must be q or p)
                while (true) {
                    m = in.nextLine().charAt(0);
                    if (m == 'q' || m == 'Q') {
                        alive = false;
                        break;
                    }
                    if (m == 'p' || m == 'P') {
                        break;
                    }
                }
            }
            moved = false;
            board.fillRandom((Math.random() < 0.75) ? 2 : 4); // fill board with random tile
            board.draw(); // draw board
            if (!board.isAlive()) // check if user still alive (added tile does not kill them)
                alive = false;
            if (!alive) // if user dead, break out of game loop
                break;

            // User move
            tmpBoard = new Tiles(board.getBoard()); // copy array to compare if they actually moved
            while (!moved) {
                m = in.next().charAt(0);
                if (m == 'l' || m == 'L') // left
                    board.left();
                else if (m == 'r' || m == 'R') // right
                    board.right();
                else if (m == 'u' || m == 'U') // up
                    board.up();
                else if (m == 'd' || m == 'D') // down
                    board.down();
                else if (m == 'q' || m == 'Q') { // quit
                    alive = false;
                    break;
                }
                moved = !Tiles.sameArray(board, tmpBoard); // compare tmpBoard and board to see if user actually moved
            }
        }

        // Basic end conditions
        if (board.won()) // user has won
            System.out.println("WIN");
        else // user has lost
            System.out.println("DEAD");
        System.out.println("SCORE " + board.getScore()); // score

        // Close scanner to prevent memory leaks.
        in.close();
    }
}
