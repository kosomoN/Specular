package com.tint.specular.game;

import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class SingleplayerGameState extends GameState {
	
	//FIELDS
	private Player player;
	
	public SingleplayerGameState(Specular game) {
		super(game);
	}
	
	@Override
	public void render(float delta) {
		game.camera.position.set(player.getCenterX(), player.getCenterY(), 0);
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		super.render(10);
		
		if(player.isDead()) {
			getPlayers().removeValue(getPlayer(), false);
		}
	}
	
	@Override
	public void show() {
		super.show();
		
		pss.spawn(1);
		player = getPlayers().first();
	}

	public Player getPlayer() {
		return player;
	}
}