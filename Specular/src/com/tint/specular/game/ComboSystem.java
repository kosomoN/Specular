package com.tint.specular.game;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class ComboSystem {
	
	private int combo = 0;
	private float timer = 0;
	private boolean activated = false;
	
	public void update() {
		if(activated)
			timer -= 10;
		if(timer <= 0)
			deactivate();
	}
	
	public void activate(int enemiesAlive) {
		activated = true;
		timer = 500 - enemiesAlive * 1.5f;
		combo++;
	}
	
	private void deactivate() {
		activated = false;
		timer = 0;
		combo = 0;
	}
	
	public int getCombo() {
		return combo;
	}
}
