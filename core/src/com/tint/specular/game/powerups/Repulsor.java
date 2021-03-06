package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.ShockWaveRenderer;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyDasher;
import com.tint.specular.states.UpgradeState;


public class Repulsor extends PowerUp {
	private static final float PUSHAWAY_RANGE = 500 * 500;
	private static float maxActiveTime = 800;
	private static AtlasRegion texture;
	private static AtlasRegion levelTex;
	private static Sound repulsorSound = Gdx.audio.newSound(Gdx.files.internal("audio/fx/RepulsorEffect.ogg"));
	private static int stacks;
	
	public Repulsor(float x, float y, GameState gs) {
		super(x, y, gs, maxActiveTime);
	}
	
	public Repulsor() {
		super();
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/Repulsor");
		maxActiveTime = Specular.prefs.getFloat("Repulsor Max Time");
	}
	
	public static void reloadLevelTextures(float grade) {
		levelTex = UpgradeState.getUpgradeLevelTexture(grade);
	}
	
	@Override
	protected void updatePowerup(Player player) {
		
		float distanceSquared;
		double angle;
		float playerX = player.getX(), playerY = player.getY(), deltaX = 0, deltaY = 0;
		for(Enemy e : gs.getEnemies()) {
			if(!(e instanceof EnemyDasher)){
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
	}
	
	@Override
	protected void affect(Player player) {
		super.affect(player);
		repulsorSound.loop();
		stacks++;
	}

	@Override
	public void removeEffect(Player player) {
		super.removeEffect(player);
		stacks--;
		if(stacks <= 0)
			repulsorSound.stop();
	}

	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		if(isActivated()) {
			ShockWaveRenderer.renderShockwave(batch, gs.getPlayer().getX(), gs.getPlayer().getY(), (activeTime / 50f) % 1, true);
		}
	}

	public static void stopSound() { repulsorSound.stop(); }
	public static void setMaxActiveTime(float maxActiveTime) { Repulsor.maxActiveTime = maxActiveTime; }
	public static float getMaxActiveTime() { return maxActiveTime; }
	
	@Override
	public AtlasRegion getTexture() {
		return texture;
	}

	@Override
	public AtlasRegion getLevelTexture() {
		return levelTex;
	}
}
