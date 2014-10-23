package com.tint.specular.game.gamemodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;

public class Ranked extends GameMode {
	
	public Ranked(GameState gs) {
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
		gs.getPlayer().addScore((int) (e.getValue() * gs.getScoreMultiplier() * 200 + 1));
	}

	@Override
	public void playerKilled() {
		gameOver = true;
		Gdx.input.setInputProcessor(gs.getStage());
	}

	@Override
	public boolean isGameOver() {
		return gameOver;
	}
}
