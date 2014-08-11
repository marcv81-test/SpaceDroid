package marcv81.gfx2d;

public abstract class DriftingSprite extends Sprite {

    private Vector2f speed;

    public DriftingSprite(Vector2f position, Vector2f speed) {
        super(position);
        this.speed = new Vector2f(speed);
    }

    public Vector2f getSpeed() {
        return speed;
    }

    public abstract float getMass();

    public abstract float getDiameter();

    public void update(long timeSlice) {

        // Update position
        Vector2f deltaSpeed = new Vector2f(speed);
        deltaSpeed.scale(timeSlice / 1000f);
        getPosition().add(deltaSpeed);
    }

    public boolean overlaps(DriftingSprite s) {
        return (getDistance(s) < (getDiameter() + s.getDiameter()) / 2f);
    }

    public void collide(DriftingSprite s) {

        Vector2f speedDiff = new Vector2f(getSpeed());
        speedDiff.sub(s.getSpeed());
        Vector2f positionDiff = new Vector2f(getPosition());
        positionDiff.sub(s.getPosition());

        Vector2f deviation;
        float ratio = 2f * speedDiff.dot(positionDiff)
                / ((getMass() + s.getMass()) * positionDiff.normSquare());

        deviation = new Vector2f(positionDiff);
        deviation.scale(s.getMass() * ratio);
        getSpeed().sub(deviation);

        deviation = new Vector2f(positionDiff);
        deviation.scale(-getMass() * ratio);
        s.getSpeed().sub(deviation);
    }
}
