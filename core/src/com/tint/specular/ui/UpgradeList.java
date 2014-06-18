package com.tint.specular.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
import com.badlogic.gdx.utils.Disposable;
import com.tint.specular.upgrades.Upgrade;

public class UpgradeList extends Widget implements Cullable, Disposable {

	private static float rowHeight = 160;
	
	private ProgressBar[] bars;
	private Rectangle cullingArea;
	private Upgrade[] upgrades;
	private float prefHeight;
	
	public UpgradeList(Upgrade[] upgrades) {
		super();
		this.upgrades = upgrades;
		bars = new ProgressBar[upgrades.length];
		for(int i = 0; i < upgrades.length; i++) {
			bars[i] = new ProgressBar(1088, 128);
			bars[i].setValue(upgrades[i].getGrade());
			bars[i].setMaxValue(10);
		}
		
		prefHeight = upgrades.length * rowHeight;
		invalidateHierarchy();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		float itemY = getHeight() - rowHeight;
		float progressX = getX() + upgrades[0].getTexture().getWidth() + 13 + 50;
		float y = getY() + itemY + 20;
		if(upgrades != null) {
			for(int i = 0; i < upgrades.length; i++) {
				y = getY() + itemY + 0;
				bars[i].setPosition((int) progressX, (int) y);
				
				if (cullingArea == null || (itemY - rowHeight <= cullingArea.y + cullingArea.height && itemY >= cullingArea.y)) {
					batch.draw(upgrades[i].getTexture(), getX() + 13, y);
					bars[i].render(batch);
				} else if (itemY < cullingArea.y) {
					break;
				}
				itemY -= rowHeight;
			}
		}
	}

	@Override
	public void setCullingArea(Rectangle cullingArea) {
		this.cullingArea = cullingArea;
	}
	
	@Override
	public float getPrefHeight() {
		return prefHeight;
	}
	
	public static float rowHeight() {
		return rowHeight;
	}

	@Override
	public void dispose() {
		
	}
}
