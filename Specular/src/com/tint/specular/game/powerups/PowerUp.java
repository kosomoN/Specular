package com.tint.specular.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;

public class PowerUp {

	public enum Type {
		ADD_ONE_LIFE(1), SPEEDBOOST(2), SLOWDOWN_ENEMIES(3), BULLETBURST(4);
		
		private int time;
		private Type(int time) {
			this.time = time;
		}
		
		public int getTime() {
			return time;
		}
	}
	
	//FIELDS
	private float x, y;
	private boolean toBeRemoved = false;
	
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
			player.setTime(player.getSpeedTimer(), 5f);
			break;
			
		case SLOWDOWN_ENEMIES :
			for(Enemy e : enemies) {
				e.setSpeedUtilization(0.5f);
				e.setTimer(5f);
			}
			break;
			
		case BULLETBURST :
			player.setBulletBurst(3);
			player.setTime(player.getBulletTimer(), 5f);
			break;
			
		default :
			break;
		}
		
		toBeRemoved = true;
	}
	
	//RENDER&UPDATE loop
/*_________________________________________________________________*/
	public void render(SpriteBatch batch) {
		batch.begin();
		batch.draw(texture, x, y);
		batch.end();
	}
	
	public boolean update(float delta) {
		/*if(useTimer) {
			timeActive -= delta;
			if(timeActive <= 0) {
				toBeRemoved = true;
			}
		}*/
		
		return toBeRemoved;
	}
/*_________________________________________________________________*/
	
	//SETTERS
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
	
	//GETTERS
	public float getCenterX() { return x + texture.getWidth() / 2; }
	public float getCenterY() {	return y + texture.getHeight() / 2; }
	public float getX() { return x;	}
	public float getY() { return y; }
	public boolean toBeRemoved() { return toBeRemoved; }
	public Type getType() { return type; }
	public Texture getTexture() { return texture; }
	
	public void dispose() {
		texture.dispose();
	}
}
