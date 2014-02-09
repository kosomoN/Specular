package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.tint.specular.Specular;
import com.tint.specular.input.SettingsInputProcessor;

public class SettingsMenuState extends State {

//	private Texture background;
	private boolean rendered;
	private Texture background, selected, back, backPressed, controls, controlsPressed;
	private SettingsInputProcessor processor;
	private BitmapFont font = new BitmapFont();
	
	public SettingsMenuState(Specular game) {
		super(game);
		background = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Options.png"));
		selected = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Selected.png"));
		back = new Texture(Gdx.files.internal("graphics/menu/highscore/Back.png"));
		backPressed = new Texture(Gdx.files.internal("graphics/menu/highscore/Back Pressed.png"));
		controls = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Controls 1130 810.png"));
		controlsPressed = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Controls Pressed.png"));
	}

	@Override
	public void render(float delta) {
		// Clearing screen
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(!rendered) {
			Specular.camera.position.set(Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight / 2, 0);
			Specular.camera.update();
			game.batch.setProjectionMatrix(Specular.camera.combined);
			rendered = true;
		}
		
		game.batch.begin();
		game.batch.draw(background, 0, 0, Specular.camera.viewportWidth, Specular.camera.viewportHeight);
		
		if(processor.particlesEnabled())
			game.batch.draw(selected, 170, 604);
		if(!processor.musicMuted())
			game.batch.draw(selected, 170, 443);
		if(!processor.soundsMuted())
			game.batch.draw(selected, 170, 285);
		
		// Back button
		if(!processor.backPressed())
			game.batch.draw(back, 47, 0);
		else
			game.batch.draw(backPressed, 47, 0);
		
		if(!processor.controlsPressed())
			game.batch.draw(controls, Specular.camera.viewportWidth - controls.getWidth() - 47, -20);
		else
			game.batch.draw(controlsPressed, Specular.camera.viewportWidth - controls.getWidth() - 47, -20);
		
		font.draw(game.batch, (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth + ", " + (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight, 10, Specular.camera.viewportHeight - 40);
		game.batch.end();
	}
	
	private void createUI() {
		rendered = false;
		Specular.camera.position.set(Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight / 2, 0);
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
	}

	@Override
	public void show() {
		super.show();
		createUI();
		processor = new SettingsInputProcessor(game);
		Gdx.input.setInputProcessor(processor);
	}
}
