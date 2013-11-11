package com.tint.specular.game.entities.enemies;

import com.tint.specular.game.entities.Entity;

public abstract class Enemy implements Entity {
	
	//FIELDS
	protected float x, y;
	protected float width, height;
	protected float direction;
	
	protected float speedUtilization;
	protected float speedTimer;
	
	protected boolean isHit;
	
	public Enemy(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean update() {
		return isHit;
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
		if(speedTimer <= 0) {
			speedTimer = seconds < 0 ? 0: seconds;
		}
	}
	
	public void hit() {
		isHit = true;
	}
	
	//GETTERS
	public float getTimer() {
		return speedTimer;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	@Override
	public void dispose() {}
}
