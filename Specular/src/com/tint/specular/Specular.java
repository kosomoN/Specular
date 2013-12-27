package com.tint.specular;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.SingleplayerGameState;
import com.tint.specular.states.Facebook;
import com.tint.specular.states.HighscoreState;
import com.tint.specular.states.MainmenuState;
import com.tint.specular.states.State;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class Specular extends Game {
	public enum States {
		MAINMENUSTATE, SETTINGSMENUSTATE, SINGLEPLAYER_GAMESTATE, MULTIPLAYER_GAMESTATE, PROFILE_STATE;
	}
	
	private static final int WIDTH = 1920, HEIGHT = 1080;
	
	public static Facebook facebook;
	
	//FIELDS
	private Map<States, State> states = new EnumMap<Specular.States, State>(States.class);
	
	public Preferences prefs;
	public static OrthographicCamera camera;
	public SpriteBatch batch;
	
	public Specular(Facebook facebook) {
		Specular.facebook = facebook;
	}
	
	@Override
	public void create() {
		
		float displayAspectRatio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		float cameraAspectRatio = (float) (WIDTH/ HEIGHT);
		float w, h;
		if(displayAspectRatio > cameraAspectRatio) {
			h = HEIGHT;
			w = HEIGHT * displayAspectRatio;
		} else {
			w = WIDTH;
			h = WIDTH * Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
		}
		camera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();
		prefs = Gdx.app.getPreferences("Preferences");
		
		Gdx.input.setCatchBackKey(true);
		
		states.put(States.MAINMENUSTATE, new MainmenuState(this));
//		states.put(States.SETTINGSMENUSTATE, new SettingsMenuState(this));
		states.put(States.SINGLEPLAYER_GAMESTATE, new SingleplayerGameState(this));
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
		prefs.flush();
	}
}