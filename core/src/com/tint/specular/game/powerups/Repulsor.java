package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.ShockWaveRenderer;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;


public class Repulsor extends PowerUp {
	private static final float PUSHAWAY_RANGE = 500 * 500;
	private static float maxActiveTime = 800;
	private static AtlasRegion texture;
	private static Texture levelTex;
	
	public Repulsor(float x, float y, GameState gs) {
		super(x, y, gs, maxActiveTime);
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/Repulsor");
//		maxActiveTime = Specular.prefs.getFloat("Repulsor Max Time");
	}
	
	public static void reloadLevelTextures() {
		int grade = Specular.prefs.getInteger("Repulsor Upgrade Grade");
		
		if(grade > 10) { // Infinity
			levelTex = new Texture(Gdx.files.internal("graphics/game/powerups/level inf.png"));
		} else if(grade == 10) { // Max
			levelTex = new Texture(Gdx.files.internal("graphics/game/powerups/level 5.png"));
		} else if(grade >= 5) {
			levelTex = new Texture(Gdx.files.internal("graphics/game/powerups/level 4.png"));
		} else if(grade >= 3) {
			levelTex = new Texture(Gdx.files.internal("graphics/game/powerups/level 3.png"));
		} else if(grade >= 2) {
			levelTex = new Texture(Gdx.files.internal("graphics/game/powerups/level 2.png"));
		} else if(grade >= 1) {
			levelTex = new Texture(Gdx.files.internal("graphics/game/powerups/level 1.png"));
		}
	}
	
	@Override
	protected void updatePowerup(Player player) {
		float distanceSquared;
		double angle;
		float playerX = player.getX(), playerY = player.getY(), deltaX = 0, deltaY = 0;
		for(Enemy e : gs.getEnemies()) {
			deltaX = e.getX() - playerX;
			deltaY = e.getY() - playerY;
			distanceSquared = deltaX * deltaX + deltaY * deltaY;
			if(PUSHAWAY_RANGE > distanceSquared) {
				angle = Math.atan2(deltaY, deltaX);
				e.setX((float) (e.getX() + Math.cos(angle) * 20 * (1 - distanceSquared / PUSHAWAY_RANGE)));
				e.setY((float) (e.getY() + Math.sin(angle) * 20 * (1 - distanceSquared / PUSHAWAY_RANGE)));
			}
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		if(isActivated()) {
			ShockWaveRenderer.renderShockwave(batch, gs.getPlayer().getX(), gs.getPlayer().getY(), (activeTime / 50f) % 1, true);
		}
	}

	public static void setMaxActiveTime(float maxActiveTime) { Repulsor.maxActiveTime = maxActiveTime; }
	public static float getMaxActiveTime() { return maxActiveTime; }
	
	@Override
	public AtlasRegion getTexture() {
		return texture;
	}

	@Override
	public Texture getLevelTexture() {
		return levelTex;
	}
}
