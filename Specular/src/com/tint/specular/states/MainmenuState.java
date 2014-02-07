package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.input.MenuInputProcessor;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class MainmenuState extends State {
	
	private Texture background;
	private Music music;
	private MenuInputProcessor menuInputProcessor;
	
	public MainmenuState(Specular game) {
		super(game);
		GLTexture.setEnforcePotImages(false);
		background = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Title Background.png"));

		music = Gdx.audio.newMusic(Gdx.files.internal("audio/MainMenuMusic.mp3"));
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
		game.batch.draw(background,0,0);
		menuInputProcessor.getPlayBtn().render();
		menuInputProcessor.getProfileBtn().render();
		menuInputProcessor.getOptionsBtn().render();
		game.batch.end();
		
		if(Gdx.input.isKeyPressed(Keys.BACK))
			Gdx.app.exit();
	}
	
	
	public void update(float delta) {
		
	}

	@Override
	public void show() {
		super.show();
		Specular.camera.position.set(Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight / 2, 0);
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		menuInputProcessor = new MenuInputProcessor(game,this);
		startMusic();
		
		Gdx.input.setInputProcessor(menuInputProcessor);
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

	public void stopMusic() {
		music.pause();
	}
	
	public void startMusic() {
		if(!music.isPlaying() && !Specular.prefs.getBoolean("MusicMuted"))
			music.play();
	}
}
