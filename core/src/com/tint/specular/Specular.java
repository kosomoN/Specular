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
		LOADINGSTATE, MAINMENUSTATE, SETTINGSMENUSTATE, CONTROLSETUPSTATE, SINGLEPLAYER_GAMESTATE, MULTIPLAYER_GAMESTATE, PROFILE_STATE, UPGRADESTATE;
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
			"Swarm Upgrade Grade", "Repulsor Upgrade Grade", "Ricochet Upgrade Grade", "Slowdown Upgrade Grade", "Boardshock Upgrade Grade"};
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
		checkPreferences(0);
		Gdx.input.setCatchBackKey(true);
		
		
		states.put(States.LOADINGSTATE, new LoadingState(this));
		
		//Enter loading screen state before main menu state
		enterState(States.LOADINGSTATE);
	}
	
	/**
	 * Checks if everything is okay with the preferences and the program is good to go.
	 * To reset a specific preference, use the list in the parameters to add the indexes for them.
	 */
	private void checkPreferences(int index) {
		List<Integer> missingIndexes = new ArrayList<Integer>();
		
		/* Checking that the entries exists and are of valid type
		 * Check order should stay same in both checkPreferences and createPreferences [int, float, boolean]
		 */
		
		try {
			// Create new ones if there was any errors, error means List<Integer> ints == null or if an exception is thrown; it's handled below
			for(; index < iPrefs.length; index++) {
				if(!prefs.contains(iPrefs[index])) {
					missingIndexes.add(index);
					
					System.err.println(iPrefs[index] + " missing");
				}
				prefs.getInteger(iPrefs[index]);
			}
			
			for(; index - iPrefs.length < fPrefs.length; index++) {
				if(!prefs.contains(fPrefs[index - iPrefs.length])) {
					missingIndexes.add(index);
					System.err.println(fPrefs[index - iPrefs.length] + " missing");
				}
				prefs.getFloat(fPrefs[index - iPrefs.length]);
			}
			
			for(; index - fPrefs.length - iPrefs.length < bPrefs.length; index++) {
				if(!prefs.contains(bPrefs[index - fPrefs.length - iPrefs.length])) {
					missingIndexes.add(index);
					System.err.println(bPrefs[index - fPrefs.length - iPrefs.length] + " missing");
				}
				prefs.getBoolean(bPrefs[index - iPrefs.length - fPrefs.length]);
			}
			
			if(missingIndexes.isEmpty())
				System.out.println("No preferences missing");
			
		} catch(NumberFormatException e) {
			missingIndexes.add(index);
			index++;
			checkPreferences(index);
			e.printStackTrace();
		}
		
		createPreferences(missingIndexes);
	}
	
	/**
	 * Creates default preferences for those who has errors
	 */
	private void createPreferences(List<Integer> missingIndexes) {
		// Preferences that might have errors and if so, replace with default value. Else, don't do anything
		int i = 0;
		int index = 0;
		for(i = 0; i < missingIndexes.size(); i++) {
			index = missingIndexes.get(i);
			setDefaultPrefsValue(index);
		}
		
		// Checking if there is too many preferences than there should and removes the unnecessary ones
		// Duplicates isn't handled here but automatically removed (The last value stays)
		Set<String> keys = prefs.get().keySet();
		System.out.println("Preferences changed: " + missingIndexes.size());
		System.out.println("Current prefs size: " + keys.size() + " | Right prefs size: " + (iPrefs.length + fPrefs.length + bPrefs.length));
		if(keys.size() > iPrefs.length + fPrefs.length + bPrefs.length) {
			iteration:
			for(Iterator<String> it = keys.iterator(); it.hasNext();) {
				int a = 0;
				String s = it.next();
				for(; a < iPrefs.length; a++) {
					if(iPrefs[a].equals(s)) {
						continue iteration;
					}
				}
				
				for(; a - iPrefs.length < fPrefs.length; a++) {
					if(fPrefs[a - iPrefs.length].equals(s)) {
						continue iteration;
					}
				}
				
				for(; a - iPrefs.length - fPrefs.length < bPrefs.length; a++)
					if(bPrefs[a - iPrefs.length - fPrefs.length].equals(s)) {
						continue iteration;
					}
				
				System.err.println("Key: " + s + " and its value removed");
				prefs.remove(s);
			}
		}
		
		//Needed for android to save the settings
		prefs.flush();
	}
	
	private void setDefaultPrefsValue(int index) {
		if(index < iPrefs.length) {
			// Graphics settings (High on by default) 0 = low, 1 = medium, 2 = high
			prefs.putInteger(iPrefs[index], index == 0 ? 2 : 0); // First element in String array is graphics
		} else if(index - iPrefs.length < fPrefs.length) {
			index -= iPrefs.length;
			
			float x = -camera.viewportWidth / 6 * 2;
			float y = -camera.viewportHeight / 6;
			
			// The first four elements in float prefs array are stick positions
			if(index < 4) {
				if(index % 2 == 1) {
					prefs.putFloat(fPrefs[index], y);
				} else {
					if(index == 2)
						prefs.putFloat(fPrefs[index], -x);
					else
						prefs.putFloat(fPrefs[index], x);
				}
			}
			// All the others
			else if(index == 4) {
				prefs.putFloat(fPrefs[index], 1f); // Sensitivity
			} else {
				prefs.putFloat(fPrefs[index], 0);
			}
			index += iPrefs.length;
			
		} else if(index - iPrefs.length - fPrefs.length < bPrefs.length) {
			int trueBooleans = 3; // This must be checked when booleans are added
			
			index -= (iPrefs.length + fPrefs.length);
			prefs.putBoolean(bPrefs[index], index < trueBooleans); // The first three booleans have a default value of true, rest of them false
			index += (iPrefs.length + fPrefs.length);
		}
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
}