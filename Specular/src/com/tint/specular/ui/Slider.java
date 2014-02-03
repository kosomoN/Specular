package com.tint.specular.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class Slider extends Table {
	
	private Drawable thumb;
	private int minValue, maxValue;
	private ClickListener clickListener;
	
	public Slider(int minValue, int maxValue) {
		initialize();
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	private void initialize() {
		setTouchable(Touchable.enabled);
		addListener(clickListener = new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
			}
			
		});
	}
	
	public int getValue() {
		return (int) (getWidth() / (maxValue - minValue));
	}
}
