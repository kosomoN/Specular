package com.tint.specular.map;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class MapHandler {
	
	//FIELDS
	private HashMap<String, Map> maps = new HashMap<String, Map>();
	
	//ADD MAP
	/**
	 * Add a new map
	 * @param name - Name of the map, also the key
	 * @param texture - The texture of the map
	 * @param width - The width
	 * @param height - The height
	 */
	public void addMap(String name, Texture texture, Texture shockLight, Texture parallax, int width, int height, GameState gs) {
		if(!maps.containsKey(name))
			maps.put(name, new Map(texture, shockLight, parallax, name, width, height, gs));
	}
	
	/**
	 * Add a existing map
	 * @param name - Name of the map, also the key
	 * @param map - The map itself
	 */
	public void addMap(String name, Map map) {
		if(!maps.containsValue(map))
			maps.put(name, map);
	}
	
	//RENDER
	
	/**
	 * Renders a smaller version
	 * @param batch - A SpriteBatch
	 * @param name - Name of the map to be drawn
	 * @param width - The width
	 * @param height - The height
	 */
	public void renderPreview(SpriteBatch batch, String name, int width, int height) {
		if(maps.containsKey(name))
			batch.draw(maps.get(name).getTexture(), width, height);
	}
	
	//GETTERS
	/**
	 * Returns a map by it's name or creates a new EMPTY one
	 * @param name - The name of the map
	 * @return The map
	 */
	public Map getMap(String name) {
		if(!maps.containsKey(name))
			maps.put(name, new Map());
		
		return maps.get(name);
	}
	
	/**
	 * 
	 * @return All maps
	 */
	public HashMap<String, Map> getMaps() {
		return maps;
	}
}
