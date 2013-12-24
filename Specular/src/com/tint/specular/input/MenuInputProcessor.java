package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.ui.Button;

public class MenuInputProcessor implements InputProcessor{
	
	private Texture playTex, profileTex, optionsTex, playTexPr, profileTexPr, optionsTexPr;
	private Button playBtn, profileBtn, optionsBtn;
	private Music music;
	private Specular game;
	
	public MenuInputProcessor(Specular game) {
		this.game = game;
		
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
				width = 768;
				
				profileBtn = new Button(x, y, width, height);
				
				// Set button textures
				playTex = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Play 780 490.png"));
				optionsTex = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Options.png"));
				profileTex = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Profiles 1000 710.png"));
				playTexPr = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Play Pressed.png"));
				optionsTexPr = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Options Pressed.png"));
				profileTexPr = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Profiles Pressed.png"));
				playBtn.setTexture(playTex);
				optionsBtn.setTexture(optionsTex);
				profileBtn.setTexture(profileTex);
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		// This seems to check whether the screen is being touched and if so where
		float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
		float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
			
		// It then checks if that touchpoint collides with the buttons on screen
		// and acts accordingly
		if(playBtn.isOver(touchpointx, touchpointy, true)) {
			playBtn.setTexture(playTexPr);
		} else if(profileBtn.isOver(touchpointx, touchpointy, true)) {
			profileBtn.setTexture(profileTexPr);
		} else if(optionsBtn.isOver(touchpointx, touchpointy, true)) {
				optionsBtn.setTexture(optionsTexPr);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		// This seems to check whether the screen is being touched and if so where
		float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
		float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
		
		profileBtn.setTexture(profileTex);
		optionsBtn.setTexture(optionsTex);
		playBtn.setTexture(playTex);
			
		// It then checks if that touchpoint collides with the buttons on screen
		// and acts accordingly
		if(playBtn.isOver(touchpointx, touchpointy, true)) {
			music.stop();
			game.enterState(States.SINGLEPLAYER_GAMESTATE);
		} else if(profileBtn.isOver(touchpointx, touchpointy, true)) {
			game.enterState(States.PROFILE_STATE);
		} else if(optionsBtn.isOver(touchpointx, touchpointy, true)) {

		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Button getPlayBtn() {
		return playBtn; }
	public Button getProfileBtn() {
		return profileBtn; }
	public Button getOptionsBtn() {
		return optionsBtn; }

}
