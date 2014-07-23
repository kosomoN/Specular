package com.tint.specular.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.tint.specular.Specular;
import com.tint.specular.states.NativeAndroid;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class DesktopLauncher {
	public static void main(String[] args) {
		Settings s = new Settings();
		s.paddingX = 0;
		s.paddingY = 0;
//		TexturePacker.process(s, "graphics/game/unpacked", "graphics/game/packed", "Specular");
		
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Specular";
		cfg.width = 1280;
		cfg.height = 680;
		
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
