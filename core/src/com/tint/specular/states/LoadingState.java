package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;


/**
 * 
 * @author Hugo Holmqvist
 *
 */

public class LoadingState extends State {
	
	private Texture background;
//	private long timeShowed;
	private boolean hasRendered = false; 

	public LoadingState(Specular game) {
		super(game);
		
		background = new Texture(Gdx.files.internal("graphics/menu/loading/Loading.png"));
	}

	@Override
	public void hide() {
		super.hide();
		dispose();
	}

	public void render(float delta) {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		game.batch.draw(background, 0, 0);
		game.batch.end();
		
		if(hasRendered) {
			game.load();
			
//			if(System.currentTimeMillis() - timeShowed > 2000) {
				game.enterState(States.MAINMENUSTATE);
//			} 
		}
		
		hasRendered = true;
	}

	@Override
	public void show() {
		super.show();
		Specular.camera.position.set(Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight / 2, 0);
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		
//		timeShowed = System.currentTimeMillis();
	}

	@Override
	public void dispose() {
		super.dispose();
		if(background != null) {
			background.dispose();
			background = null;
		}
	}
}
