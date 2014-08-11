package marcv81.gfx2d;

public class Sprite {

    protected static final float TAU = 6.2831853071f;
    protected static final float DEGREE_PER_RADIAN = 57.2957795f;

    private float x = 0f, y = 0f;

    // Constructor
    public Sprite(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
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

    // Get the distance between the projections on the XY plane of two sprites
    public float getDistance(Sprite sprite) {
        float dx = sprite.getX() - x;
        float dy = sprite.getY() - y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
