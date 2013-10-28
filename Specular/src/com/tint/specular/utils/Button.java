package com.tint.specular.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Button {
	private float x, y, width, height;
	private Texture texture;
	
	public Button() {
		x = 0;
		y = 0;
		width = 0;
		height = 0;
	}
	
	public void render(SpriteBatch batch) {
		batch.begin();
		batch.draw(texture, x, y);
		batch.end();
	}
	
	public void update() {
		
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
		width = this.texture.getWidth();
		height = this.texture.getHeight();
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public Texture getTexture() {
		return texture;
	}
}
