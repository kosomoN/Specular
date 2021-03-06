package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
	private Button retry, menu, highscores, upgrades;
	
	// Other
	private boolean touch;
	private boolean touchUpgradeBtn = false;
	
	public GameOverInputProcessor(Specular game, GameState gs) {
		this.game = game;
		this.gs = gs;
		
		// Initializing buttons
		Texture retryTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Retry.png"));
		Texture retryPressedTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Retry Pressed.png"));
		
		retry = new Button(-30, -526, retryTex.getWidth(), retryTex.getHeight(), game.batch, retryTex, retryPressedTex);
		
		
		Texture menuTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Main Menu.png"));
		Texture menuPressedTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Main Menu Pressed.png"));
		
		menu = new Button(-810, -526, menuTex.getWidth(), menuTex.getHeight(), game.batch, menuTex, menuPressedTex);
		
		
		Texture highscoresTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Highscores.png"));
		Texture highscoresPressedTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Highscores Pressed.png"));
		
		highscores = new Button(-420, -526, highscoresTex.getWidth(), highscoresTex.getHeight(), game.batch, highscoresTex, highscoresPressedTex);
		
		Texture upgradesTex = new Texture(Gdx.files.internal("graphics/game/packed/Upgrades.png"));
		Texture upgradesPressedTex = new Texture(Gdx.files.internal("graphics/game/packed/Upgrades Pressed.png"));
		
		upgrades = new Button(-upgradesTex.getWidth() / 2, -upgradesTex.getHeight() - 60, upgradesTex.getWidth(), upgradesTex.getHeight(), game.batch, upgradesTex, upgradesPressedTex);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.BACK)
			game.enterState(States.MAINMENUSTATE);
		return false;
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
		else if(highscores.isOver(touchpointx, touchpointy, false))
			highscores.touchOver(touchpointx, touchpointy);
		else if(upgrades.isOver(touchpointx, touchpointy, false)) {
			upgrades.touchOver(touchpointx, touchpointy);
			touchUpgradeBtn = true;
		}

		
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
			}
			
			if(menu.isOver(touchpointx, touchpointy, false)) {
				// Return to menu
				game.enterState(States.MAINMENUSTATE);
		}
			
			retry.touchUp();
			menu.touchUp();
			
			if(highscores.isOver(touchpointx, touchpointy, false)) {
				
				// Go to highscores
				game.enterState(States.PROFILE_STATE);
			} else {
				highscores.touchUp();
			}
			
			
			if(upgrades.isOver(touchpointx, touchpointy, false)) {
				
				// Go to highscores
				game.enterState(States.UPGRADESTATE);
			} else {
				upgrades.touchUp();
			}
		}
		
		touchUpgradeBtn = false;
		
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
		
		if(highscores.isOver(touchpointx, touchpointy, false)) {
			highscores.touchOver(touchpointx, touchpointy);
		} else {
			highscores.touchUp();
		}
		
		if(upgrades.isOver(touchpointx, touchpointy, false)) {
			upgrades.touchOver(touchpointx, touchpointy);
		} else {
			upgrades.touchUp();
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
	public Button getHighscoreBtn() { return highscores; }
	public Button getUpgradeBtn() { return upgrades; }
	public boolean isTouchingUpgradeBtn() { return touchUpgradeBtn; }
}