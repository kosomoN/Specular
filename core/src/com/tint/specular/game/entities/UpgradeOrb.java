package com.tint.specular.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.tint.specular.Specular;
import com.tint.specular.game.Camera;
import com.tint.specular.game.GameState;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class UpgradeOrb implements Entity, Poolable {

	private GameState gs;
	private float x, y, dx, dy;
	private float value;
	private float lifetime;
	private static AtlasRegion tex;
	
	public UpgradeOrb(GameState gs) {
		this.gs = gs;
		value = (float) (Math.random() * 20);
	}
	
	public static void init(TextureAtlas ta) {
		tex = ta.findRegion("game1/Orb");
	}

	@Override
	public boolean update() {
		// Lifetime decrease
		lifetime--;
		float dist = (gs.getPlayer().getX() - x) * (gs.getPlayer().getX() - x) + (gs.getPlayer().getY() - y) * (gs.getPlayer().getY() - y);
		float radiusDist = (Player.getRadius() + tex.getRegionWidth()) * (Player.getRadius() + tex.getRegionWidth());
		if(dist <= radiusDist * 20) {
			
			//Calculating angle and force
			double angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
			dx += (float) (Math.cos(angle) * 1.4f);
			dy += (float) (Math.sin(angle) * 1.4f);
			
			if(dist <= radiusDist) {
				gs.getPlayer().addUpgradePoints(1);
				return true;
			}
		}
		
		// Movement
		x += dx;
		y += dy;
		dx *= 0.93f;
		dy *= 0.93f;
		
		// Bounce off borders, * 0.9f is for the amount of force the border absorbs
		if(x - 20 - 18 < 0) {
			dx = -dx * 0.9f;
			dy *= 0.9f;
		} else if(x + 20 + 18 > gs.getCurrentMap().getHeight()){
			dx = -dx * 0.9f;
			dy *= 0.9f;
		}
		
		if(y - 20 - 18 < 0) {
			dx *= 0.9f;
			dy = -dy * 0.9f;
		} else if(y + 20 + 18 > gs.getCurrentMap().getHeight()){
			dx *= 0.9f;
			dy = -dy * 0.9f;
		}
		
		return lifetime <= 0;
	}

	@Override
	public void render(SpriteBatch batch) {
		// Checking if on screen to increase performance
		if(Camera.getCameraX() - Specular.camera.viewportWidth / 2 * Camera.getZoom() - 100 < x &&
				Camera.getCameraX() + Specular.camera.viewportWidth / 2 * Camera.getZoom() + 100 > x &&
				Camera.getCameraY() - Specular.camera.viewportHeight / 2 * Camera.getZoom() - 100 < y &&
				Camera.getCameraY() + Specular.camera.viewportHeight / 2 * Camera.getZoom() + 100 > y) {
			
			float size = Math.min((lifetime / 50f) * (lifetime / 50f), 1);
			batch.setColor(1, 1, 1, size);
			Util.drawCentered(batch, tex, x, y, 0);
			batch.setColor(1, 1, 1, 1);
		}
	}

	public float getValue() {
		return value;
	}
	
	public float getLifetime() {
		return lifetime;
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}
	
	public void reUse(float x, float y, float direction, float initialDx, float initialDy) {
		this.x = x;
		this.y = y;
		
		float cos = (float) Math.cos(Math.toRadians(direction));
		float sin = (float) Math.sin(Math.toRadians(direction));
		dx = (float) (cos * (Math.random() * 2 + 1));
		dy = (float) (sin * (Math.random() * 2 + 1));
		
		// Adding speed the enemy had
		dx += initialDx;
		dy += initialDy;
		
		lifetime = (float) (250 + Math.random() * 50);
	}

	@Override
	public void reset() {}
}