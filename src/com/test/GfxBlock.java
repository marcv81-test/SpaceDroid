package com.test;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class GfxBlock {

    static final int COORDS_PER_VERTEX = 3;

    private final FloatBuffer vertexBuffer;
    private final float vertices[];
    private final float color[];

    public GfxBlock(float size, float depth, float red, float green, float blue)
    {
        vertices = new float[]
        {
            -0.5f*size, -0.5f*size, depth, // bottom left
            -0.5f*size,  0.5f*size, depth, // top left
             0.5f*size, -0.5f*size, depth, // bottom right
             0.5f*size,  0.5f*size, depth  // top right
        };
        
        color = new float[] { red, green, blue, 1.0f };
        
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    public void draw(GL10 gl, float x, float y)
    {
        gl.glPushMatrix();
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glTranslatef(x, y, 0f);
        gl.glColor4f(color[0], color[1], color[2], color[3]);
        gl.glVertexPointer(COORDS_PER_VERTEX, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glPopMatrix();
    }
}
