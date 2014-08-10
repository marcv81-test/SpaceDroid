package marcv81.gfx2d;

public class Sprite {

    protected final static float TAU = 6.2831853071f;

    private float x = 0f, y = 0f;

    // Accessors
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

    // Override these methods if the texture supports it
    public int getAnimation() {
        return 0;
    }
    public float getAngle() {
        return 0f;
    }
    public float getTransparency() {
        return 1f;
    }
    public float getScale() { return 1f; }

    // Get the distance between the projections on the XY plane of two sprites
    public float getDistance(Sprite sprite) {
        float dx = sprite.getX() - x;
        float dy = sprite.getY() - y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
