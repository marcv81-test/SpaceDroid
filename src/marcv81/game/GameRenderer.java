package marcv81.game;

import android.content.Context;
import marcv81.gfx2d.Renderer;
import marcv81.gfx2d.Texture;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    protected static final float FOREGROUND_DEPTH = 0f;
    protected static final float BACKGROUND_DEPTH = 8f;

    private static final int SPRITE_BACKGROUND = 0;
    private static final int SPRITE_PLAYER = 1;
    private static final int SPRITE_ASTEROID = 2;
    private static final int SPRITE_FIREBALL = 3;

    private static final int ASTEROID_MAX_COUNT = 100;
    private static final float ASTEROID_COLLISION_DISTANCE = 0.3f;
    private static final float PLAYER_COLLISION_DISTANCE = 0.15f;

    // Touchscreen status
    private float pointerX = 0f, pointerY = 0f;
    private boolean pointerDown = false;

    private final Random random = new Random();

    private final Texture[] textures = {
            new Texture(
                    BACKGROUND_RESOURCE, BACKGROUND_SIZE,
                    BACKGROUND_ANIMATIONS_X, BACKGROUND_ANIMATIONS_Y,
                    BACKGROUND_SUPPORT_ANGLE, BACKGROUND_SUPPORT_TRANSPARENCY),
            new Texture(
                    PLAYER_RESOURCE, PLAYER_SIZE,
                    PLAYER_ANIMATIONS_X, PLAYER_ANIMATIONS_Y,
                    PLAYER_SUPPORT_ANGLE, PLAYER_SUPPORT_TRANSPARENCY),
            new Texture(
                    ASTEROID_RESOURCE, ASTEROID_SIZE,
                    ASTEROID_ANIMATIONS_X, ASTEROID_ANIMATIONS_Y,
                    ASTEROID_SUPPORT_ANGLE, ASTEROID_SUPPORT_TRANSPARENCY),
            new Texture(
                    FIREBALL_RESOURCE, FIREBALL_SIZE,
                    FIREBALL_ANIMATIONS_X, FIREBALL_ANIMATIONS_Y,
                    FIREBALL_SUPPORT_ANGLE, FIREBALL_SUPPORT_TRANSPARENCY)
    };

    private final List<Fireball> fireballs = new ArrayList<>();
    private final List<Asteroid> asteroids = new ArrayList<>();
    private final Player player = new Player();

    // Constructor
    GameRenderer(Context context) {
        super(context);
    }

    void setPointerDown(boolean pointerDown) {
        this.pointerDown = pointerDown;
    }

    void setPointerXY(float x, float y) {
        this.pointerX = x;
        this.pointerY = y;
    }

    @Override
    protected Texture[] getTextures() {
        return textures;
    }

    @Override
    protected void update(long timeSlice) {
        updatePlayer(timeSlice);
        updateAsteroids(timeSlice);
        updateFireballs(timeSlice);
    }

    @Override
    protected void draw(GL10 gl) {
        drawBackground(gl);
        drawPlayer(gl);
        drawAsteroids(gl);
        drawFireballs(gl);
    }

    private void updatePlayer(long timeSlice) {
        if (pointerDown) {
            float x = -2f * ((pointerX / getWidth()) - 0.5f);
            float y = -2f * ((pointerY / getHeight()) - 0.5f);
            float norm = (float) Math.sqrt(x * x + y * y);
            player.setAccelXY(x / norm, y / norm);
        } else {
            player.setAccelXY(0f, 0f);
        }
        player.update(timeSlice);
        setXY(player.getX(), player.getY());
    }

    private void updateAsteroids(long timeSlice) {

        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {

            Asteroid asteroid = asteroidIterator.next();
            asteroid.update(timeSlice);

            // Remove the asteroids which are too far or have exploded
            if (asteroid.isOutOfScope(getCameraX(), getCameraY())
                    || asteroid.hasExploded()) {
                asteroidIterator.remove();
            }
        }

        // Add asteroids if we have space
        while (asteroids.size() < ASTEROID_MAX_COUNT) {
            asteroids.add(new Asteroid(random, getCameraX(), getCameraY()));
        }

        // Asteroid collision detection
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid1 = asteroids.get(i);
            if (!asteroid1.isExploding()) {
                for (int j = i + 1; j < asteroids.size(); j++) {
                    Asteroid asteroid2 = asteroids.get(j);
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
                            fireballs
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
                    fireballs
                            .add(new Fireball(random, fx, fy, fsx, fsy));

                    // Start the asteroid explosion
                    asteroid1.explode();
                }
            }
        }
    }

    private void updateFireballs(long timeSlice) {

        Iterator<Fireball> fireballIterator = fireballs.iterator();
        while (fireballIterator.hasNext()) {

            Fireball fireball = fireballIterator.next();
            fireball.update(timeSlice);

            // Remove the expired fireballs
            if (fireball.isExpired()) {
                fireballIterator.remove();
            }
        }
    }

    // Draw the 4 background tiles the closest to the camera
    private void drawBackground(GL10 gl) {
        List<Background> tiles = new ArrayList<>();
        float x1 = Math.round(getCameraX() / BACKGROUND_SIZE);
        float y1 = Math.round(getCameraY() / BACKGROUND_SIZE);
        for (float x2 : new float[]{x1 - 0.5f, x1 + 0.5f}) {
            for (float y2 : new float[]{y1 - 0.5f, y1 + 0.5f}) {
                Background background = new Background();
                background.setX(BACKGROUND_SIZE * x2);
                background.setY(BACKGROUND_SIZE * y2);
                tiles.add(background);
            }
        }
        textures[SPRITE_BACKGROUND].draw(gl, tiles);
    }

    private void drawPlayer(GL10 gl) {
        List<Player> players = new ArrayList<>();
        players.add(player);
        textures[SPRITE_PLAYER].draw(gl, players);
    }

    private void drawAsteroids(GL10 gl) {
        textures[SPRITE_ASTEROID].draw(gl, asteroids);
    }

    private void drawFireballs(GL10 gl) {
        textures[SPRITE_FIREBALL].draw(gl, fireballs);
    }
}
