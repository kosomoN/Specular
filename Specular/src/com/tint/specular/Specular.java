package com.tint.specular;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.SingleplayerGameState;
import com.tint.specular.states.ControlSetupState;
import com.tint.specular.states.Facebook;
import com.tint.specular.states.HighscoreState;
import com.tint.specular.states.LoadingState;
import com.tint.specular.states.MainmenuState;
import com.tint.specular.states.SettingsMenuState;
import com.tint.specular.states.State;



/**
 * 
 * @author Onni Kosomaa
 *
 */

public class Specular extends Game {
	public enum States {
		LOADINGSTATE, MAINMENUSTATE, SETTINGSMENUSTATE, CONTROLSETUPSTATE, SINGLEPLAYER_GAMESTATE, MULTIPLAYER_GAMESTATE, PROFILE_STATE;
	}
	
	private static final int WIDTH = 1920, HEIGHT = 1080;
	
	public static Facebook facebook;
	
	private Map<States, State> states = new EnumMap<Specular.States, State>(States.class);
	
	public static OrthographicCamera camera;
	public static Preferences prefs;
	public SpriteBatch batch;
	
	
	public Specular(Facebook facebook) {
		Specular.facebook = facebook;
	}
	
	@Override
	public void create() {
		float displayAspectRatio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		float cameraAspectRatio = (float) WIDTH / HEIGHT;
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
		
		prefs = Gdx.app.getPreferences("Specular Preferences");
		// Checks if the preferences are missing or it is the first time the app is run
		checkPreferences();
		Gdx.input.setCatchBackKey(true);
		
		
		states.put(States.LOADINGSTATE, new LoadingState(this));
		
		//Enter loading screen state before main menu state
		enterState(States.LOADINGSTATE);
	}
	
	/**
	 * Checks if everything is okay with the preferences and the program is good to go
	 */
	private void checkPreferences() {
		try {
			// Size needs to be changed if the amount of preferences or settings is changed
			if(prefs.get().keySet().size() == 11) {
				/* Checking that the entries are of valid type,
				 * booleans doesn't need to be checked because in case of wrong case they return false
				 */
				prefs.getFloat("Sensitivity");
				prefs.getFloat("Move Stick Pos X");
				prefs.getFloat("Move Stick Pos Y");
				prefs.getFloat("Shoot Stick Pos X");
				prefs.getFloat("Shoot Stick Pos Y");
			} else {
				createPreferences();
			}
		} catch(NumberFormatException nfe) {
			createPreferences();
		}
	}
	
	/**
	 * Creates default preferences
	 */
	private void createPreferences() {
		prefs.clear();
		
		prefs.putBoolean("Tilt", false);
		prefs.putBoolean("Static", false);
		prefs.putFloat("Sensitivity", 1f);
		prefs.putBoolean("Particles", true);
		prefs.putBoolean("MusicMuted", false);
		prefs.putBoolean("SoundsMuted", false);
		prefs.putBoolean("FirstTime", true);
		prefs.putFloat("Move Stick Pos X", -camera.viewportWidth / 6 * 2);
		prefs.putFloat("Move Stick Pos Y", -camera.viewportHeight / 6);
		
		prefs.putFloat("Shoot Stick Pos X", camera.viewportWidth / 6 * 2);
		prefs.putFloat("Shoot Stick Pos Y", -camera.viewportHeight / 6);
		
		//Needed for android to save the settings
		prefs.flush();
	}
	
	public void load() {
		states.put(States.MAINMENUSTATE, new MainmenuState(this));
		states.put(States.SETTINGSMENUSTATE, new SettingsMenuState(this));
		states.put(States.SINGLEPLAYER_GAMESTATE, new SingleplayerGameState(this));
		states.put(States.PROFILE_STATE, new HighscoreState(this));
		states.put(States.CONTROLSETUPSTATE, new ControlSetupState(this));
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
	}
	
@Override 
public void resize (int width, int height) {
	super.resize(width, height);
}

@Override
public void render() {
	super.render();
	
	
}


}