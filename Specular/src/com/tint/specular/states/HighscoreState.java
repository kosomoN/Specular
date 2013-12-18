package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tint.specular.Specular;

public class HighscoreState extends State {
	
	private Stage stage;
	private boolean shouldUpdate;
	private Object[] oldScores = new String[] {"Loading..."};
	
	public HighscoreState(Specular game) {
		super(game);
	}

	@Override
	public void render(float delta) {
		if(shouldUpdate) {
			shouldUpdate = false;
			createUi();
		}

		if(Gdx.input.isKeyPressed(Keys.BACK))
			game.enterState(Specular.States.MAINMENUSTATE);
		
		Gdx.gl.glClearColor(0.2f, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act();
		stage.draw();
		
//		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		super.show();
		createUi();
	}
	
	private void createUi() {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, game.batch);
		
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		if(Specular.facebook.isLoggedIn()) {
			
			final List list = new List(oldScores, skin);
			list.setSelectable(false);
			list.setSelectedIndex(-1);
			
			Specular.facebook.getHighScores(new Facebook.HighscoreCallback() {

				@Override
				public void gotHighscores(Object[] scores) {
					if(scores != null) {
						oldScores = scores;
						list.setItems(scores);
						list.setSelectedIndex(-1);
					} else {
						list.setItems(new String[] {"Failed to load highscores"});
					}
				}
				
			});
			
			ScrollPane sp = new ScrollPane(list);
			sp.getStyle().background = skin.getDrawable("black");
			sp.setSize(Gdx.graphics.getWidth() / 2 - 37, Gdx.graphics.getHeight() - 50);
			sp.setPosition(Gdx.graphics.getWidth() / 2 + 25 / 2, 25);
	
			Table table = new Table();
			table.setSize(Gdx.graphics.getWidth() / 2 - 37, Gdx.graphics.getHeight() - 50);
			table.setPosition(25, 25);
			table.setSkin(skin);
			
			table.setBackground("black");
			table.left().top();
			table.add("Test user").left().row();
			
			table.add("Highscore: 2214").row().padLeft(30);
			table.add("Testing: 346234534514").row().padLeft(30);
			table.add("More testing: 24534534").row().padLeft(30);
			stage.addActor(table);
			stage.addActor(sp);
		} else {
			TextButton loginBtn = new TextButton("Login", skin);
			loginBtn.setSize(300, 100);
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
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void dispose() {
		super.dispose();
		
		stage.dispose();
	}
	
	
}
