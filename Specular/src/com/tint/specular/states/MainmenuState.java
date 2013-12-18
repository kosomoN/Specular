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

	private Texture background;
	private Music music;
	private Button playBtn, profileBtn, optionsBtn;
	
	public MainmenuState(Specular game) {
		super(game);
		
		GLTexture.setEnforcePotImages(false);
		
		background = new Texture(Gdx.files.internal("graphics/mainmenu/Menu.png"));
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/04.ogg"));

		//Setting up buttons for main menu
		int y = (int) (380);
		int x = 800;
		
		int height = (int) (180);
		int width = 730;
		
		playBtn = new Button(x, y, width, height);
		
		y = 0;
		x = 1500;
		
		height = (int) (120);
		width = 420;
		
		optionsBtn = new Button(x, y, width, height);
		
		y = (int) (135);
		x = 1020;
		
		height = (int) (190);
		width = 715;
		
		profileBtn = new Button(x, y, width, height);
	}
	
	@Override
	public void render(float delta) {
		//Update
		update(delta);
		
		//Render
		renderMenu();
	}
	
	public void renderMenu() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		game.batch.draw(background, 0, 0);
		game.batch.end();
	}
	
	public void update(float delta) {
		if(Gdx.input.justTouched()) {
			float viewportx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
			float viewporty = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
			
			if(playBtn.isOver(viewportx, viewporty, true)) {
				music.stop();
				game.enterState(States.SINGLEPLAYER_GAMESTATE);
			} else if(profileBtn.isOver(viewportx, viewporty, true)) {
				game.enterState(States.PROFILE_STATE);
			} else if(optionsBtn.isOver(viewportx, viewporty, true)) {
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
