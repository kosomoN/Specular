package com.tint.specular.game.gamemodes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;

public class BossBattle extends GameMode {

	public BossBattle(GameState gs) {
		super(gs);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		
	}
	
	@Override
	public void update(float delta) {
		
	}

	@Override
	public void enemyKilled(Enemy e) {
		
	}

	@Override
	public void playerKilled() {
		
	}

	@Override
	public boolean isGameOver() {
		return gameOver;
	}

}
