package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Player;

public class Swarm extends PowerUp {

	private static Texture tex;
	private static float timeToStack;
	private int stacks;
	
	public Swarm(float x, float y, GameState gs) {
		super(x, y, gs, 800);
	}
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/powerups/Swarm.png"));
	}
	
	@Override
	protected void affect(Player player) {
		player.setFireRate((float) (10 * Math.pow(2 / 3f, 2)));
		player.setBulletBurst(5);
		
		Bullet.setTwist(true);
		Bullet.setDamage(0.4f);
		
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
	protected void removeEffect(Player player) {
		if(stacks <= 1) {
			player.setFireRate(10f);
			player.setBulletBurstLevel(0);
			Bullet.setTwist(false);
			Bullet.setDamage(1);
		}
		stacks--;
	}

	@Override
	public Texture getTexture() {
		return tex;
	}
}
