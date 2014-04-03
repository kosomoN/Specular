package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.game.GameState;
import com.tint.specular.ui.Button;

public class PauseInputProcessor implements InputProcessor {

	// For changing states
	private Specular game;
	private GameState gs;
	
	// Buttons
	private Button resume, backToMenu;
	
	private boolean touch;
	
	public PauseInputProcessor(Specular game, GameState gs) {
		this.game = game;
		this.gs = gs;
		
		// Initializing buttons
		Texture resumeTex = new Texture(Gdx.files.internal("graphics/menu/pausemenu/Resume.png"));
		Texture resumePressedTex = new Texture(Gdx.files.internal("graphics/menu/pausemenu/Resume Pressed.png"));
		resume = new Button(-resumeTex.getWidth() / 2, -300, resumeTex.getWidth(), resumeTex.getHeight(), game.batch, resumeTex, resumePressedTex);
	
		Texture toMenuTex = new Texture(Gdx.files.internal("graphics/menu/pausemenu/Menu.png"));
		Texture toMenuPressedTex = new Texture(Gdx.files.internal("graphics/menu/pausemenu/Menu Pressed.png"));
		backToMenu = new Button(-toMenuTex.getWidth() / 2, -500, toMenuTex.getWidth(), toMenuTex.getHeight(), game.batch, toMenuTex, toMenuPressedTex);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.BACK || keycode == Keys.ESCAPE)
			game.enterState(States.MAINMENUSTATE);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//Translate touch event to per cent -> multiply with viewport to get viewport coordinates -> add half because 0, 0 is in the middle
		float touchpointx = toViewportX(screenX);
		float touchpointy = toViewportY(screenY);
		
		if(resume.isOver(touchpointx, touchpointy, false))
			resume.touchOver(touchpointx, touchpointy);
		else if(backToMenu.isOver(touchpointx, touchpointy, false))
			backToMenu.touchOver(touchpointx, touchpointy);
		
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
			if(resume.isOver(touchpointx, touchpointy, false)) {
				// Unpause
				Gdx.input.setInputProcessor(gs.getGameProcessor());
				gs.setPaused(false);
			}
				
			if(backToMenu.isOver(touchpointx, touchpointy, false)) {
				// Return to main menu
				game.enterState(States.MAINMENUSTATE);
			}
			
			// Reset buttons to untouched
			resume.touchUp();
			backToMenu.touchUp();
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//Translate touch event to per cent -> multiply with viewport to get viewport coordinates -> add half because 0, 0 is in the middle
		float touchpointx = toViewportX(screenX);
		float touchpointy = toViewportY(screenY);
		
		if(resume.isOver(touchpointx, touchpointy, false))
			resume.touchOver(touchpointx, touchpointy);
		else
			resume.touchUp();
		
		if(backToMenu.isOver(touchpointx, touchpointy, false))
			backToMenu.touchOver(touchpointx, touchpointy);
		else
			backToMenu.touchUp();
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
	
	private float toViewportX(int screenX) {
		return (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth - Specular.camera.viewportWidth / 2;
	}
	
	private float toViewportY(int screenY) {
		return Specular.camera.viewportHeight - (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight - Specular.camera.viewportHeight / 2;
	}

	public Button getResumeButton() {
		return resume;
	}
	
	public Button getToMenuButton() {
		return backToMenu;
	}
}
