package com.tint.specular.input;

import com.badlogic.gdx.InputProcessor;
import com.tint.specular.states.MainmenuState;
import com.tint.specular.states.SettingsMenuState;
import com.tint.specular.states.State;

public class MenuInputProcessor implements InputProcessor {
	
	//FIELDS
	State state;
	
	public MenuInputProcessor(MainmenuState state) {
		this.state = state;
	}
	
	public MenuInputProcessor(SettingsMenuState state) {
		this.state = state;
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
		if(state instanceof SettingsMenuState) {
			((SettingsMenuState) state).updateButtons(screenX, screenY);
		} else if(state instanceof MainmenuState) {
			
		}
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(state instanceof SettingsMenuState) {

		} else if(state instanceof MainmenuState) {
			
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
}
