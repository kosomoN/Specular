package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.game.GameState;
import com.tint.specular.ui.Button;

public class GameOverInputProcessor extends InputAdapter {

	// For changing and using states
	private Specular game;
	private GameState gs;

	// Buttons
	private Button retry;
	private Button menu;
	
	private boolean touch;
	
	public GameOverInputProcessor(Specular game, GameState gs) {
		this.game = game;
		this.gs = gs;
		
		// Initializing button textures
		Texture retryTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Retry 600 550.png"));
		Texture retryPressedTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Retry pressed.png"));
		
		Texture menuTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Main menu button 90 820.png"));
		Texture menuPressedTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Main menu button pressed.png"));
		
		// Initializing buttons
		retry = new Button(-retryTex.getWidth() / 2, -100, 780, 254, game.batch, retryTex, retryPressedTex);
		menu = new Button(-menuTex.getWidth() / 2, -400, 885, 190, game.batch, menuTex, menuPressedTex);
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//Translate touch event to per cent -> multiply with viewport to get viewport coordinates -> add half because 0, 0 is in the middle
		float touchpointx = toViewportX(screenX);
		float touchpointy = toViewportY(screenY);
		
		if(retry.isOver(touchpointx, touchpointy, false))
			retry.touchOver(touchpointx, touchpointy);
		else if(menu.isOver(touchpointx, touchpointy, false))
			menu.touchOver(touchpointx, touchpointy);
			
		touch = true;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//Translate touch event to per cent -> multiply with viewport to get viewport coordinates -> add half because 0, 0 is in the middle
		float touchpointx = toViewportX(screenX);
		float touchpointy = toViewportY(screenY);
		
		if(touch) {
			touch = false;
			if(retry.isOver(touchpointx, touchpointy, false)) {
				// Restart game
				gs.reset();
			} else {
				retry.touchUp();
			}
			if(menu.isOver(touchpointx, touchpointy, false)) {
			
				// Return to menu
				game.enterState(States.MAINMENUSTATE);
				gs.stopGameMusic();
			} else {
				menu.touchUp();
			}
		}
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		float touchpointx = toViewportX(screenX);
		float touchpointy = toViewportY(screenY);

		if(retry.isOver(touchpointx, touchpointy, false)) {
			retry.touchOver(touchpointx, touchpointy);
		} else {
			retry.touchUp();
		}
		if(menu.isOver(touchpointx, touchpointy, false)) {
			menu.touchOver(touchpointx, touchpointy);
		} else {
			menu.touchUp();
		}
		return false;
	}
	
	private float toViewportX(int screenX) {
		return (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth - Specular.camera.viewportWidth / 2;
	}
	
	private float toViewportY(int screenY) {
		return Specular.camera.viewportHeight - (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight - Specular.camera.viewportHeight / 2;
	}

	public Button getRetryBtn() { return retry; }
	public Button getMenuBtn() { return menu; }
}