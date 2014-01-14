package com.tint.specular;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.tint.specular.states.Facebook;

public class MainActivity extends AndroidApplication {
	
    private GraphUser fbuser;
    
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final Activity activity = this;
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        cfg.useAccelerometer = false;
        cfg.useCompass = false;
        
        Session.openActiveSession(activity, false, null);
        if(Session.getActiveSession() != null && Session.getActiveSession().isOpened()) {
			Request.newMeRequest(Session.getActiveSession(), new Request.GraphUserCallback() {
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (user != null) {
						fbuser = user;
                        Toast.makeText(getApplicationContext(), "Hello " + user.getName() + "!", Toast.LENGTH_LONG).show();
                    } else {
                    	Toast.makeText(getApplicationContext(), "User is null " + response.getError(), Toast.LENGTH_LONG).show();
                    	Log.e("Specular", "User is null " + response.getError());
                    }
				}
			}).executeAsync();
        }
        	
        initialize(new Specular(new Facebook() {
        	
			@Override
			public boolean login(final LoginCallback callback) {
				Session.StatusCallback sessioinCallback = new Session.StatusCallback() {
					@Override
					public void call(final Session session, SessionState state, Exception exception) {
						Log.i("Specular", state.toString());
						if (state.equals(SessionState.OPENED)) {
							System.out.println("Opened");
							Request.newMeRequest(session, new Request.GraphUserCallback() {
								@Override
								public void onCompleted(GraphUser user, Response response) {
									if (user != null) {
										fbuser = user;
				                        Toast.makeText(getApplicationContext(), "Hello " + user.getName() + "!", Toast.LENGTH_LONG).show();
				                    } else {
				                    	Toast.makeText(getApplicationContext(), "User is null " + response.getError(), Toast.LENGTH_LONG).show();
				                    	Log.e("Specular", "User is null " + response.getError());
				                    }
								}
							}).executeAsync();
							callback.loginSuccess();
						} else if(state.equals(SessionState.CLOSED) || state.equals(SessionState.CLOSED_LOGIN_FAILED)) {
							callback.loginFailed();
						}
					}
				};

				Session.openActiveSession(activity, true, sessioinCallback);
            	
				return true;
			}
			
			@Override
			public boolean isLoggedIn() {
				return Session.getActiveSession() != null ? Session.getActiveSession().isOpened() : false;
			}

			@Override
			public boolean postHighscore(int score) {
				Session session = Session.getActiveSession();
				if(!session.getPermissions().contains("publish_actions")) {
					session.requestNewPublishPermissions(new NewPermissionsRequest(activity, "publish_actions"));
					if(!session.getPermissions().contains("publish_actions")) {
						return false;
					}
				}
				if(fbuser == null) {
					System.err.println("User is null");
					return false;
				} else {
					Bundle b = new Bundle();
					b.putString("access_token", session.getAccessToken());
					b.putInt("score", score);
					final Request r = new Request(session, fbuser.getId() + "/scores", b, HttpMethod.POST, new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							Log.i("Specular", "Highscore post response: " + response);
						}
					});
					
					MainActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							r.executeAsync();
						}
					});
				}
				
				return true;
			}

			@Override
			public void getHighScores(final HighscoreCallback highscoreCallback) {
				
				Session session = Session.getActiveSession();
				Bundle b = new Bundle();
				b.putString("access_token", session.getAccessToken());
				final Request r = new Request(session, session.getApplicationId() + "/scores", b, HttpMethod.GET, new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						Log.i("Specular", "Highscore get response: " + response);
						List<String> scores = new ArrayList<String>();
						for(GraphObject go : response.getGraphObject().getPropertyAsList("data", GraphObject.class)) {
							scores.add(go.getPropertyAs("user", GraphObject.class).getProperty("name") + ": " + go.getProperty("score"));
						}
						String[] array = scores.toArray(new String[scores.size()]);
						highscoreCallback.gotHighscores(array);
					}
				});
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						r.executeAsync();
					}
				});
			}

			@Override
			public void logout() {
				Session.getActiveSession().closeAndClearTokenInformation();
			}
        }), cfg);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}