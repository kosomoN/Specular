package com.tint.specular;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.states.NetworkTest;
import com.tint.specular.states.State;

public class Specular extends Game {
	public enum States {
		GAMESTATE
	}
	private Map<States, State> states = new HashMap<States, State>();
	
	public OrthographicCamera camera;
	public SpriteBatch batch;
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();
		
		states.put(States.GAMESTATE, new GameState(this));
		
		enterState(States.GAMESTATE);
	}
	
	public void enterState(States state) {
		setScreen(states.get(state));
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
