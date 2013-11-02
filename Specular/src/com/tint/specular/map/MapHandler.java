package com.tint.specular.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class MapHandler {
	private static Array<Map> maps = new Array<Map>();
	
	public static void addMap(String name, Texture texture, int width, int height) {
		maps.add(new Map(texture, name, width, height));
	}
	
	public static Array<Map> getMaps() {
		return maps;
	}
}
