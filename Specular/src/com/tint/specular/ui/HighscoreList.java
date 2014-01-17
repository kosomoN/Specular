package com.tint.specular.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;

public class HighscoreList extends Widget implements Cullable {
	
	private static float textOffsetX = 10, textOffsetY = 0, rowHeight = 50;
	private static Texture itemBackground;
	private static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzåäöABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	
	private Rectangle cullingArea;
	private String[] highscores = new String[] {"Loading..."};
	private BitmapFont font;
	private float prefHeight;
	
	public HighscoreList() {
		super();
		
		if(itemBackground == null) {
//			itemBackground = new Texture(Gdx.files.internal("graphics/menu/highscore/HighscoreItem.png"));
			FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Battlev2l.ttf"));
			font = fontGen.generateFont(50 * Gdx.graphics.getWidth() / 1920, FONT_CHARACTERS, false);
			font.setColor(Color.WHITE);
			fontGen.dispose();
			
			textOffsetY = (rowHeight - font.getLineHeight()) / 2;
			rowHeight = 50 * Gdx.graphics.getWidth() / 1920f;
		}
		prefHeight = highscores.length * rowHeight;
		invalidateHierarchy();
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		float itemY = getHeight();
		for (int i = 0; i < highscores.length; i++) {
			if (cullingArea == null || (itemY - rowHeight <= cullingArea.y + cullingArea.height && itemY >= cullingArea.y)) {
				font.draw(batch, highscores[i], getX() + textOffsetX, getY() + itemY - textOffsetY);
			} else if (itemY < cullingArea.y) {
				break;
			}
			itemY -= rowHeight;
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

	public void setItems(String[] scores) {
		highscores = scores;
		prefHeight = highscores.length * rowHeight;
		invalidateHierarchy();
	}		
}
