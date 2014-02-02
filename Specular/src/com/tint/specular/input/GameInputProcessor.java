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

	private boolean w, a, s, d;
	private boolean touch;
	
	private AnalogStick shoot, move;
	
	private GameState gs;
	
	public GameInputProcessor(Specular game, GameState gs) {
		this.gs = gs;
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
			float viewportx = (float) screenX / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
			float viewporty = (float) screenY / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
			
			//Checking if touching boardshock button
			if(viewporty > Specular.camera.viewportHeight - 90 &&
					viewportx - Specular.camera.viewportWidth / 2 > -350 && viewportx - Specular.camera.viewportWidth / 2 < 350) {
				BoardShock.activate(gs);
			} else if(viewportx <= Specular.camera.viewportWidth / 2) {
				move.setBasePos(viewportx - Specular.camera.viewportWidth / 2,
						- (viewporty - Specular.camera.viewportHeight / 2));
				move.setHeadPos(viewportx - Specular.camera.viewportWidth / 2,
						- (viewporty - Specular.camera.viewportHeight / 2));
				move.setPointer(pointer);
			} else {
				shoot.setBasePos(viewportx- Specular.camera.viewportWidth / 2,
						- (viewporty - Specular.camera.viewportHeight / 2));
				shoot.setHeadPos(viewportx- Specular.camera.viewportWidth / 2,
						- (viewporty - Specular.camera.viewportHeight / 2));
				shoot.setPointer(pointer);
			}
			
			touch = true;

			return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(touch) {
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
		
		float viewportx = (float) screenX / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
		float viewporty = (float) screenY / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
		
		if(pointer == shoot.getPointer())
		shoot.setHeadPos(viewportx - Specular.camera.viewportWidth / 2,
				- (viewporty - Specular.camera.viewportHeight / 2));
		
		else if(pointer == move.getPointer())
		move.setHeadPos(viewportx - Specular.camera.viewportWidth / 2,
				- (viewporty - Specular.camera.viewportHeight / 2));
		
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