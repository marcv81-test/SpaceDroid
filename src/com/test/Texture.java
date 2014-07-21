package com.test;

import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

class Texture
{
    private FloatBuffer textureBuffer;  // buffer holding the texture coordinates
    private int[] textures = new int[1];

    public int getTexture() { return textures[0]; }
    public FloatBuffer getTextureBuffer() { return textureBuffer; }

    private float texture[] =
    {
        // Mapping coordinates for the vertices
        0.0f, 1.0f,     // top left     (V2)
        0.0f, 0.0f,     // bottom left  (V1)
        1.0f, 1.0f,     // top right    (V4)
        1.0f, 0.0f      // bottom right (V3)
    };

    public Texture()
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(texture.length * 4);
        bb.order(ByteOrder.nativeOrder());
        textureBuffer = bb.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
    }

    public void load(GL10 gl, Context context, int id)
    {
        // loading texture
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);

        // generate one texture pointer
        gl.glGenTextures(1, textures, 0);
        // ...and bind it to our array
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

        // create nearest filtered texture
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

        // Clean up
        bitmap.recycle();
    }
}
