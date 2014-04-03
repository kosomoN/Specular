package com.tint.specular.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tint.specular.Specular;
import com.tint.specular.states.NativeAndroid;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class DesktopLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Specular";
		cfg.width = 1280;
		cfg.height = 720;
		
		//There is no need to implement Facebook highscores in the desktop-version, so these are left empty
		new LwjglApplication(new Specular(new NativeAndroid() {

			private boolean isLoggedIn = false;
			
			@Override
			public boolean login(RequestCallback loginCallback) {
				isLoggedIn = true;
				loginCallback.success();
				return false;
			}

			@Override
			public boolean isLoggedIn() {
				return isLoggedIn;
			}

			@Override
			public boolean postHighscore(int score) {
				Specular.prefs.putInteger("Highscore", score);
				Specular.prefs.flush();
				return false;
			}

			@Override
			public void getHighScores(HighscoreCallback highscoreCallback) {
			}

			@Override
			public void logout() {
				
			}

			@Override
			public String getVersionName() {
				return "Dev";
			}

			@Override
			public void sendAnalytics(String category, String action, String label, Long value) {
				Gdx.app.log("Specular", "Sending analytics: " + category + " + "+ action + " + " + label + " + " + value);
			}
			
		}), cfg);
	}
}
