package com.tint.specular.states;

import com.tint.specular.ui.HighscoreList.Highscore;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public interface NativeAndroid {
	public boolean login(RequestCallback loginCallback);
	public void logout();
	public boolean isLoggedIn();
	public boolean postHighscore(int score);
	
	public interface RequestCallback {
		public void success();
		public void failed();
	}
	
	public interface HighscoreCallback {
		public void gotHighscores(Highscore[] scores);
	}
	public void getHighScores(HighscoreCallback highscoreCallback);
	
	public void sendAnalytics(String category, String action, String label, Long value);
	public String getVersionName();
}
