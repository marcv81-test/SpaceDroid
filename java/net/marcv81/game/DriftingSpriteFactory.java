package net.marcv81.game;

import net.marcv81.gfx2d.Renderer;
import net.marcv81.gfx2d.Vector2f;

import java.util.Random;

public abstract class DriftingSpriteFactory<T extends DriftingSprite> {

    private static final float TAU = 6.2831853071f;
    private static final float SPRITE_SPAWNING_DISTANCE = 0.5f;

    private Renderer renderer;
    protected Random random;

    protected abstract float getMinSpeed();
    protected abstract float getMaxSpeed();
    protected abstract T create();

    public DriftingSpriteFactory(Renderer renderer, Random random) {
        this.renderer = renderer;
        this.random = random;
    }

    private Vector2f randomLeftEdge() {
        return new Vector2f(
                renderer.getLeft() - SPRITE_SPAWNING_DISTANCE,
                renderer.getBottom() + random.nextFloat() * (renderer.getTop() - renderer.getBottom()));
    }

    private Vector2f randomRightEdge() {
        return new Vector2f(
                renderer.getRight() + SPRITE_SPAWNING_DISTANCE,
                renderer.getBottom() + random.nextFloat() * (renderer.getTop() - renderer.getBottom()));
    }

    private Vector2f randomTopEdge() {
        return new Vector2f(
                renderer.getRight() + random.nextFloat() * (renderer.getLeft() - renderer.getRight()),
                renderer.getTop() + SPRITE_SPAWNING_DISTANCE);
    }

    private Vector2f randomBottomEdge() {
        return new Vector2f(
                renderer.getRight() + random.nextFloat() * (renderer.getLeft() - renderer.getRight()),
                renderer.getBottom() - SPRITE_SPAWNING_DISTANCE);
    }

    protected Vector2f randomPosition() {
        switch (random.nextInt(4)) {
            case 0: return randomLeftEdge();
            case 1: return randomRightEdge();
            case 2: return randomTopEdge();
            default: return randomBottomEdge();
        }
    }

    protected Vector2f randomSpeed() {
        float driftAngle = TAU * random.nextFloat();
        float driftSpeed = getMinSpeed() + random.nextFloat() * (getMaxSpeed() - getMinSpeed());
        return (new Vector2f(driftAngle)).multiply(driftSpeed);
    }
}
