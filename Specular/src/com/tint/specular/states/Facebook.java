package com.tint.specular.states;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public interface Facebook {
	public boolean login(LoginCallback loginCallback);
	public void logout();
	public boolean isLoggedIn();
	public boolean postHighscore(int score);
	
	public interface LoginCallback {
		public void loginSuccess();
		public void loginFailed();
	}
	
	public interface HighscoreCallback {
		public void gotHighscores(String[] scores);
	}
	public void getHighScores(HighscoreCallback highscoreCallback);
}
