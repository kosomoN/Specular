package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;

public class PowerUp implements Entity {

	public enum Type {
		ADD_ONE_LIFE, SPEEDBOOST, SLOWDOWN_ENEMIES, BULLETBURST, SCORE_MULTIPLIER_2X;
	}
	
	//FIELDS
	private float x, y;
	private boolean toBeRemoved = false;
	
	private Type type;
	private Texture texture;
	
	private GameState gs;
	
	//Constructors
	public PowerUp(Type type, float x, float y, GameState gs) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.gs = gs;
		
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
			player.setTimer(player.getSpeedTimer(), 5000f);
			break;
			
		case SLOWDOWN_ENEMIES :
			for(Enemy e : enemies) {
				e.setSpeedUtilization(0.5f);
				e.setTimer(5000f);
			}
			break;
			
		case BULLETBURST :
			player.setBulletBurst(3);
			player.setTimer(player.getBulletTimer(), 5000f);
			break;
			
		case SCORE_MULTIPLIER_2X :
			gs.setScoreMultiplier(2);
			break;
			
		default :
			break;
		}
		
		toBeRemoved = true;
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
		
		for(Player p : gs.getPlayers()) {
			if(Math.pow(getCenterX() - p.getCenterX(), 2) +	Math.pow(getCenterY() - p.getCenterY(), 2)
					< Math.pow(p.getRadius() + (getCenterX() - getX()), 2)) {
				
				affect(p, gs.getEnemies());
				toBeRemoved = true;
			} else {
				toBeRemoved = false;
			}
		}
		
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
			texture = new Texture(Gdx.files.internal("graphics/game/PowerUp.png"));
			break;
			
		case SPEEDBOOST :
			//Set texture
			texture = new Texture(Gdx.files.internal("graphics/game/PowerUp.png"));
			break;
			
		case SLOWDOWN_ENEMIES :
			//Set texture
			texture = new Texture(Gdx.files.internal("graphics/game/PowerUp.png"));
			break;
			
		case BULLETBURST :
			//Set Texture
			texture = new Texture(Gdx.files.internal("graphics/game/PowerUp.png"));
			break;
			
		case SCORE_MULTIPLIER_2X :
			//Set Texture
			texture = new Texture(Gdx.files.internal("graphics/game/PowerUp.png"));
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
	
	@Override
	public void dispose() {
		texture.dispose();
	}
}
