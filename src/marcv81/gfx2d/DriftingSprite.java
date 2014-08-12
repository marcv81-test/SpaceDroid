package marcv81.gfx2d;

public abstract class DriftingSprite extends Sprite {

    private Vector2f speed;

    // Constructor
    public DriftingSprite(Vector2f position, Vector2f speed) {
        super(position);
        this.speed = new Vector2f(speed);
    }

    public Vector2f getSpeed() {
        return speed;
    }

    public abstract float getMass();

    public abstract float getDiameter();

    // Update the sprite position
    public void update(long timeSlice) {
        Vector2f deltaSpeed = new Vector2f(speed);
        deltaSpeed.scale(timeSlice / 1000f);
        getPosition().add(deltaSpeed);
    }

    // Find if two sprites overlap
    public boolean overlaps(DriftingSprite s) {
        return (getDistance(s) < (getDiameter() + s.getDiameter()) / 2f);
    }

    public Vector2f impactPoint(DriftingSprite s) {
        Vector2f position1 = new Vector2f(this.getPosition());
        position1.scale(s.getDiameter());
        Vector2f position2 = new Vector2f(s.getPosition());
        position2.scale(this.getDiameter());
        position1.add(position2);
        position1.scale(1f / (this.getDiameter() + s.getDiameter()));
        return position1;
    }

    // Deviate two colliding sprites
    public boolean collide(DriftingSprite s) {

        // Prepare the collision delta vectors
        Vector2f deltaSpeed = new Vector2f(getSpeed());
        deltaSpeed.sub(s.getSpeed());
        Vector2f deltaPosition = new Vector2f(getPosition());
        deltaPosition.sub(s.getPosition());

        // Calculate the dot product between the delta vectors
        float dotProduct = deltaSpeed.dot(deltaPosition);

        // Prevent collisions from generating attracting deviations
        if (dotProduct < 0f) {

            // Calculate the collision factor
            float ratio = 2f * dotProduct / ((getMass() + s.getMass()) * deltaPosition.normSquare());

            // Apply the deviation to this sprite
            Vector2f deviation1 = new Vector2f(deltaPosition);
            deviation1.scale(s.getMass() * ratio);
            getSpeed().sub(deviation1);

            // Apply the deviation to the parameter sprite
            Vector2f deviation2 = new Vector2f(deltaPosition);
            deviation2.scale(-getMass() * ratio);
            s.getSpeed().sub(deviation2);

            return true;
        }

        // If the collision would have generated an attracting deviation
        else {
            return false;
        }
    }
}
