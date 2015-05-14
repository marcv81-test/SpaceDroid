package net.marcv81.gfx2d;

/**
 * This class handles sprites. The game engine updates them, and the gave view renders them,
 */
public class Sprite {

    protected static final float TAU = 6.2831853071f;
    protected static final float DEGREE_PER_RADIAN = 57.2957795f;

    /**
     * Sprite position in game world coordinates.
     */
    private final Vector2f position = new Vector2f(0f, 0f);

    /**
     * Constructor from position.
     */
    public Sprite(Vector2f position) {
        this.position.set(new Vector2f(position));
    }

    /**
     * Gets the Sprite position.
     */
    public Vector2f getPosition() {
        return new Vector2f(position);
    }

    /**
     * Sets the Sprite position.
     */
    public void setPosition(Vector2f v) {
        position.set(v);
    }

    /**
     * Adds a vector to the position of this Sprite.
     */
    public void addToPosition(Vector2f v) {
        position.plus(v);
    }

    /**
     * Calculates the distance in game units between this Sprite and another one.
     */
    public float getDistance(Sprite sprite) {
        return position.distance(sprite.position);
    }

    /**
     * Returns the current animation. Override if this Sprite supports animations.
     */
    public int getAnimation() {
        return 0;
    }

    /**
     * Returns the current angle. Override if this Sprite supports orientation.
     */
    public float getAngle() {
        return 0f;
    }

    /**
     * Returns the current transparency. Override if this Sprite supports transparency.
     *
     * @return Transparency between 0.0f (transparent) and 1,0f (opaque).
     */
    public float getTransparency() {
        return 1f;
    }

    /**
     * Returns the current scaling factor. Override if this Sprite supports scaling.
     */
    public float getScale() {
        return 1f;
    }
}
