package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.utils.Util;

public class PowerUp implements Entity {
	
	private float x, y;
	
	private Texture texture;
	protected GameState gs;
	
	//Constructors
	public PowerUp(float x, float y, GameState gs) {
		this.x = x;
		this.y = y;
		this.gs = gs;
		texture = new Texture(Gdx.files.internal("graphics/game/PowerUp.png"));
	}
	
	//RENDER&UPDATE loop
/*_________________________________________________________________*/
	@Override
	public void render(SpriteBatch batch) {
		Util.drawCentered(batch, texture, x, y, 0);
	}
	
	@Override
	public boolean update() {
		return true;
	}
/*_________________________________________________________________*/
	
	//SETTERS
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	//GETTERS
	public float getCenterX() { return x; }
	public float getCenterY() {	return y; }
	public float getRadius() { return texture.getWidth() / 2; }
	public Texture getTexture() { return texture; }
	
	@Override
	public void dispose() {
		texture.dispose();
	}
}
