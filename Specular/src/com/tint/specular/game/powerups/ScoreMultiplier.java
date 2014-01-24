package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class ScoreMultiplier extends PowerUp {
	private static Texture texture;
	private int increaseRate = 60;
	private int timeSinceLastIncrease;
	private int timeAlive = 300; // updates
	private int longestTime;
	private int lastTime;
	private boolean activated;
	
	public ScoreMultiplier(float x, float y, GameState gs) {
		super(x, y, gs);
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/Multiplier.png"));
	}
	
	@Override
	protected void affect(Player player) {
		gs.setScoreMultiplier(gs.getScoreMultiplier() + 1);
	}

	@Override
	public boolean update() {
		//Own timing system
		if(activated) {
			timeAlive--;
			despawnTime++;
		}
		
		if(super.update()) {
			timeSinceLastIncrease++;
			activated = true;
			
			if(timeSinceLastIncrease >= increaseRate) {
				affect(null);
				timeSinceLastIncrease = 0;
			}
		} else {
			timeSinceLastIncrease = 0;
			
			lastTime = 300 - timeAlive - lastTime;
			if(lastTime > longestTime)
				longestTime = lastTime;
		}
		return timeAlive <= 0;
	}

	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, texture, x, y, 0);
	}

	@Override
	public float getRadius() {
		return texture.getWidth() / 2;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
	
	@Override
	public void dispose() {
		texture.dispose();
	}
}
