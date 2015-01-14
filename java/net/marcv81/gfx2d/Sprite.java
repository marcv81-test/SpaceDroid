package net.marcv81.gfx2d;

public class Sprite {

    protected static final float TAU = 6.2831853071f;
    protected static final float DEGREE_PER_RADIAN = 57.2957795f;

    private Vector2f position;

    // Constructor
    public Sprite(Vector2f position) {
        this.position = new Vector2f(position);
    }

    public Vector2f getPosition() {
        return new Vector2f(position);
    }

    public void setPosition(Vector2f v) {
        position.set(v);
    }

    public void addToPosition(Vector2f v) {
        position.plus(v);
    }

    // Get the distance between two sprites
    public float getDistance(Sprite sprite) {
        return position.distance(sprite.position);
    }

    // Override this method if the sprites support multiple animations
    public int getAnimation() {
        return 0;
    }

    // Override this method if the sprites support drawing at an angle
    public float getAngle() {
        return 0f;
    }

    // Override this method if the sprites support transparency
    public float getTransparency() {
        return 1f;
    }

    // Override this method if the sprites support scaling
    public float getScale() {
        return 1f;
    }
}
