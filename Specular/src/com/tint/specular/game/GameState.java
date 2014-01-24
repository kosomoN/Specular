package com.tint.specular.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.Specular;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Particle;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyFast;
import com.tint.specular.game.entities.enemies.EnemyNormal;
import com.tint.specular.game.entities.enemies.EnemyVirus;
import com.tint.specular.game.entities.enemies.EnemyWanderer;
import com.tint.specular.game.entities.enemies.EnemyWorm;
import com.tint.specular.game.gamemodes.GameMode;
import com.tint.specular.game.gamemodes.Ranked;
import com.tint.specular.game.powerups.AddLife;
import com.tint.specular.game.powerups.BulletBurst_5;
import com.tint.specular.game.powerups.FireRateBoost;
import com.tint.specular.game.powerups.ScoreMultiplier;
import com.tint.specular.game.powerups.ShieldUpgrade;
import com.tint.specular.game.powerups.SlowdownEnemies;
import com.tint.specular.game.spawnsystems.EnemySpawnSystem;
import com.tint.specular.game.spawnsystems.ParticleSpawnSystem;
import com.tint.specular.game.spawnsystems.PlayerSpawnSystem;
import com.tint.specular.game.spawnsystems.PowerUpSpawnSystem;
import com.tint.specular.input.AnalogStick;
import com.tint.specular.input.GameInputProcessor;
import com.tint.specular.input.GameOverInputProcessor;
import com.tint.specular.map.Map;
import com.tint.specular.map.MapHandler;
import com.tint.specular.states.State;
import com.tint.specular.ui.HUD;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen, Onni Kosomaa
 *
 */

public class GameState extends State {
	
	// Different spawnsystems and a system for keeping track of combos
	protected EnemySpawnSystem ess;
	protected PlayerSpawnSystem pss;
	protected PowerUpSpawnSystem puss;
	protected ParticleSpawnSystem pass;
	protected ComboSystem cs;
	
	// Boolean fields for start and end of game
	protected boolean ready;
	
	// Other
	protected GameMode gameMode;
	protected Player player;
	private Stage stage;
	private float camOffsetX, camOffsetY;
	
	// Fields related to game time
	public static float TICK_LENGTH = 1000000000 / 60f; //1 sec in nanos
	private float unprocessed;
	private int ticks;
	private long lastTickTime = System.nanoTime();
	private boolean paused = false;
	private int powerUpSpawnTime = 600;		// 10 sec in updates / ticks
	
	// Fields that affect score or gameplay
	private double scoreMultiplier = 1;
	private boolean enablePowerUps = true;
	private int enemiesKilled;
//	private int comboToNextScoreMult = 2;
	
	// Lists for keeping track of entities in the world
	private Array<Entity> entities = new Array<Entity>(false, 128);
	private Array<Enemy> enemies = new Array<Enemy>(false, 64);
	private Array<Bullet> bullets = new Array<Bullet>(false, 64);
	
	// Map control
	private Map currentMap;
	private MapHandler mapHandler;
	
	// Input related fields
	private Input input;
	private GameInputProcessor gameInputProcessor;
	private GameOverInputProcessor ggInputProcessor;
	
	// Custom and default fonts
	private BitmapFont arial15 = new BitmapFont();
	private BitmapFont scoreFont, multiplierFont;
	private static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	
	// Art
	private HUD hud;
	private Texture gameOverTex;
	private Music music;

	
	public GameState(Specular game) {
		super(game);
		
		// Loading map texture from a internal directory
		Texture mapTexture = new Texture(Gdx.files.internal("graphics/game/Level.png"));
		Texture parallax = new Texture(Gdx.files.internal("graphics/game/Parallax.png"));
		
		// Loading gameover texture
		gameOverTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Game Over Title.png"));
		
		//Loading HUD
		hud = new HUD(this);
		
		// Initializing map handler for handling many maps
		mapHandler = new MapHandler();
		mapHandler.addMap("Map", mapTexture, parallax, mapTexture.getWidth(), mapTexture.getHeight());
		currentMap = mapHandler.getMap("Map");
		
		// Initializing font
		FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Battlev2l.ttf"));
		scoreFont = fontGen.generateFont(64, FONT_CHARACTERS, false);
		scoreFont.setColor(Color.RED);
		
		multiplierFont = fontGen.generateFont(40, FONT_CHARACTERS, false);
		multiplierFont.setColor(Color.RED);
		
		fontGen.dispose();
		
		// Initializing entities and analogstick statically
		Player.init();
		Bullet.init();
		Particle.init();
		EnemyWanderer.init();
		EnemyNormal.init();
		EnemyFast.init();
		EnemyBooster.init();
		EnemyWorm.init();
		EnemyVirus.init();
		AnalogStick.init();
		
		// Initializing power-ups
		AddLife.init();
		BulletBurst_5.init();
		FireRateBoost.init();
		ScoreMultiplier.init();
		ShieldUpgrade.init();
		SlowdownEnemies.init();
		
		ess = new EnemySpawnSystem(this, enemies);
		pss = new PlayerSpawnSystem(this);
		puss = new PowerUpSpawnSystem(this);
		pass = new ParticleSpawnSystem(this);
		cs = new ComboSystem();
		
		input = Gdx.input;
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/02.mp3"));
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
	
	
	
	protected void update() {
		if(!paused) {
			// Adding played time
			if(entities.contains(player, true))
				ticks++;
			
			if(scoreMultiplier > 1)
				scoreMultiplier -= (int) (enemies.size / 10) / 240f;
			else
				scoreMultiplier = 1;
			
			if(gameMode.isGameOver()) {
				// Code for death animation
			} else {
				// Update game mode, enemy spawning and player hit detection
				gameMode.update(TICK_LENGTH / 1000000);
				player.updateHitDetection();
				// So that they don't spawn while death animation is playing
				if(!player.isSpawning() && !player.isHit())
					ess.update(ticks);
			}
			
			// Update power-ups
			powerUpSpawnTime--;
			if(powerUpSpawnTime < 0) {
				if(enablePowerUps) {
					puss.spawn();
					powerUpSpawnTime = 600;
				}
			}
					
			if(player != null && !player.isDead()) {
				// Checking if any bullet hit an enemy
				for(Bullet b : bullets) {
					for(Enemy e : enemies) {
						if((b.getX() - e.getX()) * (b.getX() - e.getX()) + (b.getY() - e.getY()) * (b.getY() - e.getY()) <
								e.getOuterRadius() * e.getOuterRadius() + b.getWidth() * b.getWidth() * 4) {
							
							// Add " * damageBooster" to enable combo damage
							e.hit(Bullet.damage);
							b.hit();
							
							//Adding a small camerashake
							CameraShake.shake(0.1f, 0.05f);
							
							// Rewarding player depending on game mode
							if(e.getLife() <= 0) {
								gameMode.enemyKilled(e);
								
								cs.activate(enemies.size);
								
								enemiesKilled++;
								
								//Adding a stronger camera shake when the enemy dies
								CameraShake.shake(0.2f, 0.1f);
								break;
							}
						}
					}
				}
				
				if(player.isHit()) {
					boolean containsParticles = false;
					for(Entity ent : entities) {
						if(ent instanceof Particle) {
							containsParticles = true;
							continue;
						}
					}
					if(!containsParticles) {
						player.respawn();
			        	pss.spawn(player.getLife(), true);
			        	player.setHit(false);
					}
		        }
			}
			
			// Updating combos
			cs.update();
			
			if(cs.getCombo() > 7) {
				setScoreMultiplier(scoreMultiplier + 1);
				cs.resetCombo();
			}
			
			boolean playerKilled = false;		// A variable to keep track of player status
			
			// Removing destroyed entities
			for(Iterator<Entity> it = entities.iterator(); it.hasNext();) {
				Entity ent = it.next();
				
				if(ent.update()) {
					if(ent instanceof Particle)
						pass.getPool().free((Particle) ent);
					else if(ent instanceof Enemy)
						enemies.removeIndex(enemies.indexOf((Enemy) ent, true));
					else if(ent instanceof Bullet)
						bullets.removeIndex(bullets.indexOf((Bullet) ent, true));
					else if(ent instanceof Player) {
						gameMode.playerKilled();
						playerKilled = true;
					}
					it.remove();
				}
			}
			
			if(playerKilled) {
				// Time attack
				if(!gameMode.isGameOver()) {
					clearLists();
					resetGameTime();
					pss.spawn(3, false);
				}
				// Ranked
				else {
					input.setInputProcessor(ggInputProcessor);
				}
			}
			
			CameraShake.update();
		}
	}
	
	protected void renderGame() {
		// Clearing screen, positioning camera, rendering map and entities
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
//		game.batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE ); //Amazing stuff happens xD; Answer(Daniel): Jup xD
		
		//Positioning camera to the player
		Specular.camera.position.set(player.getCenterX() / 2 + camOffsetX, player.getCenterY() / 2 + camOffsetY, 0);
		Specular.camera.zoom = 1;
		Specular.camera.update();
		
		game.batch.setProjectionMatrix(Specular.camera.combined);
		CameraShake.moveCamera();
		game.batch.begin();
		game.batch.draw(currentMap.getParallax(), -2048, -2048, 4096, 4096);
		
		Specular.camera.position.set(player.getCenterX(), player.getCenterY(), 0);
		CameraShake.moveCamera();
		Specular.camera.update();
		
		// Rendering map and entities
		game.batch.setProjectionMatrix(Specular.camera.combined);
		CameraShake.moveCamera();
		currentMap.render(game.batch);
		for(Entity ent : entities) {
			ent.render(game.batch);
		}
		if(player.hasShield())
			Shield.render(game.batch);
		
		// Re-positioning camera for HUD
		Specular.camera.position.set(0, 0, 0);
		Specular.camera.zoom = 1;
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		
		// Drawing analogsticks
		if(!gameMode.isGameOver()) {
			gameInputProcessor.getShootStick().render(game.batch);
			gameInputProcessor.getMoveStick().render(game.batch);

			//Drawing HUD
			hud.render(game.batch);
			
			// Drawing SCORE in the middle top of the screen
			Util.writeCentered(game.batch, scoreFont, String.valueOf(player.getScore()), 0,
					Specular.camera.viewportHeight / 2 - 36);
			// Drawing MULTIPLIER on screen
			Util.writeCentered(game.batch, multiplierFont, (int) scoreMultiplier + "x", 0,
					Specular.camera.viewportHeight / 2 - 98);
			gameMode.render(game.batch);
		}
		
		game.batch.end();
		
		if(gameMode.isGameOver()) {
			game.batch.begin();
			game.batch.draw(gameOverTex, Specular.camera.viewportWidth * (-Specular.camera.viewportWidth / 2 / 1920f), Specular.camera.viewportHeight * (70 / 1080f),
					Specular.camera.viewportWidth * (1920 / 1920f), Specular.camera.viewportHeight * (512 / 1080f));
			scoreFont.draw(game.batch, String.valueOf(player.getScore()), Specular.camera.viewportWidth * (-50 / 1920f), Specular.camera.viewportHeight * (200 / 1080f));
			
			ggInputProcessor.getRetryBtn().renderTexture(game.batch, ggInputProcessor.getRetryBtn().getX() - Specular.camera.viewportWidth / 2,
					ggInputProcessor.getRetryBtn().getY() - Specular.camera.viewportHeight / 2);
			ggInputProcessor.getMenuBtn().renderTexture(game.batch, ggInputProcessor.getMenuBtn().getX() - Specular.camera.viewportWidth / 2,
					ggInputProcessor.getMenuBtn().getY() - Specular.camera.viewportHeight / 2);
			ggInputProcessor.getPostBtn().renderTexture(game.batch, ggInputProcessor.getPostBtn().getX() - Specular.camera.viewportWidth / 2,
					ggInputProcessor.getPostBtn().getY() - Specular.camera.viewportHeight / 2);
			
			game.batch.end();
		}
		
		Specular.camera.position.set(player.getCenterX(), player.getCenterY(), 0);
	}
	
	public void addEntity(Entity entity) {
		if(entity instanceof Player)
			player = (Player) entity;
		else if(entity instanceof Enemy)
			enemies.add((Enemy) entity);
		else if(entity instanceof Bullet)
			bullets.add((Bullet) entity);
		
		entities.add(entity);
	}
	
	public void setScoreMultiplier(double multiplier) { scoreMultiplier = multiplier < 1 ? 1 : multiplier; }
	
	public Specular getGame() {	return game; }
	public GameInputProcessor getGameProcessor() { return gameInputProcessor; }
	public Stage getStage() { return stage;	}
	
	public Array<Bullet> getBullets() {	return bullets;	}
	public Array<Enemy> getEnemies() { return enemies; }
	public Array<Entity> getEntities() { return entities; }
	public Player getPlayer() {	return player; }
	
	public ParticleSpawnSystem getParticleSpawnSystem() { return pass; }
	public Map getCurrentMap() { return currentMap;	}
	
	public double getScoreMultiplier() { return scoreMultiplier; }
	
	/** Get a custom BitmapFont based on its size. If there is no font with that size it returns default font.
	 * @param size - The size of the wanted font
	 * @return The custom or default font
	 */
	public BitmapFont getCustomFont(int size) {
		if(size == 50)
			return scoreFont;
		
		return arial15;
	}
	
	public int getEnemiesKilled() {
		return enemiesKilled;
	}
	
	public ComboSystem getComboSystem() {
		return cs;
	}

	public void stopGameMusic() {
		music.stop();
	}

	// Reset stuff
	/**
	 * Reset all entities
	 */
	private void clearLists() {
		// Entity reset
		entities.clear();
		enemies.clear();
		bullets.clear();
	}
	
	/**
	 * Reset only game time
	 */
	private void resetGameTime() {
		ticks = 0;
		lastTickTime = System.nanoTime();
	}
	
	/**
	 * Total reset
	 */
	public void reset() {
		clearLists();
		
		gameMode = new Ranked(this);
		enemiesKilled = 0;
		// Adding player and setting up input processor
		pss.spawn(3, false);
		
		gameInputProcessor.reset();
		input.setInputProcessor(gameInputProcessor);
		
		Shield.init(player);
		resetGameTime();
		
		// Disable or enable virus spawn in start, > 0 = enable & < 0 = disable
		EnemyVirus.virusAmount = 0;
	}

	@Override
	public void show() {
		super.show();
		music.play();
		music.setLooping(true);
		music.setVolume(0.5f);
		gameInputProcessor = new GameInputProcessor(game);
		ggInputProcessor = new GameOverInputProcessor(game, this);
		
		reset();
	}
	
	@Override
	public void resume() {
		super.resume();
		lastTickTime = System.nanoTime();
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