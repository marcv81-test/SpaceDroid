package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Vector2f;

/**
 * Implementations of this interface drift according to Newton's laws of motion in 2D,
 * providing the bases of a physics engine for the game.
 */
public interface Driftable {

    /**
     * @return Position in game world coordinates.
     */
    Vector2f getPosition();

    /**
     * @return Speed in game units.
     */
    Vector2f getSpeed();

    /**
     * @param speed New speed in game units.
     */
    void setSpeed(Vector2f speed);
}
