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
import com.tint.specular.game.spawnsystems.EnemySpawnSystem;
import com.tint.specular.game.spawnsystems.PowerUpSpawnSystem;
import com.tint.specular.map.Map;
import com.tint.specular.map.MapHandler;
import com.tint.specular.states.State;

public class GameState extends State {
	
	//FIELDS
	private static float TICK_LENGTH = 1000000000 / 60f; //1 sec in nanos
	private float unprocessed;
	private long lastTickTime = System.nanoTime();
	
	private int wave = 1;
	private int enemiesKilledThisWave = 0;
	private int totalEnemiesKilled = 0;
	private int points = 0;
	private float scoreMultiplier = 1;
	
	private boolean justEntered;
	
	private Array<Entity> entities = new Array<Entity>();
	private Array<Enemy> enemies = new Array<Enemy>();
	private Array<Bullet> bullets = new Array<Bullet>();
	private Array<PowerUp> powerUps = new Array<PowerUp>();
	
	private Player player = new Player(this);
	private Music music;
	private MapHandler mapHandler;
	private Map currentMap;
	
	private EnemySpawnSystem ess;
	private PowerUpSpawnSystem pss;
	
	private BitmapFont font = new BitmapFont();
	
	//CONSTRUCTOR
	public GameState(Specular game) {
		super(game);
		
		Texture mapTexture = new Texture(Gdx.files.internal("graphics/game/Map.png"));
		
		mapHandler = new MapHandler();
		mapHandler.addMap("Map", mapTexture, mapTexture.getWidth() * 2, mapTexture.getHeight() * 2);
		currentMap = mapHandler.getMap("Map");
		
		Player.init();
		Bullet.init();
		EnemyNormal.init();
		EnemyFast.init();
		EnemyBooster.init();
		
		ess = new EnemySpawnSystem(this);
		pss = new PowerUpSpawnSystem(this);
		
		//Adding player
		player.setCenterX(currentMap.getWidth() / 2);
		player.setCenterY(currentMap.getHeight() / 2);
		addEntity(player);
		
		//Adding enemies
		addEntity(new EnemyFast(400, 400, player, this));
		
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
		if(justEntered)
			justEntered = false;
		
		//Checking if any bullet hit an enemy
		for(Bullet b : bullets) {
			for(Enemy e : enemies) {
				if(Math.pow(b.getX() - e.getX(), 2) + Math.pow(b.getY() - e.getY(), 2) <
						Math.pow(e.getWidth() / 2, 2) + Math.pow(b.getWidth() / 2, 2)) {
					e.hit();
					b.hit();
					
					if(e.isDead()) {
						enemiesKilledThisWave++;
						totalEnemiesKilled++;
					}
					
					//Spawning power-ups
					if(totalEnemiesKilled % 5 == 0) {
						pss.spawn(e);
					}
				}
			}
		}
		
		//Player hit detection
		player.updateHitDetection();
		
		//Removing destroyed entities
		for(Iterator<Entity> it = entities.iterator(); it.hasNext();) {
			Entity ent = it.next();
			if(ent.update()) {
				if(ent instanceof Enemy) {
					enemies.removeIndex(enemies.indexOf((Enemy) ent, true));
				} else if(ent instanceof Bullet) {
					bullets.removeIndex(bullets.indexOf((Bullet) ent, true));
				} else if(ent instanceof PowerUp) {
					powerUps.removeIndex(powerUps.indexOf((PowerUp) ent, true));
				}
				
				it.remove();
			}
		}
		
		//IN CASE OF DEFEAT
		if(player.isDead()) {
			System.out.println(getPoints());
			game.enterState(States.MENUSTATE);
		}
		
		if(enemiesKilledThisWave >= wave)
			nextWave();
		
	}
	
	private void renderGame() {
		//Positioning camera, rendering map and entities
		Gdx.gl.glClearColor(0.2f, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		game.camera.position.set(player.getCenterX(), player.getCenterY(), 0);
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		game.batch.begin();
		currentMap.render(game.batch);
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
		font.draw(game.batch, "Points: " + getPoints(), -game.camera.viewportWidth / 2 + 10, game.camera.viewportHeight / 2 - 70);
		game.batch.end();
	}
/*____________________________________________________________________*/
	
	public void nextWave() {
		wave++;
		ess.spawn(wave);
		enemiesKilledThisWave = 0;
	}
	
	//SETTERS
	public void addEntity(Entity entity) {
		if(entity instanceof Enemy)
			enemies.add((Enemy) entity);
		if(entity instanceof Bullet)
			bullets.add((Bullet) entity);
		if(entity instanceof PowerUp) {
			powerUps.add((PowerUp) entity);
		}
		
		entities.add(entity);
	}
	
	public void addPoints(int points) {
		this.points += points * getScoreMultiplier();
	}
	
	public void setScoreMultiplier(float multiplier) {
		scoreMultiplier = multiplier;
	}
	
	public void enter() {
		justEntered = true;
	}
	
	//GETTERS
	public Player getPlayer() {
		return player;
	}
	
	public Array<Enemy> getEnemies() {
		return enemies;
	}
	
	public Map getCurrentMap() {
		return currentMap;
	}
	
	public int getPoints() {
		return points;
	}
	
	public float getScoreMultiplier() {
		return scoreMultiplier;
		
	}
	
	@Override
	public void show() {
		super.show();
		
		//reset
		if(player.isDead() || justEntered) {
			player = new Player(this);
			player.reset();
			
			entities.clear();
			enemies.clear();
			bullets.clear();
			
			player.setCenterX(mapHandler.getMap("Map").getWidth() / 2);
			player.setCenterY(mapHandler.getMap("Map").getHeight() / 2);
			addEntity(player);
			addEntity(new EnemyFast(400, 400, player, this));
			
			wave = 1;
			enemiesKilledThisWave = 0;
			
			points = 0;
			
			ess = new EnemySpawnSystem(this);
			pss = new PowerUpSpawnSystem(this);
		}
		
		music.play();
	}

	@Override
	public void dispose() {
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
