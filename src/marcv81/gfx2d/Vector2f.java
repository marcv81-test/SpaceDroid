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

    public void set(Vector2f v) {
        this.x = v.x;
        this.y = v.y;
    }

    public void add(Vector2f v) {
        x += v.x;
        y += v.y;
    }

    public void sub(Vector2f v) {
        x -= v.x;
        y -= v.y;
    }

    public void scale(float f) {
        x *= f;
        y *= f;
    }

    public void negate() {
        x = -x;
        y = -y;
    }


    // Dot product
    public float dot(Vector2f v) {
        return x * v.x + y * v.y;
    }

    public float norm() {
        return (float) Math.sqrt(normSquare());
    }

    public float normSquare() {
        return (x * x) + (y * y);
    }

    public float distance(Vector2f v) {
        Vector2f u = new Vector2f(this);
        u.sub(v);
        return u.norm();
    }

    public float angle() {
        return (float) Math.atan2(y, x);
    }
}
