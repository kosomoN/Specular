package com.tint.specular.game;

import java.util.Iterator;

import com.badlogic.gdx.utils.Array;
import com.tint.specular.Specular;
import com.tint.specular.multiplayer.Client;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class MultiplayerGameState extends GameState {

	//FIELDS
	private Array<Client> clients = new Array<Client>();
	
	public MultiplayerGameState(Specular game) {
		super(game);
	}

	@Override
	public void render(float delta) {
		for(Iterator<Client> it = clients.iterator(); it.hasNext(); ) {
			Client c = it.next();
			if(c.update()) {
				disconnectClient(c);
			}
		}
		
		super.render(10);
	}
	
	public void connectClient(Client client) {
		clients.add(client);
		pss.spawn(3, false);
//		client.setPlayer(getPlayers().get(getPlayers().size - 1));
	}
	
	public void disconnectClient(Client client) {
//		getPlayers().removeValue(client.getPlayer(), false);
		clients.removeValue(client, false);
	}

	@Override
	public void show() {
		super.show();
		connectClient(new Client(game, this));
		connectClient(new Client(game, this));
	}
}
