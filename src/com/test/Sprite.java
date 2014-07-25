package com.test;

import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class Sprite {

	protected static final int COORDINATES_PER_VERTEX = 3;

	protected final Texture texture;
	protected final FloatBuffer verticesBuffer;

	// Constructor
	public Sprite(Texture texture, float scaleX, float scaleY) {

		// Assign texture
		this.texture = texture;

		// Create vertices buffer according to scale
		// Bottom left, top left, bottom right, top right
		float vertices[] = new float[] { -0.5f * scaleX, -0.5f * scaleY, 0f,
				-0.5f * scaleX, 0.5f * scaleY, 0f, 0.5f * scaleX,
				-0.5f * scaleY, 0f, 0.5f * scaleX, 0.5f * scaleY, 0f };
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
		bb.order(ByteOrder.nativeOrder());
		verticesBuffer = bb.asFloatBuffer();
		verticesBuffer.put(vertices);
		verticesBuffer.position(0);
	}

	// Draw the sprite
	public void draw(GL10 gl, float x, float y, float z, float angle,
			int animation) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glPushMatrix();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.getName());
		gl.glTranslatef(x, y, z);
		gl.glRotatef(angle, 0f, 0f, 1f);
		gl.glFrontFace(GL10.GL_CW);
		gl.glVertexPointer(COORDINATES_PER_VERTEX, GL10.GL_FLOAT, 0,
				verticesBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0,
				texture.getCoordinatesBuffer(animation));
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
