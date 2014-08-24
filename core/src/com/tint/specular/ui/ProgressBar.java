package com.tint.specular.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class ProgressBar extends Widget {

	private Texture progressBar, barFill;
	private float width, height;
	private float x, y;
	private float value, maxValue;
	
	public ProgressBar(float width, float height) {
		this.width = width;
		this.height = height;
		
		progressBar = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Frame.png"));
		barFill = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/ProgressBar.png"));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(progressBar, x - 17, y - 45, width + 40, 256);
		float length = value / maxValue;
		batch.draw(barFill, x + 64, y + 16, length < 0.01f ? 0 : length * (width - 128), height);
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setBarFrame(Texture progressBar) {
		this.progressBar = progressBar;
	}
	
	public void setBarFill(Texture barFill) {
		this.barFill = barFill;
	}
	
	public void setValue(float value) {
		this.value = value;
	}
	
	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getValue() {
		return value;
	}
}
