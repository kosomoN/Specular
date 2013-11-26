package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.states.State;
import com.tint.specular.ui.Button;

public class MainmenuState extends State {

	//FIELDS
//	private static Texture background;
	private Music music;
	private Button playBtn, exitBtn;
	
	//CONSTRUCTOR
	public MainmenuState(Specular game) {
		super(game);
		
		/*background = new Texture("graphics/mainmenu/Title.png");
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);*/
		
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/04.ogg"));
		
		//TODO Set texture, set size and position according to that
		playBtn = new Button();
		playBtn.setSize(100, 30);
		playBtn.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		
		exitBtn = new Button();
		exitBtn.setSize(100, 30);
		exitBtn.setPosition(150, 150);
	}
	
	//RENDER&UPDATE loop
/*_____________________________________________________*/
	@Override
	public void render(float delta) {
		//Update
		update(delta);
		
		//Render
		renderMenu();
	}
	
	public void renderMenu() {
		Gdx.gl.glClearColor(0.2f, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		//game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.batch.end();
		
		playBtn.renderShape(game.shape);
		exitBtn.renderShape(game.shape);
	}
	
	public void update(float delta) {
		playBtn.update(delta);
		exitBtn.update(delta);
		
		if(Gdx.input.justTouched()) {
			if(playBtn.isOver(Gdx.input.getX(), Gdx.input.getY())) {
				music.stop();
				game.enterState(States.SINGLEPLAYER_GAMESTATE);
			} else if(exitBtn.isOver(Gdx.input.getX(), Gdx.input.getY())) {
				music.stop();
				Gdx.app.exit();
			}
		}
	}
/*_____________________________________________________*/

	@Override
	public void show() {
		super.show();
		music.play();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void dispose() {
		super.dispose();
		music.dispose();
	}
}
