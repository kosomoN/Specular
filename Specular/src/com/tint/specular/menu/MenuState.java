package com.tint.specular.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.tint.specular.Specular;
import com.tint.specular.states.State;
import com.tint.specular.ui.Button;

public class MenuState extends State {

	private static Texture background;
	private Button playBtn, exitBtn;
	
	public MenuState(Specular game) {
		super(game);
		Texture.setEnforcePotImages(false);
		
		background = new Texture("graphics/mainmenu/Title.png");
		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		playBtn = new Button();
		playBtn.setPosition(0, 0);
		
		exitBtn = new Button();
		exitBtn.setPosition(0, 0);
	}

	@Override
	public void render(float delta) {
		
		game.batch.begin();
		game.batch.draw(background, 0, 0);
		game.batch.end();
	}
}
