package marcv81.game;

import android.content.Context;
import marcv81.gfx2d.Renderer;
import marcv81.gfx2d.SpriteGeometry;
import marcv81.gfx2d.SpriteGroup;
import marcv81.gfx2d.SpriteTexture;
import javax.microedition.khronos.opengles.GL10;
import java.util.Iterator;
import java.util.Random;

class GameRenderer extends Renderer {

    // Background texture
    private static final int BACKGROUND_RESOURCE = R.drawable.stars;
    public static final float BACKGROUND_SIZE = 16f;
    private static final int BACKGROUND_ANIMATIONS_X = 1;
    private static final int BACKGROUND_ANIMATIONS_Y = 1;
    private static final boolean BACKGROUND_SUPPORT_ANGLE = false;
    private static final boolean BACKGROUND_SUPPORT_TRANSPARENCY = false;

    // Player texture
    private static final int PLAYER_RESOURCE = R.drawable.player;
    private static final float PLAYER_SIZE = 0.2f;
    private static final int PLAYER_ANIMATIONS_X = 1;
    private static final int PLAYER_ANIMATIONS_Y = 1;
    private static final boolean PLAYER_SUPPORT_ANGLE = true;
    private static final boolean PLAYER_SUPPORT_TRANSPARENCY = false;

    // Asteroid texture
    private static final int ASTEROID_RESOURCE = R.drawable.asteroid;
    private static final float ASTEROID_SIZE = 0.4f;
    private static final int ASTEROID_ANIMATIONS_X = 8;
    private static final int ASTEROID_ANIMATIONS_Y = 8;
    private static final boolean ASTEROID_SUPPORT_ANGLE = false;
    private static final boolean ASTEROID_SUPPORT_TRANSPARENCY = true;

    // Fireball texture
    private static final int FIREBALL_RESOURCE = R.drawable.fireball;
    private static final float FIREBALL_SIZE = 0.3f;
    private static final int FIREBALL_ANIMATIONS_X = 4;
    private static final int FIREBALL_ANIMATIONS_Y = 4;
    private static final boolean FIREBALL_SUPPORT_ANGLE = true;
    private static final boolean FIREBALL_SUPPORT_TRANSPARENCY = false;

    // Stardust texture
    private static final int SMOKE_RESOURCE = R.drawable.stardust;
    private static final float SMOKE_SIZE = 0.05f;
    private static final int SMOKE_ANIMATIONS_X = 1;
    private static final int SMOKE_ANIMATIONS_Y = 1;
    private static final boolean SMOKE_SUPPORT_ANGLE = true;
    private static final boolean SMOKE_SUPPORT_TRANSPARENCY = true;

    protected static final float FOREGROUND_DEPTH = 0f;
    protected static final float BACKGROUND_DEPTH = 8f;

    private static final int ASTEROID_MAX_COUNT = 100;
    private static final float ASTEROID_COLLISION_DISTANCE = 0.3f;
    private static final float PLAYER_COLLISION_DISTANCE = 0.15f;

    // Touchscreen status
    private float pointerX = 0f, pointerY = 0f;
    private boolean pointerDown = false;

    private final Random random = new Random();

    // Sprites groups
    private final SpriteGroup<Background> backgrounds = new SpriteGroup<>(
            new SpriteTexture(BACKGROUND_RESOURCE, BACKGROUND_ANIMATIONS_X, BACKGROUND_ANIMATIONS_Y),
            new SpriteGeometry(BACKGROUND_SIZE), BACKGROUND_DEPTH,
            BACKGROUND_SUPPORT_ANGLE, BACKGROUND_SUPPORT_TRANSPARENCY
    );
    private final SpriteGroup<Player> players = new SpriteGroup<>(
            new SpriteTexture(PLAYER_RESOURCE, PLAYER_ANIMATIONS_X, PLAYER_ANIMATIONS_Y),
            new SpriteGeometry(PLAYER_SIZE), FOREGROUND_DEPTH,
            PLAYER_SUPPORT_ANGLE, PLAYER_SUPPORT_TRANSPARENCY
    );
    private final SpriteGroup<Asteroid> asteroids = new SpriteGroup<>(
            new SpriteTexture(ASTEROID_RESOURCE, ASTEROID_ANIMATIONS_X, ASTEROID_ANIMATIONS_Y),
            new SpriteGeometry(ASTEROID_SIZE), FOREGROUND_DEPTH,
            ASTEROID_SUPPORT_ANGLE, ASTEROID_SUPPORT_TRANSPARENCY
    );
    private final SpriteGroup<Fireball> fireballs = new SpriteGroup<>(
            new SpriteTexture(FIREBALL_RESOURCE, FIREBALL_ANIMATIONS_X, FIREBALL_ANIMATIONS_Y),
            new SpriteGeometry(FIREBALL_SIZE), FOREGROUND_DEPTH,
            FIREBALL_SUPPORT_ANGLE, FIREBALL_SUPPORT_TRANSPARENCY
    );
    private final SpriteGroup<Smoke> smokes = new SpriteGroup<>(
            new SpriteTexture(SMOKE_RESOURCE, SMOKE_ANIMATIONS_X, SMOKE_ANIMATIONS_Y),
            new SpriteGeometry(SMOKE_SIZE), FOREGROUND_DEPTH,
            SMOKE_SUPPORT_ANGLE, SMOKE_SUPPORT_TRANSPARENCY
    );

    Player player = new Player();

    // Constructor
    GameRenderer(Context context) {
        super(context);
        players.getSprites().add(player);
    }

    void setPointerDown(boolean pointerDown) {
        this.pointerDown = pointerDown;
    }

    void setPointerXY(float x, float y) {
        this.pointerX = x;
        this.pointerY = y;
    }

    @Override
    protected SpriteTexture[] getTextures() {
        return new SpriteTexture[]{
                backgrounds.getTexture(), players.getTexture(),
                asteroids.getTexture(), fireballs.getTexture(),
                smokes.getTexture()};
    }

    @Override
    protected void update(long timeSlice) {
        updateBackground();
        updatePlayer(timeSlice);
        updateAsteroids(timeSlice);
        updateFireballs(timeSlice);
        updateSmokes(timeSlice);
    }

    @Override
    protected void draw(GL10 gl) {

        backgrounds.draw(gl);
        players.draw(gl);
        asteroids.draw(gl);
        fireballs.draw(gl);
        smokes.draw(gl);
    }

    private void updateBackground() {

        backgrounds.getSprites().clear();
        float x1 = Math.round(getCameraX() / BACKGROUND_SIZE);
        float y1 = Math.round(getCameraY() / BACKGROUND_SIZE);
        for (float x2 : new float[]{x1 - 0.5f, x1 + 0.5f}) {
            for (float y2 : new float[]{y1 - 0.5f, y1 + 0.5f}) {
                Background background = new Background();
                background.setX(BACKGROUND_SIZE * x2);
                background.setY(BACKGROUND_SIZE * y2);
                backgrounds.getSprites().add(background);
            }
        }
    }

    private void updatePlayer(long timeSlice) {
        if (pointerDown) {
            float x = -2f * ((pointerX / getWidth()) - 0.5f);
            float y = -2f * ((pointerY / getHeight()) - 0.5f);
            float norm = (float) Math.sqrt(x * x + y * y);
            player.setAccelXY(x / norm, y / norm);
            smokes.getSprites().add(new Smoke(random,
                    player.getExhaustX(), player.getExhaustY(),
                    random.nextFloat() - 0.5f, random.nextFloat() - 0.5f));
        } else {
            player.setAccelXY(0f, 0f);
        }
        player.update(timeSlice);
        setXY(player.getX(), player.getY());
    }

    private void updateAsteroids(long timeSlice) {

        Iterator<Asteroid> asteroidIterator = asteroids.getSprites().iterator();
        while (asteroidIterator.hasNext()) {

            Asteroid asteroid = asteroidIterator.next();
            asteroid.update(timeSlice);

            // Remove the asteroids which are too far or have exploded
            if (asteroid.isOutOfScope(player)
                    || asteroid.hasExploded()) {
                asteroidIterator.remove();
            }
        }

        // Add asteroids if we have space
        while (asteroids.getSprites().size() < ASTEROID_MAX_COUNT) {
            asteroids.getSprites().add(new Asteroid(random, getCameraX(), getCameraY()));
        }

        // Asteroid collision detection
        for (int i = 0; i < asteroids.getSprites().size(); i++) {
            Asteroid asteroid1 = (Asteroid) asteroids.getSprites().get(i);
            if (!asteroid1.isExploding()) {
                for (int j = i + 1; j < asteroids.getSprites().size(); j++) {
                    Asteroid asteroid2 = (Asteroid) asteroids.getSprites().get(j);
                    if (!asteroid2.isExploding()) {

                        // Check the distance between the asteroids
                        float distance = asteroid1.getDistance(asteroid2);
                        if (distance < ASTEROID_COLLISION_DISTANCE) {

                            // Create a fireball
                            float fx = (asteroid1.getX() + asteroid2.getX()) / 2f;
                            float fy = (asteroid1.getY() + asteroid2.getY()) / 2f;
                            float fsx = (asteroid1.getSpeedX() + asteroid2
                                    .getSpeedX()) / 2f;
                            float fsy = (asteroid1.getSpeedY() + asteroid2
                                    .getSpeedY()) / 2f;
                            fireballs.getSprites()
                                    .add(new Fireball(random, fx, fy, fsx, fsy));

                            // Start the asteroids explosion
                            asteroid1.explode();
                            asteroid2.explode();
                        }
                    }
                }
                if (asteroid1.getDistance(player) < (ASTEROID_COLLISION_DISTANCE
                        + PLAYER_COLLISION_DISTANCE) / 2f) {

                    // Create a fireball
                    float fx = (asteroid1.getX() + player.getX()) / 2f;
                    float fy = (asteroid1.getY() + player.getY()) / 2f;
                    float fsx = asteroid1.getSpeedX();
                    float fsy = asteroid1.getSpeedY();
                    fireballs.getSprites()
                            .add(new Fireball(random, fx, fy, fsx, fsy));

                    // Start the asteroid explosion
                    asteroid1.explode();
                }
            }
        }
    }

    private void updateFireballs(long timeSlice) {

        Iterator<Fireball> fireballIterator = fireballs.getSprites().iterator();
        while (fireballIterator.hasNext()) {

            Fireball fireball = fireballIterator.next();
            fireball.update(timeSlice);

            // Remove the expired fireballs
            if (fireball.isExpired()) {
                fireballIterator.remove();
            }
        }
    }

    private void updateSmokes(long timeSlice) {

        Iterator<Smoke> smokeIterator = smokes.getSprites().iterator();
        while (smokeIterator.hasNext()) {

            Smoke smoke = smokeIterator.next();
            smoke.update(timeSlice);

            if (smoke.isExpired()) {
                smokeIterator.remove();
            }
        }
    }
}
