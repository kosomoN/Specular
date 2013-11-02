package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;

public class PowerUp {

	public enum Type {
		ADD_ONE_LIFE, SPEEDBOOST, SLOWDOWN_ENEMIES, BULLETBURST;
	}
	
	//Fields
	private float x, y;
	private Type type;
	private Texture texture;
	
	//Constructors
	public PowerUp(Type type, float x, float y) {
		this.x = x;
		this.y = y;
		this.type = type;
		
		setType(type);
	}
	
	public PowerUp(Type type) {
		this.type = type;
		setType(type);
	}
	
	public void affect(Player player, Array<Enemy> enemies) {
		switch(type) {
		case ADD_ONE_LIFE :
			player.addLives(1);
			break;
			
		case SPEEDBOOST :
			player.activateSpeedBonus();
			break;
			
		case SLOWDOWN_ENEMIES :
			for(Enemy e : enemies) {
				e.setUseOfSpeed(0.5f);
			}
			break;
			
		case BULLETBURST :
			player.setBulletBurst(3);
			break;
			
		default :
			break;
		}
	}
	
	public void render(SpriteBatch batch) {
		batch.begin();
		batch.draw(texture, x, y);
		batch.end();
	}
	
	public void update() {
		
	}
	
	//Dispose
	public void dispose() {
		texture.dispose();
	}
	
	//Setters
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	private void setType(Type type) {
		switch(type) {
		case ADD_ONE_LIFE :
			//Set texture
			break;
			
		case SPEEDBOOST :
			//Set texture
			break;
			
		case SLOWDOWN_ENEMIES :
			//Set texture
			break;
			
		case BULLETBURST :
			//Set Texture
			break;
			
		default :
			System.err.println("No such PowerUp as: " + type.toString());
			break;
		}
	}
	
	//Getters
	public float getCenterX() {
		return x + texture.getWidth() / 2;
	}
	
	public float getCenterY() {
		return y + texture.getHeight() / 2;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public Type getType() {
		return type;
	}
	
}
