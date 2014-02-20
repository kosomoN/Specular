package com.tint.specular;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.tint.specular.states.NativeAndroid;

public class MainActivity extends AndroidApplication {
	
    private GraphUser fbuser;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final Activity activity = this;
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;
        cfg.useAccelerometer = false;
        cfg.useCompass = false;
        
        //When running dry no data is sent to the Google servers
        GoogleAnalytics.getInstance(this).setDryRun(true);
        
        //Check if user has previously logged in and in that case log in again
        Session.openActiveSession(activity, false, null);
        if(Session.getActiveSession() != null && Session.getActiveSession().isOpened()) {
        	
        	//Request user information
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
        	
        initialize(new Specular(new NativeAndroid() {
        	
			@Override
			public boolean login(final LoginCallback callback) {
				Session.StatusCallback sessioinCallback = new Session.StatusCallback() {
					@Override
					public void call(final Session session, SessionState state, Exception exception) {
						Log.i("Specular", state.toString());
						//If logged in
						if(state.equals(SessionState.OPENED) && fbuser != null) {
							System.out.println("Opened");
							
							//Request user information
							Request.newMeRequest(session, new Request.GraphUserCallback() {
								@Override
								public void onCompleted(GraphUser user, Response response) {
									if (user != null) {
										fbuser = user;
				                        Toast.makeText(getApplicationContext(), "Hello " + user.getName() + "!", Toast.LENGTH_LONG).show();
				                        callback.loginSuccess();
				                    } else {
				                    	Toast.makeText(getApplicationContext(), "User is null " + response.getError(), Toast.LENGTH_LONG).show();
				                    	Log.e("Specular", "User is null " + response.getError());
				                    	callback.loginFailed();
				                    }
								}
							}).executeAsync();
							
						} else if(state.equals(SessionState.CLOSED) || state.equals(SessionState.CLOSED_LOGIN_FAILED)) {
							fbuser = null;
							//If failed
							Log.e("Specular Facebook", "Login failed", exception);
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
			public boolean postHighscore(final int score) {
				final Session session = Session.getActiveSession();
				
				//Checking for publish permissions
				if(!session.getPermissions().contains("publish_actions")) {
					final NewPermissionsRequest permissionRequest = new NewPermissionsRequest(activity, "publish_actions");
					permissionRequest.setCallback(new StatusCallback() {
						@Override
						public void call(Session session, SessionState state, Exception exception) {
							if(!session.getPermissions().contains("publish_actions")) {
								Toast.makeText(getApplicationContext(), "Highscore posting failed: No publish permission", Toast.LENGTH_LONG).show();
							} else {
								checkAndSendHighscore(session, score);
							}
						}
					});
					
					//The request has to be run on the UI thread or bad things happen
					MainActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							session.requestNewPublishPermissions(permissionRequest);
						}
					});
				} else {
					MainActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							checkAndSendHighscore(session, score);
						}
					});
				}
				
				return true;
			}
			
			private void checkAndSendHighscore(final Session session, final int score) {
				if(fbuser == null) {
					Toast.makeText(getApplicationContext(), "Highscore posting failed: User is null", Toast.LENGTH_LONG).show();
				} else {
					//Request own score
					Bundle b = new Bundle();
					b.putString("access_token", session.getAccessToken());
					new Request(session, fbuser.getId() + "/scores", b, HttpMethod.GET, new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							try {
								
								//Read score from JSON
								JSONArray jsonArray = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
								long oldScore = 0;
								
								//Check if the user has a previous score
								if(jsonArray.length() > 0)
									oldScore = (Integer) ((JSONObject) jsonArray.get(0)).get("score");
								
								//Send if new highscore
								if(oldScore < score) {
									sendHighscore(score, session);
								}
									
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}).executeAsync();
				}
			}
			

			private void sendHighscore(int score, Session session) {
				//Send a highscore post request
				Bundle b = new Bundle();
				b.putString("access_token", session.getAccessToken());
				b.putInt("score", score);
				new Request(session, fbuser.getId() + "/scores", b, HttpMethod.POST, new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						Toast.makeText(getApplicationContext(), "Highscore post response: " + response.getGraphObject().getInnerJSONObject().toString().contains("true"), Toast.LENGTH_LONG).show();
					}
				}).executeAsync();
			}

			@Override
			public void getHighScores(final HighscoreCallback highscoreCallback) {
				//Send highscore get request
				Session session = Session.getActiveSession();
				Bundle b = new Bundle();
				b.putString("access_token", session.getAccessToken());
				final Request r = new Request(session, session.getApplicationId() + "/scores", b, HttpMethod.GET, new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						Log.i("Specular", "Highscore get response: " + response);
						List<String> scores = new ArrayList<String>();
						
						//Read scores from list
						for(GraphObject go : response.getGraphObject().getPropertyAsList("data", GraphObject.class)) {
							scores.add(go.getPropertyAs("user", GraphObject.class).getProperty("name") + ": " + go.getProperty("score"));
						}
						String[] array = scores.toArray(new String[scores.size()]);
						highscoreCallback.gotHighscores(array);
					}
				});
				
				//The request has to be run on the UI thread or bad things happen
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

			@Override
			public void sendAnalytics(String category, String action, String label, Long value) {
				Gdx.app.log("Specular", "Sending analytics: " + category + " + "+ action + " + " + label + " + " + value);
				EasyTracker.getInstance(MainActivity.this).send(MapBuilder.createEvent(category, action, label, value).build());
			}

			@Override
			public String getVersionName() {
				try {
					return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
				} catch (NameNotFoundException e) {
					e.printStackTrace();
					return "NaN";
				}
			}
        }), cfg);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

	@Override
	protected void onStart() {
		super.onStart();
		//Google analytics
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		//Google analytics
		EasyTracker.getInstance(this).activityStop(this);
	}
    
}