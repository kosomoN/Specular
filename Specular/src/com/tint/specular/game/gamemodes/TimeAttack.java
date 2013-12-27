package com.tint.specular.game.gamemodes;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class TimeAttack {
	private float timeLeft;
	
	public TimeAttack(float timeLeft) {
		this.timeLeft = timeLeft;
	}
	
	public void render() {
		
	}
	
	public boolean update() {
		timeLeft -= 10;
		
		return timeLeft <= 0;
	}
	
	public float getTimeLeft() {
		return timeLeft;
	}
}
