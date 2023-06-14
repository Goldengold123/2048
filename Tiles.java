/*
 * Author: Grace Pu
 * Date: May 30
 * Description: 
 * Tiles class defines behaviours for the tiles
 * Part of the BETA PROGRAM of the ICS4U final culminating project.
 */

import java.awt.*;
import java.awt.event.*;

public class Tiles {

    // Variable declaration

    // Colours of tiles array
    public static final Color[] colours = {
            new Color(205, 193, 180),
            new Color(238, 228, 218),
            new Color(237, 224, 200),
            new Color(242, 177, 121),
            new Color(245, 149, 99),
            new Color(246, 124, 95),
            new Color(246, 94, 59),
            new Color(237, 207, 114),
            new Color(237, 204, 97),
            new Color(237, 200, 80),
            new Color(237, 197, 63),
            new Color(237, 194, 46),
            new Color(60, 58, 50)
    };

    private int[][] board;
    private int score;
    private boolean win;
    private static int winScore = 11;

    public int TILE_SIZE;

    private int startX;
    private int startY;

    // Main constructor
    public Tiles(int size, int x, int y, int s) {
        board = new int[size][size];
        score = 0;
        win = false;
        startX = x;
        startY = y;
        TILE_SIZE = s;
    }

    // Constructor for copying Tiles, used to check if moves valid
    public Tiles(int[][] old) {
        board = new int[old.length][old[0].length];
        for (int i = 0; i < old.length; i++)
            for (int j = 0; j < old[0].length; j++)
                board[i][j] = old[i][j];
    }

    // Method for restarting tiles
    public void restart() {
        board = new int[board.length][board.length];
        score = 0;
        win = false;
    }

    // Method for filling random unfilled (value is 0) element of a 2D array with n.
    public void fillRandom(int n) {
        int sx, sy;

        // Generate random indices, while the element is not 0 (i.e. element is filled)
        do {
            sx = (int) (Math.random() * board[0].length);
            sy = (int) (Math.random() * board.length);
        } while (board[sx][sy] != 0);

        // Set the element to value n
        board[sx][sy] = n;
    }

    // Helper Method for moving zeros to end and return # non-zero elements.
    // This is used in sliding the tiles of an array.
    private static int moveZerosToBack(int[] arr) {
        // Variable declaration for counter for the number of non-zeros in arr.
        int nonZeros = 0;

        // Loop through all the elements in arr.
        // If element at j is not zero,
        // set the element at the next index to the element at j.
        for (int j = 0; j < arr.length; j++) {
            if (arr[j] != 0) {
                arr[nonZeros++] = arr[j];
            }
        }

        // Change the last elements (number is counted above) to 0 as necessary.
        for (int j = nonZeros; j < arr.length; j++)
            arr[j] = 0;

        // Return the number of non-zeros in arr.
        return nonZeros;
    }

    // Helper Method for sliding array tiles to the left (1D array).
    // This is used to slide the overall 2D array tiles.
    private static int slideArrLeft(int[] arr) {
        int s = 0;
        boolean win = false;
        // PART 1 - move zeros (empty tiles) to the end
        int cnt; // Variable declaration for counter for the number of non-zeros in arr.
        cnt = moveZerosToBack(arr); // move the zeros of arr to the back

        // PART 2 - combine adjacent tiles if they are identical
        for (int i = 0; i + 1 < cnt; i++) {
            if (arr[i] == arr[i + 1]) { // if adjacent tiles are the same, combine them
                arr[i] += 1; // set one tile to combined value
                arr[i + 1] = 0; // set other tile to 0 (empty tile)
                s += 1 << arr[i];
            }
            if (arr[i] == winScore) // if winScore obtained, set win to true
                win = true;
        }

        // PART 3 - if any new zeros (empty tiles) were created, move them to back again
        cnt = moveZerosToBack(arr);

        // PART 4 - if winScore obtained, return score as positive, otherwise negative
        if (win)
            return s;
        return -s;
    }

    // Helper Method for reflecting 1D array.
    private static void reverseRow(int[] arr) {
        for (int i = 0; 2 * i < arr.length; i++) {
            arr[i] += arr[arr.length - i - 1];
            arr[arr.length - i - 1] = arr[i] - arr[arr.length - i - 1];
            arr[i] -= arr[arr.length - i - 1];
        }
    }

    // Helper Method for reflecting all rows in a 2D array.
    // This is used to transform arrays such that a right slide becomes a left.
    private void reverse() {
        for (int i = 0; i < board.length; i++)
            reverseRow(board[i]);
    }

    // Helper Method for rotating a 2D array CCW.
    // This is used to transform arrays such that an up/down slide becomes a left.
    private void rotateCCW() {
        int[][] tmp = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                tmp[board.length - 1 - j][i] = board[i][j];
            }
        }
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[0].length; j++)
                board[i][j] = tmp[i][j];
    }

    // Helper Method for rotating a 2D array CW.
    // This is used to transform arrays such that an up/down slide becomes a left.
    private void rotateCW() {
        int[][] tmp = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                tmp[j][board.length - i - 1] = board[i][j];
            }
        }
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[0].length; j++)
                board[i][j] = tmp[i][j];
    }

    // Method for sliding 2D array to the left.
    public void left() {
        int t;
        for (int i = 0; i < board.length; i++) {
            t = slideArrLeft(board[i]);
            if (t > 0)
                win = true;
            score += Math.abs(t);
        }
    }

    // Method for sliding 2D array to the right.
    // Reflect, apply left, then reflect.
    public void right() {
        reverse();
        left();
        reverse();
    }

    // Method for sliding 2D array up.
    // Rotate CCW, apply left, then rotate CW.
    public void up() {
        rotateCCW();
        left();
        rotateCW();
    }

    // Method for sliding 2D array down.
    // Rotate CW, apply left, then rotate CCW.
    public void down() {
        rotateCW();
        left();
        rotateCCW();
    }

    // Method to check if 2 arrays are identical (same elements in the same order).
    public boolean sameArray(Tiles tilesCmp) {
        int[][] arrCmp = tilesCmp.board;

        if (board.length != arrCmp.length || board[0].length != arrCmp[0].length)
            return false;
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[0].length; j++)
                if (board[i][j] != arrCmp[i][j])
                    return false;
        return true;
    }

    // Method to check if the player is alive (possible moves) and return boolean.
    public boolean isAlive() {
        Tiles tmpBoard;

        // Copy the array, slide to left, check if it is the same array
        tmpBoard = new Tiles(board);
        tmpBoard.left();
        if (!sameArray(tmpBoard))
            return true;

        // Copy the array, slide to right, check if it is the same array
        tmpBoard = new Tiles(board);
        tmpBoard.right();
        if (!sameArray(tmpBoard))
            return true;

        // Copy the array, slide up, check if it is the same array
        tmpBoard = new Tiles(board);
        tmpBoard.up();
        if (!sameArray(tmpBoard))
            return true;

        // Copy the array, slide down, check if it is the same array
        tmpBoard = new Tiles(board);
        tmpBoard.down();
        if (!sameArray(tmpBoard))
            return true;

        // If no value returned yet, no possible moves -> return false
        return false;
    }

    // Method for getting board.
    public int[][] getBoard() {
        return board;
    }

    // Method for getting score.
    public int getScore() {
        return score;
    }

    // Method for checking if user won.
    public boolean won() {
        return win;
    }

    // called from GamePanel when any keyboard input is detected
    // slides the tiles in the direction if arrow keys pressed
    public void keyPressed(KeyEvent e) {
        print();
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            left();
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            right();
        if (e.getKeyCode() == KeyEvent.VK_UP)
            up();
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
            down();
        if (e.getKeyCode() == KeyEvent.VK_9)
            board[0][0] = 17;
        print();
    }

    // prints the board in the console for debugging purposes
    public void print() {
        for (int[] l : board) {
            for (int c : l) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // draws centered text
    public static void drawCenteredText(Graphics g, String s, int x, int y) {
        int w = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
        int h = (int) g.getFontMetrics().getStringBounds(s, g).getHeight();
        int newX = x - w / 2;
        int newY = y + h / 2;

        g.drawString(s, newX, newY);
    }

    // draws the tile to the screen
    public static void drawTile(Graphics g, int n, int x, int y, int SIZE) {
        g.setColor(colours[Math.min(12, n)]);
        g.fillRoundRect(x, y, SIZE, SIZE, SIZE / 3, SIZE / 3);
        g.setColor((n <= 2) ? new Color(119, 110, 101) : new Color(249, 246, 242));
        g.setFont(new Font("Impact", Font.PLAIN, 32));
        if (n != 0)
            drawCenteredText(g, (1 << n) + "", x + SIZE / 2, y + SIZE / 2);
    }

    // called frequently from the GamePanel class
    // draws tiles to the screen
    public void draw(Graphics g) {
        g.setColor(new Color(52, 44, 37));
        g.fillRoundRect(startX, startY, board[0].length * TILE_SIZE * 11 / 10 + TILE_SIZE / 10,
                board.length * TILE_SIZE * 11 / 10 + TILE_SIZE / 10, TILE_SIZE / 4, TILE_SIZE / 4);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                drawTile(g, board[i][j], startX + TILE_SIZE / 10 + j * TILE_SIZE * 11 / 10,
                        startY + TILE_SIZE / 10 + i * TILE_SIZE * 11 / 10, TILE_SIZE);
            }
        }
    }
}
