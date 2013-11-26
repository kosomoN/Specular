package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.ui.Button;

public class SettingsMenuState extends State {
	//FIELDS
	private boolean useParticles;
	private boolean usePowerUps;
	
	private float sensitivity;
	
	private int width, height;
	private int controls;
	
	private Texture background;
	private Button[] buttons;
	
	public SettingsMenuState(Specular game) {
		super(game);
		
		/*buttons = {
			new Button(),
			new Button(),
			new Button(),
			new Button(),
			new Button()
		};*/
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		game.camera.position.set(0, 0, 0);
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		game.batch.begin();
		game.batch.draw(background, 0, 0);
		for(Button b : buttons) {
			b.renderTexture(game.batch);
		}
		game.batch.end();
	}
	
	public void update() {
		for(Button b : buttons) {
			b.update(10);
			
			if(b.isPressed()) {
				if(b.equals(buttons[0])) {
					
				} else if(b.equals(buttons[1])) {
					
				} else if(b.equals(buttons[2])) {
					
				} else if(b.equals(buttons[3])) {
					
				} else if(b.equals(buttons[4])) {
					
				}
			}
		}
	}
	
	public void changeResolution(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setSensitivity(float sensitivity) {
		this.sensitivity = sensitivity;
	}
	
	public void useParticles(boolean use) {
		useParticles = use;
	}
	
	public void usePowerUps(boolean use) {
		usePowerUps = use;
	}
	
	public void setControls(int controls) {
		this.controls = controls;
	}
	
	public boolean isUsingParticles() {
		return useParticles;
	}
	
	public boolean isUsingPowerUps() {
		return usePowerUps;
	}
	
	public int getScreenWidth() {
		return width;
	}
	
	public int getScreenHeight() {
		return height;
	}
	
	public int getControls() {
		return controls;
	}
	
	public float getSensitivity() {
		return sensitivity;
	}
}