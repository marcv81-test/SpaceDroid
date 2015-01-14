package marcv81.gfx2d;

public class Vector2f {

    public float x, y;

    // Constructor
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Constructor: unit vector from angle
    public Vector2f(float angle) {
        this.x = (float) Math.cos(angle);
        this.y = (float) Math.sin(angle);
    }

    // Copy constructor
    public Vector2f(Vector2f v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2f set(Vector2f v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2f plus(Vector2f v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2f minus(Vector2f v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2f multiply(float f) {
        this.x *= f;
        this.y *= f;
        return this;
    }

    public Vector2f divide(float f) {
        this.x /= f;
        this.y /= f;
        return this;
    }

    public float dot(Vector2f v) {
        return this.x * v.x + this.y * v.y;
    }

    public float normSquare() {
        return (x * x) + (y * y);
    }

    public float norm() {
        return (float) Math.sqrt(normSquare());
    }

    public float distance(Vector2f v) {
        return (new Vector2f(this)).minus(v).norm();
    }

    public float angle() {
        return (float) Math.atan2(y, x);
    }
}
