package com.egyed.adam.endlesshiker;

/**
 * Created by Adam on 5/10/16.
 * Utility class used solely by GameEngine to get timings for updates and renders
 */
public class Timer {

    private double lastLoopTime;

    public Timer() {
        //init();
    }

    public void init() {
        lastLoopTime = getTime();
    }

    public double getTime() {
        return System.nanoTime() / 1000000000.0;
    }

    public float getEllapsedTime() {
        double time = getTime();
        float ellapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return ellapsedTime;
    }

    public double getLastLoopTime() {
        return lastLoopTime;
    }
}
