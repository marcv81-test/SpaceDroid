package net.marcv81.gfx2d;

import javax.microedition.khronos.opengles.GL10;
import java.util.List;

public final class SpriteRenderer {

    private final SpriteTexture texture;
    private final SpriteGeometry geometry;

    // Sprites drawing capabilities
    private final boolean supportAngle;
    private final boolean supportTransparency;
    private final boolean supportAnimation;
    private final boolean supportScaling;

    private final List<? extends Sprite> sprites;

    // Constructor
    public SpriteRenderer(SpriteTexture texture, SpriteGeometry geometry,
                          boolean supportAngle, boolean supportTransparency, boolean supportScaling,
                          List<? extends Sprite> sprites) {

        this.texture = texture;
        this.geometry = geometry;

        this.supportAngle = supportAngle;
        this.supportTransparency = supportTransparency;
        this.supportScaling = supportScaling;
        this.supportAnimation = (texture.getTotalAnimations() > 1);

        this.sprites = sprites;
    }

    public SpriteTexture getTexture() {
        return texture;
    }

    // Draw the sprites in the group
    public void draw(GL10 gl) {

        texture.bind(gl);
        geometry.setVertices(gl);

        if (!supportTransparency) {
            gl.glColor4f(1f, 1f, 1f, 1f);
        }
        if (!supportAnimation) {
            texture.setAnimation(gl, 0);
        }

        for (Sprite sprite : sprites) {

            gl.glPushMatrix();
            gl.glTranslatef(sprite.getPosition().getX(), sprite.getPosition().getY(), 0f);

            if (supportAngle) {
                gl.glRotatef(sprite.getAngle(), 0f, 0f, 1f);
            }
            if (supportTransparency) {
                float transparency = sprite.getTransparency();
                gl.glColor4f(transparency, transparency, transparency, transparency);
            }
            if (supportAnimation) {
                texture.setAnimation(gl, sprite.getAnimation());
            }
            if (supportScaling) {
                float scale = sprite.getScale();
                gl.glScalef(scale, scale, scale);
            }

            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
            gl.glPopMatrix();
        }
    }
}