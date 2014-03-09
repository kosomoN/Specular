package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.tint.specular.Specular;
import com.tint.specular.input.MenuInputProcessor;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class MainmenuState extends State {
	
	private Texture background, howToPlay;
	private Music music;
	private MenuInputProcessor menuInputProcessor;
	private boolean showHowToPlay;
	private float screenHeightToCameraHeight;
	private BitmapFont versionFont;
	private String versionName;
	
	public MainmenuState(Specular game) {
		super(game);
		GLTexture.setEnforcePotImages(false);
		background = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Title Background.png"));
		howToPlay = new Texture(Gdx.files.internal("graphics/menu/mainmenu/How to Play.png"));
		
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/00.ogg"));
		music.setLooping(true);
		screenHeightToCameraHeight = Specular.camera.viewportHeight / 1080f;
		
		FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Battlev2l.ttf"));
		versionFont = fontGen.generateFont(50, "1234567890.:DevVrsion", false);
		versionFont.setColor(Color.RED);
		fontGen.dispose();
		
		versionName = "Version: " + Specular.nativeAndroid.getVersionName();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(showHowToPlay) {
			game.batch.begin();
			float width =  screenHeightToCameraHeight * howToPlay.getWidth();
			game.batch.draw(howToPlay, (Specular.camera.viewportWidth - width) / 2, 0, width, Specular.camera.viewportHeight);
			game.batch.end();
			
			if(Gdx.input.justTouched())
				showHowToPlay = false;
		} else {
			game.batch.begin();
			game.batch.draw(background, 0, 0);
			menuInputProcessor.getPlayBtn().render();
			menuInputProcessor.getProfileBtn().render();
			menuInputProcessor.getOptionsBtn().render();
			menuInputProcessor.getHelpButton().render();
			
			versionFont.draw(game.batch, versionName, 750, 80);
			
			game.batch.end();
		}
	}
	
	public void setHowToPlay(boolean showHowToPlay) {
		this.showHowToPlay = showHowToPlay;
	}
	
	public boolean showHowToPlay() {
		return showHowToPlay;
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
