package net.marcv81.spacedroid.game;

import net.marcv81.spacedroid.common.Vector2f;
import net.marcv81.spacedroid.physics.Collidable;
import net.marcv81.spacedroid.physics.Driftable;
import net.marcv81.spacedroid.physics.Expirable;
import net.marcv81.spacedroid.physics.Updatable;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class World implements Updatable {

    private static final int SPARKLES_PER_IMPACT = 5;
    private static final float WORLD_BOUND = 1.5f;

    private Player player;
    private List<Player> players;
    private List<Asteroid> asteroids;
    private List<Bonus> bonuses;
    private List<Sparkle> sparkles;
    private List<Smoke> smokes;

    private final Random random = new Random();
    private final WorldPhysics worldPhysics = new WorldPhysics();

    /**
     * Updates this World.
     */
    public void update(long timeSlice) {

        // Update the objects
        updateList(players, timeSlice);
        updateList(asteroids, timeSlice);
        updateList(bonuses, timeSlice);
        updateList(sparkles, timeSlice);
        updateList(smokes, timeSlice);

        // Remove the expired objects
        removeExpired(bonuses);
        removeExpired(sparkles);
        removeExpired(smokes);

        // Remove the out-of-bounds objects
        removeOutOfBounds(asteroids);
        removeOutOfBounds(bonuses);

        // Handle the collisions
        collideList(asteroids);
        collideList(bonuses);
        collideLists(asteroids, bonuses);
        collideLists(players, asteroids);
        collideLists(players, bonuses);
    }

    /**
     * Updates all the Updatables in a list.
     */
    private void updateList(List<? extends Updatable> updatables, long timeSlice) {
        for (Updatable updatable : updatables) {
            updatable.update(timeSlice);
        }
    }

    /**
     * Removes the expired Expirables from a list.
     */
    public <T extends Expirable> void removeExpired(List<T> expirables) {

        // Iterate over expirables
        Iterator<T> iterator = expirables.iterator();
        while (iterator.hasNext()) {

            // Remove expired expirables
            T expirable = iterator.next();
            if (expirable.isExpired()) {
                iterator.remove();
            }
        }
    }

    private <T extends Driftable> void removeOutOfBounds(List<T> driftables) {

        // Iterate over driftables
        Iterator<T> iterator = driftables.iterator();
        while (iterator.hasNext()) {

            // Remove out of bounds driftables
            T driftable = iterator.next();
            if (isOutOfBounds(driftable)) {
                iterator.remove();
            }
        }
    }

    private boolean isOutOfBounds(Driftable driftable) {
        return (Math.abs(player.getPosition().getX() - driftable.getPosition().getX()) > WORLD_BOUND) ||
                (Math.abs(player.getPosition().getY() - driftable.getPosition().getY()) > WORLD_BOUND);
    }

    /**
     * Checks for collisions between any two Collidables from the same list.
     */
    private <T extends Collidable> void collideList(List<T> collidables) {

        // Iterate over collidables pairs
        for (int i = 0; i < collidables.size(); i++) {
            T collidable1 = collidables.get(i);
            for (int j = i + 1; j < collidables.size(); j++) {
                T collidable2 = collidables.get(j);

                // Attempt collision
                collide(collidable1, collidable2);
            }
        }
    }

    /**
     * Checks for collisions between two Collidables coming from a different
     * lists each.
     */
    private <T1 extends Collidable, T2 extends Collidable> void collideLists(
            List<T1> collidables1, List<T2> collidables2) {

        // Iterate over collidables pairs
        for (T1 collidable1 : collidables1) {
            for (T2 collidable2 : collidables2) {

                // Attempt collision
                collide(collidable1, collidable2);
            }
        }
    }

    /**
     * Checks for a collision between two Collidables.
     */
    private <T1 extends Collidable, T2 extends Collidable> void collide(
            T1 collidable1, T2 collidable2) {

        Vector2f deviation1 = new Vector2f(0f, 0f);
        Vector2f deviation2 = new Vector2f(0f, 0f);

        // Check collidables pair for collision
        // Update deviations when appropriate
        if (worldPhysics.collide(collidable1, collidable2,
                deviation1, deviation2)) {

            // Notify the collidables of the deviation
            collidable1.collide(collidable2.getClass(), deviation1);
            collidable2.collide(collidable1.getClass(), deviation2);

            // Handle the consequences on this world
            handleCollision(collidable1, collidable2);
        }
    }

    /**
     * Handles the consequences on the game world of a collision between two
     * Collidables.
     */
    private <T1 extends Collidable, T2 extends Collidable> void handleCollision(
            T1 collidable1, T2 collidable2) {

        // Create impact sparkles unless the collision is between player and bonus
        if (!isPairOfTypes(collidable1, collidable2, Player.class, Bonus.class)) {
            createCollisionSparkles(collidable1, collidable2);
        }
    }

    /**
     * Creates a few Sparkles in this World.
     */
    private void createCollisionSparkles(Collidable c1, Collidable c2) {
        Vector2f impactPoint = worldPhysics.impactPoint(c1, c2);
        for (int n = 0; n < SPARKLES_PER_IMPACT; n++) {
            sparkles.add(new Sparkle(impactPoint, random));
        }
    }

    /**
     * Checks if the pair of objects is of the pair of types. The order in which
     * the objects and types are given has no influence on the result.
     */
    private static boolean isPairOfTypes(Object o1, Object o2, Class c1, Class c2) {
        return (o1.getClass() == c1 && o2.getClass() == c2) ||
                (o1.getClass() == c2 && o2.getClass() == c1);
    }
}
