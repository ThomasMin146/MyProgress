package com.thomas.myprogress;

import android.os.SystemClock;

public class Stopwatch {
    private long startTime;
    private long elapsedTime;
    private boolean isRunning;

    public void start() {
        if (!isRunning) {
            startTime = SystemClock.elapsedRealtime();
            isRunning = true;
        }
    }

    public void pause() {
        if (isRunning) {
            elapsedTime += SystemClock.elapsedRealtime() - startTime;
            isRunning = false;
        }
    }

    public void resume() {
        if (!isRunning) {
            startTime = SystemClock.elapsedRealtime();
            isRunning = true;
        }
    }

    public void reset() {
        elapsedTime = 0;
        isRunning = false;
    }

    public long getElapsedTime() {
        if (isRunning) {
            return elapsedTime + (SystemClock.elapsedRealtime() - startTime);
        } else {
            return elapsedTime;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}
