package engine.core;

public class Timer {
    private double lastTime;
    private double timer;

    public Timer() {
        lastTime = getTime();
    }

    public double getDelta() {
        double time = getTime();
        double delta = time - lastTime;
        lastTime = time;
        return delta;
    }

    public boolean hasSecondPassed(double delta) {
        timer += delta;
        if (timer >= 1.0) {
            timer -= 1.0;
            return true;
        }
        return false;
    }

    public double getTime() {
        return System.nanoTime() / 1_000_000_000.0;
    }
}
