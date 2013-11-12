package com.tint.specular.utils;

public class Timer {
	
	//FIELDS
	private float time;
	
	//CONSTRUCTORS
	public Timer() {}
	public Timer(float time) { this.time = time; }
	
	//UPDATE
	public void update(float deltaInSeconds) {
		if(time > 0)
			time -= deltaInSeconds;
	}
	
	//SETTERS
	public void setTime(float time) { this.time = time; }
	
	//GETTERS
	public float getTime() { return time; }
}
