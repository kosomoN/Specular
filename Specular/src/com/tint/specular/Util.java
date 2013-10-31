package com.tint.specular;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Util {
	public static Animation getAnimation(Texture texture, int spriteWidth, int spriteHeight, float frameTime, int firstX, int firstY, int lastX, int lastY) {
		TextureRegion tr = new TextureRegion(texture);
		TextureRegion[][] trArr = tr.split(spriteWidth, spriteHeight);
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int y = firstY; y <= lastY; y++) {
			int x = 0;
			if(y == firstY)
				x = firstX;
			
			for(; x < 8; x++) {
				if(y == lastY && x == lastX)
					break;
				frames.add(trArr[y][x]);
			}
		}
		return new Animation(frameTime, frames);
	}
	
	public static void drawCentered(SpriteBatch batch, Texture tex, float x, float y, float rotation) {
		batch.draw(tex, x - tex.getWidth() / 2, y - tex.getHeight() / 2, tex.getWidth() / 2, tex.getHeight() / 2, tex.getWidth(), tex.getHeight(), 1, 1, rotation, 0, 0, tex.getWidth(), tex.getHeight(), false, false);
	}
}
