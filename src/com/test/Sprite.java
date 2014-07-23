package com.test;

import javax.microedition.khronos.opengles.GL10;
import java.nio.FloatBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class Sprite {

	protected static final int COORDINATES_PER_VERTEX = 3;

	protected final Texture texture;
	protected final FloatBuffer verticesBuffer;

	protected float x, y, z, angle;
	protected int animation = 0;

	// Set sprite x and y coordinates
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	// Set sprite angle
	public void setAngle(float angle) {
		this.angle = angle;
	}

	// Set sprite animation
	public void setAnimation(int animation) {
		this.animation = animation;
	}

	// Constructor
	// There is a single scale parameter because all the sprites are square
	public Sprite(float x, float y, float z, float angle, float scale,
			Texture texture) {

		// Set sprite location and orientation
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = angle;

		// Create vertices buffer according to scale
		// Bottom left, top left, bottom right, top right
		float vertices[] = new float[] { -0.5f * scale, -0.5f * scale, 0f,
				-0.5f * scale, 0.5f * scale, 0f, 0.5f * scale, -0.5f * scale,
				0f, 0.5f * scale, 0.5f * scale, 0f };
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
		bb.order(ByteOrder.nativeOrder());
		verticesBuffer = bb.asFloatBuffer();
		verticesBuffer.put(vertices);
		verticesBuffer.position(0);

		// Assign texture
		this.texture = texture;
	}

	// Draw the sprite
	public void draw(GL10 gl) {
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
