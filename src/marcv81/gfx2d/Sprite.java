package marcv81.gfx2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Sprite {

    private static final int COORDINATES_PER_VERTEX = 3;

    private final FloatBuffer verticesBuffer;
    private final FloatBuffer[] textureCoordsBuffers;

    private final int resourceId;

    private int textureName;

    // Constructor
    public Sprite(int resourceId, int gfxX, int gfxY, float size) {

        // Create vertices buffer according to the sprite size
        // Bottom left, top left, bottom right, top right
        float vertices[] = new float[]{-0.5f * size, -0.5f * size, 0f,
                -0.5f * size, 0.5f * size, 0f, 0.5f * size, -0.5f * size, 0f,
                0.5f * size, 0.5f * size, 0f};
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        // Create texture coordinates buffers
        // There may be several buffers for different graphics (for sprites
        // animation or background tiles)
        textureCoordsBuffers = new FloatBuffer[gfxX * gfxY];
        for (int j = 0; j < gfxY; j++) {
            for (int i = 0; i < gfxX; i++) {
                int index = i + (j * gfxX);
                // Top left, bottom left, top right, bottom right
                float coords[] = {i / (float) gfxX, (j + 1) / (float) gfxY,
                        i / (float) gfxX, j / (float) gfxY,
                        (i + 1) / (float) gfxX, (j + 1) / (float) gfxY,
                        (i + 1) / (float) gfxX, j / (float) gfxY};
                ByteBuffer tbb = ByteBuffer.allocateDirect(coords.length * 4);
                tbb.order(ByteOrder.nativeOrder());
                textureCoordsBuffers[index] = tbb.asFloatBuffer();
                textureCoordsBuffers[index].put(coords);
                textureCoordsBuffers[index].position(0);
            }
        }

        // Resource ID to load the texture bitmap
        this.resourceId = resourceId;
    }

    // Load the texture from the bitmap resource
    public void loadTexture(GL10 gl, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                resourceId);
        int names[] = new int[1];
        gl.glGenTextures(1, names, 0);
        textureName = names[0];
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

    // Draw the sprite
    public void draw(GL10 gl, float x, float y, float z, float angle,
                     int gfxId, float transparency) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glPushMatrix();
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
        gl.glTranslatef(x, y, z);
        gl.glRotatef(angle, 0f, 0f, 1f);
        gl.glFrontFace(GL10.GL_CW);
        gl.glColor4f(transparency, transparency, transparency, transparency);
        gl.glVertexPointer(COORDINATES_PER_VERTEX, GL10.GL_FLOAT, 0,
                verticesBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureCoordsBuffers[gfxId]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glPopMatrix();
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
