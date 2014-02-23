package com.tint.specular.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
import com.badlogic.gdx.utils.Disposable;

public class HighscoreList extends Widget implements Cullable, Disposable {
	
	private static float textOffsetX = 10, textOffsetY, rowHeight = 200;
	private Texture itemBackground, highscoreText;
	
	private Rectangle cullingArea;
	private static Highscore[] highscores = new Highscore[] {new Highscore("Loading", -1)};
	private BitmapFont font;
	private float prefHeight;
	
	public HighscoreList(BitmapFont font) {
		super();
		this.font = font;
		itemBackground = new Texture(Gdx.files.internal("graphics/menu/highscore/Profiles.png"));
		highscoreText = new Texture(Gdx.files.internal("graphics/menu/highscore/Highscores.png"));
		textOffsetY = (rowHeight - font.getLineHeight()) / 2;
		prefHeight = highscores.length * rowHeight + rowHeight;//For the highscore text
		invalidateHierarchy();
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		float itemY = getHeight();
		batch.draw(highscoreText, getX() - 10, getY() + itemY - 170);
		itemY -= rowHeight;
		if(highscores != null) {
		for (int i = 0; i < highscores.length; i++) {
			if (cullingArea == null || (itemY - rowHeight <= cullingArea.y + cullingArea.height && itemY >= cullingArea.y)) {
				batch.draw(itemBackground, getX() + 13, getY() + itemY - 190);
				textOffsetX = 80;
				font.draw(batch, highscores[i].name, getX() + textOffsetX, getY() + itemY - textOffsetY + 25);
				if(highscores[i].score != -1) {
					font.draw(batch, String.valueOf(highscores[i].score), getX() + getWidth() - textOffsetX - font.getBounds(String.valueOf(highscores[i].score)).width, getY() + itemY - textOffsetY - 25);
				}
			} else if (itemY < cullingArea.y) {
				break;
			}
			itemY -= rowHeight;
		}
		} else {
			
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

	public void setItems(Highscore[] scores) {
		for(Highscore h : scores) {
			//Calculating where to cut the name if it is too long
			if(font.getBounds(h.name).width > getWidth() - textOffsetX * 2) {
				int index = 0;
				BitmapFontData data = font.getData();
				int width = 0;
				Glyph lastGlyph = null;
				lastGlyph = data.getGlyph(h.name.charAt(index++));
				width = lastGlyph.xadvance;
				while (index < h.name.length()) {
					char ch = h.name.charAt(index++);
					Glyph g = data.getGlyph(ch);
					if (g != null) {
						width += lastGlyph.getKerning(ch);
						lastGlyph = g;
						width += g.xadvance;
						if(width >= getWidth() - textOffsetX * 2 - 50) {
							break;
						}
					}
				}
				
				h.name = h.name.substring(0, index) + "...";
			}
		}
		highscores = scores;
		prefHeight = highscores.length * rowHeight + rowHeight;
		invalidateHierarchy();
	}

	@Override
	public void dispose() {
		itemBackground.dispose();
		highscoreText.dispose();
	}	
	
	public static class Highscore {
		public String name;
		public int score;
		
		public Highscore(String name, int score) {
			this.name = name;
			this.score = score;
		}
	}
}
