package com.tint.specular.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.Specular;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Player;
import com.tint.specular.states.State;

public class GameState extends State {
	
	private static float TICK_LENGTH = 1000000000 / 60f; //1 sec in nanos
	private static Texture map;
	
	private Array<Entity> entities = new Array<Entity>();
	private Player player = new Player(this);
	private float unprocessed;
	private long lastTickTime = System.nanoTime();
	
	public GameState(Specular game) {
		super(game);
		
		map = new Texture(Gdx.files.internal("graphics/game/Map.png"));
		map.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Player.init();
		Bullet.init();
		
		player.setX(map.getWidth());
		player.setY(map.getHeight());
		entities.add(player);
	}

	@Override
	public void render(float delta) {
		long currTime = System.nanoTime();
		unprocessed += (currTime - lastTickTime) / TICK_LENGTH;
		lastTickTime = currTime;
		while(unprocessed >= 1) {
			unprocessed--;
			for(Entity ent : entities) {
				ent.update();
			}
		}
		
		Gdx.gl.glClearColor(0.2f, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		game.camera.position.set(player.getX(), player.getY(), 0);
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		
		game.batch.begin();
		game.batch.draw(map, 0, 0, map.getWidth() * 2, map.getHeight() * 2);
		for(Entity ent : entities) {
			ent.render(game.batch);
		}
		game.batch.end();
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public float getMapWidth() {
		return map.getWidth() * 2;
	}

	public float getMapHeight() {
		return map.getHeight() * 2;
	}
}
