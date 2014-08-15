package com.tint.specular.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
import com.badlogic.gdx.utils.Disposable;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.states.UpgradeState;
import com.tint.specular.upgrades.Upgrade;
import com.tint.specular.utils.Util;

public class UpgradeList extends Widget implements Cullable, Disposable {

	private static float rowHeight = 160;
	private static BitmapFont upgradeFont;
	private ProgressBar[] bars;
	private Rectangle cullingArea;
	private Upgrade[] upgrades;
	private float prefHeight;
	
	public UpgradeList(Upgrade[] upgrades) {
		super();
		this.upgrades = upgrades;
		
		bars = new ProgressBar[upgrades.length];
		for(int i = 0; i < upgrades.length; i++) {
			bars[i] = new ProgressBar(Specular.camera.viewportWidth - 200, 128);
			bars[i].setValue(upgrades[i].getGrade());
			bars[i].setMaxValue(10);
		}
		
		prefHeight = upgrades.length * rowHeight;
		invalidateHierarchy();
	}
	
	public static void init() {
		// Initializing font
		FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Battlev2l.ttf"));
		FreeTypeFontParameter ftfp = new FreeTypeFontParameter();
		ftfp.size = 40;
		ftfp.characters = GameState.FONT_CHARACTERS;
		upgradeFont = fontGen.generateFont(ftfp);
		upgradeFont.setColor(Color.RED);
		
		fontGen.dispose();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		float itemY = getHeight() - rowHeight;
		float y = getY() + itemY + 20;
		Texture levelTex = null;
		if(upgrades != null) {
			for(int i = 0; i < upgrades.length; i++) {
				y = getY() + itemY + 0;
				bars[i].setPosition((int) getX(), (int) y);
				
				if (cullingArea == null || (itemY - rowHeight <= cullingArea.y + cullingArea.height && itemY + rowHeight >= cullingArea.y)) {
					// The + 64 is for the space from edge of bar frame texture to actual frame edge
					batch.draw(upgrades[i].getTexture(), getX() + 64 + 40, y + 35, upgrades[i].getTexture().getRegionWidth() * 1.5f, upgrades[i].getTexture().getRegionHeight() * 1.5f);
					
					levelTex = UpgradeState.getUpgradeLevelTexture(upgrades[i].getGrade());
					if(levelTex != null) {
						batch.draw(levelTex, getX() + 64 + 40, y + 35, levelTex.getWidth() * 1.5f, levelTex.getHeight() * 1.5f);
					}
					
					bars[i].draw(batch, parentAlpha);
					Util.writeCentered((SpriteBatch) batch, upgradeFont, upgrades[i].getDescription(), getX() + bars[i].getWidth() / 2, y + bars[i].getHeight() / 2);
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

	public ProgressBar[] getProgressBars() {
		return bars;
	}
}
