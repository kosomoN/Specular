package com.tint.specular.game;

import com.tint.specular.Specular;

public class SingleplayerGameState extends GameState {
	
	public SingleplayerGameState(Specular game) {
		super(game);
	}
	
	@Override
	public void render(float delta) {
		if(ready) {
			super.render(10);
		}
	}
	
	@Override
	public void show() {
		super.show();
		ready = true;
	}
}