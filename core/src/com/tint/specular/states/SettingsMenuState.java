package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.input.SettingsInputProcessor;

public class SettingsMenuState extends State {

//	private Texture background;
	private boolean rendered;
	private Texture background, selected, back, backPressed, controls, controlsPressed, daeron, daeronPressed, warriyo, warriyoPressed, artists;
	private SettingsInputProcessor processor;
	
	public SettingsMenuState(Specular game) {
		super(game);
		background = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Options.png"));
		selected = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Selected.png"));
		back = new Texture(Gdx.files.internal("graphics/menu/highscore/Back.png"));
		backPressed = new Texture(Gdx.files.internal("graphics/menu/highscore/Back Pressed.png"));
		controls = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Controls.png"));
		controlsPressed = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Controls Pressed.png"));
		
		daeron = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Daeron.png"));
		daeronPressed = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Daeron Pressed.png"));
		
		warriyo = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/WarriYo.png"));
		warriyoPressed = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/WarriYo Pressed.png"));
		
		artists = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Artists.png"));
	}

	@Override
	public void render(float delta) {
		// Clearing screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(!rendered) {
			Specular.camera.position.set(Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight / 2, 0);
			Specular.camera.update();
			game.batch.setProjectionMatrix(Specular.camera.combined);
			rendered = true;
		}
		
		game.batch.begin();
		game.batch.draw(background, 0, 0);
		
		game.batch.draw(artists, 1050, 570);
		
		if(processor.particlesEnabled())
			game.batch.draw(selected, 142, 600);
		if(!processor.musicMuted())
			game.batch.draw(selected, 142, 460);
		if(!processor.soundsMuted())
			game.batch.draw(selected, 142, 323);
		
		// Back button
		if(!processor.backPressed())
			game.batch.draw(back, 47, 0);
		else
			game.batch.draw(backPressed, 47, 0);
		
		if(!processor.controlsPressed())
			game.batch.draw(controls, Specular.camera.viewportWidth - controls.getWidth() - 47, -20);
		else
			game.batch.draw(controlsPressed, Specular.camera.viewportWidth - controls.getWidth() - 47, -20);
		
		if(!processor.isDaeronPressed())
			game.batch.draw(daeron, 1050, 450);
		else
			game.batch.draw(daeronPressed, 1050, 450);
		
		if(!processor.isWarriyoPressed())
			game.batch.draw(warriyo, 1050, 330);
		else
			game.batch.draw(warriyoPressed, 1050, 330);
		
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

	@Override
	public void hide() {
		super.hide();
		Specular.prefs.putBoolean("SoundsMuted", processor.soundsMuted()); 
        Specular.prefs.putBoolean("MusicMuted", processor.musicMuted()); 
        Specular.prefs.putBoolean("Particles", processor.particlesEnabled()); 
        Specular.prefs.flush(); 
	}
}
