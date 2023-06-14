/*
 * Author: Grace Pu
 * Date: June 10
 * Description: This program simulates a working version of the puzzle game, 2048. 
 * It serves as the BETA PROGRAM of the ICS4U final culminating project.
 * Stopwatch class defines behaviours for the stopwatch (timer).
 */

public class Stopwatch {

    // Variable Declaration
    private double start;
    private double curr;
    private double elapsed;

    // constructor creates stopwatch at current time as start
    public Stopwatch() {
        start = System.currentTimeMillis();
    }

    // method to override elapsed time (for pauses during prompts)
    public void override(double t) {
        elapsed = t;
    }

    // method to restart stopwatch
    public void restart() {
        start = System.currentTimeMillis();
    }

    // method to update current and elapsed time
    public void update() {
        curr = System.currentTimeMillis();
        elapsed = curr - start;
    }

    // method that returns elapsed time in seconds
    public int getElapsed() {
        return (int) (elapsed / 1000);
    }
}
