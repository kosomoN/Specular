package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public abstract class PowerUp implements Entity {
	
	protected GameState gs;
	protected float x, y;
	protected float despawnTime = 900; // 15s
	protected float activeTime;
	private boolean activated;
	
	public PowerUp(float x, float y, GameState gs) {
		this.x = x;
		this.y = y;
		this.gs = gs;
	}
	
	@Override
	public boolean update() {
		if(!activated) {
			despawnTime--;
			if((getCenterX() - gs.getPlayer().getCenterX()) * (getCenterX() - gs.getPlayer().getCenterX()) + (getCenterY() - gs.getPlayer().getCenterY()) * (getCenterY() - gs.getPlayer().getCenterY())
					< (Player.getRadius() + getRadius()) * (Player.getRadius() + getRadius())) {
				affect(gs.getPlayer());
				activated = true;
			}
			return false;
		} else {
			if(activeTime > 0)
				activeTime--;
			else if(activeTime <= 0)
				return true;
			
			return false;
		}
	}
	
	protected abstract void affect(Player p);

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getCenterX() { return x; }
	public float getCenterY() {	return y; }
	public abstract float getRadius();
	public abstract Texture getTexture();
}
