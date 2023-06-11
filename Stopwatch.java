
public class Stopwatch {
    private double start;
    private double curr;
    private double elapsed;

    public Stopwatch() {
        start = System.currentTimeMillis();
    }

    public void restart() {
        start = System.currentTimeMillis();
    }

    public void update() {
        curr = System.currentTimeMillis();
        elapsed = curr - start;
    }

    public double getElapsed() {
        return elapsed / 1000.0;
    }
}
