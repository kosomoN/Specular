package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.states.MainmenuState;
import com.tint.specular.ui.Button;

/**
 * 
 * @author Casper Talvio
 *
 */

public class MenuInputProcessor implements InputProcessor{
	
	private Texture playTex, profileTex, optionsTex, playTexPr, profileTexPr, optionsTexPr;
	private Button playBtn, profileBtn;//, optionsBtn;
	private Specular game;
	
	private MainmenuState menuState;
    
    public MenuInputProcessor(Specular game, MainmenuState menuState) {
        this.menuState = menuState;
		this.game = game;
		
		//Defining main menu buttons locations and sizes
		int x = 720;
		int y = (int) (404);
		
		int height = (int) (256);
		int width = 768;
		
		playBtn = new Button(x, y, width, height);
		
		x = 1400;
		y = 32;
		
		height = (int) (128);
		width = 512;
		
//		optionsBtn = new Button(x, y, width, height);
		
		x = 880;
		y = (int) (164);
		
		height = (int) (256);
		width = 768;
		
		profileBtn = new Button(x, y, width, height);
		
		// Set button textures
		playTex = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Play 720 420.png"));
//		optionsTex = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Options 1400 910.png"));
		profileTex = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Profiles 880 660.png"));
		playTexPr = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Play Pressed.png"));
//		optionsTexPr = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Options Pressed.png"));
		profileTexPr = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Profiles Pressed.png"));
		playBtn.setTexture(playTex);
//		optionsBtn.setTexture(optionsTex);
		profileBtn.setTexture(profileTex);
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
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
//		} else if(optionsBtn.isOver(touchpointx, touchpointy, true)) {
//			optionsBtn.setTexture(optionsTexPr);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		// This seems to check whether the screen is being touched and if so where
		float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
		float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
		
		profileBtn.setTexture(profileTex);
//		optionsBtn.setTexture(optionsTex);
		playBtn.setTexture(playTex);
			
		// It then checks if that touchpoint collides with the buttons on screen
		// and acts accordingly
		if(playBtn.isOver(touchpointx, touchpointy, true)) {
			game.enterState(States.SINGLEPLAYER_GAMESTATE);
			menuState.stopMusic();
		} else if(profileBtn.isOver(touchpointx, touchpointy, true)) {
			game.enterState(States.PROFILE_STATE);
//		} else if(optionsBtn.isOver(touchpointx, touchpointy, true)) {

		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	public Button getPlayBtn() {
		return playBtn; }
	public Button getProfileBtn() {
		return profileBtn; }
//	public Button getOptionsBtn() { return optionsBtn; }

}
