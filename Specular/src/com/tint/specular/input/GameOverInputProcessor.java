package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.game.GameState;
import com.tint.specular.states.Facebook.LoginCallback;
import com.tint.specular.ui.Button;

public class GameOverInputProcessor implements InputProcessor{

	// For changing and using states
	private Specular game;
	private GameState gs;

	// Buttons
	private Button retry;
	private Button menu;
	private Button post;
	
	// Button textures
	private Texture retryTex, retryPressedTex;
	private Texture menuTex, menuPressedTex;
	private Texture postTex, postPressedTex;
	
	public GameOverInputProcessor(Specular game, GameState gs) {
		this.game = game;
		this.gs = gs;
		
		// Initializing button textures
		retryTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Retry 600 550.png"));
		retryPressedTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Retry pressed.png"));
		
		menuTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Main menu button 90 820.png"));
		menuPressedTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Main menu button pressed.png"));

		postTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Post 1250 790.png"));
		postPressedTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Post Pressed.png"));
		
		// Initializing buttons
		/*
		 * This is MADNESS!! MADNESS? THIS. IS. TINT GAMES!!!
		 * 
		 */
		retry = new Button(Specular.camera.viewportWidth * (600 / 1920f), 	// x
				Specular.camera.viewportHeight * (300 / 1080f),			// y
				Specular.camera.viewportWidth * (780 / 1920f), Specular.camera.viewportHeight * (254 / 1080f)); // width, height
		retry.setTexture(retryTex);
		
		menu = new Button(Specular.camera.viewportWidth * (98 / 1920f),		// x
				Specular.camera.viewportHeight * (15 / 1080f),				// y
				Specular.camera.viewportWidth * (885 / 1920f), Specular.camera.viewportHeight * (190 / 1080f)); // width, height
		menu.setTexture(menuTex);
		
		post = new Button(Specular.camera.viewportWidth * (1253 / 1920f),	// x
				Specular.camera.viewportHeight * (45 / 1080f),				// y
				Specular.camera.viewportWidth * (510 / 1920f), Specular.camera.viewportHeight * (225 / 1080f)); // width, height
		post.setTexture(postTex);
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
		float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
		float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
		if(retry.isOver(touchpointx, touchpointy, true)) {
			retry.setTexture(retryPressedTex);
		} else if(menu.isOver(touchpointx, touchpointy, true))
			menu.setTexture(menuPressedTex);
		else if(post.isOver(touchpointx, touchpointy, true))
			post.setTexture(postPressedTex);
			
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
		float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
		
		retry.setTexture(retryTex);
		menu.setTexture(menuTex);
		post.setTexture(postTex);
		
		if(retry.isOver(touchpointx, touchpointy, true)) {
			// Restart game
			gs.reset();
		} else if(menu.isOver(touchpointx, touchpointy, true)) {
			// Return to menu
			game.enterState(States.MAINMENUSTATE);
		} else if(post.isOver(touchpointx, touchpointy, true)) {
			// Post score to Facebook
			if(!Specular.facebook.isLoggedIn()) {
				Specular.facebook.login(new LoginCallback() {
					@Override
					public void loginSuccess() {
						Specular.facebook.postHighscore(gs.getPlayer().getScore());
						game.enterState(States.MAINMENUSTATE);
					}

					@Override
					public void loginFailed() {
						game.enterState(States.MAINMENUSTATE);
					}
				});
			} else {
				Specular.facebook.postHighscore(gs.getPlayer().getScore());
				game.enterState(States.MAINMENUSTATE);
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
	
	public Button getRetryBtn() { return retry; }
	public Button getMenuBtn() { return menu; }
	public Button getPostBtn() { return post; }
	

}