package com.tint.specular.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
import com.badlogic.gdx.utils.Disposable;
import com.tint.specular.upgrades.Upgrade;

public class UpgradeList extends Widget implements Cullable, Disposable {

	private static float rowHeight = 160;
	
	private Texture progressBar, barFill;
	private Rectangle cullingArea;
	private Upgrade[] upgrades;
	private float prefHeight;
	
	public UpgradeList(Upgrade[] upgrades) {
		super();
		progressBar = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/ProgressBar.png"));
		barFill = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/BarFill.png"));
		this.upgrades = upgrades;
		prefHeight = upgrades.length * rowHeight;
		invalidateHierarchy();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		float itemY = getHeight() - rowHeight;
		float progressX = getX() + upgrades[0].getTexture().getWidth() + 13 + 50;
		float y = getY() + itemY + 20;
		if(upgrades != null) {
			TextureRegion fill;
			for(int i = 0; i < upgrades.length; i++) {
				y = getY() + itemY + 0;
				
				if (cullingArea == null || (itemY - rowHeight <= cullingArea.y + cullingArea.height && itemY >= cullingArea.y)) {
					batch.draw(upgrades[i].getTexture(), getX() + 13, y);
					batch.draw(progressBar, progressX, y);
					
					fill = new TextureRegion(barFill, (int) (upgrades[i].getGrade() / (float) upgrades[i].getMaxGrade() * progressBar.getWidth()), barFill.getHeight());
					batch.draw(fill, progressX, y);
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
		progressBar.dispose();
		barFill.dispose();
	}
}
