package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Player;

public class Ricochet extends PowerUp {

	private static AtlasRegion texture;
	
	public Ricochet(float x, float y, GameState gs) {
		super(x, y, gs, 1000);
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/Ricochet");
	}

	@Override
	protected void affect(Player p) {
		Bullet.maxBounces += 1;
	}
	
	@Override
	public void removeEffect(Player p) {
		Bullet.maxBounces -= 1;
	}

	@Override
	public AtlasRegion getTexture() {
		return texture;
	}
}
