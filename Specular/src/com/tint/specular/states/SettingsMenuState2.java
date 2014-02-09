package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.input.GameInputProcessor;

public class SettingsMenuState2 extends State {

	private Stage stage;
//	private Texture background;
	private int controls;
	private boolean soundsMuted, musicMuted, particlesEnabled;
	private boolean rendered;
	
	public SettingsMenuState2(Specular game) {
		super(game);
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
		/*
		game.batch.begin();
		game.batch.draw(background, 0, 0, Specular.camera.viewportWidth, Specular.camera.viewportHeight);
		game.batch.end();*/
		
		stage.act();
		stage.draw();
	}
	
	private void createUI() {
		rendered = false;
		Specular.camera.position.set(Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight / 2, 0);
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		stage = new Stage(Specular.camera.viewportWidth, Specular.camera.viewportHeight, false, game.batch);
		stage.setCamera(Specular.camera);
		
		// Styles
		ButtonStyle controlStyle = new ButtonStyle();
		controlStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Up.png"))));
		controlStyle.checked = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Checked.png"))));
		
		ButtonStyle checkboxStyle = new ButtonStyle();
		checkboxStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Up.png"))));
		checkboxStyle.checked = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Checked.png"))));
		
		// Creating buttons and positioning them
		final Button staticTiltControl = new Button(controlStyle);
		staticTiltControl.setPosition(Specular.camera.viewportWidth / 5f, 700);
		staticTiltControl.setChecked(Specular.prefs.getInteger("Controls") == 0);
		
		final Button tiltControl = new Button(controlStyle);
		tiltControl.setPosition(Specular.camera.viewportWidth * 2 / 5f, 700);
		tiltControl.setChecked(Specular.prefs.getInteger("Controls") == 1);
		
		final Button staticStickControl = new Button(controlStyle);
		staticStickControl.setPosition(Specular.camera.viewportWidth * 3 / 5f, 700);
		staticStickControl.setChecked(Specular.prefs.getInteger("Controls") == 2);
		
		final Button dynamicControl = new Button(controlStyle);
		dynamicControl.setPosition(Specular.camera.viewportWidth * 4 / 5f, 700);
		dynamicControl.setChecked(Specular.prefs.getInteger("Controls") == 3);
		
		final Button muteSounds = new Button(checkboxStyle);
		muteSounds.setPosition((Specular.camera.viewportWidth - muteSounds.getWidth()) / 2, 500);
		muteSounds.setChecked(Specular.prefs.getBoolean("SoundsMuted"));
		
		final Button muteMusic = new Button(checkboxStyle);
		muteMusic.setPosition((Specular.camera.viewportWidth + 300) / 2, 500);
		muteMusic.setChecked(Specular.prefs.getBoolean("MusicMuted"));
		
		final Button particles = new Button(checkboxStyle);
		particles.setPosition((Specular.camera.viewportWidth - 300) / 2 - particles.getWidth(), 500);
		particles.setChecked(Specular.prefs.getBoolean("Particles"));
		
		Button confirm = new Button(controlStyle);
		confirm.setSize(64, 64);
		confirm.setPosition(Specular.camera.viewportWidth - confirm.getWidth(), 0);
		
		Button sensitivityButton = new Button(controlStyle);
		sensitivityButton.setSize(64, 64);
		sensitivityButton.setPosition(Specular.camera.viewportWidth / 2, 200);
		
		// Event listeners
		staticTiltControl.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				staticTiltControl.setChecked(true);
				tiltControl.setChecked(false);
				staticStickControl.setChecked(false);
				dynamicControl.setChecked(false);
				
				Specular.prefs.putInteger("Controls", GameInputProcessor.STATIC_TILT);
			}
		});
		
		tiltControl.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				tiltControl.setChecked(true);
				staticTiltControl.setChecked(false);
				staticStickControl.setChecked(false);
				dynamicControl.setChecked(false);
				
				Specular.prefs.putInteger("Controls", GameInputProcessor.TILT);
			}
		});
		
		staticStickControl.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				staticStickControl.setChecked(true);
				staticTiltControl.setChecked(false);
				dynamicControl.setChecked(false);
				tiltControl.setChecked(false);
				
				Specular.prefs.putInteger("Controls", GameInputProcessor.STATIC_STICKS);
			}
		});
		
		dynamicControl.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dynamicControl.setChecked(true);
				staticTiltControl.setChecked(false);
				staticStickControl.setChecked(false);
				tiltControl.setChecked(false);
				
				Specular.prefs.putInteger("Controls", GameInputProcessor.DYNAMIC_STICKS);
			}
		});
		
		muteSounds.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				soundsMuted = muteSounds.isChecked();
			}
		});
		
		muteMusic.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				musicMuted = muteMusic.isChecked();
			}
		});
		
		particles.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				particlesEnabled = particles.isChecked();
			}
		});
		
		confirm.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(staticTiltControl.isChecked())
					controls = GameInputProcessor.STATIC_TILT;
				else if(tiltControl.isChecked())
					controls = GameInputProcessor.TILT;
				else if(staticStickControl.isChecked())
					controls = GameInputProcessor.STATIC_STICKS;
				else
					controls = GameInputProcessor.DYNAMIC_STICKS;
				
				Specular.prefs.putInteger("Controls", controls);
				Specular.prefs.putBoolean("SoundsMuted", soundsMuted);
				Specular.prefs.putBoolean("MusicMuted", musicMuted);
				Specular.prefs.putBoolean("Particles", particlesEnabled);
				Specular.prefs.flush();
				
				game.enterState(States.MAINMENUSTATE);
			}
		});
		
		sensitivityButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Specular.prefs.flush();
				game.enterState(States.CONTROLSETUPSTATE);
			}
		});
		
		// Adding actors to scene
		stage.addActor(staticStickControl);
		stage.addActor(dynamicControl);
		stage.addActor(staticTiltControl);
		stage.addActor(tiltControl);
		
		stage.addActor(muteSounds);
		stage.addActor(muteMusic);
		stage.addActor(particles);
		
		stage.addActor(confirm);
		stage.addActor(sensitivityButton);
	}

	@Override
	public void show() {
		super.show();
		createUI();
		
		controls = Specular.prefs.getInteger("Controls");
		soundsMuted = Specular.prefs.getBoolean("SoundsMuted");
		musicMuted = Specular.prefs.getBoolean("MusicMuted");
		particlesEnabled = Specular.prefs.getBoolean("Particles");
		
//		background = new Texture(Gdx.files.internal(""));
		Gdx.input.setInputProcessor(stage);
	}
}
