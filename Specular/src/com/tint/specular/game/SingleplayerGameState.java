package com.tint.specular.game;

import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class SingleplayerGameState extends GameState {
	
	//FIELDS
	private Player player;
	
	public SingleplayerGameState(Specular game) {
		super(game);
		player = new Player(this);
		addEntity(player);
	}
	
	@Override
	public void render(float delta) {
		game.camera.position.set(player.getCenterX(), player.getCenterY(), 0);
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		super.render(10);
	}
	
	public Player getPlayer() {
		return player;
	}
}