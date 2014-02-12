package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.states.MainmenuState;
import com.tint.specular.ui.Button;

public class MenuInputProcessor extends InputAdapter {
	
	private Button playBtn, profileBtn, optionsBtn, helpBtn;
	private Specular game;
	private boolean firstTime, howToPlay, helpPressed;
	
	private MainmenuState menuState;
    
    public MenuInputProcessor(Specular game, MainmenuState menuState) {
        this.menuState = menuState;
		this.game = game;
		
		//Defining main menu buttons locations and sizes
		Texture playTex = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Play 720 420.png"));
		Texture playTexPr = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Play Pressed.png"));
		playBtn = new Button(720, 404, 768, 256, game.batch, playTex, playTexPr);

		
		Texture profileTex = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Profiles 880 660.png"));
		Texture profileTexPr = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Profiles Pressed.png"));
		profileBtn = new Button(880, 164, 768, 256, game.batch, profileTex, profileTexPr);
		
		Texture optionsTex = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Options 1400 910.png"));
		Texture optionsTexPr = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Options Pressed.png"));
		optionsBtn = new Button(1400, 32, 512, 128, game.batch, optionsTex, optionsTexPr);
		
		Texture helpTex = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Help.png"));
		Texture helpTexPr = new Texture(Gdx.files.internal("graphics/menu/mainmenu/Help.png"));
		helpBtn = new Button(1620 , 470, 128, 128, game.batch, helpTex, helpTexPr);
		
		firstTime = Specular.prefs.getBoolean("FirstTime");
	}
    
	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.BACK)
			if(!howToPlay)
				Gdx.app.exit();
		return super.keyUp(keycode);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		// Translate screen coordinates to viewport coordinates
		float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
		float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
		
		howToPlay = menuState.showHowToPlay();
		
		// It then checks if that touchpoint collides with the buttons on screen and doesn't return from how to play screen,
		// and acts accordingly
		if(!howToPlay) {
			if(playBtn.isOver(touchpointx, touchpointy, true)) {
				playBtn.touchOver(touchpointx, touchpointy);
			} else if(profileBtn.isOver(touchpointx, touchpointy, true)) {
				profileBtn.touchOver(touchpointx, touchpointy);
			} else if(optionsBtn.isOver(touchpointx, touchpointy, true)) {
				optionsBtn.touchOver(touchpointx, touchpointy);
			} else if(helpBtn.isOver(touchpointx, touchpointy, true)) {
				helpBtn.touchOver(touchpointx, touchpointy);
			}
		} else if(!helpPressed){
			game.enterState(States.SINGLEPLAYER_GAMESTATE);
			menuState.setHowToPlay(false);
			menuState.stopMusic();
		} else {
			menuState.setHowToPlay(false);
			helpPressed = false;
		}
//		//Play button sound
//		Sound soundButton1 = Gdx.audio.newSound(Gdx.files.internal("audio/Button.wav"));	
//		if (playBtn.isOver(touchpointx, touchpointy, true)) {
//		soundButton1.play(1.0f);
//		}
		return false;	
		

	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		
		// Translate screen coordinates to viewport coordinates
		float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
		float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
		
		// It then checks if that touchpoint collides with the buttons on screen and doesn't return from how to play screen,
		// and acts accordingly
		if(!howToPlay) {
			if(playBtn.isOver(touchpointx, touchpointy, true)) {
				playBtn.touchUp();
				if(firstTime) {
					menuState.setHowToPlay(true);
				} else {
					game.enterState(States.SINGLEPLAYER_GAMESTATE);
					menuState.setHowToPlay(false);
					menuState.stopMusic();
				}
			} else if(profileBtn.isOver(touchpointx, touchpointy, true)) {
				profileBtn.touchUp();
				game.enterState(States.PROFILE_STATE);
			} else if(optionsBtn.isOver(touchpointx, touchpointy, true)) {
				optionsBtn.touchUp();
				game.enterState(States.SETTINGSMENUSTATE);
			} else if(helpBtn.isOver(touchpointx, touchpointy, true)) {
				helpBtn.touchUp();
				menuState.setHowToPlay(true);
				helpPressed = true;
			}
		}
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		
		// Translate screen coordinates to viewport coordinates
		float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
		float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
		
		if(playBtn.isOver(touchpointx, touchpointy, true)) {
			playBtn.touchOver(touchpointx, touchpointy);
		} else {
			playBtn.touchUp();
		}
		
		if(profileBtn.isOver(touchpointx, touchpointy, true)) {
			profileBtn.touchOver(touchpointx, touchpointy);
		} else {
			profileBtn.touchUp();
		}
		
		if(optionsBtn.isOver(touchpointx, touchpointy, true)) {
			optionsBtn.touchOver(touchpointx, touchpointy);
		} else {
			optionsBtn.touchUp();
		}
		
//		//Play button sound
//		Sound soundButton1 = Gdx.audio.newSound(Gdx.files.internal("audio/Button.wav"));	
//		if (profileBtn.isOver(touchpointx, touchpointy, true)) {
//		soundButton1.play(1.0f);
//		}
		return false;
	}
	
	public Button getPlayBtn() { return playBtn; }
	public Button getProfileBtn() { return profileBtn; }
	public Button getOptionsBtn() {	return optionsBtn; }
	public Button getHelpButton() { return helpBtn; }
}
