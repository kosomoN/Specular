package com.tint.specular.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.Specular;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyFast;
import com.tint.specular.game.entities.enemies.EnemyNormal;
import com.tint.specular.game.powerups.PowerUp;
import com.tint.specular.map.Map;
import com.tint.specular.states.State;

public class GameState extends State {
	
	private static float TICK_LENGTH = 1000000000 / 60f; //1 sec in nanos
//	private static Texture mapTexture;
	
	private Array<Entity> entities = new Array<Entity>();
	private Array<Enemy> enemies = new Array<Enemy>();
	private Array<PowerUp> powerUps = new Array<PowerUp>();
	private Player player = new Player(this);
	private float unprocessed;
	private long lastTickTime = System.nanoTime();
	private Music music;
	private Map map;
	
	private BitmapFont font = new BitmapFont();
	
	public GameState(Specular game) {
		super(game);
		
		Texture mapTexture = new Texture(Gdx.files.internal("graphics/game/Map.png"));
		
		map = new Map(mapTexture, "Map", mapTexture.getWidth() * 2, mapTexture.getHeight() * 2);		
		Player.init();
		Bullet.init();
		EnemyNormal.init();
		EnemyFast.init();
		EnemyBooster.init();
		
		for(PowerUp.Type t : PowerUp.Type.values())
			powerUps.add(new PowerUp(t));
		
		player.setX(map.getWidth() / 2);
		player.setY(map.getHeight() / 2);
		entities.add(player);
		
		addEntity(new EnemyFast(400, 400, player));
		addEntity(new EnemyFast(400, 398, player));
		
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/02.ogg"));
		music.play();
	}

	@Override
	public void render(float delta) {
		long currTime = System.nanoTime();
		unprocessed += (currTime - lastTickTime) / TICK_LENGTH;
		lastTickTime = currTime;
		while(unprocessed >= 1) {
			unprocessed--;
			update();
		}
		renderGame();
	}
	
	private void update() {
		for(Iterator<Entity> it = entities.iterator(); it.hasNext();) {
			Entity ent = it.next();
			if(ent.update()) {
				if(ent instanceof Enemy)
					enemies.removeIndex(enemies.indexOf((Enemy) ent, true));
				it.remove();
			}
		}
		
		if(Gdx.input.justTouched()) {
			addEntity(new EnemyFast(Gdx.input.getX(), Gdx.input.getY(), player));
		}
	}
	
	private void renderGame() {
		Gdx.gl.glClearColor(0.2f, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		game.camera.position.set(player.getX(), player.getY(), 0);
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		game.batch.begin();
		map.render(game.batch);
		for(Entity ent : entities) {
			ent.render(game.batch);
		}
		game.batch.end();
		
		game.camera.position.set(0, 0, 0);
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		game.batch.begin();
		font.draw(game.batch, "Enities: " + entities.size, -game.camera.viewportWidth / 2 + 10, game.camera.viewportHeight / 2 - 10);
		font.draw(game.batch, "Enemies: " + enemies.size, -game.camera.viewportWidth / 2 + 10, game.camera.viewportHeight / 2 - 30);
		font.draw(game.batch, "Memory Usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024f / 1024,
						-game.camera.viewportWidth / 2 + 10, game.camera.viewportHeight / 2 - 50);
		game.batch.end();
	}
	
	public void addEntity(Entity entity) {
		if(entity instanceof Enemy)
			enemies.add((Enemy) entity);
		entities.add(entity);
	}
	
	public Array<PowerUp> getPowerUps() {
		return powerUps;
	}
	
	public Array<Enemy> getEnemies() {
		Array<Enemy> enemies = new Array<Enemy>();
		
		for(Entity ent : entities) {
			if(ent instanceof Enemy) {
				enemies.add((Enemy) ent);
			}
		}
		
		return enemies;
	}

	public float getMapWidth() {
		return map.getWidth();
	}

	public float getMapHeight() {
		return map.getHeight();
	}
}
