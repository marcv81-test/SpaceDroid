package marcv81.gfx2d;

import java.util.Iterator;

public class ParticleGroup<T extends Particle> extends SpriteGroup<T> {

    // Constructor
    public ParticleGroup(SpriteTexture texture, SpriteGeometry geometry,
                         boolean supportAngle, boolean supportTransparency, boolean supportScaling) {
        super(texture, geometry, supportAngle, supportTransparency, supportScaling);
    }

    public void update(long timeSlice) {

        // Iterate over all the particles
        Iterator<T> iterator = getSprites().iterator();
        while (iterator.hasNext()) {

            // Update each particle
            T particle = iterator.next();
            particle.update(timeSlice);

            // Remove the expired particles
            if (particle.isExpired()) {
                iterator.remove();
            }
        }
    }
}
