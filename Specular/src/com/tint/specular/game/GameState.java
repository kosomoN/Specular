package com.tint.specular.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyFast;
import com.tint.specular.game.entities.enemies.EnemyNormal;
import com.tint.specular.game.powerups.PowerUp;
import com.tint.specular.map.MapHandler;
import com.tint.specular.states.State;

public class GameState extends State {
	
	//FIELDS
	private static float TICK_LENGTH = 1000000000 / 60f; //1 sec in nanos
	private float unprocessed;
	private long lastTickTime = System.nanoTime();
	
	private Array<Entity> entities = new Array<Entity>();
	private Array<Enemy> enemies = new Array<Enemy>();
	private Array<Bullet> bullets = new Array<Bullet>();
	private Array<PowerUp> powerUps = new Array<PowerUp>();
	
	private Player player = new Player(this);
	private Music music;
	private MapHandler mapHandler;
	
	private BitmapFont font = new BitmapFont();
	
	//CONSTRUCTOR
	public GameState(Specular game) {
		super(game);
		
		Texture mapTexture = new Texture(Gdx.files.internal("graphics/game/Map.png"));
		
		mapHandler = new MapHandler();
		mapHandler.addMap("Map", mapTexture, mapTexture.getWidth() * 2, mapTexture.getHeight() * 2);
		
		Player.init();
		Bullet.init();
		EnemyNormal.init();
		EnemyFast.init();
		EnemyBooster.init();
		
		for(PowerUp.Type t : PowerUp.Type.values())
			powerUps.add(new PowerUp(t));
		
		//Adding player
		player.setCenterX(mapHandler.getMap("Map").getWidth() / 2);
		player.setCenterY(mapHandler.getMap("Map").getHeight() / 2);
		addEntity(player);
		
		//Adding enemies
		addEntity(new EnemyFast(400, 400, player));
		
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/02.ogg"));
	}

	//UPDATE-RENDER loop, divided into update(float) and renderGame()
/*____________________________________________________________________*/
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
		//Checking if any bullet hit an enemy
		for(Bullet b : bullets) {
			for(Enemy e : enemies) {
				if(Math.pow(b.getX() - e.getX(), 2) + Math.pow(b.getY() - e.getY(), 2) <
						Math.pow(e.getWidth() / 2, 2) + Math.pow(b.getWidth() / 2, 2)) {
					e.hit();
					b.hit();
				}
			}
		}
		
		//Player hit detection
		player.updateHitDetection();
		
		//Removing destroyed entities
		for(Iterator<Entity> it = entities.iterator(); it.hasNext();) {
			Entity ent = it.next();
			if(ent.update()) {
				if(ent instanceof Enemy)
					enemies.removeIndex(enemies.indexOf((Enemy) ent, true));
				else if(ent instanceof Bullet)
					bullets.removeIndex(bullets.indexOf((Bullet) ent, true));
				
				it.remove();
			}
		}
		
		//IN CASE OF DEFEAT
		if(player.isDead()) {
			game.enterState(States.MENUSTATE);
		}
	}
	
	private void renderGame() {
		//Positioning camera, rendering map and entities
		Gdx.gl.glClearColor(0.2f, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		game.camera.position.set(player.getCenterX(), player.getCenterY(), 0);
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		game.batch.begin();
		mapHandler.getMap("Map").render(game.batch);
		for(Entity ent : entities) {
			ent.render(game.batch);
		}
		game.batch.end();
		
		//Drawing HUD
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
/*____________________________________________________________________*/
	
	public void addEntity(Entity entity) {
		if(entity instanceof Enemy)
			enemies.add((Enemy) entity);
		if(entity instanceof Bullet)
			bullets.add((Bullet) entity);
		entities.add(entity);
	}
	
	//GETTERS
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
	
	public Array<Bullet> getBullets() {
		Array<Bullet> bullets = new Array<Bullet>();
		
		for(Entity ent : entities) {
			if(ent instanceof Bullet) {
				bullets.add((Bullet) ent);
			}
		}
		
		return bullets;
	}

	public float getMapWidth() {
		return mapHandler.getMap("Map").getWidth();
	}

	public float getMapHeight() {
		return mapHandler.getMap("Map").getHeight();
	}
	
	@Override
	public void show() {
		super.show();
		music.play();
	}

	@Override
	public void dispose() {
		System.out.println("Dispose");
		super.dispose();
		music.dispose();
		player.dispose();
		
		for(Entity ent : entities)
			ent.dispose();
		enemies.clear();
		bullets.clear();
		
		for(PowerUp pu : powerUps) {
			if(pu.getTexture() != null)
				pu.dispose();
		}
			
	}
}
