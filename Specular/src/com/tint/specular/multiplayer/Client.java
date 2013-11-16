package com.tint.specular.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class Client {

	private Player player;
	private OrthographicCamera cam;
	private Specular game;
	
	public Client(Specular game, GameState state) {
		player = new Player(state);
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.game = game;
	}
	
	public void update() {
		cam.position.set(player.getCenterX(), player.getCenterY(), 0);
		cam.update();
		game.batch.setProjectionMatrix(cam.combined);
	}
	
	public Player getPlayer() {
		return player;
	}
}
