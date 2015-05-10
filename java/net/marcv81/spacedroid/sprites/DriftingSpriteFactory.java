package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.GameView;
import net.marcv81.gfx2d.Vector2f;

import java.util.Random;

public abstract class DriftingSpriteFactory<T extends DriftingSprite> {

    private static final float TAU = 6.2831853071f;
    private static final float SPRITE_SPAWNING_DISTANCE = 0.5f;

    private final GameView view;
    protected final Random random;

    protected abstract float getMinSpeed();
    protected abstract float getMaxSpeed();
    protected abstract T create();

    public DriftingSpriteFactory(GameView view, Random random) {
        this.view = view;
        this.random = random;
    }

    private Vector2f randomLeftEdge() {
        return new Vector2f(
                view.getLeftEdge() - SPRITE_SPAWNING_DISTANCE,
                view.getBottomEdge() + random.nextFloat() * (view.getTopEdge() - view.getBottomEdge()));
    }

    private Vector2f randomRightEdge() {
        return new Vector2f(
                view.getRightEdge() + SPRITE_SPAWNING_DISTANCE,
                view.getBottomEdge() + random.nextFloat() * (view.getTopEdge() - view.getBottomEdge()));
    }

    private Vector2f randomTopEdge() {
        return new Vector2f(
                view.getRightEdge() + random.nextFloat() * (view.getLeftEdge() - view.getRightEdge()),
                view.getTopEdge() + SPRITE_SPAWNING_DISTANCE);
    }

    private Vector2f randomBottomEdge() {
        return new Vector2f(
                view.getRightEdge() + random.nextFloat() * (view.getLeftEdge() - view.getRightEdge()),
                view.getBottomEdge() - SPRITE_SPAWNING_DISTANCE);
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
