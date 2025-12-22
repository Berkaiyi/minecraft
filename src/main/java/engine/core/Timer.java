package engine.core;

public class Timer {
    private double lastTime;

    public Timer() {
        lastTime = getTime();
    }

    public double getDelta() {
        double time = getTime();
        double delta = time - lastTime;
        lastTime = time;
        return delta;
    }

    public double getTime() {
        return System.nanoTime() / 1_000_000_000.0;
    }
}
