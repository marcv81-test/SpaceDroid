package net.marcv81.spacedroid;

import net.marcv81.gfx2d.GameView;
import net.marcv81.gfx2d.Vector2f;
import net.marcv81.spacedroid.sprites.Asteroid;
import net.marcv81.spacedroid.sprites.Bonus;
import net.marcv81.spacedroid.sprites.DriftingSprite;

import java.util.Iterator;
import java.util.Random;

/**
 * This class handles the part of the engine responsible for spawning Asteroids and Bonuses
 * outside of the scope of the view, and destroying them when they drift too far away.
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
     * Margin between the edge of the view and where the DriftingSprites spawn.
     */
    private static final float SPRITE_SPAWN_DISTANCE = 0.5f;

    /**
     * Margin between the edge of the view and where the DriftingSprites are destroyed.
     */
    private static final float SPRITE_DESTROY_DISTANCE = 0.75f;

    /**
     * Number of attempts to spawn a DriftingSprites until we give up. Failures happen
     * when the random position is already occupied by another DriftingSprite.
     */
    private static final int SPAWN_MAX_RETRIES = 3;

    private final SpacedroidEngine engine;
    private final GameView gameView;
    private final Random random;

    /**
     * Constructor.
     */
    public SpawnEngine(SpacedroidEngine engine, GameView gameView, Random random) {
        this.engine = engine;
        this.gameView = gameView;
        this.random = random;
    }

    /**
     * Spawn Asteroids at random, a margin away from the edges of the view. Stop when
     * the required number of Asteroids is reached, or when space constraints prevent
     * any more Asteroids from being created.
     */
    public void spawnAsteroids() {

        boolean spawnSuccess = true;

        // Spawns asteroids until there are enough or there was a failure (i.e.: not enough space)
        while (engine.getAsteroids().size() < ASTEROID_MAX_COUNT && spawnSuccess) {

            // Only retry a limited number of times
            int spawnRetries = SPAWN_MAX_RETRIES;
            while (spawnRetries > 0) {

                // Create a random asteroid
                Vector2f speed = getRandomSpeed(ASTEROID_MIN_SPEED, ASTEROID_MAX_SPEED);
                Asteroid asteroid = new Asteroid(getRandomPosition(), speed, random);

                // If no other sprite occupies the space add the asteroid to the game engine
                if (hasSpace(asteroid)) {
                    engine.getAsteroids().add(asteroid);
                    spawnSuccess = true;
                    break;
                }

                // Otherwise try again during the next iteration
                else {
                    spawnRetries--;
                    spawnSuccess = false;
                }
            }
        }
    }

    /**
     * Destroy the Asteroids which are too far away from the edges of the view.
     */
    public void destroyAsteroids() {

        // Iterate over all the asteroids
        Iterator<Asteroid> asteroidIterator = engine.getAsteroids().iterator();
        while (asteroidIterator.hasNext()) {

            // Remove the asteroids which are too far
            Asteroid asteroid = asteroidIterator.next();
            if (gameView.isOutOfScope(asteroid, SPRITE_DESTROY_DISTANCE)) {
                asteroidIterator.remove();
            }
        }
    }

    /**
     * Spawn Bonuses at random, a margin away from the edges of the view. Stop when
     * the required number of Bonuses is reached, or when space constraints prevent
     * any more Bonuses from being created.
     */
    public void spawnBonuses() {

        boolean spawnSuccess = true;

        // Spawns bonuses until there are enough or there was a failure (i.e.: not enough space)
        while (engine.getBonuses().size() < BONUS_MAX_COUNT && spawnSuccess) {

            // Only retry a limited number of times
            int spawnRetries = SPAWN_MAX_RETRIES;
            while (spawnRetries > 0) {

                // Create a random bonus
                Vector2f speed = getRandomSpeed(BONUS_MIN_SPEED, BONUS_MAX_SPEED);
                Bonus bonus = new Bonus(getRandomPosition(), speed);

                // If no other sprite occupies the space add the bonus to the game engine
                if (hasSpace(bonus)) {
                    engine.getBonuses().add(bonus);
                    spawnSuccess = true;
                    break;
                }

                // Otherwise try again during the next iteration
                else {
                    spawnRetries--;
                    spawnSuccess = false;
                }
            }
        }
    }

    /**
     * Destroy the Bonuses which are too far away from the edges of the view or expired.
     */
    public void destroyBonuses() {

        // Iterate over all the bonuses
        Iterator<Bonus> bonusIterator = engine.getBonuses().iterator();
        while (bonusIterator.hasNext()) {

            // Remove the bonuses which are too far or expired
            Bonus bonus = bonusIterator.next();
            if (gameView.isOutOfScope(bonus, SPRITE_DESTROY_DISTANCE) || bonus.isExpired()) {
                bonusIterator.remove();
            }
        }
    }

    /**
     * Gets a random position a margin away from the edges of the game view.
     *
     * @return Random position in game world coordinates.
     */
    private Vector2f getRandomPosition() {

        switch (random.nextInt(4)) {
            // Left
            case 0:
                return new Vector2f(
                        gameView.getLeftEdge() - SPRITE_SPAWN_DISTANCE,
                        getRandomY());
            // Right
            case 1:
                return new Vector2f(
                        gameView.getRightEdge() + SPRITE_SPAWN_DISTANCE,
                        getRandomY());
            // Top
            case 2:
                return new Vector2f(
                        getRandomX(),
                        gameView.getTopEdge() + SPRITE_SPAWN_DISTANCE);
            // Bottom
            default:
                return new Vector2f(
                        getRandomX(),
                        gameView.getBottomEdge() - SPRITE_SPAWN_DISTANCE);
        }
    }

    /**
     * Gets a random bounded speed vector.
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
     * Gets a random abscissa between the left and right edges of the game view.
     *
     * @return Random abscissa in game world coordinates.
     */
    private float getRandomX() {
        float deltaX = gameView.getRightEdge() - gameView.getLeftEdge();
        return gameView.getLeftEdge() + random.nextFloat() * deltaX;
    }

    /**
     * Gets a random ordinate between the bottom and top edges of the game view.
     *
     * @return Random ordinate in game world coordinates.
     */
    private float getRandomY() {
        float deltaY = gameView.getTopEdge() - gameView.getBottomEdge();
        return gameView.getBottomEdge() + random.nextFloat() * deltaY;
    }

    /**
     * Checks that a new DriftingSprite does not overlap with one already part of the game engine.
     * Only valid when attempting to spawn DriftingSprites at getRandomPosition(). In other cases
     * we may need to perform extra checks (i.e.: overlap with the Player).
     */
    private boolean hasSpace(DriftingSprite sprite) {

        // Check overlap with asteroids
        for (Asteroid a : engine.getAsteroids()) {
            if (a.overlaps(sprite)) {
                return false;
            }
        }

        // Check overlap with bonuses
        for (Bonus b : engine.getBonuses()) {
            if (b.overlaps(sprite)) {
                return false;
            }
        }

        return true;
    }
}
