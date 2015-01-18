package net.marcv81.game;

import net.marcv81.gfx2d.Renderer;

import java.util.Random;

public class BonusFactory extends DriftingSpriteFactory {

    private static final float BONUS_MIN_SPEED = 0.1f;
    private static final float BONUS_MAX_SPEED = 0.25f;

    public BonusFactory(Renderer renderer, Random random) {
        super(renderer, random);
    }

    @Override
    protected float getMinSpeed() {
        return BONUS_MIN_SPEED;
    }

    @Override
    protected float getMaxSpeed() {
        return BONUS_MAX_SPEED;
    }

    public Bonus create() {
        return new Bonus(randomPosition(), randomSpeed());
    }
}
