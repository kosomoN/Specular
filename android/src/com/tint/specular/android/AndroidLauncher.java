package com.tint.specular.android;

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
import com.tint.specular.Specular;
import com.tint.specular.states.NativeAndroid;
import com.tint.specular.ui.HighscoreList.Highscore;

public class AndroidLauncher extends AndroidApplication {
	
    private GraphUser fbuser;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final Activity activity = this;
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = false;
        cfg.useCompass = false;
        cfg.useImmersiveMode = true;
        
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
			public boolean login(final RequestCallback callback) {
				final Session.StatusCallback sessioinCallback = new Session.StatusCallback() {
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
				                        callback.success();
				                    } else {
				                    	Toast.makeText(getApplicationContext(), "User is null " + response.getError(), Toast.LENGTH_LONG).show();
				                    	Log.e("Specular", "User is null " + response.getError());
				                    	callback.failed();
				                    }
								}
							}).executeAsync();
							
						} else if(state.equals(SessionState.CLOSED) || state.equals(SessionState.CLOSED_LOGIN_FAILED)) {
							fbuser = null;
							//If failed
							Log.e("Specular Facebook", "Login failed", exception);
							callback.failed();
						}
					}
				};
				AndroidLauncher.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Session.openActiveSession(activity, false, sessioinCallback);
						Session.openActiveSession(activity, true, sessioinCallback);
					}
				});
				
            	
				return true;
			}
			
			@Override
			public boolean isLoggedIn() {
				return Session.getActiveSession() != null ? Session.getActiveSession().isOpened() && fbuser != null : false;
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
					AndroidLauncher.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							session.requestNewPublishPermissions(permissionRequest);
						}
					});
				} else {
					AndroidLauncher.this.runOnUiThread(new Runnable() {
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
			

			private void sendHighscore(final int score, Session session) {
				//Send a highscore post request
				Bundle b = new Bundle();
				b.putString("access_token", session.getAccessToken());
				b.putInt("score", score);
				new Request(session, fbuser.getId() + "/scores", b, HttpMethod.POST, new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						boolean success = response.getGraphObject().getInnerJSONObject().toString().contains("true");
						if(success)
							Toast.makeText(getApplicationContext(), "Highscore posting successful", Toast.LENGTH_LONG).show();
						else
							Toast.makeText(getApplicationContext(), "Highscore posting failed: " + response.getError(), Toast.LENGTH_LONG).show();
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
						if(response.getError() == null) {
							Log.i("Specular", "Highscore get response: " + response);
							List<Highscore> scores = new ArrayList<Highscore>();
							
							//Read scores from list
							for(GraphObject go : response.getGraphObject().getPropertyAsList("data", GraphObject.class)) {
								String name = go.getPropertyAs("user", GraphObject.class).getProperty("name").toString();
								System.out.println(fbuser);
								if(go.getPropertyAs("user", GraphObject.class).getProperty("id").equals(fbuser.getId())) {
									Specular.prefs.putInteger("Highscore", (Integer) go.getProperty("score"));
									Specular.prefs.flush();
								}
								scores.add(new Highscore(name, (Integer) go.getProperty("score")));
							}
							Highscore[] array = scores.toArray(new Highscore[scores.size()]);
							highscoreCallback.gotHighscores(array);
						} else {
							Log.e("Specular", "Highscore load error: " + response.getError());
							highscoreCallback.gotHighscores(null);
						}
					}
				});
				
				//The request has to be run on the UI thread or bad things happen
				AndroidLauncher.this.runOnUiThread(new Runnable() {
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
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
    
}