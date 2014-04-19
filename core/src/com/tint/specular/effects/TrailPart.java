package com.tint.specular.effects;


public class TrailPart {
		
	public static final int AMOUNT = 50, SIZE = 128;
	private float x;
	private float y;
	private float size;
	
	public TrailPart(float x, float y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	public boolean update() {
		size -= 3;
		return size < 1;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

	public void reset(float x, float y) {
		this.x = x;
		this.y = y;
		size = SIZE;
	}

	public float getSize() {
		return size;
	}
}