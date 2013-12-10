package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Entity;

public class PowerUp implements Entity {
	private float x, y;
	private boolean toBeRemoved = false;
	
	private Texture texture;
	
	protected GameState gs;
	protected boolean collected;
	
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
		if(texture != null) {
			batch.draw(texture, x, y);
		}
	}
	
	@Override
	public boolean update() {
		return collected;
	}
/*_________________________________________________________________*/
	
	//SETTERS
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	//GETTERS
	public float getCenterX() { return x + texture.getWidth() / 2; }
	public float getCenterY() {	return y + texture.getHeight() / 2; }
	public float getX() { return x;	}
	public float getY() { return y; }
	public boolean toBeRemoved() { return toBeRemoved; }
	public Texture getTexture() { return texture; }
	
	@Override
	public void dispose() {
		texture.dispose();
	}
}
