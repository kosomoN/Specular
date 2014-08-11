package com.tint.specular.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class Util {
	
	/**
	 * Checks if point(checkX, checkY) is inside or touching rect(x, y, width, height)
	 * @param checkX - The x - coordinate to be checked
	 * @param checkY - The y - coordinate to be checked
	 * @param x - Lower left corner x
	 * @param y - Lower left y
	 * @param width - The width of the rectangle
	 * @param height - The height of the rectangle
	 * @return - If the point is inside the rect returns true, else false
	 */
	public static boolean isTouching(float checkX, float checkY, float x, float y, float width, float height, boolean invertedy) {
		if(checkX >= x) {
			if(checkX <= x + width) {
				if(!invertedy) {
					if(checkY >= y) {
						if(checkY <= y + height) {
							return true;
						}
					}
				} else {
					if(Gdx.graphics.getHeight() - checkY >= y) {
						if(Gdx.graphics.getHeight() - checkY <= y + height) {
							return true;
						}
					}
				}
				
			}
		}
		return false;
	}
	
	public static boolean isOnLine(float startX, float startY, float endX, float endY, float checkX, float checkY, float lineWidth) {
		float startDirection = (float) (Math.atan2(startY - endY, startX - endX));
		float direction = (float) Math.atan2(startY - checkY, startX - checkX);
		
		if(lineWidth != 1) {
			float maxDifference = (float) Math.abs((startDirection - 2f * Math.atan2(startY - endY, startX - (endX + lineWidth / 2))));
			if(direction < startDirection + maxDifference && direction > startDirection - maxDifference) {
				return true;
			}
		} else {
			if(direction == startDirection) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Calculates distance with pythagoras statement
	 *  @param x1 - x value of the first point
	 * @param y1 - y value of the first point
	 * @param x2 - x value of the second point
	 * @param y2 - y value of the second point
	 * @return - Distance between the two points squared
	 */
	public static float getDistanceSquared(float x1, float y1, float x2, float y2) {
		return (float) ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	/**
	 * 
	 * @param texture - The texture containing all frames
	 * @param spriteWidth - The width of the frame
	 * @param spriteHeight - The height of the frame
	 * @param frameTime - The duration every frame will be showed
	 * @param firstX - The first frames x coordianate
	 * @param firstY - The first frames y coordianate
	 * @param lastX - The last frames x coordianate
	 * @param lastY - The last frames y coordianate
	 * @return an animation
	 */
	public static Animation getAnimation(TextureRegion texture, int spriteWidth, int spriteHeight, float frameTime, int firstX, int firstY, int lastX, int lastY) {
		TextureRegion[][] trArr = texture.split(spriteWidth, spriteHeight);
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int y = firstY; y <= lastY; y++) {
			int x = 0;
			if(y == firstY)
				x = firstX;
			
			for(; x < trArr[0].length; x++) {
				if(y == lastY && x == lastX)
					break;
				frames.add(trArr[y][x]);
			}
		}
		return new Animation(frameTime, frames);
	}
	
	/**
	 * 
	 * @param batch - The spritebatch to use
	 * @param tex - The texture to draw
	 * @param centerx - The centerx
	 * @param centery - The centery
	 * @param rotation - The angle of the rotation around the centerpoint
	 */
	public static void drawCentered(SpriteBatch batch, Texture tex, float centerx, float centery, float rotation) {
		int texWidth = tex.getWidth();
		int texHeight = tex.getHeight();
		batch.draw(tex, centerx - texWidth / 2, centery - texHeight / 2, texWidth / 2, texHeight / 2, texWidth, texHeight, 1, 1, rotation, 0, 0, texWidth, texHeight, false, false);
	}
	
	/**
	 * 
	 * @param batch - The spritebatch to use
	 * @param tex - The texture to draw
	 * @param centerx - The centerx
	 * @param centery - The centery
	 * @param scale - The amount to scale the texture
	 * @param rotation - The angle of the rotation around the centerpoint
	 */
	public static void drawCentered(SpriteBatch batch, Texture tex, float centerx, float centery, float scale, float rotation) {
		int texWidth = tex.getWidth();
		int texHeight = tex.getHeight();
		batch.draw(tex, centerx - texWidth / 2, centery - texHeight / 2, texWidth / 2, texHeight / 2, texWidth, texHeight, scale, scale, rotation, 0, 0, texWidth, texHeight, false, false);
	}
	
	/**
	 * 
	 * @param batch - The spritebatch to use
	 * @param tex - The texture to draw
	 * @param centerx - The centerx
	 * @param centery - The centery
	 * @param rotation - The angle of the rotation around the centerpoint
	 */
	public static void drawCentered(SpriteBatch batch, TextureRegion tex, float centerx, float centery, float width, float height, float rotation) {
		batch.draw(tex, centerx - width / 2, centery - height / 2, width / 2, height / 2, width, height, 1, 1, rotation);
	}
	
	/**
	 * 
	 * @param batch - The spritebatch to use
	 * @param tex - The texture to draw
	 * @param centerx - The centerx
	 * @param centery - The centery
	 * @param rotation - The angle of the rotation around the centerpoint
	 */
	public static void drawCentered(SpriteBatch batch, TextureRegion tex, float centerx, float centery, float rotation) {
		batch.draw(tex, centerx - tex.getRegionWidth() / 2, centery - tex.getRegionHeight() / 2,
				tex.getRegionWidth() / 2, tex.getRegionHeight() / 2, tex.getRegionWidth(), tex.getRegionHeight(), 1, 1, rotation);
	}
	
	public static void writeCentered(SpriteBatch batch, BitmapFont font, String s, float centerx, float centery) {
		font.draw(batch, s, centerx - font.getBounds(s).width / 2, centery + font.getCapHeight() / 2);
		
	}
}
