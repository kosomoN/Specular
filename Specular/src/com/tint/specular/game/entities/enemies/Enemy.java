package com.tint.specular.game.entities.enemies;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Timer;
import com.tint.specular.utils.Util;

public abstract class Enemy implements Entity {
	
	//FIELDS
	protected float x, y, dx, dy;
	protected float width, height;
	protected float direction;
	
	protected float speedUtilization;
	
	protected int life;
	protected boolean isHit;
	
	protected GameState gs;
	protected Player killer;
	protected Timer speedTimer;
	
	public Enemy(float x, float y, GameState gs) {
		this.x = x;
		this.y = y;
		this.gs = gs;
		
		speedTimer = new Timer();
	}
	
	@Override
	public boolean update() {
		if(!speedTimer.update(10))
			setSpeedUtilization(1);
		if(killer != null) {
			killer.setHit(false);
			killer = null;
		}
			
		return isDead();
	}

	//SETTERS
	/**
	 * 
	 * @param utilization - Max value 1, minimum value 0.
	 * If it higher or lower than the restriction it will be changed to
	 * nearest accepted value
	 */
	public void setSpeedUtilization(float utilization) {
		speedUtilization = utilization;
		
		//Checking boundaries
		speedUtilization = speedUtilization > 1 ? 1 : speedUtilization;
		speedUtilization = speedUtilization < 0 ? 0 : speedUtilization;
	}
	
	public void setTimer(float seconds) {
		if(speedTimer.getTime() <= 0) {
			speedTimer.setTime(seconds < 0 ? 0: seconds);
		}
	}
	
	public void hit(Player shooter) {
		life--;
	}
	
	public void kill(Player killer) {
		life = 0;
		this.killer = killer;
	}
	
	//GETTERS
	public Player getClosestPlayer() {
		//Calculates the closest player
		Player closest = null;
		float distance = Float.MAX_VALUE;
		float tempDistSqrd;
		
		for(Player p : gs.getPlayers()) {
			tempDistSqrd = Util.getDistanceSquared(x, y, p.getCenterX(), p.getCenterY());
			if(tempDistSqrd < Math.pow(distance, 2)) {
				distance = tempDistSqrd;
				closest = p;
			}
		}
		
		return closest;
	}
	
	public boolean isDead() {
		return life <= 0;
	}
	
	public Timer getSpeedTimer() {
		return speedTimer;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public abstract float getDeltaX();
	
	public abstract float getDeltaY();
	
	public float getInnerRadius() {
		return 0;
	}
	
	public float getOuterRadius() {
		return 0;
	}
	
	@Override
	public void dispose() {}
}
