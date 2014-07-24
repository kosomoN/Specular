package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Player;
import com.tint.specular.states.UpgradeState;

public class Ricochet extends PowerUp {

	private static AtlasRegion texture;
	private static Texture levelTex;
	private static int addBounces = 1;
	
	public Ricochet(float x, float y, GameState gs) {
		super(x, y, gs, 1000);
	}
	
	public Ricochet() {
		super();
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/Ricochet");
	}
	
	public static void reloadLevelTextures(float grade) {
		levelTex = UpgradeState.getUpgradeLevelTexture(grade);
	}

	@Override
	protected void affect(Player p) {
		Bullet.maxBounces += addBounces;
	}
	
	@Override
	public void removeEffect(Player p) {
		Bullet.maxBounces -= addBounces;
	}

	public static void setAddBounces(int addBounces) {
		Ricochet.addBounces = addBounces;
	}
	
	@Override
	public AtlasRegion getTexture() {
		return texture;
	}

	@Override
	public Texture getLevelTexture() {
		return levelTex;
	}
}
