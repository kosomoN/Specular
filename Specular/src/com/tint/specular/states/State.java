package com.tint.specular.states;

import com.badlogic.gdx.Screen;
import com.tint.specular.Specular;

public abstract class State implements Screen {
	
	protected Specular game;
	
	public State(Specular game) {
		this.game = game;
	}
	
	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {}
}
