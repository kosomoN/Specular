package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.Player.AmmoType;
import com.tint.specular.states.UpgradeState;

public class LaserPowerup extends PowerUp {

	private static AtlasRegion tex;
	private static AtlasRegion levelTex;
	
	public LaserPowerup(float x, float y, GameState gs) {
		super(x, y, gs, 800);
	}
	
	public LaserPowerup() {
		super();
	}
	
	public static void init(TextureAtlas ta) {
		tex = ta.findRegion("game1/Laser Powerup");
	}
	
	public static void reloadLevelTextures(float grade) {
		levelTex = UpgradeState.getUpgradeLevelTexture(grade);
	}

	@Override
	protected void affect(Player player) {
		player.changeAmmo(AmmoType.LASER);
	}

	@Override
	public void removeEffect(Player player) {
		player.changeAmmo(AmmoType.BULLET);
	}
	
	@Override
	public void pause() {
		super.pause();
		gs.getPlayer().changeAmmo(AmmoType.BULLET);
	}

	@Override
	public void resume() {
		super.resume();
		gs.getPlayer().changeAmmo(AmmoType.LASER);
	}

	@Override
	public AtlasRegion getTexture() {
		return tex;
	}

	@Override
	public AtlasRegion getLevelTexture() {
		return levelTex;
	}
}
