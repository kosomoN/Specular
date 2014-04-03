package com.tint.specular;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.SingleplayerGameState;
import com.tint.specular.states.ControlSetupState;
import com.tint.specular.states.HighscoreState;
import com.tint.specular.states.LoadingState;
import com.tint.specular.states.MainmenuState;
import com.tint.specular.states.NativeAndroid;
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
	
	public static NativeAndroid nativeAndroid;
	
	private Map<States, State> states = new EnumMap<Specular.States, State>(States.class);
	
	public static OrthographicCamera camera;
	public static Preferences prefs;
	public SpriteBatch batch;
	
	
	public Specular(NativeAndroid facebook) {
		Specular.nativeAndroid = facebook;
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
		checkPreferences(new ArrayList<Integer>());
		Gdx.input.setCatchBackKey(true);
		
		
		states.put(States.LOADINGSTATE, new LoadingState(this));
		
		//Enter loading screen state before main menu state
		enterState(States.LOADINGSTATE);
	}
	
	/**
	 * Checks if everything is okay with the preferences and the program is good to go
	 */
	private void checkPreferences(List<Integer> ints) {
		int prefsFailureIndex = 0;
		try {
			/* Checking that the entries exists and are of valid type,
			 * booleans doesn't need to be validated because in case of wrong type they return false
			 * MUST BE KEPT IN SAME ORDER AS IN 'CREATEPREFERENCES(List<Integer> i)'!
			 */
			if(ints != null) {
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Sensitivity"))
						throw new NoPreferenceKeyException("Sensitivity missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Move Stick Pos X"))
						throw new NoPreferenceKeyException("Move Stick Pos X missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Move Stick Pos Y"))
						throw new NoPreferenceKeyException("Move Stick Pos Y missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Shoot Stick Pos X"))
						throw new NoPreferenceKeyException("Shoot Stick Pos X missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Shoot Stick Pos Y"))
						throw new NoPreferenceKeyException("Shoot Stick Pos Y missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Highscore"))
						throw new NoPreferenceKeyException("Highscore missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Time Played Ticks"))
						throw new NoPreferenceKeyException("Time Played Ticks missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Games Played"))
						throw new NoPreferenceKeyException("Games Played missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Bullets Fired"))
						throw new NoPreferenceKeyException("Bullets Fired missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Bullets Missed"))
						throw new NoPreferenceKeyException("Shots Missed missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Enemies Killed"))
						throw new NoPreferenceKeyException("Enemies Killed missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Tilt"))
						throw new NoPreferenceKeyException("Enemies Killed missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Static"))
						throw new NoPreferenceKeyException("Enemies Killed missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("Particles"))
						throw new NoPreferenceKeyException("Enemies Killed missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("MusicMuted"))
						throw new NoPreferenceKeyException("Enemies Killed missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("SoundsMuted"))
						throw new NoPreferenceKeyException("Enemies Killed missing");
				prefsFailureIndex++;
				
				if(!ints.contains(prefsFailureIndex))
					if(!prefs.contains("FirstTime"))
						throw new NoPreferenceKeyException("Enemies Killed missing");
				prefsFailureIndex++;
				
				// Create new ones if there was any errors, error means List<Integer> ints != null
				createPreferences(ints);
			} else {
				checkPreferences(new ArrayList<Integer>());
			}
		} catch(NumberFormatException nfe) {
			List<Integer> tempInts = new ArrayList<Integer>();
			tempInts.addAll(ints);
			tempInts.add(prefsFailureIndex);
			checkPreferences(tempInts);
			nfe.printStackTrace();
		} catch (NoPreferenceKeyException e) {
			List<Integer> tempInts = new ArrayList<Integer>();
			tempInts.addAll(ints);
			tempInts.add(prefsFailureIndex);
			checkPreferences(tempInts);
			System.out.println(prefsFailureIndex);
			e.printStackTrace();
		} 
	}
	
	/**
	 * Creates default preferences
	 */
	private void createPreferences(List<Integer> indexInts) {
		// MUST BE KEPT IN SAME ORDER AS IN 'CHECKPREFERENCES(List<Integer> i)'!
		// Preferences that might have errors and if so, replace with default value
		int i = 0;
		if(indexInts.contains(i))
			prefs.putFloat("Sensitivity", 1f);
		i++;
		
		if(indexInts.contains(i))
			prefs.putFloat("Move Stick Pos X", -camera.viewportWidth / 6 * 2);
		i++;
		
		if(indexInts.contains(i))
			prefs.putFloat("Move Stick Pos Y", -camera.viewportHeight / 6);
		i++;
		
		if(indexInts.contains(i))
			prefs.putFloat("Shoot Stick Pos X", camera.viewportWidth / 6 * 2);
		i++;
		
		if(indexInts.contains(i))
			prefs.putFloat("Shoot Stick Pos Y", -camera.viewportHeight / 6);
		i++;
		
		if(indexInts.contains(i))
			prefs.putInteger("Highscore", 0);
		i++;
		
		if(indexInts.contains(i))
			prefs.putInteger("Time Played Ticks", 0);
		i++;
		
		if(indexInts.contains(i))
			prefs.putInteger("Games Played", 0);
		i++;
		
		if(indexInts.contains(i))
			prefs.putInteger("Bullets Fired", 0);
		i++;
		
		if(indexInts.contains(i))
			prefs.putInteger("Bullets Missed", 0);
		i++;
		
		if(indexInts.contains(i))
			prefs.putInteger("Enemies Killed", 0);
		i++;
		
		if(indexInts.contains(i))
			prefs.putBoolean("Tilt", false);
		i++;
		
		if(indexInts.contains(i))
			prefs.putBoolean("Static", true);
		i++;
		
		if(indexInts.contains(i))
			prefs.putBoolean("Particles", true);
		i++;
		
		if(indexInts.contains(i))
			prefs.putBoolean("MusicMuted", false);
		i++;
		
		if(indexInts.contains(i))
			prefs.putBoolean("SoundsMuted", false);
		i++;
		
		if(indexInts.contains(i))
			prefs.putBoolean("FirstTime", true);
		i++;
		
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
	
	private class NoPreferenceKeyException extends Exception {
		private static final long serialVersionUID = 1L;
		public NoPreferenceKeyException(String message) { super(message); }
	}
}