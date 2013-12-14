package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.tint.specular.Specular;
import com.tint.specular.utils.Util;

public class OptionState extends State {
	
	private TextureRegion scrollbar, slider;
	
	private Rectangle scrollbarBox;
	private Rectangle sliderBox;
	
	private int pressedx = 0, pressedy = 0;
	
	public OptionState(Specular game) {
		super(game);
		
		scrollbarBox = new Rectangle((Gdx.graphics.getWidth() - 500f / 1280 * Gdx.graphics.getWidth()) / 2,
				(Gdx.graphics.getHeight() - 5f / 8 * Gdx.graphics.getHeight()) / 2, 500f / 1280 * Gdx.graphics.getWidth(),
				5f / 80 * Gdx.graphics.getHeight());
		
		sliderBox = new Rectangle(scrollbarBox.getX() + (scrollbarBox.getWidth() - 15f / 1280 * Gdx.graphics.getWidth()) / 2,
				scrollbarBox.getY() + scrollbarBox.getHeight() * 15f / 50, 15f / 1280 * Gdx.graphics.getWidth(),
				1f / 40 * Gdx.graphics.getHeight());
	}
	
	//RENDER&UPDATE loop
	@Override
	public void render(float delta) {
		//Rendering
		Gdx.gl.glClearColor(30f / 255, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		game.batch.draw(scrollbar, scrollbarBox.getX(), scrollbarBox.getY(), scrollbarBox.getWidth(),
				scrollbarBox.getHeight());
		game.batch.draw(slider, sliderBox.getX(), sliderBox.getY(), sliderBox.getWidth(),
				sliderBox.getHeight());
		game.batch.end();
		
		//Updating
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			dispose();
		}
		
		if(Gdx.input.justTouched()) {
			pressedx = Gdx.input.getX();
			pressedy = Gdx.input.getY();
		}
		
		if(Gdx.input.isTouched()) {
			if(Util.isTouching(pressedx, pressedy, scrollbarBox.getX(), scrollbarBox.getY(),
					scrollbarBox.getWidth(), scrollbarBox.getHeight(), true)) {
				sliderBox.setX(Gdx.input.getX() - sliderBox.getWidth() / 2);
			}
		}
	}

	@Override
	public void show() {
		scrollbar = new TextureRegion(new Texture("data/Slider.png"), 0, 0, 500, 50);
		slider = new TextureRegion(new Texture("data/Slider.png"), 505, 0, 15, 20);
	}

	@Override
	public void dispose() {}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}
}
