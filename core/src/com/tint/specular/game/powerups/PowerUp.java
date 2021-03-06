package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.ShockWaveRenderer;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public abstract class PowerUp implements Entity {
	private static final int PUSHAWAY_TIME = 160, PUSHAWAY_RANGE_SQUARED = 500 * 500;
	private static Sound shockwaveSound = Gdx.audio.newSound(Gdx.files.internal("audio/fx/Shockwave.ogg"));
	
	protected GameState gs;
	protected float x, y;
	private float despawnTime = 900; // 15s
	protected float activeTime;
	private boolean activated, isPaused, hasRemovedEffect;
	protected float maxActiveTime;
	
	public PowerUp(float x, float y, GameState gs, float maxActiveTime) {
		this.x = x;
		this.y = y;
		this.gs = gs;
		this.maxActiveTime = maxActiveTime;
	}
	
	public PowerUp() { }
	
	@Override
	public boolean update() {
		if(!isPaused) {
			if(!activated) {
				despawnTime--;
				if((getX() - gs.getPlayer().getX()) * (getX() - gs.getPlayer().getX()) + (getY() - gs.getPlayer().getY()) * (getY() - gs.getPlayer().getY())
						< (Player.getRadius() + getRadius()) * (Player.getRadius() + getRadius())) {
					affect(gs.getPlayer());
					activated = true;
				}
				return gs.tutorialHasEnded() && despawnTime <= 0;
			} else {
				if(activeTime >= maxActiveTime) {
					if(!hasRemovedEffect) {
						removeEffect(gs.getPlayer());
						hasRemovedEffect = true;
					}
					if(activeTime >= PUSHAWAY_TIME)
						return true;
				}
				activeTime++;
				
				if(activeTime < maxActiveTime)
					updatePowerup(gs.getPlayer());
				
				if(activeTime < PUSHAWAY_TIME) {
					float distanceSquared;
					double angle;
					for(Enemy e : gs.getEnemies()) {
						distanceSquared = (e.getX() - getX()) * (e.getX() - getX()) + (e.getY() - getY()) * (e.getY() - getY());
						if(PUSHAWAY_RANGE_SQUARED > distanceSquared) {
							angle = Math.atan2(e.getY() - getY(), e.getX() - getX());
							//Math.cos(angle) * distance (0-1) to make it push less if the enemy is far away * time^2 to make it smoothly disappear
							e.setX((float) (e.getX() + Math.cos(angle) * 10 * (1 - distanceSquared / PUSHAWAY_RANGE_SQUARED) * (1 - (activeTime / PUSHAWAY_TIME) * (activeTime / PUSHAWAY_TIME))));
							e.setY((float) (e.getY() + Math.sin(angle) * 10 * (1 - distanceSquared / PUSHAWAY_RANGE_SQUARED) * (1 - (activeTime / PUSHAWAY_TIME) * (activeTime / PUSHAWAY_TIME))));
						}
					}
				}
			}
		}
		
		return false;
	}
	
	protected void updatePowerup(Player player) {}
	protected void affect(Player player) {}
	public void removeEffect(Player player) {}
	
	@Override
	public void render(SpriteBatch batch) {
		if(!isActivated() && (despawnTime > 0 || !gs.tutorialHasEnded())) {
			batch.draw(getTexture(), x - getTexture().getRegionWidth() / 2, y - getTexture().getRegionHeight() / 2);
			if(getLevelTexture() != null)
				batch.draw(getLevelTexture(), x - getTexture().getRegionWidth() / 2, y - getTexture().getRegionHeight() / 2);
		}
		if(gs.isSoundEnabled() && activated && activeTime == 0) {
			shockwaveSound.play();
		}
		
		if(activated && activeTime < 20) {
			ShockWaveRenderer.renderShockwave(batch, x, y, activeTime / 20, false);
		}
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void pause() { isPaused = true;	}
	public void resume() { isPaused = false; }
	
	public float getX() { return x; }
	public float getY() { return y; }
	public boolean isPaused() { return isPaused; }
	public boolean isActivated() { return activated; }
	public abstract AtlasRegion getTexture();
	public abstract AtlasRegion getLevelTexture();

	@Override
	public void dispose() {

	}

	public float getRadius() {
		return getTexture().getRegionWidth() / 2;
	}

	public float getDespawnTime() {
		return despawnTime;
	}
}