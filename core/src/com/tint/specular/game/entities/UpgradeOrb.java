package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool.Poolable;
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
	private static Texture tex;
	
	public UpgradeOrb(GameState gs) {
		this.gs = gs;
		value = (float) (Math.random() * 20);
	}
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/unpacked/game1/Orb.png"));
	}

	@Override
	public boolean update() {
		// Lifetime decrease
		lifetime--;
		
		if((gs.getPlayer().getX() - x) * (gs.getPlayer().getX() - x) + (gs.getPlayer().getY() - y) * (gs.getPlayer().getY() - y) <= 
				(Player.getRadius() + tex.getWidth()) * (Player.getRadius() + tex.getWidth())) {
			gs.getPlayer().addUpgradePoints(0.01f);
			return true;
		}
		
		// Movement
		x += dx;
		y += dy;
		dx *= 0.97f;
		dy *= 0.97f;
		
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
		float size = Math.min((lifetime / 160f) * (lifetime / 160f), 1);
		batch.setColor(1, 1, 1, size);
		Util.drawCentered(batch, tex, x, y, size, 0);
		batch.setColor(1, 1, 1, 1);
	}

	public float getValue() {
		return value;
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
		dx = (float) (cos * Math.random() * 5);
		dy = (float) (sin * Math.random() * 5);
		
		// Adding speed the enemy had
		dx += initialDx;
		dy += initialDy;
		
		lifetime = (float) (300 + Math.random() * 100);
	}

	@Override
	public void reset() {}
}
