package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Player;
import com.tint.specular.states.UpgradeState;

public class Swarm extends PowerUp {

	private static AtlasRegion tex;
	private static Texture levelTex;
	private static float timeToStack;
	private static float effect = 2;
	private int stacks;
	
	public Swarm(float x, float y, GameState gs) {
		super(x, y, gs, 800);
	}
	
	public Swarm() {
		super();
	}
	
	public static void init(TextureAtlas ta) {
		tex = ta.findRegion("game1/Swarm");
//		effect = Specular.prefs.getFloat("Swarm Effect");
	}
	
	public static void reloadLevelTextures(float grade) {
		levelTex = UpgradeState.getUpgradeLevelTexture(grade);
	}
	
	@Override
	protected void affect(Player player) {
		player.setFireRate((float) (10 * Math.pow(2 / 3f, effect)));
		player.setBulletBurst(5);
		
		Bullet.setTwist(true);
		Bullet.setDamage(0.4f);
		
		for(PowerUp pu : gs.getPowerUps())
			if(pu instanceof LaserPowerup)
				pu.pause();
		
		if(stacks > 0) {
			activeTime -= timeToStack;
		}
		timeToStack = maxActiveTime;
		stacks++;
	}
	
	@Override
	protected void updatePowerup(Player player) {
		timeToStack = activeTime < 0 ? maxActiveTime : maxActiveTime - activeTime;
	}

	@Override
	public void removeEffect(Player player) {
		for(PowerUp pu : gs.getPowerUps())
			if(pu instanceof LaserPowerup)
				pu.resume();
		
		if(stacks <= 1) {
			player.setFireRate(10f);
			player.setBulletBurstLevel(0);
			Bullet.setTwist(false);
			Bullet.setDamage(1);
		}
		stacks--;
	}
	
	public static void setEffect(float effect) { Swarm.effect = effect; }
	public static float getEffect() { return effect; }

	@Override
	public AtlasRegion getTexture() {
		return tex;
	}

	@Override
	public Texture getLevelTexture() {
		return levelTex;
	}
}
