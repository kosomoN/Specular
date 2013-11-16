package com.tint.specular.utils;

public class Timer {
	
	//FIELDS
	private float time;
	
	//CONSTRUCTORS
	public Timer() {}
	public Timer(float time) { this.time = time; }
	
	//UPDATE
	public boolean update(float deltaInMilliSeconds) {
		if(time > 0)
			time -= deltaInMilliSeconds;
		
		return time > 0;
	}
	
	//SETTERS
	/**
	 * 
	 * @param time - In milliseconds
	 */
	public void setTime(float time) { this.time = time; }
	
	//GETTERS
	public float getTime() { return time; }
}
