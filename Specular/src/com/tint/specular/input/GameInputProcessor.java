package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.tint.specular.Specular;

public class GameInputProcessor implements InputProcessor {

	private boolean w, a, s, d;
	
	private AnalogStick shoot, move;
	
	public GameInputProcessor(Specular game) {
		shoot = new AnalogStick();
		move = new AnalogStick();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.W)
			w = true;
		else if(keycode == Keys.A)
			a = true;
		else if(keycode == Keys.S)
			s = true;
		else if(keycode == Keys.D)
			d = true;
		
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.W)
			w = false;
		else if(keycode == Keys.A)
			a = false;
		else if(keycode == Keys.S)
			s = false;
		else if(keycode == Keys.D)
			d = false;
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		/*if(game.prefs.getString("Controls").equals("Accelerometer and stick")) {
			shootPointer = pointer;
			shooting = true;
		} else if(game.prefs.getString("Controls").equals("Two sticks")) {*/
			if(screenX <= Gdx.graphics.getWidth() / 2) {
				move.setBasePos(Gdx.input.getX(move.getPointer()) - Gdx.graphics.getWidth() / 2,
						- (Gdx.input.getY(move.getPointer()) - Gdx.graphics.getHeight() / 2));
				move.setPointer(pointer);
			} else {
				shoot.setBasePos(Gdx.input.getX(shoot.getPointer()) - Gdx.graphics.getWidth() / 2,
						- (Gdx.input.getY(shoot.getPointer()) - Gdx.graphics.getHeight() / 2));
				shoot.setPointer(pointer);
			}

			return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(move.getPointer() == pointer) {
			move.setPointer(-1);
		} else if(shoot.getPointer() == pointer) {
			shoot.setPointer(-1);
		}
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(pointer == shoot.getPointer())
		shoot.setHeadPos(Gdx.input.getX(shoot.getPointer()) - Gdx.graphics.getWidth() / 2,
				- (Gdx.input.getY(shoot.getPointer()) - Gdx.graphics.getHeight() / 2));
		
		else if(pointer == move.getPointer())
		move.setHeadPos(Gdx.input.getX(shoot.getPointer()) - Gdx.graphics.getWidth() / 2,
				- (Gdx.input.getY(shoot.getPointer()) - Gdx.graphics.getHeight() / 2));
		
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
	
	public boolean isWDown() {
		return w;
	}

	public boolean isADown() {
		return a;
	}

	public boolean isSDown() {
		return s;
	}

	public boolean isDDown() {
		return d;
	}
	
	public AnalogStick getMoveStick() {
		return move;
	}
	
	public AnalogStick getShootStick() {
		return shoot;
	}
}