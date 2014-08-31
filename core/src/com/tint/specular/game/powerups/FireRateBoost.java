package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.states.UpgradeState;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class FireRateBoost extends PowerUp {
	private static AtlasRegion texture;
	private static AtlasRegion levelTex;
	private static float boost = 2 / 3f;
	public static int stacks;
	
	public FireRateBoost(float x, float y, GameState gs) {
		super(x, y, gs, 800);
	}
	
	public FireRateBoost() {
		super();
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/FireRate");
		boost = Specular.prefs.getFloat("Firerate Boost");
	}
	
	public static void reloadLevelTextures(float grade) {
		levelTex = UpgradeState.getUpgradeLevelTexture(grade);
	}
	
	@Override
	protected void affect(Player player) {
		stacks++;
		float firerate = (float) (10 * Math.pow(boost, stacks));
		if(firerate <= gs.getPlayer().getFireRate())
			player.setFireRate(firerate);
	}
	
	@Override
	public void removeEffect(Player player) {
		stacks--;
		float firerate = (float) (10 * Math.pow(boost, stacks));
		for(PowerUp pu : gs.getPowerUps())
			if(pu instanceof Swarm)
				if(firerate > gs.getPlayer().getFireRate())
					return;
		player.setFireRate(firerate);
	}
	
	public static void setBoost(float boost) { FireRateBoost.boost = boost; }
	public static float getBoost() { return boost; }
	
	@Override
	public AtlasRegion getTexture() {
		return texture;
	}

	@Override
	public AtlasRegion getLevelTexture() {
		return levelTex;
	}
}
