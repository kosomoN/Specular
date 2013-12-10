package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
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
		
		Texture.setEnforcePotImages(false);
		
		background = new Texture("graphics/mainmenu/Menu.png");
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/04.ogg"));
		int backgroundWidth = Gdx.graphics.getHeight() * 16 / 9;
		System.out.println(backgroundWidth);
		
		int y = Gdx.graphics.getHeight() * 380 / 1080;
		int x = backgroundWidth * 800 / 1920;
		
		int height = Gdx.graphics.getHeight() * 180 / 1080;
		int width = backgroundWidth * 730 / 1920;
		
		playBtn = new Button(x, y, width, height);
		
		y = 0;
		x = backgroundWidth * 1500 / 1920;
		
		height = Gdx.graphics.getHeight() * 120 / 1080;
		width = backgroundWidth * 420 / 1920;
		
		optionsBtn = new Button(x, y, width, height);
		
		y = Gdx.graphics.getHeight() * 135 / 1080;
		x = backgroundWidth * 1020 / 1920;
		
		height = Gdx.graphics.getHeight() * 190 / 1080;
		width = backgroundWidth * 715 / 1920;
		
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
		game.batch.draw(background, 0, 0, Gdx.graphics.getHeight() * 16 / 9, Gdx.graphics.getHeight());
		game.batch.end();
	}
	
	public void update(float delta) {
		if(Gdx.input.justTouched()) {
			if(playBtn.isOver(Gdx.input.getX(), Gdx.input.getY(), true)) {
				music.stop();
				game.enterState(States.SINGLEPLAYER_GAMESTATE);
			} else if(profileBtn.isOver(Gdx.input.getX(), Gdx.input.getY(), true)) {
				game.enterState(States.PROFILE_STATE);
			} else if(optionsBtn.isOver(Gdx.input.getX(), Gdx.input.getY(), true)) {
				System.out.println("Options");
			}
		}
	}

	@Override
	public void show() {
		super.show();
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
