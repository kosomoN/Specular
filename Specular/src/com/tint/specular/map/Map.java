package com.tint.specular.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map {
	
	private int width, height;
	private Texture texture;
	private String name;
	
	public Map(Texture texture, String name, int width, int height) {
		this.name = name;
		this.texture = texture;
		this.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.width = width;
		this.height = height;
	}
	
	public void render(SpriteBatch batch) {
		batch.begin();
		batch.draw(texture, 0, 0, width, height);
		batch.end();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Texture getTexture() {
		return texture;
	}
	
	public String getName() {
		return name;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
