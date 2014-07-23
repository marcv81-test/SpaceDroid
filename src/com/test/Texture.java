package com.test;

import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Texture {

	protected int names[] = new int[1];
	protected final FloatBuffer[] coordinatesBuffers;

	// Get texture name
	public int getName() {
		return names[0];
	}

	// Get texture coordinates buffer
	// Different coordinates buffers store different sprite animations
	public FloatBuffer getCoordinatesBuffer(int i) {
		return coordinatesBuffers[i];
	}

	// Constructor
	// x and y represent the number of sprite animations on each axis
	public Texture(int x, int y) {

		// Create texture coordinates buffers
		coordinatesBuffers = new FloatBuffer[x * y];
		for (int j = 0; j < y; j++) {
			for (int i = 0; i < x; i++) {
				int index = i + (j * x);
				// Top left, bottom left, top right, bottom right
				float coordinates[] = { i / (float) x, (j + 1) / (float) y,
						i / (float) x, j / (float) y, (i + 1) / (float) x,
						(j + 1) / (float) y, (i + 1) / (float) x, j / (float) y };
				ByteBuffer bb = ByteBuffer
						.allocateDirect(coordinates.length * 4);
				bb.order(ByteOrder.nativeOrder());
				coordinatesBuffers[index] = bb.asFloatBuffer();
				coordinatesBuffers[index].put(coordinates);
				coordinatesBuffers[index].position(0);
			}
		}
	}

	// Create the texture name and load it with a bitmap from a resource
	public void load(GL10 gl, Context context, int resourceId) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				resourceId);
		loadBitmap(gl, bitmap);
		bitmap.recycle();
	}

	// Create the texture name and load it with a bitmap
	public void loadBitmap(GL10 gl, Bitmap bitmap) {
		gl.glGenTextures(1, names, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, names[0]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	}
}
