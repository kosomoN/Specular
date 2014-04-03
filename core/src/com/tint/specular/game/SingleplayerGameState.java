package com.tint.specular.game;

import com.tint.specular.Specular;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class SingleplayerGameState extends GameState {
	
	public SingleplayerGameState(Specular game) {
		super(game);
	}
	
	@Override
	public void render(float delta) {
		if(ready) {
			super.render(delta);
		}
	}
	
	@Override
	public void show() {
		super.show();
		ready = true;
	}
}