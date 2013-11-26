package com.tint.specular;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tint.specular.game.MultiplayerGameState;
import com.tint.specular.game.SingleplayerGameState;
import com.tint.specular.states.*;

public class Specular extends Game {
	public enum States {
		MAINMENUSTATE, SETTINGSMENUSTATE, SINGLEPLAYER_GAMESTATE, MULTIPLAYER_GAMESTATE
	}
	
	//FIELDS
	private Map<States, State> states = new EnumMap<Specular.States, State>(States.class);
	
	public OrthographicCamera camera;
	public SpriteBatch batch;
	public ShapeRenderer shape;
	
	/*
	 * 0. Keyboard and shooting stick 
	 * 1. Keyboard and moving stick
	 * 2. Twin sticks
	 */
	public int system = 0;
	
	@Override
	public void create() {
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		
		states.put(States.MAINMENUSTATE, new MainmenuState(this));
		states.put(States.SETTINGSMENUSTATE, new SettingsMenuState(this));
		states.put(States.SINGLEPLAYER_GAMESTATE, new SingleplayerGameState(this));
		states.put(States.MULTIPLAYER_GAMESTATE, new MultiplayerGameState(this));
		
		enterState(States.MAINMENUSTATE);
	}
	
	public void enterState(States state) {
		State s = states.get(state);
		if(s != null) {
			s.enter();
			setScreen(s);
		} else {
			throw new RuntimeException("No state assigned to this enum: " + state);
		}
	}
	
	public State getState(States state) {
		return states.get(state);
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		shape.dispose();
	}
}