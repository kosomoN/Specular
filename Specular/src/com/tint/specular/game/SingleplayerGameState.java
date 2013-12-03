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
			
			if(player.isDead()) {
				getPlayers().removeValue(player, false);
			}
		}
	}
	
	@Override
	public void show() {
		super.show();
		player = getPlayers().first();
		ready = true;
	}
}