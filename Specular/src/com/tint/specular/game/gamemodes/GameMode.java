package com.tint.specular.game.gamemodes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;

public abstract class GameMode {

	protected boolean gameOver;
	protected GameState gs;
	
	public GameMode(GameState gs) {
		this.gs = gs;
	}
	
	public abstract void render(SpriteBatch batch);
	public abstract void update(float delta);
	public abstract void enemyKilled(Enemy e);
	public abstract void playerKilled();
	public abstract boolean isGameOver();
}
