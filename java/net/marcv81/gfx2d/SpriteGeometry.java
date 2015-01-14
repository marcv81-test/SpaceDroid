package net.marcv81.gfx2d;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class SpriteGeometry {

    private static final int COORDINATES_PER_VERTEX = 3;

    private final FloatBuffer verticesBuffer;

    public SpriteGeometry(float size) {

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
    }

    public void setVertices(GL10 gl) {
        gl.glVertexPointer(COORDINATES_PER_VERTEX, GL10.GL_FLOAT, 0, verticesBuffer);
    }
}
