package net.marcv81.spacedroid.sprites;

/**
 * Implementations of this interface may be updated during the game loop.
 */
public interface Updatable {

    /**
     * Update this Updatable according to its internal state and the time slice duration.
     *
     * @param timeSlice Time slice duration in milliseconds.
     */
    void update(long timeSlice);
}
