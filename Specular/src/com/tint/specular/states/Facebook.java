package com.tint.specular.states;

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
		public void gotHighscores(Object[] scores);
	}
	public void getHighScores(HighscoreCallback highscoreCallback);
}
