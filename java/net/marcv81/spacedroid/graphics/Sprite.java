package net.marcv81.spacedroid.graphics;

import net.marcv81.spacedroid.common.Vector2f;

/**
 * This interface handles sprites which can be rendered.
 */
public interface Sprite {

    /**
     * @return Position in game world coordinates.
     */
    Vector2f getPosition();

    /**
     * @return Rendering animation index.
     */
    int getAnimationIndex();

    /**
     * @return Rendering angle.
     */
    float getAngle();

    /**
     * @return Rendering transparency between 0f (transparent) and 1f (opaque).
     */
    float getTransparency();

    /**
     * @return Rendering scaling ratio.
     */
    float getScale();
}
