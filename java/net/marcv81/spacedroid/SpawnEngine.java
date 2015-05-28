package net.marcv81.spacedroid;

import net.marcv81.gfx2d.GameView;
import net.marcv81.gfx2d.Sprite;
import net.marcv81.gfx2d.Vector2f;
import net.marcv81.spacedroid.sprites.*;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * This class handles the part of the engine responsible for creating and destroying
 * Asteroids and Bonuses. They are created a margin away from the edges of the GameView.
 * They are destroyed when they drift a larger margin away.
 */
public class SpawnEngine {

    private static final float TAU = 6.2831853071f;

    private static final int ASTEROID_MAX_COUNT = 20;
    private static final float ASTEROID_MIN_SPEED = 0.1f;
    private static final float ASTEROID_MAX_SPEED = 0.5f;

    private static final int BONUS_MAX_COUNT = 3;
    private static final float BONUS_MIN_SPEED = 0.1f;
    private static final float BONUS_MAX_SPEED = 0.25f;

    /**
     * Margin between the edges of the GameView and where the DriftingSprites are created.
     */
    private static final float SPRITE_CREATE_DISTANCE = 0.5f;

    /**
     * Margin between the edges of the GameView and where the DriftingSprites are destroyed.
     */
    private static final float SPRITE_DESTROY_DISTANCE = 0.75f;

    /**
     * Number of attempts to create a DriftingSprites until we give up. Failures happen
     * when the random position is already occupied by another Drifter.
     */
    private static final int CREATE_MAX_RETRIES = 3;

    private final SpacedroidEngine engine;
    private final GameView gameView;
    private final Random random;

    /**
     * Interface implemented by the Drifter factories.
     */
    private interface DriftingSpriteFactory<T> {
        T create(Vector2f position, Vector2f speed);
    }

    /**
     * Constructor.
     */
    public SpawnEngine(SpacedroidEngine engine, GameView gameView, Random random) {
        this.engine = engine;
        this.gameView = gameView;
        this.random = random;
    }

    /**
     * Adds Asteroids to the game engine when required.
     */
    public void createAsteroids() {

        // Declare Asteroid factory
        class AsteroidFactory implements DriftingSpriteFactory<Asteroid> {
            public Asteroid create(Vector2f position, Vector2f speed) {
                return new Asteroid(position, speed, random);
            }
        }

        // Call generic sprite creation function
        createDriftingSprites(engine.asteroids, new AsteroidFactory(),
                ASTEROID_MIN_SPEED, ASTEROID_MAX_SPEED, ASTEROID_MAX_COUNT);
    }

    /**
     * Destroys the Asteroids which are out of scope.
     */
    public void destroyAsteroids() {
        destroyDriftingSprites(engine.asteroids);
    }

    /**
     * Adds Bonuses to the game engine when required.
     */
    public void createBonuses() {

        // Declare Bonus factory
        class BonusFactory implements DriftingSpriteFactory<Bonus> {
            public Bonus create(Vector2f position, Vector2f speed) {
                return new Bonus(position, speed);
            }
        }

        // Call generic sprite creation function
        createDriftingSprites(engine.bonuses, new BonusFactory(),
                BONUS_MIN_SPEED, BONUS_MAX_SPEED, BONUS_MAX_COUNT);
    }

    /**
     * Destroys the Bonuses which are out of scope.
     */
    public void destroyBonuses() {
        destroyDriftingSprites(engine.bonuses);
    }

    /**
     * Adds DriftingSprites to a list until its contains a maximum number of elements, or
     * until there is no more physical space in the game world to add any more. A factory
     * instantiates the DriftingSprites objects. Their initial position is random a margin
     * away from the edges of the GameView. Their initial speed is bounded random.
     */
    public <T extends Sprite & Collidable> void createDriftingSprites(
            List<T> list, DriftingSpriteFactory<T> factory,
            float minSpeed, float maxSpeed, int maxCount) {

        boolean createSuccess = true;

        // Create sprites until there are enough
        while (list.size() < maxCount && createSuccess) {

            // Retry a limited number of times
            int createRetries = CREATE_MAX_RETRIES;
            while (createRetries > 0) {

                // Create a sprite
                Vector2f position = getRandomPosition();
                Vector2f speed = getRandomSpeed(minSpeed, maxSpeed);
                T sprite = factory.create(position, speed);

                // Add the sprite to the game engine if the space is unoccupied
                if (hasSpace(sprite)) {
                    list.add(sprite);
                    createSuccess = true;
                    break;
                }

                // Otherwise try again during the next iteration
                else {
                    createRetries--;
                    createSuccess = false;
                }
            }
        }
    }

    /**
     * Destroys from a list the DriftingSprites which are more than a margin away from
     * the edges of the GameView. The margin is included to avoid removing the sprites
     * too early.
     */
    private <T extends Sprite> void destroyDriftingSprites(List<T> list) {

        // Iterate over all the sprites
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {

            // Remove the sprites which are out of scope
            Sprite sprite = iterator.next();
            if (gameView.isOutOfScope(sprite, SPRITE_DESTROY_DISTANCE)) {
                iterator.remove();
            }
        }
    }

    /**
     * Gets a random position a margin away from the edges of the GameView.
     *
     * @return Random position in game world coordinates.
     */
    private Vector2f getRandomPosition() {

        switch (random.nextInt(4)) {
            // Left
            case 0:
                return new Vector2f(
                        gameView.getLeftEdge() - SPRITE_CREATE_DISTANCE,
                        getRandomY());
            // Right
            case 1:
                return new Vector2f(
                        gameView.getRightEdge() + SPRITE_CREATE_DISTANCE,
                        getRandomY());
            // Top
            case 2:
                return new Vector2f(
                        getRandomX(),
                        gameView.getTopEdge() + SPRITE_CREATE_DISTANCE);
            // Bottom
            default:
                return new Vector2f(
                        getRandomX(),
                        gameView.getBottomEdge() - SPRITE_CREATE_DISTANCE);
        }
    }

    /**
     * Gets a bounded random speed vector.
     *
     * @param minSpeed Minimum norm of the speed vector in game units.
     * @param maxSpeed Maximum norm of the speed vector in game units.
     * @return Random speed vector.
     */
    private Vector2f getRandomSpeed(float minSpeed, float maxSpeed) {
        float driftAngle = TAU * random.nextFloat();
        float driftSpeed = minSpeed + random.nextFloat() * (maxSpeed - minSpeed);
        return (new Vector2f(driftAngle)).multiply(driftSpeed);
    }

    /**
     * Gets a bounded random abscissa between the left and right edges of the GameView.
     *
     * @return Random abscissa in game world coordinates.
     */
    private float getRandomX() {
        float deltaX = gameView.getRightEdge() - gameView.getLeftEdge();
        return gameView.getLeftEdge() + random.nextFloat() * deltaX;
    }

    /**
     * Gets a bounded random ordinate between the bottom and top edges of the GameView.
     *
     * @return Random ordinate in game world coordinates.
     */
    private float getRandomY() {
        float deltaY = gameView.getTopEdge() - gameView.getBottomEdge();
        return gameView.getBottomEdge() + random.nextFloat() * deltaY;
    }

    /**
     * Checks that a new Drifter does not overlap with one already part of the game engine.
     * Only valid when attempting to create DriftingSprites at getRandomPosition(). In other cases
     * we may need to perform extra checks (i.e.: overlap with the Player).
     */
    private boolean hasSpace(Collidable collider) {

        // Check overlap with asteroids
        for (Asteroid a : engine.asteroids) {
            if (CollisionUtils.overlap(a, collider)) {
                return false;
            }
        }

        // Check overlap with bonuses
        for (Bonus b : engine.bonuses) {
            if (CollisionUtils.overlap(b, collider)) {
                return false;
            }
        }

        return true;
    }
}
