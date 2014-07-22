package com.test;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class GfxBlock {

    static final int COORDS_PER_VERTEX = 3;

    private final FloatBuffer vertexBuffer;
    private final float vertices[];
    private Texture texture;

    public GfxBlock(float size, float depth, Texture texture)
    {
        vertices = new float[]
        {
            -0.5f*size, -0.5f*size, depth, // bottom left
            -0.5f*size,  0.5f*size, depth, // top left
             0.5f*size, -0.5f*size, depth, // bottom right
             0.5f*size,  0.5f*size, depth  // top right
        };
        
        this.texture = texture;

        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    public void draw(GL10 gl, float x, float y, float angle)
    {
        gl.glPushMatrix();

        // bind the previously generated texture
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getTexture());

        // Point to our buffers
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glTranslatef(x, y, 0f);

        if (angle != 0f)
            gl.glRotatef(angle, 0f, 0f, 1f);

        // Set the face rotation
        gl.glFrontFace(GL10.GL_CW);

        // Point to our vertex buffer
        gl.glVertexPointer(COORDS_PER_VERTEX, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.getTextureBuffer());

        // Draw the vertices as triangle strip
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glPopMatrix();
    }
}
