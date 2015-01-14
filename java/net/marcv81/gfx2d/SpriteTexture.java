package net.marcv81.gfx2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class SpriteTexture {

    private final int resourceId;

    private final FloatBuffer[] textureCoordinatesBuffers;
    private final int totalAnimations;

    private int textureName;

    // Constructor
    public SpriteTexture(int resourceId, int animationsX, int animationsY) {

        // Create texture coordinates buffers
        // There may be several buffers for different graphics (for sprite animation)
        totalAnimations = animationsX * animationsY;
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

        // Resource ID to load the texture bitmap
        this.resourceId = resourceId;
    }

    // Load the texture from the bitmap resource
    public void load(GL10 gl, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        int[] names = new int[1];
        gl.glGenTextures(1, names, 0);
        textureName = names[0];
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

    // Bind the texture
    public void bind(GL10 gl) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
    }

    // Set the current animation
    public void setAnimation(GL10 gl, int i) {
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureCoordinatesBuffers[i]);
    }

    // Return the total number of animations
    public int getTotalAnimations() {
        return totalAnimations;
    }
}
