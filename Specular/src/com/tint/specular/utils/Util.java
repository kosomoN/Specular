package com.tint.specular.utils;

import com.badlogic.gdx.Gdx;

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
}
