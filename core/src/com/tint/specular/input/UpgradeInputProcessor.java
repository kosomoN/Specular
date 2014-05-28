package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.states.UpgradeState;
import com.tint.specular.ui.Button;

public class UpgradeInputProcessor implements InputProcessor {

	private Specular game;
	private UpgradeState upgradeState;
	private Button confirmBtn, resetBtn;
	private Button[] upgradeBtns = new Button[10];
	private boolean somethingUpgraded;
	
	public UpgradeInputProcessor(Specular game, UpgradeState upgradeState) {
		this.game = game;
		this.upgradeState = upgradeState;
		somethingUpgraded = false;
		
		// Initializing buttons
		confirmBtn = new Button(0, 0, 0, 0, game.batch, null, null);
		resetBtn = new Button(0, 0, 0, 0, game.batch, null, null);
		for(int i = 0; i < upgradeBtns.length; i++) {
			upgradeBtns[i] = new Button(0, 0, 0, 0, game.batch, null, null);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.BACK || keycode == Keys.ESCAPE) {
			// Will have a promt in future
			upgradeState.resetUpgrades();
			game.enterState(States.MAINMENUSTATE);
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// Translate screen coordinates to viewport coordinates
		float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
		float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
		
		// Upgrade buttons, maybe not necessary
		for(int i = 0; i < upgradeBtns.length; i++) {
			if(upgradeBtns[i].isOver(touchpointx, touchpointy, true)) {
				upgradeBtns[i].touchOver(touchpointx, touchpointy);
			}
		}
		
		// Confirm & reset button
		if(somethingUpgraded) {
			if(confirmBtn.isOver(touchpointx, touchpointy, true)) {
				confirmBtn.touchOver(touchpointx, touchpointy);
			} else if(resetBtn.isOver(touchpointx, touchpointy, true)) {
				resetBtn.touchOver(touchpointx, touchpointy);
			}
		}
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// Translate screen coordinates to viewport coordinates
		float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
		float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
		
		// Upgrade buttons
		for(int i = 0; i < upgradeBtns.length; i++) {
			if(upgradeBtns[i].isOver(touchpointx, touchpointy, true)) {
				upgradeBtns[i].touchUp();
				if(upgradeState.getUpgrades()[i].getCost() <= upgradeState.getUpgradePoints()) {
					upgradeState.getUpgrades()[i].upgrade();
					somethingUpgraded = true;
				}
			}
		}
		
		// Confirm & reset button
		if(somethingUpgraded) {
			if(confirmBtn.isOver(touchpointx, touchpointy, true)) {
				confirmBtn.touchUp();
				upgradeState.saveUpgrades();
			} else if(resetBtn.isOver(touchpointx, touchpointy, true)) {
				upgradeState.resetUpgrades();
				somethingUpgraded = false;
			}
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

	public Button getConfirmButton() {
		return confirmBtn;
	}
	
	public Button getResetButton() {
		return resetBtn;
	}
}
