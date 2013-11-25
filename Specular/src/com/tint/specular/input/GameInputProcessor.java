package com.tint.specular.input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class GameInputProcessor implements InputProcessor {

	private int touches;
	private int movePointer = -1, shootPointer = -1;
	private boolean moving, shooting;
	private boolean w, a, s, d;
	private static boolean space;
	
	public GameInputProcessor() {
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
		touches++;
		
		/*if(controls.equals(Controls.KEYBOARD_AND_STICK) || controls.equals(Controls.ACCELEROMETER_AND_STICK)) {*/
			shootPointer = pointer;
			shooting = true;
		/*} else if(controls.equals(Controls.TWINSTICKS)) {
			if(screenX <= Gdx.graphics.getWidth() / 2) {
				movePointer = pointer;
				moving = true;
			} else {
				shootPointer = pointer;
				shooting = true;
			}
		}*/
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		touches--;
		
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
		//Shooting with stick
//		if(controls.equals(Controls.KEYBOARD_AND_STICK) || controls.equals(Controls.ACCELEROMETER_AND_STICK)) {
//			shootStick.setHeadPos(Gdx.input.getX(pointer), Gdx.input.getY(pointer));
//		}
		
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
	
	public int getTouches() {
		return touches;
	}
	
	public int getMovePointer() {
		return movePointer;
	}
	
	public int getShootPointer() {
		return shootPointer;
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	public boolean isShooting() {
		return shooting;
	}
}
