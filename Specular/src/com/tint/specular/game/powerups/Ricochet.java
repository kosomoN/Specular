package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Player;

public class Ricochet extends PowerUp {

	private static Texture texture;
	
	public Ricochet(float x, float y, GameState gs) {
		super(x, y, gs, 600);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/powerups/Ricochet.png"));
	}

	@Override
	protected void affect(Player p) {
		Bullet.maxBounces += 1;
	}
	
	@Override
	public boolean update() {
		if(super.update()) {
			if(isActivated()) {
				Bullet.maxBounces -= 1;
			}
			return true;
		}
		
		return false;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
}