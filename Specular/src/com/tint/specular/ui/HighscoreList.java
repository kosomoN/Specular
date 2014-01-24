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
import com.badlogic.gdx.utils.Disposable;

public class HighscoreList extends Widget implements Cullable, Disposable {
	
	private static float textOffsetX = 10, textOffsetY, rowHeight = 200;
	private Texture itemBackground, highscoreText;
	private static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzåäöABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	
	private Rectangle cullingArea;
	private String[] highscores = new String[] {"Loading..."};
	private BitmapFont font;
	private float prefHeight;
	
	public HighscoreList() {
		super();
		itemBackground = new Texture(Gdx.files.internal("graphics/menu/highscore/Profiles.png"));
		highscoreText = new Texture(Gdx.files.internal("graphics/menu/highscore/Highscores.png"));
		FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Battlev2l.ttf"));
		font = fontGen.generateFont(30, FONT_CHARACTERS, false);
		font.setColor(new Color(0.96f, 0.05f, 0.05f, 1));
		fontGen.dispose();
		
		textOffsetY = (rowHeight - font.getLineHeight()) / 2;
		prefHeight = highscores.length * rowHeight + rowHeight;//For the highscore text
		invalidateHierarchy();
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		float itemY = getHeight();
		batch.draw(highscoreText, getX() - 10, getY() + itemY - 170);
		itemY -= rowHeight;
		for (int i = 0; i < highscores.length; i++) {
			if (cullingArea == null || (itemY - rowHeight <= cullingArea.y + cullingArea.height && itemY >= cullingArea.y)) {
				batch.draw(itemBackground, getX() + 13, getY() + itemY - 190);
				textOffsetX = 80;
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

	@Override
	public void dispose() {
		itemBackground.dispose();
		highscoreText.dispose();
	}		
}
