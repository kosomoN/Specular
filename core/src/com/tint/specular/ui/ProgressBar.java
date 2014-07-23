package com.tint.specular.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ProgressBar {

	private Texture progressBar, barFill;
	private int width, height;
	private int x, y;
	private float value, maxValue;
	
	public ProgressBar(int width, int height) {
		this.width = width;
		this.height = height;
		
		progressBar = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Frame.png"));
		barFill = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/ProgressBar.png"));
	}

	public void render(Batch batch) {
		batch.draw(progressBar, x - 80, y - 45, width + 160, 256);
		batch.draw(barFill, x + 17, y + 16, (int) (value / maxValue * width) - 45, height);
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setValue(float value) {
		this.value = value;
	}
	
	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public float getValue() {
		return value;
	}
}
