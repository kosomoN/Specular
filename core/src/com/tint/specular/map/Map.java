package com.tint.specular.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.tint.specular.Specular;
import com.tint.specular.effects.TrailPart;
import com.tint.specular.game.Camera;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Bullet;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class Map {
	private static ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	private int width, height;
	private Texture texture, shockLight, parallax;
	private String name;
	private GameState gs;
	
	
	public Map(Texture texture, Texture shockLight, Texture parallax, String name, int width, int height, GameState gs) {
		this.name = name;
		this.texture = texture;
		this.parallax = parallax;
		this.shockLight = shockLight;
		this.width = width;
		this.height = height;
		this.gs = gs;
	}
	
	public Map() {
		
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(texture, 0, 0, width, height);
		batch.end();
		
		Gdx.gl.glDepthFunc(GL20.GL_LESS);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthMask(true);
		Gdx.gl.glColorMask(false, false, false, false);
		
		shapeRenderer.setProjectionMatrix(Specular.camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		for(Bullet b : gs.getBullets()) {
			shapeRenderer.circle(b.getX(), b.getY(), 60);
		}
		
		for(TrailPart tp : gs.getPlayer().getTrail())
			shapeRenderer.circle(tp.getX(), tp.getY(), tp.getSize());
		
		shapeRenderer.end();
		
		Gdx.gl.glColorMask(true, true, true, true);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
		
		batch.begin();
		batch.draw(shockLight, 0, 0, width, height);
		batch.flush();
		
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
	}

	public int getWidth() { return width; }
	public int getHeight() { return height;	}
	public Texture getTexture() { return texture; }
	public String getName() { return name; }
	public void setTexture(Texture texture) { this.texture = texture; }
	public Texture getParallax() { return parallax;	}}
