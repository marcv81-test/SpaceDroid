package net.marcv81.spacedroid.sprites;

/**
 * Implementations of this interface can expire, so that the game engine may
 * collect the expired instance.
 */
public interface Expirable {

    boolean isExpired();
}
