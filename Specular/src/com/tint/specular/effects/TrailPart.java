package com.tint.specular.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TrailPart {
		
	private static Texture tex;
	private Color color;
	private float x;
	private float y;
	private float alpha = 1;
	
	public TrailPart(float x, float y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/TrailPart.png"));
	}
	
	public void render(SpriteBatch batch) {
		batch.setColor(color.r, color.g, color.b, alpha);
		batch.draw(tex, x, y);
		batch.setColor(color);
	}
	
	public boolean update() {
		alpha -= Gdx.graphics.getDeltaTime() / 60;
		return false;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}