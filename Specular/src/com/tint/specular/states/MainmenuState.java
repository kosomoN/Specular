package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.ui.Button;

public class MainmenuState extends State {

	private Texture playTex;
	private Music music;
	private Button playBtn, profileBtn, optionsBtn;
	
	public MainmenuState(Specular game) {
		super(game);
		
		GLTexture.setEnforcePotImages(false);
		
		playTex = new Texture(Gdx.files.internal("graphics/mainmenu/Menu.png"));
		playTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/04.ogg"));

		//Defining main menu buttons locations and sizes
		int x = 780;
		int y = (int) (334);
		
		int height = (int) (256);
		int width = 768;
		
		playBtn = new Button(x, y, width, height);
		
		x = 1408;
		y = 0;
		
		height = (int) (128);
		width = 512;
		
		optionsBtn = new Button(x, y, width, height);
		
		x = 1000;
		y = (int) (114);
		
		height = (int) (256);
		width = 512;
		
		profileBtn = new Button(x, y, width, height);
	}
	
	@Override
	public void render(float delta) {
		
		update(delta);
		
		renderMenu();
	}
	
	public void renderMenu() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		game.batch.draw(playTex, 0, 0);
		game.batch.end();
	}
	
	// This function seems to check whether the screen is being touched and if so where
	public void update(float delta) {
		if(Gdx.input.justTouched()) {
			float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
			float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
			
			// It then checks if that touchpoint collides with the buttons on screen
			if(playBtn.isOver(touchpointx, touchpointy, true)) {
				music.stop();
				game.enterState(States.SINGLEPLAYER_GAMESTATE);
			} else if(profileBtn.isOver(touchpointx, touchpointy, true)) {
				game.enterState(States.PROFILE_STATE);
			} else if(optionsBtn.isOver(touchpointx, touchpointy, true)) {
				System.out.println("Options");
			}
		}
	}

	@Override
	public void show() {
		super.show();
		Specular.camera.position.set(Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight / 2, 0);
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		music.play();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void dispose() {
		super.dispose();
		music.dispose();
	}
}
