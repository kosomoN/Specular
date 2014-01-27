package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tint.specular.Specular;
import com.tint.specular.ui.HighscoreList;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class HighscoreState extends State {
	
	private Stage stage;
	private Texture background;
	private boolean shouldUpdate;
	private HighscoreList list;
	private boolean isLoggedIn = false;
	
	public HighscoreState(Specular game) {
		super(game);
	}

	@Override
	public void render(float delta) {
		if(shouldUpdate) {
			shouldUpdate = false;
			createUi();
		}

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		if(isLoggedIn) {
			game.batch.setColor(Color.WHITE);
			game.batch.begin();
			game.batch.draw(background, 0, 0, Specular.camera.viewportWidth, Specular.camera.viewportHeight);
			game.batch.end();
		}
		stage.act();
		stage.draw();
		
		if(Gdx.input.isKeyPressed(Keys.BACK))
			game.enterState(Specular.States.MAINMENUSTATE);
	}

	@Override
	public void show() {
		super.show();
		createUi();
		background = new Texture(Gdx.files.internal("graphics/menu/highscore/Frames.png"));
		
		Specular.camera.position.set(Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight / 2, 0);
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
	}
	
	private void createUi() {
		stage = new Stage(Specular.camera.viewportWidth, Specular.camera.viewportHeight, false, game.batch);
		stage.setCamera(Specular.camera);
		
		if(Specular.facebook.isLoggedIn()) {
			isLoggedIn = true;
			list = new HighscoreList();
			
			Specular.facebook.getHighScores(new Facebook.HighscoreCallback() {

				@Override
				public void gotHighscores(String[] scores) {
					if(scores != null) {
						list.setItems(scores);
					} else {
						list.setItems(new String[] {"Failed to load highscores"});
					}
				}
			});
			
			ScrollPane sp = new ScrollPane(list);
			System.out.println(Specular.camera.viewportWidth + " " + Specular.camera.viewportHeight);
			sp.setSize(Specular.camera.viewportWidth * (930f / 1920), Specular.camera.viewportHeight);
			sp.setPosition(Specular.camera.viewportWidth * (850f / 1920), 0);
	
			stage.addActor(sp);
		} else {
			Image loginScreen = new Image(new Texture(Gdx.files.internal("graphics/menu/highscore/Login Background.png")));
			stage.addActor(loginScreen);
			
			Texture tex = new Texture(Gdx.files.internal("graphics/menu/highscore/Login.png"));
			Texture pressedTex = new Texture(Gdx.files.internal("graphics/menu/highscore/Login Pressed.png"));
			
			Button loginBtn = new Button(new TextureRegionDrawable(new TextureRegion(tex)), 
					new TextureRegionDrawable(new TextureRegion(pressedTex)));
			loginBtn.setPosition(1250, 15);
			
			loginBtn.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
                    Specular.facebook.login(new Facebook.LoginCallback() {
                        
                        @Override
                        public void loginSuccess() {
                                shouldUpdate = true;
                        }

                        @Override
                        public void loginFailed() {
                        }
                    });
				}
			});
			
			stage.addActor(loginBtn);
		}
		
		Texture tex = new Texture(Gdx.files.internal("graphics/menu/highscore/Back.png"));
		Texture pressedTex = new Texture(Gdx.files.internal("graphics/menu/highscore/Back Pressed.png"));
		
		Button backBtn = new Button(new TextureRegionDrawable(new TextureRegion(tex)), 
								new TextureRegionDrawable(new TextureRegion(pressedTex)));
		backBtn.setPosition(47, 0);
		
		backBtn.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.enterState(Specular.States.MAINMENUSTATE);
			}
		});
		stage.addActor(backBtn);
		
		Gdx.input.setInputProcessor(stage);
	}
	
	

	@Override
	public void hide() {
		super.hide();
		dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
		if(list != null)
			list.dispose();
		stage.dispose();
		background.dispose();
	}
}
