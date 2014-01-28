package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLTexture;
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

	public LoadingState(Specular game) {
		super(game);
		
		GLTexture.setEnforcePotImages(false);
		background = new Texture(Gdx.files.internal("graphics/menu/loading/Loading.png"));
		


	}

	


	@Override
	public void hide() {

		super.hide();
	}


	private float timer; 
	

	public void render(float delta) {
		
		
			
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		game.batch.draw(background,0,0);
		game.batch.end();
		
		if(Gdx.input.isKeyPressed(Keys.BACK))
			Gdx.app.exit();
		
		
		//LoadingScreen timer (value 2 is in seconds)
		timer += delta;
		
		if (timer > 2 ) {
						
			game.enterState(States.MAINMENUSTATE);
		
		} 
	
	}
		
		
	


	public void update(float delta) {
		
	}

	@Override
	public void show() {
		super.show();
		Specular.camera.position.set(Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight / 2, 0);
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);


	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void dispose() {
		super.dispose();

	}

}
