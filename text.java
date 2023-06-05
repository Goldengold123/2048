/*
 * Author: Grace Pu
 * Date: May 30
 * Description: This program simulates a minimal working text version of the puzzle game, 2048. 
 * It serves as the ALPHA PROGRAM of the ICS4U final culminating project.
 */

// Import scanner class
import java.util.Scanner;

public class text {

    // Global variables declaration
    static int winScore = 2048; // integer score to win
    static boolean win = false; // boolean for if user has gotten winScore tile
    static int score = 0; // score of user

    // Method for filling random unfilled (value is 0) element of a 2D array with n.
    public static void fillRandom(int[][] arr, int n) {
        int sx, sy;

        // Generate random indices, while the element is not 0 (i.e. element is filled)
        do {
            sx = (int) (Math.random() * arr.length);
            sy = (int) (Math.random() * arr.length);
        } while (arr[sx][sy] != 0);

        // Set the element to value n
        arr[sx][sy] = n;
    }

    // Method for printing the board array.
    public static void drawBoard(int[][] arr) {
        for (int[] line : arr) {
            for (int cell : line)
                System.out.print(cell + " ");
            System.out.println();
        }
        System.out.println();
    }

    // Helper Method for moving zeros to end and return # non-zero elements.
    // This is used in sliding the tiles of an array.
    public static int moveZerosToBack(int[] arr) {
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
    public static void slideArrLeft(int[] arr) {
        // PART 1 - move zeros (empty tiles) to the end
        int cnt; // Variable declaration for counter for the number of non-zeros in arr.
        cnt = moveZerosToBack(arr); // move the zeros of arr to the back

        // PART 2 - combine adjacent tiles if they are identical
        for (int i = 0; i + 1 < cnt; i++) {
            if (arr[i] == arr[i + 1]) { // if adjacent tiles are the same, combine them
                score += arr[i];
                arr[i] *= 2; // set one tile to combined value
                arr[i + 1] = 0; // set other tile to 0 (empty tile)
            }
            if (arr[i] == winScore) // if winScore obtained, set win to true
                win = true;
        }

        // PART 3 - if any new zeros (empty tiles) were created, move them to back again
        cnt = moveZerosToBack(arr);
    }

    // Helper Method for reflecting 1D array.
    public static void reflectRow(int[] arr) {
        for (int i = 0; 2 * i < arr.length; i++) {
            arr[i] += arr[arr.length - i - 1];
            arr[arr.length - i - 1] = arr[i] - arr[arr.length - i - 1];
            arr[i] -= arr[arr.length - i - 1];
        }
    }

    // Helper Method for reflecting all rows in a 2D array.
    // This is used to transform arrays such that a right slide becomes a left.
    public static void reflect(int[][] arr) {
        for (int i = 0; i < arr.length; i++)
            reflectRow(arr[i]);
    }

    // Helper Method for rotating a 2D array CCW.
    // This is used to transform arrays such that an up/down slide becomes a left.
    public static void rotateCCW(int[][] arr) {
        int[][] tmp = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                tmp[arr.length - 1 - j][i] = arr[i][j];
            }
        }
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[0].length; j++)
                arr[i][j] = tmp[i][j];
    }

    // Helper Method for rotating a 2D array CW.
    // This is used to transform arrays such that an up/down slide becomes a left.
    public static void rotateCW(int[][] arr) {
        int[][] tmp = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                tmp[j][arr.length - i - 1] = arr[i][j];
            }
        }
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[0].length; j++)
                arr[i][j] = tmp[i][j];
    }

    // Method for sliding 2D array to the left.
    public static void left(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            slideArrLeft(arr[i]);
        }
    }

    // Method for sliding 2D array to the right.
    // Reflect, apply left, then reflect.
    public static void right(int[][] arr) {
        reflect(arr);
        left(arr);
        reflect(arr);
    }

    // Method for sliding 2D array up.
    // Rotate CCW, apply left, then rotate CW.
    public static void up(int[][] arr) {
        rotateCCW(arr);
        left(arr);
        rotateCW(arr);
    }

    // Method for sliding 2D array down.
    // Rotate CW, apply left, then rotate CCW.
    public static void down(int[][] arr) {
        rotateCW(arr);
        left(arr);
        rotateCCW(arr);
    }

    // Helper Method to copy an array and return new array.
    public static int[][] copyArray(int[][] arr) {
        int[][] tmpArr = new int[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[0].length; j++)
                tmpArr[i][j] = arr[i][j];
        return tmpArr;
    }

    // Method to check if 2 arrays are identical (same elements in the same order).
    public static boolean sameArray(int[][] arr1, int[][] arr2) {
        if (arr1.length != arr2.length || arr1[0].length != arr2[0].length)
            return false;
        for (int i = 0; i < arr1.length; i++)
            for (int j = 0; j < arr1[0].length; j++)
                if (arr1[i][j] != arr2[i][j])
                    return false;
        return true;
    }

    // Method to check if the player is alive (possible moves) and return boolean.
    public static boolean isAlive(int[][] arr) {
        int[][] tmpArr;

        // Copy the array, slide to left, check if it is the same array
        tmpArr = copyArray(arr);
        left(tmpArr);
        if (!sameArray(arr, tmpArr))
            return true;

        // Copy the array, slide to right, check if it is the same array
        tmpArr = copyArray(arr);
        right(tmpArr);
        if (!sameArray(arr, tmpArr))
            return true;

        // Copy the array, slide up, check if it is the same array
        tmpArr = copyArray(arr);
        up(tmpArr);
        if (!sameArray(arr, tmpArr))
            return true;

        // Copy the array, slide down, check if it is the same array
        tmpArr = copyArray(arr);
        down(tmpArr);
        if (!sameArray(arr, tmpArr))
            return true;

        // If no value returned yet, no possible moves -> return false
        return false;
    }

    public static void main(String[] args) {

        // Variable Declaration
        Scanner in = new Scanner(System.in); // scanner for input
        boolean alive = true; // is user alive
        boolean moved = false; // has user moved (should new board be printed?)
        boolean asked = false; // user has won, have they been asked whether they want to continue or quit?
        int BOARD_SIZE = 4; // board size
        int board[][] = new int[BOARD_SIZE][BOARD_SIZE]; // board
        int tmpBoard[][]; // temporary board to verify if user has moved or not (copy and compare array)
        char m; // user input character
        win = false; // has user won

        // Random initial tile
        fillRandom(board, 2);

        // Main game loop
        while (alive) {
            if (win && !asked) { // if user has gotten win tile and not asked if they want to quit/continue, ask
                asked = true;
                System.out.println("You reached " + winScore + "! Enter q to quit or p to continue playing.");
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
            fillRandom(board, ((Math.random() < 0.75) ? 2 : 4)); // fill board with random tile
            drawBoard(board); // draw board
            if (!isAlive(board)) // check if user still alive (added tile does not kill them)
                alive = false;
            if (!alive) // if user dead, break out of game loop
                break;

            // User move
            tmpBoard = copyArray(board); // copy array to compare if they actually moved
            while (!moved) {
                m = in.next().charAt(0);
                if (m == 'l' || m == 'L') // left
                    left(board);
                else if (m == 'r' || m == 'R') // right
                    right(board);
                else if (m == 'u' || m == 'U') // up
                    up(board);
                else if (m == 'd' || m == 'D') // down
                    down(board);
                else if (m == 'q' || m == 'Q') { // quit
                    alive = false;
                    break;
                }
                moved = !sameArray(board, tmpBoard); // compare tmpBoard and new board to see if user actually moved
            }
        }

        // Basic end conditions
        if (win) // user has won
            System.out.println("WIN");
        else // user has lost
            System.out.println("DEAD");
        System.out.println("SCORE " + score); // score

        // Close scanner to prevent memory leaks.
        in.close();
    }
}
