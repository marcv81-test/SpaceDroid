package marcv81.gfx2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

public class Texture {

    private static final int COORDINATES_PER_VERTEX = 3;

    private final FloatBuffer verticesBuffer;
    private final FloatBuffer[] textureCoordsBuffers;

    private final int resourceId;
    private int textureName;

    // Texture display capabilities
    private final boolean supportAnimation;
    private final boolean supportAngle;
    private final boolean supportTransparency;

    // Constructor
    public Texture(int resourceId, float size, int animationsX, int animationsY,
                   boolean supportAngle, boolean supportTransparency) {

        // Create vertices buffer according to the sprite size
        // Bottom left, top left, bottom right, top right
        float vertices[] = new float[]{
                -0.5f * size, -0.5f * size, 0f,
                -0.5f * size, 0.5f * size, 0f,
                0.5f * size, -0.5f * size, 0f,
                0.5f * size, 0.5f * size, 0f};
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);

        // Create texture coordinates buffers
        // There may be several buffers for different graphics (for sprites animation)
        textureCoordsBuffers = new FloatBuffer[animationsX * animationsY];
        for (int j = 0; j < animationsY; j++) {
            for (int i = 0; i < animationsX; i++) {
                int index = i + (j * animationsX);
                // Top left, bottom left, top right, bottom right
                float coords[] = {
                        i / (float) animationsX, (j + 1) / (float) animationsY,
                        i / (float) animationsX, j / (float) animationsY,
                        (i + 1) / (float) animationsX, (j + 1) / (float) animationsY,
                        (i + 1) / (float) animationsX, j / (float) animationsY};
                ByteBuffer tbb = ByteBuffer.allocateDirect(coords.length * 4);
                tbb.order(ByteOrder.nativeOrder());
                textureCoordsBuffers[index] = tbb.asFloatBuffer();
                textureCoordsBuffers[index].put(coords);
                textureCoordsBuffers[index].position(0);
            }
        }

        // Resource ID to load the texture bitmap
        this.resourceId = resourceId;

        // Define the texture display capabilities
        this.supportAnimation = (animationsX > 1) || (animationsY > 1);
        this.supportAngle = supportAngle;
        this.supportTransparency = supportTransparency;
    }

    // Load the texture from the bitmap resource
    public void load(GL10 gl, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        int names[] = new int[1];
        gl.glGenTextures(1, names, 0);
        textureName = names[0];
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

    // Draw sprites using the texture
    public void draw(GL10 gl, List<? extends Sprite> sprites) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName);
        gl.glFrontFace(GL10.GL_CW);
        if (!supportTransparency) {
            gl.glColor4f(1f, 1f, 1f, 1f);
        }
        if (!supportAnimation) {
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureCoordsBuffers[0]);
        }
        for (Sprite sprite : sprites) {
            gl.glPushMatrix();
            gl.glTranslatef(sprite.getX(), sprite.getY(), sprite.getZ());
            if (supportAngle) {
                gl.glRotatef(sprite.getAngle(), 0f, 0f, 1f);
            }
            if (supportTransparency) {
                float transparency = sprite.getTransparency();
                gl.glColor4f(transparency, transparency, transparency, transparency);
            }
            if (supportAnimation) {
                gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureCoordsBuffers[sprite.getAnimation()]);
            }
            gl.glVertexPointer(COORDINATES_PER_VERTEX, GL10.GL_FLOAT, 0, verticesBuffer);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
            gl.glPopMatrix();
        }
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
