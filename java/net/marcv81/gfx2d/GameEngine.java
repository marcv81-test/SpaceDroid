package net.marcv81.gfx2d;

import java.util.List;

public abstract class GameEngine {

    private static final long MIN_TIME_SLICE = 20; // 50 FPS
    private static final long MAX_TIME_SLICE = 50; // 20 FPS

    private long previousTime = 0;

    protected GameView view;

    public void setView(GameView view, List<SpriteRenderer> spriteGroups) {
        this.view = view;
        this.view.setRenderer(this, spriteGroups);
    }

    // Update the engine
    protected abstract void update(long timeSlice);

    protected void update() {

        long currentTime = System.currentTimeMillis();
        long timeSlice = currentTime - previousTime;

        // Wait until the time slice is long enough
        while (timeSlice < MIN_TIME_SLICE) {
            try {
                Thread.sleep(MIN_TIME_SLICE - timeSlice);
            } catch (InterruptedException e) {
                // Don't care
            }
            currentTime = System.currentTimeMillis();
            timeSlice = currentTime - previousTime;
        }

        // Slow the game down if the time slice is too long
        if (timeSlice > MAX_TIME_SLICE) {
            timeSlice = MAX_TIME_SLICE;
        }

        // Update the engine
        update(timeSlice);

        // Get ready to calculate the next time slice
        previousTime = currentTime;
    }
}
