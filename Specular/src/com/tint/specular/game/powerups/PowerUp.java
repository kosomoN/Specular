package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;

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
	
	public PowerUp(float x, float y, GameState gs, float activeTime) {
		this.x = x;
		this.y = y;
		this.gs = gs;
		this.activeTime = activeTime;
	}
	
	@Override
	public boolean update() {
		if(!activated) {
			despawnTime--;
			if((getX() - gs.getPlayer().getX()) * (getX() - gs.getPlayer().getX()) + (getY() - gs.getPlayer().getY()) * (getY() - gs.getPlayer().getY())
					< (Player.getRadius() + getRadius()) * (Player.getRadius() + getRadius())) {
				affect(gs.getPlayer());
				activated = true;
				
				// Pushing enemies away when picked up
				for(Enemy e : gs.getEnemies()) {
					float angle = (float) Math.atan2(y - e.getY(), x - e.getX());
					float distance = (float) Math.sqrt((x - e.getX()) * (x - e.getX()) + (y - e.getY()) * (y - e.getY()));
					float pushDx = (float) Math.cos(angle) * (1 - distance / 2896 * 0.2f);
					float pushDy = (float) Math.sin(angle) * (1 - distance / 2896 * 0.2f);
					
					e.push(-pushDx, -pushDy);
				}
				
			}
			return despawnTime <= 0;
		} else {
			if(activeTime > 0)
				activeTime--;
			else
				return true;
			
			return false;
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if(!isActivated() && despawnTime > 0)	
			batch.draw(getTexture(), x - getTexture().getWidth() / 2, y - getTexture().getHeight() / 2);
	}
	
	protected abstract void affect(Player p);

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() { return x; }
	public float getY() { return y; }
	public boolean isActivated() { return activated; }
	public abstract Texture getTexture();

	@Override
	public void dispose() {
		getTexture().dispose();
	}

	public float getRadius() {
		return getTexture().getWidth() / 2;
	}
}
