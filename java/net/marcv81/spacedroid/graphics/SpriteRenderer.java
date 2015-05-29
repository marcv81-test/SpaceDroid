package net.marcv81.spacedroid.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

/**
 * This class handles the magic to draw 2D sprites with OpenGL ES. A sprite is a texture
 * mapped on a square surface on the XY plane, with the camera looking along the Z axis.
 * This class may draw one or more sprites who share a common texture, geometry, and
 * drawing capabilities (orientation, transparency, scaling, and animation). Animation
 * is implemented selecting the appropriate texture subdivision for rendering.
 */

public final class SpriteRenderer {

    private static final int COORDINATES_PER_VERTEX = 3;

    /**
     * Geometry of the Sprites to draw using this SpriteRenderer.
     */
    private final FloatBuffer verticesBuffer;

    /**
     * Texture subdivisions to implement animation. This array shall contain
     * a single element if this SpriteRenderer does not support animation.
     */
    private final FloatBuffer[] textureCoordinatesBuffers;

    /**
     * Resource identifier to load the texture.
     */
    private final int textureResourceId;

    /**
     * Identifier to retrieve the loaded texture and draw it.
     */
    private int textureName;

    /**
     * Whether this SpriteRenderer supports orientation or not.
     */
    private final boolean supportsOrientation;

    /**
     * Whether this SpriteRenderer supports transparency or not.
     */
    private final boolean supportsTransparency;

    /**
     * Whether this SpriteRenderer supports scaling or not.
     */
    private final boolean supportsScaling;

    /**
     * Whether this SpriteRenderer supports animation or not.
     */
    private final boolean supportsAnimation;

    /**
     * This list of Sprite to draw using this SpriteRenderer.
     */
    private final List<? extends Sprite> sprites;

    // Constructor
    public SpriteRenderer(float size,
                          int textureResourceId, int animationsX, int animationsY,
                          boolean supportsOrientation, boolean supportsTransparency, boolean supportsScaling,
                          List<? extends Sprite> sprites) {

        // Create the vertices buffer according to the sprites size
        // Bottom left, top left, bottom right, top right
        float[] vertices = new float[]{
                -0.5f * size, -0.5f * size, 0f,
                -0.5f * size, 0.5f * size, 0f,
                0.5f * size, -0.5f * size, 0f,
                0.5f * size, 0.5f * size, 0f};
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        // Create the texture coordinates buffers
        // There may be several buffers for different graphics (for sprite animation)
        textureCoordinatesBuffers = new FloatBuffer[animationsX * animationsY];
        for (int j = 0; j < animationsY; j++) {
            for (int i = 0; i < animationsX; i++) {
                int index = i + (j * animationsX);

                // Top left, bottom left, top right, bottom right
                float[] coordinates = {
                        i / (float) animationsX, (j + 1) / (float) animationsY,
                        i / (float) animationsX, j / (float) animationsY,
                        (i + 1) / (float) animationsX, (j + 1) / (float) animationsY,
                        (i + 1) / (float) animationsX, j / (float) animationsY};

                ByteBuffer tbb = ByteBuffer.allocateDirect(coordinates.length * 4);
                tbb.order(ByteOrder.nativeOrder());
                textureCoordinatesBuffers[index] = tbb.asFloatBuffer();
                textureCoordinatesBuffers[index].put(coordinates);
                textureCoordinatesBuffers[index].position(0);
            }
        }

        // Resource filename to load the texture bitmap
        this.textureResourceId = textureResourceId;

        // Sprite drawing capabilities
        this.supportsOrientation = supportsOrientation;
        this.supportsTransparency = supportsTransparency;
        this.supportsScaling = supportsScaling;
        this.supportsAnimation = (animationsX * animationsY > 1);

        // Sprites list
        this.sprites = sprites;
    }

    /**
     * Draws the Sprites associated to this SpriteRenderer.
     */
    protected void draw(GL10 gl) {

        // Bind the texture
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);

        // Set the vertices
        gl.glVertexPointer(COORDINATES_PER_VERTEX, GL10.GL_FLOAT, 0, verticesBuffer);

        if (!supportsTransparency) {
            gl.glColor4f(1f, 1f, 1f, 1f);
        }
        if (!supportsAnimation) {
            setAnimationIndex(gl, 0);
        }

        for (Sprite sprite : sprites) {

            gl.glPushMatrix();
            gl.glTranslatef(sprite.getPosition().getX(), sprite.getPosition().getY(), 0f);

            if (supportsOrientation) {
                gl.glRotatef(sprite.getAngle(), 0f, 0f, 1f);
            }
            if (supportsTransparency) {
                float transparency = sprite.getTransparency();
                gl.glColor4f(transparency, transparency, transparency, transparency);
            }
            if (supportsAnimation) {
                setAnimationIndex(gl, sprite.getAnimationIndex());
            }
            if (supportsScaling) {
                float scale = sprite.getScale();
                gl.glScalef(scale, scale, scale);
            }

            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
            gl.glPopMatrix();
        }
    }

    /**
     * Loads the SpriteRenderer texture from a bitmap resource.
     */
    protected void loadTexture(GL10 gl, Context context) {

        // Load the bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), textureResourceId);

        // Load the texture from the bitmap
        int[] names = new int[1];
        gl.glGenTextures(1, names, 0);
        textureName = names[0];
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

    /**
     * Sets the animation index to render next.
     */
    private void setAnimationIndex(GL10 gl, int i) {
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureCoordinatesBuffers[i]);
    }
}
