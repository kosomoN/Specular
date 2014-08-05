package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.InputProcessor;
import com.tint.specular.Specular;
import com.tint.specular.game.Camera;
import com.tint.specular.game.GameState;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class GameInputProcessor implements InputProcessor {

	private boolean w, a, s, d;
	private boolean touch;
	private boolean tilt, staticSticks;
	private AnalogStick shoot, move;
	private GameState gs;
	private boolean pausePressed;
	
	private Sound btnSound = Gdx.audio.newSound(Gdx.files.internal("audio/fx/ButtonPress.ogg"));
	
	public GameInputProcessor(GameState gs) {
		this.gs = gs;
		
		tilt = Specular.prefs.getBoolean("Tilt");
		staticSticks = Specular.prefs.getBoolean("Static");
		
		shoot = new AnalogStick(staticSticks);
		move = new AnalogStick(staticSticks);
		
		move.setBasePos(Specular.prefs.getFloat("Move Stick Pos X"), Specular.prefs.getFloat("Move Stick Pos Y"));
		shoot.setBasePos(Specular.prefs.getFloat("Shoot Stick Pos X"), Specular.prefs.getFloat("Shoot Stick Pos Y"));
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
		
		else if(keycode == Keys.F)
			Camera.shake(1, 0.01f);
		
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
		
		if(keycode == Keys.BACK || keycode == Keys.ESCAPE)
			gs.setPaused(true);
		
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
		
		if(gs.getTutorial().isEnding() && gs.getScoreFontAlpha() > 0.8f) {
			gs.endTutorial();
		} else if(gs.getTutorial().isStarting()) {
			gs.startTutorial();
		} else {
			//Checking if touching boardshock button
			if(viewporty > Specular.camera.viewportHeight - 90 &&
					viewportx - Specular.camera.viewportWidth / 2 > -350 && viewportx - Specular.camera.viewportWidth / 2 < 350) {
				gs.boardshock();
				return false;
			}
			if(viewporty < 70 && viewportx > Specular.camera.viewportWidth - 400) {
				btnSound.play();
				pausePressed = true;
				return false;
			}
			
			// Sticks
			
			//If NOT tilt controls
			if(!tilt) {
				
				//If touching left half
				if(viewportx <= Specular.camera.viewportWidth / 2) {
					
					//If sticks are static set it to the right position
					if(!staticSticks)
						move.setBasePos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
		
					move.setHeadPos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
					move.setPointer(pointer);
					
				} else {//Touching right half
					
					//If sticks are static set it to the right position
					if(!staticSticks)
						shoot.setBasePos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
					
					
					shoot.setHeadPos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
					shoot.setPointer(pointer);
				}
			}
			if(viewportx > Specular.camera.viewportWidth / 2) {
				if(!staticSticks)
					shoot.setBasePos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
				
				shoot.setHeadPos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
				shoot.setPointer(pointer);
			}
			
			touch = true;
		}
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		float viewportx = (float) screenX / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
		float viewporty = (float) screenY / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
		
		if(!gs.tutorialEndIsShowing()) {
			if(viewporty < 70 && viewportx > Specular.camera.viewportWidth - 400) {
				if(pausePressed) {
					gs.setPaused(true);
					pausePressed = false;
					return false;
				}
			}
			if(touch) {
				if(move.getPointer() == pointer) {
					move.setPointer(-1);
				} else if(shoot.getPointer() == pointer) {
					shoot.setPointer(-1);
				}
			}
			
			if(pausePressed)
				pausePressed = false;
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

	public boolean isStaticSticks() {
		return staticSticks;
	}
}