package com.tint.specular.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class Map {
	
	//FIELDS
	private int width, height;
	private Texture texture, parallax;
	private String name;
	
	//CONSTRUCTORS
	public Map(Texture texture, Texture parallax, String name, int width, int height) {
		this.name = name;
		this.texture = texture;
		this.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.parallax = parallax;
		this.parallax.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.width = width;
		this.height = height;
	}
	
	public Map() {
		
	}
	
	//RENDER
	public void render(SpriteBatch batch) {
		batch.draw(texture, 0, 0, width, height);
	}

	//GETTERS
	public int getWidth() { return width; }
	public int getHeight() { return height;	}
	public Texture getTexture() { return texture; }
	public String getName() { return name; }
	public void setTexture(Texture texture) { this.texture = texture; }

	public Texture getParallax() {
		return parallax;
	}
}
