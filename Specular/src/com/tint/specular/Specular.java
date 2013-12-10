package com.tint.specular;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tint.specular.game.MultiplayerGameState;
import com.tint.specular.game.SingleplayerGameState;
import com.tint.specular.states.HighscoreState;
import com.tint.specular.states.MainmenuState;
import com.tint.specular.states.SettingsMenuState;
import com.tint.specular.states.State;

public class Specular extends Game {
	public enum States {
		MAINMENUSTATE, SETTINGSMENUSTATE, SINGLEPLAYER_GAMESTATE, MULTIPLAYER_GAMESTATE, PROFILE_STATE;
	}
	
	//FIELDS
	private Map<States, State> states = new EnumMap<Specular.States, State>(States.class);
	
	public Preferences prefs;
	public OrthographicCamera camera;
	public SpriteBatch batch;
	public ShapeRenderer shape;
	
	@Override
	public void create() {
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		prefs = Gdx.app.getPreferences("Preferences");
		
		states.put(States.MAINMENUSTATE, new MainmenuState(this));
		states.put(States.SETTINGSMENUSTATE, new SettingsMenuState(this));
		states.put(States.SINGLEPLAYER_GAMESTATE, new SingleplayerGameState(this));
		states.put(States.MULTIPLAYER_GAMESTATE, new MultiplayerGameState(this));
		states.put(States.PROFILE_STATE, new HighscoreState(this));
		
		enterState(States.MAINMENUSTATE);
	}
	
	public void enterState(States state) {
		State s = states.get(state);
		if(s != null) {
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
		prefs.flush();
	}
}