package com.tint.specular;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tint.specular.states.Facebook;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Specular";
		cfg.useGL20 = false;
		cfg.width = 1280;
		cfg.height = 720;
		
		//There is no need to implement Facebook highscores in the desktop-version, so these are left empty
		new LwjglApplication(new Specular(new Facebook() {

			@Override
			public boolean login(LoginCallback loginCallback) {
				loginCallback.loginFailed();
				return false;
			}

			@Override
			public boolean isLoggedIn() {
				return false;
			}

			@Override
			public boolean postHighscore(int score) {
				return false;
			}

			@Override
			public void getHighScores(HighscoreCallback highscoreCallback) {
				
			}

			@Override
			public void logout() {
				
			}
			
		}), cfg);
	}
}
