package com.tint.specular.game.gamemodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.enemies.Enemy;

public class TimeAttack extends GameMode {
	
	private long timeLeft; // In milliseconds
	private long time; // In milliseconds
	
	public TimeAttack(GameState gs) {
		super(gs);
		timeLeft = 120000;
	}
	
	public void render(SpriteBatch batch) {
		gs.getCustomFont(15).draw(batch, "Time Left: " + timeLeft, -Specular.camera.viewportWidth / 2 + 10, Specular.camera.viewportHeight / 2 - 90);
	}
	
	@Override
	public void update(float delta) {
		// Updating time
		timeLeft -= delta;
		time += delta;
		
		if(timeLeft <= 0) {
			gs.getPlayer().kill();
			gameOver = true;
			Gdx.input.setInputProcessor(gs.getStage());
		}
		
		// Adding score
		gs.getPlayer().setScore((int) (time / 1000));
	}

	public void enemyKilled(Enemy e) {
		timeLeft += e.getValue() * gs.getScoreMultiplier();
	}
	
	public void playerKilled() {
		timeLeft -= 60000;
	}
	
	public long getTimeLeft() {
		return timeLeft;
	}
	
	public long getTime() {
		return time;
	}

	@Override
	public boolean isGameOver() {
		return gameOver;
	}
}
