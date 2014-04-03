package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tint.specular.Specular;
import com.tint.specular.ui.HighscoreList;
import com.tint.specular.ui.HighscoreList.Highscore;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class HighscoreState extends State {
	private static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzåäöABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
	
	private Stage stage;
	private Texture profileTex, highscoreBar;
	private boolean shouldUpdate;
	private HighscoreList list;
	private boolean isLoggedIn = false;
	private BitmapFont font;
	private String timePlayed, accuracy;
	
	public HighscoreState(Specular game) {
		super(game);
	}

	@Override
	public void render(float delta) {
		if(shouldUpdate) {
			shouldUpdate = false;
			createUi();
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(isLoggedIn) {
			game.batch.setColor(Color.WHITE);
			game.batch.begin();
			game.batch.draw(profileTex, 0, 0);
			game.batch.draw(highscoreBar, Specular.camera.viewportWidth * (800f / 1920), 0, highscoreBar.getWidth(), Specular.camera.viewportHeight);
			game.batch.draw(highscoreBar, Specular.camera.viewportWidth * (1780f / 1920), 0, highscoreBar.getWidth(), Specular.camera.viewportHeight);
			
			font.draw(game.batch, "Highscore:", 120, 850);
			font.draw(game.batch, String.valueOf(Specular.prefs.getInteger("Highscore")), 400, 850);
			
			font.draw(game.batch, "Time played:", 500 - font.getBounds("Time played:").width, 750);
			font.draw(game.batch, timePlayed, 515, 750);
			
			font.draw(game.batch, "Games:", 500 - font.getBounds("Games:").width, 700);
			font.draw(game.batch, String.valueOf(Specular.prefs.getInteger("Games Played")), 515, 700);
			
			font.draw(game.batch, "Shots fired:", 500 - font.getBounds("Shots fired:").width, 650);
			font.draw(game.batch, String.valueOf(Specular.prefs.getInteger("Bullets Fired")), 515, 650);
			
			font.draw(game.batch, "Accuracy:", 500 - font.getBounds("Accuracy:").width, 600);

			font.draw(game.batch, accuracy, 515, 600);
			
			font.draw(game.batch, "Enemies killed:", 500 - font.getBounds("Enemies killed:").width, 550);
			font.draw(game.batch, String.valueOf(Specular.prefs.getInteger("Enemies Killed")), 515, 550);
			
			game.batch.end();
		}
		stage.act();
		stage.draw();
	}

	@Override
	public void show() {
		super.show();
		
		FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Battlev2l.ttf"));
		FreeTypeFontParameter ftfp = new FreeTypeFontParameter();
		ftfp.size = 40;
		ftfp.characters = FONT_CHARACTERS;
		font = fontGen.generateFont(ftfp);
		font.setColor(new Color(0.96f, 0.05f, 0.05f, 1));
		
		createUi();
		
		ftfp.size = 32;
		font = fontGen.generateFont(ftfp);
		font.setColor(new Color(0.96f, 0.05f, 0.05f, 1));
		fontGen.dispose();
		
		profileTex = new Texture(Gdx.files.internal("graphics/menu/highscore/Profile.png"));
		highscoreBar = new Texture(Gdx.files.internal("graphics/menu/highscore/Bar.png"));
		
		Specular.camera.position.set(Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight / 2, 0);
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		
		((MainmenuState) game.getState(Specular.States.MAINMENUSTATE)).startMusic();
		
		int seconds = Specular.prefs.getInteger("Time Played Ticks") / 60;
		int hours = seconds / 3600;
		seconds -= hours * 3600;
		int minutes = seconds / 60;
		seconds -= minutes * 60;
		timePlayed = hours + ":" + minutes + ":" + seconds;
		
		float accuracy = 1 - (float) Specular.prefs.getInteger("Bullets Missed") / Specular.prefs.getInteger("Bullets Fired");
		accuracy = (int) (accuracy * 1000);
		accuracy /= 10;
		this.accuracy = accuracy + "%";
	}
	
	private void createUi() {
		stage = new Stage(new ScreenViewport(Specular.camera), game.batch);
		
		if(Specular.nativeAndroid.isLoggedIn()) {
			isLoggedIn = true;
			list = new HighscoreList(font);
			
			Specular.nativeAndroid.getHighScores(new NativeAndroid.HighscoreCallback() {

				@Override
				public void gotHighscores(Highscore[] scores) {
					if(scores != null) {
						list.setItems(scores);
					} else {
						list.setItems(new Highscore[] {new Highscore("Failed to load highscores", -1)});
					}
				}
			});
			
			ScrollPane sp = new ScrollPane(list);
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
                    Specular.nativeAndroid.login(new NativeAndroid.RequestCallback() {
                        
                        @Override
                        public void success() {
                            shouldUpdate = true;
                        }

                        @Override
                        public void failed() {
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
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
			@Override
			public boolean keyUp(int keycode) {
				if(keycode == Keys.BACK)
					game.enterState(Specular.States.MAINMENUSTATE);
				return super.keyUp(keycode);
			}
		}));
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
		profileTex.dispose();
		highscoreBar.dispose();
	}
}
