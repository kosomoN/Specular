package com.tint.specular;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.tint.specular.states.UpgradeState;


/**
 * 
 * @author Onni Kosomaa, Daniel Riissanen
 *
 */

public class Specular extends Game {
	public enum States {
		LOADINGSTATE, MAINMENUSTATE, SETTINGSMENUSTATE, CONTROLSETUPSTATE, SINGLEPLAYER_GAMESTATE, MULTIPLAYER_GAMESTATE, PROFILE_STATE, UPGRADESTATE, TUTORIALSTATE;
	}
	
	private static final int WIDTH = 1920, HEIGHT = 1080;
	
	public static NativeAndroid nativeAndroid;
	
	private Map<States, State> states = new EnumMap<Specular.States, State>(States.class);
	
	public static OrthographicCamera camera;
	public static Preferences prefs;
	public SpriteBatch batch;
	
	// Graphics has to be first, add new iPrefs last
	private String[] iPrefs = {"Graphics", "Highscore", "Time Played Ticks", "Games Played", "Bullets Fired", "Bullets Missed", "Enemies Killed",
			"Life Upgrade Grade", "Firerate Upgrade Grade", "Burst Upgrade Grade", "Beam Upgrade Grade", "Multiplier Upgrade Grade", "PDS Upgrade Grade", 
			"Swarm Upgrade Grade", "Repulsor Upgrade Grade", "Slowdown Upgrade Grade", "Boardshock Upgrade Grade"};
	// Add new fPrefs last
	private String[] fPrefs = {"Move Stick Pos X", "Move Stick Pos Y", "Shoot Stick Pos X", "Shoot Stick Pos Y", "Sensitivity", "Upgrade Points"};
	// The booleans which def value is true should be placed first
	private String[] bPrefs = {"First Time", "Static", "Particles", "MusicMuted", "SoundsMuted", "Tilt"};
	
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
	 * Checks if everything is okay with the preferences and the program is good to go.
	 * To reset a specific preference, use the list in the parameters to add the indexes for them.
	 */
	private void checkPreferences(List<Integer> failedIndexes) {
		int prefsFailureIndex = 0;
		
		try {
			/* Checking that the entries exists and are of valid type
			 * Check order should stay same in both checkPreferences and createPreferences [int, float, boolean]
			 */
			
			// Create new ones if there was any errors, error means List<Integer> ints == null or if an exception is thrown; it's handled below
			if(failedIndexes != null) {
				for(; prefsFailureIndex < iPrefs.length; prefsFailureIndex++) {
					if(!failedIndexes.contains(prefsFailureIndex))
						if(!prefs.contains(iPrefs[prefsFailureIndex]))
							throw new NoPreferenceKeyException(iPrefs[prefsFailureIndex] + " missing");
				}
				
				for(; prefsFailureIndex - iPrefs.length < fPrefs.length; prefsFailureIndex++) {
					if(!failedIndexes.contains(prefsFailureIndex))
						if(!prefs.contains(fPrefs[prefsFailureIndex - iPrefs.length]))
							throw new NoPreferenceKeyException(fPrefs[prefsFailureIndex - iPrefs.length] + " missing");
				}
				
				for(; prefsFailureIndex - fPrefs.length - iPrefs.length < bPrefs.length; prefsFailureIndex++) {
					if(!failedIndexes.contains(prefsFailureIndex))
						if(!prefs.contains(bPrefs[prefsFailureIndex - fPrefs.length - iPrefs.length]))
							throw new NoPreferenceKeyException(bPrefs[prefsFailureIndex - fPrefs.length - iPrefs.length] + " missing");
				}
				
				createPreferences(failedIndexes, prefsFailureIndex);
			} else {
				checkPreferences(new ArrayList<Integer>());
			}
		} catch(NumberFormatException nfe) {
			List<Integer> tempInts = new ArrayList<Integer>();
			tempInts.addAll(failedIndexes);
			tempInts.add(prefsFailureIndex);
			checkPreferences(tempInts);
			nfe.printStackTrace();
		} catch (NoPreferenceKeyException e) {
			List<Integer> tempInts = new ArrayList<Integer>();
			tempInts.addAll(failedIndexes);
			tempInts.add(prefsFailureIndex);
			checkPreferences(tempInts);
			System.out.println(prefsFailureIndex);
			e.printStackTrace();
		} 
	}
	
	/**
	 * Creates default preferences for those who has errors
	 */
	private void createPreferences(List<Integer> failedIndexes, int prefsSize) {
		// Preferences that might have errors and if so, replace with default value. Else, don't do anything
		int i = 0;
		
		// Creating integer preferences
		for(i = 0; i < iPrefs.length; i++) {
			if(failedIndexes.contains(i)) {
				// Graphics settings (High on by default) 0 = low, 1 = medium, 2 = high
				prefs.putInteger(iPrefs[i], i == 0 ? 2 : 0); // First element in String array is graphics
			}
		}
		
		float x = -camera.viewportWidth / 6 * 2;
		float y = -camera.viewportHeight / 6;
		// Creating floating point preferences
		for(int j = 0; j < fPrefs.length; j++) {
			// Stick Position 						NOTE: THIS IS NOT VERY GOOD!
			if(failedIndexes.contains(i)) {
				if(j < 4) {
					if(j % 2 == 1) {
						prefs.putFloat(fPrefs[j], y);
					} else {
						if(j == 2)
							prefs.putFloat(fPrefs[j], -x);
						else
							prefs.putFloat(fPrefs[j], x);
					}
				}
			} 
			// All the others
			else if(j == 4) {
				if(failedIndexes.contains(i)) {
					prefs.putFloat(fPrefs[j], 1f);
				}
			} else  {
				if(failedIndexes.contains(i)) {
					prefs.putFloat(fPrefs[j], 0);
				}
			}
			i++;
		}
		
		int trueBooleans = 3;
		// Creating boolean preferences
		for(int k = 0; k < fPrefs.length; k++) {
			if(failedIndexes.contains(i)) {
				prefs.putBoolean(bPrefs[k], k < trueBooleans);
			}
			i++;
		}

		// Checking if there is too many preferences than there should and removes the unnecessary ones
		// BUG! Still some wierd things happening but that doesn't interfere with anyhting important
		Set<String> keys = prefs.get().keySet();
		int a = 0;
		System.out.println(keys.size() + ", " + prefsSize);
		if(keys.size() > prefsSize) {
			iteration:
			for(Iterator<String> it = keys.iterator(); it.hasNext();) {
				String s = it.next();
				for(a = 0; a < iPrefs.length; a++)
					if(iPrefs[a].equals(s))
						continue iteration;
				
				for(a = 0; a < fPrefs.length; a++)
					if(fPrefs[a].equals(s))
						continue iteration;
				
				for(a = 0; a < bPrefs.length; a++)
					if(bPrefs[a].equals(s))
						continue iteration;
				
				System.err.println("Key: " + s + " and its value removed");
				prefs.remove(s);
			}
		}
		
		//Needed for android to save the settings
		prefs.flush();
	}
	
	public void load() {
		states.put(States.MAINMENUSTATE, new MainmenuState(this));
		states.put(States.SETTINGSMENUSTATE, new SettingsMenuState(this));
		states.put(States.SINGLEPLAYER_GAMESTATE, new SingleplayerGameState(this));
		states.put(States.PROFILE_STATE, new HighscoreState(this));
		states.put(States.CONTROLSETUPSTATE, new ControlSetupState(this));
		states.put(States.UPGRADESTATE, new UpgradeState(this));
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