package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tint.specular.Specular;

public class HighscoreState extends State {
	
	private Stage stage;
	
	public HighscoreState(Specular game) {
		super(game);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act();
		stage.draw();
		
		Table.drawDebug(stage);
	}

	@Override
	public void show() {
		super.show();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, game.batch);
		
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		String[] stringArr = new String[100];
		for(int i = 0; i < stringArr.length; i++)
			stringArr[i] = i + "";
		List list = new List(stringArr, skin);
		
		ScrollPane sp = new ScrollPane(list);
		sp.getStyle().background = skin.getDrawable("black");
		sp.setSize(Gdx.graphics.getWidth() / 2 - 37, Gdx.graphics.getHeight() - 50);
		sp.setPosition(Gdx.graphics.getWidth() / 2 + 25 / 2, 25);

		Table table = new Table();
		table.debug();
		table.setSize(Gdx.graphics.getWidth() / 2 - 37, Gdx.graphics.getHeight() - 50);
		table.setPosition(25, 25);
		table.setSkin(skin);
		
		table.setBackground("black");
		table.left().top();
		table.add("Test user").left().row();
		
		table.add("Highscore: 2214").row().padLeft(30);
		table.add("Testing: 346234534514").row().padLeft(30);
		table.add("More testing: 24534534").row().padLeft(30);
		
		stage.addActor(table);
		
		stage.addActor(sp);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void dispose() {
		super.dispose();
		
		stage.dispose();
	}
	
	
}
