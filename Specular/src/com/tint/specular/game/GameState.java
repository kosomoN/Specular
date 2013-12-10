package com.tint.specular.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.Setting;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Particle;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyFast;
import com.tint.specular.game.entities.enemies.EnemyNormal;
import com.tint.specular.game.entities.enemies.EnemyWorm;
import com.tint.specular.game.spawnsystems.EnemySpawnSystem;
import com.tint.specular.game.spawnsystems.PlayerSpawnSystem;
import com.tint.specular.game.spawnsystems.PowerUpSpawnSystem;
import com.tint.specular.input.AnalogStick;
import com.tint.specular.input.GameInputProcessor;
import com.tint.specular.map.Map;
import com.tint.specular.map.MapHandler;
import com.tint.specular.states.State;

public class GameState extends State {
	
	private static float TICK_LENGTH = 1000000000 / 60f; //1 sec in nanos
	private float unprocessed;
	private long lastTickTime = System.nanoTime();
	private long timePlayedInMillis;
	
	private float enemiesOnScreen = 0;
	private float multiplierTime;
	private double scoreMultiplier = 1;
	
	private boolean paused;
//	private boolean useParticles;
	
	private Array<Entity> entities = new Array<Entity>();
	private Array<Player> players = new Array<Player>();
	private Array<Enemy> enemies = new Array<Enemy>();
	private Array<Bullet> bullets = new Array<Bullet>();
	private Array<Particle> particles = new Array<Particle>();
	
	private MapHandler mapHandler;
	private Map currentMap;
	
	private Music music;
	private BitmapFont font = new BitmapFont();
	private Input input;
	
	protected Player player;
	
	protected EnemySpawnSystem ess;
	protected PlayerSpawnSystem pss;
	protected PowerUpSpawnSystem puss;
//	protected ParticleSpawnSystem pass;
	
	protected HashMap<String, Setting> settings;
	protected boolean ready;
	
	public GameState(Specular game) {
		super(game);
		
		Texture mapTexture = new Texture(Gdx.files.internal("graphics/game/Map.png"));
		
		mapHandler = new MapHandler();
		mapHandler.addMap("Map", mapTexture, mapTexture.getWidth() * 2, mapTexture.getHeight() * 2);
		currentMap = mapHandler.getMap("Map");
		
		Player.init();
		Bullet.init();
		Particle.init();
		EnemyNormal.init();
		EnemyFast.init();
		EnemyBooster.init();
		EnemyWorm.init();
		AnalogStick.init();
		
		ess = new EnemySpawnSystem(this);
		pss = new PlayerSpawnSystem(this);
		puss = new PowerUpSpawnSystem(this);
		
		input = Gdx.input;
		
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/02.ogg"));
	}

	//UPDATE-RENDER loop, divided into update(float) and renderGame()
/*____________________________________________________________________*/
	@Override
	public void render(float delta) {
		if(!paused) {
			long currTime = System.nanoTime();
	        unprocessed += (currTime - lastTickTime) / TICK_LENGTH;
	        lastTickTime = currTime;
	        while(unprocessed >= 1) {
	        	unprocessed--;
	        	update();
	        }
	        if(players.size != 0) {
	        	renderGame();
	        }
		}
	}
	
	protected void update() {
		//Adding played time
		timePlayedInMillis += 10;
		
		//Updating score multiplier
		multiplierTime += 10;
		if(multiplierTime % 1000 == 0)
			setScoreMultiplier(getScoreMultiplier() - 0.1);
		
		
		enemiesOnScreen += 0.01;
		//Checking if any bullet hit an enemy
		for(Bullet b : bullets) {
			for(Enemy e : enemies) {
				if(!e.isDead()) {
					if(Math.pow(b.getX() - e.getX(), 2) + Math.pow(b.getY() - e.getY(), 2) <
							Math.pow(e.getOuterRadius(), 2) + Math.pow(b.getWidth() / 2, 2)) {
						
						e.hit(b.getShooter());
						b.hit();
						
						//5% chance every hit to generate a power-up
						Random r = new Random();
						if(r.nextInt(100) < 5) {
							puss.spawn(e);
						}
					}
				}
			}
		}
				
		//In case of death it won't update entities
		if(players.size == 0) {
			game.enterState(States.MAINMENUSTATE);
		} else {
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
			
			//Spawning new enemies
			if(timePlayedInMillis >= 2000)
				if(enemies.size < Math.floor(enemiesOnScreen))
					ess.spawn((int) Math.floor(enemiesOnScreen) - enemies.size);
		}
		
		//Player hit detection
		for(Player p : players) {
			p.updateHitDetection();
		}
	}
	
	protected void renderGame() {
		if(players.size != 0) {
			//Clearing screen, positioning camera, rendering map and entities
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
			
			//Drawing analogsticks
			getProcessor().getShootStick().render(game.batch);
			getProcessor().getMoveStick().render(game.batch);
			
			//Debugging
			font.draw(game.batch, "Enities: " + entities.size, -game.camera.viewportWidth / 2 + 10, game.camera.viewportHeight / 2 - 30);
			font.draw(game.batch, "Player Life: " + player.getLife(), -game.camera.viewportWidth / 2 + 10, game.camera.viewportHeight / 2 - 50);
			font.draw(game.batch, "Memory Usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024f / 1024,
							-game.camera.viewportWidth / 2 + 10, game.camera.viewportHeight / 2 - 70);
			
			//Power-Ups
			if(getPlayers().size != 0) {
				StringBuilder playerPowerUps = new StringBuilder();
				StringBuilder enemyPowerUp = new StringBuilder();
				if(players.get(players.size - 1).getBulletTimer().getTime() > 0)
					playerPowerUps.append("BulletBurst, ");
				if(players.get(players.size - 1).getSpeedTimer().getTime() > 0)
					playerPowerUps.append("Speedboost, ");
				else
					playerPowerUps.append(" - ");
				
				if(enemies.size > 0 && enemies.get(0).getSpeedTimer().getTime() > 0)
					enemyPowerUp.append("Slowdown, ");
				else
					enemyPowerUp.append(" - ");
				
				//Draw powerups affecting player
				font.draw(game.batch, "Active PowerUps Player" + players.size + ": " + playerPowerUps.toString(),
						-game.camera.viewportWidth / 2 + 10, game.camera.viewportHeight / 2 - 90);
				
				//Draw powerups affecting enemies
				font.draw(game.batch, "Active PowerUps Enemy: " + enemyPowerUp.toString(),
						-game.camera.viewportWidth / 2 + 10, game.camera.viewportHeight / 2 - 110);
				
			}
			game.batch.end();
		}
	}
/*____________________________________________________________________*/
	
	//SETTERS
	public void addEntity(Entity entity) {
		if(entity instanceof Player)
			players.add((Player) entity);
		else if(entity instanceof Enemy)
			enemies.add((Enemy) entity);
		else if(entity instanceof Bullet)
			bullets.add((Bullet) entity);
		else if(entity instanceof Particle)
			particles.add((Particle) entity);
		
		entities.add(entity);
	}
	
	public void setScoreMultiplier(double multiplier) {
		scoreMultiplier = multiplier < 1 ? 1 : multiplier;
		multiplierTime = 0;
	}
	
	private void reset() {
		//Wave reset
		enemiesOnScreen = 0;
		
		//Entity reset
		entities.clear();
		enemies.clear();
		bullets.clear();
		
		pss.spawn(1);
		input.setInputProcessor(new GameInputProcessor(game));
	}
	
	//GETTERS
	public Specular getGame() {
		return game;
	}
	
	public GameInputProcessor getProcessor() {
		return (GameInputProcessor) Gdx.input.getInputProcessor();
	}
	
	public HashMap<String, Setting> getSettings() {
		return settings;
	}
	
	public Array<Player> getPlayers() {
		return players;
	}
	
	public Array<Enemy> getEnemies() {
		return enemies;
	}
	
	public Array<Entity> getEntities() {
		return entities;
	}
	
	public Map getCurrentMap() {
		return currentMap;
	}
	
	public double getScoreMultiplier() {
		return scoreMultiplier;
	}
	
	public long getTimePlayed() {
		return timePlayedInMillis;
	}
	
//	public ParticleSpawnSystem getParticleSpawnSystem() {
//		return pass;
//	}
	
	public BitmapFont getFont() {
		return font;
	}
	
	@Override
	public void show() {
		super.show();
		
		/*settings = ((SettingsMenuState) game.getState(States.SETTINGSMENUSTATE)).getSettings();
		game.prefs.putBoolean("Particles", settings.get("Particles").getSelectedValue().toString().equals("On"));
		game.prefs.putString("Controls", settings.get("Controls").getSelectedValue().toString());
		game.prefs.putFloat("Width", Float.parseFloat(settings.get("Resolution").getSelectedValue().toString().split("x")[0]));
		game.prefs.putFloat("Height", Float.parseFloat(settings.get("Resolution").getSelectedValue().toString().split("x")[1]));
		
		useParticles = game.prefs.getBoolean("Particles");*/
		reset();
		
		timePlayedInMillis = 0;
		music.play();
	}
	
	@Override
	public void resume() {
		super.resume();
		paused = false;
	}

	@Override
	public void pause() {
		super.pause();
		paused = true;
	}

	@Override
	public void dispose() {
		super.dispose();
		music.dispose();
		
		for(Entity ent : entities)
			ent.dispose();
		
		enemies.clear();
		bullets.clear();
	}
}
