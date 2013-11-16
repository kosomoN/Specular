package com.tint.specular.game;

import com.badlogic.gdx.utils.Array;
import com.tint.specular.Specular;
import com.tint.specular.multiplayer.Client;

public class MultiplayerGameState extends GameState {

	//FIELDS
	private Array<Client> clients = new Array<Client>();
	
	public MultiplayerGameState(Specular game) {
		super(game);
		//Adding players
		clients.add(new Client(game, this));
		
		for(Client c : clients)
			addEntity(c.getPlayer());
		
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		for(Client c : clients) {
			c.update();
		}
	}
	
	public void addClient(Client client) {
		clients.add(client);
	}
}
