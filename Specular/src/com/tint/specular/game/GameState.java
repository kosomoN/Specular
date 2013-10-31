package com.tint.specular.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.Specular;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.EnemyBooster;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.EnemyFast;
import com.tint.specular.game.entities.EnemyNormal;
import com.tint.specular.game.entities.Player;
import com.tint.specular.states.State;

public class GameState extends State {
	
	private static float TICK_LENGTH = 1000000000 / 60f; //1 sec in nanos
	private static Texture map;
	
	private Array<Entity> entities = new Array<Entity>();
	private Player player = new Player(this);
	private float unprocessed;
	private long lastTickTime = System.nanoTime();
	private Music music;
	
	private BitmapFont font = new BitmapFont();
	
	public GameState(Specular game) {
		super(game);
		
		map = new Texture(Gdx.files.internal("graphics/game/Map.png"));
		map.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Player.init();
		Bullet.init();
		EnemyNormal.init();
		EnemyFast.init();
		EnemyBooster.init();
		
		player.setX(map.getWidth());
		player.setY(map.getHeight());
		entities.add(player);
		
		entities.add(new EnemyNormal(100, 100, player, this));
		entities.add(new EnemyFast(100, 100, player));
		entities.add(new EnemyBooster(100, 100, player, this));
		
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/02.ogg"));
		//music.play();
	}

	@Override
	public void render(float delta) {
		long currTime = System.nanoTime();
		unprocessed += (currTime - lastTickTime) / TICK_LENGTH;
		lastTickTime = currTime;
		while(unprocessed >= 1) {
			unprocessed--;
			entities.iterator();
			for(Iterator<Entity> it = entities.iterator(); it.hasNext();) {
				if(it.next().update())
					it.remove();
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
		
		game.camera.position.set(0, 0, 0);
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		game.batch.begin();
		font.draw(game.batch, "Enities: " + entities.size, -game.camera.viewportWidth / 2 + 10, game.camera.viewportHeight / 2 - 10);
		font.draw(game.batch, "Memory Usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024f / 1024,
						-game.camera.viewportWidth / 2 + 10, game.camera.viewportHeight / 2 - 30);
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
