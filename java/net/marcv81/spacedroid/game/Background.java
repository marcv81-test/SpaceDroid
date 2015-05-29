package net.marcv81.spacedroid.game;

import net.marcv81.spacedroid.common.Vector2f;
import net.marcv81.spacedroid.graphics.Sprite;

public final class Background implements Sprite {

    private Vector2f position;

    public Background(Vector2f position) {
        this.position = new Vector2f(position);
    }

    //
    // Sprite implementation
    //

    public Vector2f getPosition() {
        return new Vector2f(position);
    }

    public int getAnimationIndex() {
        return 0;
    }

    public float getTransparency() {
        return 1f;
    }

    public float getScale() {
        return 1f;
    }

    public float getAngle() {
        return 0f;
    }
}
