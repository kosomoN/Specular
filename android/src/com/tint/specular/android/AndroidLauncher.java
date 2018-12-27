package com.tint.specular.android;

import android.content.pm.PackageManager;
import android.os.Bundle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tint.specular.Specular;
import com.tint.specular.states.NativeAndroid;

public class AndroidLauncher extends AndroidApplication {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = false;
        cfg.useCompass = false;
        cfg.useImmersiveMode = true;

        initialize(new Specular(new NativeAndroid() {

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
			public boolean postHighscore(int score, boolean requestPublishPermission) {
				Specular.prefs.putInteger("Highscore", score);
				Specular.prefs.flush();
				return false;
			}

			@Override
			public void getHighScores(HighscoreCallback highscoreCallback) { }

			@Override
			public void logout() { }

			@Override
			public String getVersionName() {
                try {
                    return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    return "Dev";
                }
            }

			@Override
			public void sendAnalytics(String category, String action, String label, Long value) {
			    String msg = "Sending analytics: " + category + " + "+ action + " + " + label + " + " + value;
				Gdx.app.log("Specular", msg);
			}
		}), cfg);
    }
}