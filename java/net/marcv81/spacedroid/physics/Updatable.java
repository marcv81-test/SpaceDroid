package net.marcv81.spacedroid.physics;

/**
 * Implementations of this interface may be updated within the game loop.
 */
public interface Updatable {

    /**
     * Updates this Updatable according to its internal state and the time
     * slice duration.
     *
     * @param timeSlice Time slice duration in milliseconds.
     */
    void update(long timeSlice);
}
