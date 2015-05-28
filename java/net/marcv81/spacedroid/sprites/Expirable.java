package net.marcv81.spacedroid.sprites;

/**
 * Implementations of this interface can expire. The game engine may
 * collect the expired instances.
 */
public interface Expirable {

    /**
     * @return Whether this Expirable has expired aleady or not.
     */
    boolean isExpired();
}
