package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.tint.specular.Specular;
import com.tint.specular.game.BoardShock;
import com.tint.specular.game.GameState;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class GameInputProcessor implements InputProcessor {

	public static final int STATIC_TILT = 0, TILT = 1, STATIC_STICK = 2, DYNAMIC_STICK = 3;
	private boolean w, a, s, d;
	private boolean touch;
	private int controls;
	private AnalogStick shoot, move;
	private GameState gs;
	
	public GameInputProcessor(GameState gs) {
		this.gs = gs;
		controls = gs.getPlayer().getControls();
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
		float viewportx = (float) screenX / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
		float viewporty = (float) screenY / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
		
		//Checking if touching boardshock button
		if(viewporty > Specular.camera.viewportHeight - 90 &&
				viewportx - Specular.camera.viewportWidth / 2 > -350 && viewportx - Specular.camera.viewportWidth / 2 < 350) {
			BoardShock.activate(gs);
		}
		
		// Sticks
		if(controls != TILT && controls != STATIC_TILT) {
			if(viewportx <= Specular.camera.viewportWidth / 2) {
				if(controls == STATIC_STICK)
					move.setBasePos(-1 / 4f * Specular.camera.viewportWidth, 0);
				else
					move.setBasePos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
	
				move.setHeadPos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
				move.setPointer(pointer);
			}
		}
		if(controls == STATIC_STICK || controls == STATIC_TILT)
			shoot.setBasePos(1 / 4f * Specular.camera.viewportWidth, 0);
		else
			shoot.setBasePos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
		
		shoot.setHeadPos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
		shoot.setPointer(pointer);
		
		touch = true;

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(controls != 0 && touch) {
			if(move.getPointer() == pointer) {
				move.setPointer(-1);
			} else if(shoot.getPointer() == pointer) {
				shoot.setPointer(-1);
			}
		}
		
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(controls != 0) {
			float viewportx = (float) screenX / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
			float viewporty = (float) screenY / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
			
			if(pointer == shoot.getPointer())
			shoot.setHeadPos(viewportx - Specular.camera.viewportWidth / 2,
					- (viewporty - Specular.camera.viewportHeight / 2));
			
			else if(pointer == move.getPointer())
			move.setHeadPos(viewportx - Specular.camera.viewportWidth / 2,
					- (viewporty - Specular.camera.viewportHeight / 2));
		}
		
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
	
	public void reset() {
		touch = false;
		
		// Resettng them
		shoot.setPointer(-1);
		move.setPointer(-1);
	}
}