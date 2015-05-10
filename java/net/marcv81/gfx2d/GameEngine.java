package net.marcv81.gfx2d;

/**
 * This class handles the game loop. It manages framerate variations and provides
 * the children classes with a reference to the associated GameView.
 */
public abstract class GameEngine {

    /**
     * Minimum time slice duration in milliseconds (50 FPS).
     */
    private static final long MIN_TIME_SLICE = 20;

    /**
     * Maximum time slice duration in milliseconds (20 FPS).
     */
    private static final long MAX_TIME_SLICE = 50;

    /**
     * Time of the previous call to update() in milliseconds since 1970-01-01.
     */
    private long previousTime = 0;

    /**
     * Reference to the associated GameView.
     */
    protected GameView gameView;

    /**
     * Associates a GameView to this GameEngine in order to provide an interface with the player.
     *
     * @param gameView The GameView to associate to this GameEngine.
     */
    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    /**
     * Updates the GameEngine. To be implemented by the children classes.
     *
     * @param timeSlice The time in milliseconds since the last call to this function.
     */
    protected abstract void update(long timeSlice);

    /**
     * Updates the GameEngine and manages framerate variations.
     */
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
