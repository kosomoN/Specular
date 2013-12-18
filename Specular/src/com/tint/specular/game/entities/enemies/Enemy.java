package com.tint.specular.game.entities.enemies;

import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Player;

public abstract class Enemy implements Entity {
	
	//FIELDS
	protected float x, y, dx, dy;
	protected float width, height;
	protected float direction;
	protected float slowdown;
	
	protected int life;
	protected boolean isHit;
	
	protected GameState gs;
	
	public Enemy(float x, float y, GameState gs) {
		this.x = x;
		this.y = y;
		this.gs = gs;
	}
	
	@Override
	public boolean update() {			
		return life <= 0;
	}

	//SETTERS
	/**
	 * 
	 * @param slowdown - Max value 1, minimum value 0.
	 * If it higher than 1 or lower than 0, it will be changed to
	 * the nearest accepted value
	 */
	public void setSlowdown(float slowdown) {
		this.slowdown = slowdown;
		
		//Checking boundaries
		slowdown = slowdown > 1 ? 1 : slowdown;
		slowdown = slowdown < 0 ? 0 : slowdown;
	}
	
	public void hit(Player shooter) {
		life--;
		if(Specular.camera.position.x - Specular.camera.viewportWidth / 2 - 100 < x &&
				Specular.camera.position.x + Specular.camera.viewportWidth / 2 + 100 > x &&
				Specular.camera.position.y - Specular.camera.viewportHeight / 2 - 100 < y &&
				Specular.camera.position.y + Specular.camera.viewportHeight / 2 + 100 > y) {//Check if the enemy is on the screen
			if(life == 0)
				gs.getPass().spawn(this, 15, true);
			else
				gs.getPass().spawn(this, 6, false);
		}
		
	}
	
	//GETTERS
	public Player getClosestPlayer() {
		//Calculates the closest player
		/*Player closest = null;
		float distance = Float.MAX_VALUE;
		float tempDistSqrd;
		
			tempDistSqrd = Util.getDistanceSquared(x, y, gs.getPlayer().getCenterX(), gs.getPlayer().getCenterY());
			if(tempDistSqrd < distance * distance) {
				distance = tempDistSqrd;
				closest = gs.getPlayer()player;
			}
		}*/
		
		return gs.getPlayer();
	}
	
	public float getX() { return x;	}
	public float getY() { return y; }
	public float getDx() { return dx; }
	public float getDy() { return dy; }

	public int getLife() { return life; }
	
	public abstract float getInnerRadius();
	public abstract float getOuterRadius();
	
	@Override
	public void dispose() {}
}
