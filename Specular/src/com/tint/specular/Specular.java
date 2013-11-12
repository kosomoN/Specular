package com.tint.specular;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tint.specular.game.GameState;
import com.tint.specular.menu.MenuState;
import com.tint.specular.states.State;

public class Specular extends Game {
	public enum States {
		MENUSTATE, GAMESTATE
	}
	
	//FIELDS
	private Map<States, State> states = new EnumMap<Specular.States, State>(States.class);
	
	public OrthographicCamera camera;
	public SpriteBatch batch;
	public ShapeRenderer shape;
	
	@Override
	public void create() {
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		
		states.put(States.MENUSTATE, new MenuState(this));
		states.put(States.GAMESTATE, new GameState(this));
		
		enterState(States.GAMESTATE);
	}
	
	public void enterState(States state) {
		State s = states.get(state);
		if(s != null)
			setScreen(s);
		else
			throw new RuntimeException("No state assigned to this enum: " + state);
	}
	
	public State getState(States state) {
		return states.get(state);
	}

	@Override
	public void dispose() {
		batch.dispose();
		shape.dispose();
	}
}