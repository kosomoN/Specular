package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class SlowdownEnemies extends PowerUp {
private static Texture texture;
	
	public SlowdownEnemies(float x, float y, GameState gs) {
		super(x, y, gs, 300);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/Slowdown.png"));
	}
	
	@Override
	protected void affect(Player player) {
		for(Enemy e : gs.getEnemies())
			e.setSlowdown(0.5f);
	}

	@Override
	public boolean update() {
		if(super.update())
			for(Enemy e : gs.getEnemies())
				e.setSlowdown(0f);
		else
			return despawnTime <= 0;
		
		return true;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
}
