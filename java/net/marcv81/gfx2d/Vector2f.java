package net.marcv81.gfx2d;

/**
 * This class handles 2D vectors.
 */
public final class Vector2f {

    /**
     * Vector2f coordinates.
     */
    private float x, y;

    /**
     * Constructor.
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor.
     */
    public Vector2f(Vector2f v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Unit Vector2f constructor from an angle.
     */
    public Vector2f(float angle) {
        this.x = (float) Math.cos(angle);
        this.y = (float) Math.sin(angle);
    }

    /**
     * Updates the coordinates of this Vector2f from another.
     */
    public Vector2f set(Vector2f v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    /**
     * Adds another Vector2f to this one.
     */
    public Vector2f plus(Vector2f v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    /**
     * Subtracts another Vector2f to this one.
     */
    public Vector2f minus(Vector2f v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    /**
     * Multiplies this Vector2f with a float.
     */
    public Vector2f multiply(float f) {
        this.x *= f;
        this.y *= f;
        return this;
    }

    /**
     * Divides this Vector2f by a float.
     */
    public Vector2f divide(float f) {
        this.x /= f;
        this.y /= f;
        return this;
    }

    /**
     * Gets the abscissa.
     */
    public float getX() {
        return this.x;
    }

    /**
     * Gets the ordinate.
     */
    public float getY() {
        return this.y;
    }

    /**
     * Calculates the dot product between this Vector2f and another.
     */
    public float dot(Vector2f v) {
        return this.x * v.x + this.y * v.y;
    }

    /**
     * Calculates the square of the norm of this Vector2f.
     */
    public float normSquare() {
        return (x * x) + (y * y);
    }

    /**
     * Calculates the norm of this Vector2f.
     */
    public float norm() {
        return (float) Math.sqrt(normSquare());
    }

    /**
     * Calculates the distance between this Vector2f and another.
     */
    public float distance(Vector2f v) {
        return (new Vector2f(this)).minus(v).norm();
    }

    /**
     * Calculates the angle of this Vector2f.
     */
    public float angle() {
        return (float) Math.atan2(y, x);
    }
}
