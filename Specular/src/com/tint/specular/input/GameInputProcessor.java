package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.tint.specular.Specular;

public class GameInputProcessor implements InputProcessor {

	private int movePointer = -1, shootPointer = -1;
	private int pressPointer;
	
	private boolean moving, shooting;
	private boolean press;
	private boolean w, a, s, d;
	private boolean space;
	
	private Specular game;
	
	public GameInputProcessor(Specular game) {
		this.game = game;
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
		else if(keycode == Keys.SPACE)
			space = true;
		
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
		else if(keycode == Keys.SPACE)
			space = false;
		
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		press = true;
		pressPointer = pointer;
		
		if(game.prefs.getString("Controls").equals("Accelerometer and stick")) {
			shootPointer = pointer;
			shooting = true;
		} else if(game.prefs.getString("Controls").equals("Two sticks")) {
			if(screenX <= Gdx.graphics.getWidth() / 2) {
				movePointer = pointer;
				moving = true;
			} else {
				shootPointer = pointer;
				shooting = true;
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(movePointer == pointer) {
			moving = false;
			movePointer = -1;
		} else if(shootPointer == pointer) {
			shooting = false;
			shootPointer = -1;
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
	
	public boolean isSpaceDown() {
		return space;
	}
	
	public boolean isPressed() {
		return press;
	}
	
	public int getMovePointer() {
		return movePointer;
	}
	
	public int getShootPointer() {
		return shootPointer;
	}
	
	public int getPressPointer() {
		return pressPointer;
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	public boolean isShooting() {
		return shooting;
	}
	
	public void setPress(boolean b) {
		press = b;
	}
	
	public void resetPressPointer() {
		pressPointer = -1;
	}
}
