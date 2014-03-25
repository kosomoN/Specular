package com.tint.specular.game;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
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
import com.tint.specular.game.entities.Laser;
import com.tint.specular.game.entities.Particle;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.Wave;
import com.tint.specular.game.entities.WaveManager;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyCircler;
import com.tint.specular.game.entities.enemies.EnemyDasher;
import com.tint.specular.game.entities.enemies.EnemyExploder;
import com.tint.specular.game.entities.enemies.EnemyShielder;
import com.tint.specular.game.entities.enemies.EnemyStriver;
import com.tint.specular.game.entities.enemies.EnemyTanker;
import com.tint.specular.game.entities.enemies.EnemyVirus;
import com.tint.specular.game.entities.enemies.EnemyWanderer;
import com.tint.specular.game.entities.enemies.EnemyWorm;
import com.tint.specular.game.gamemodes.GameMode;
import com.tint.specular.game.gamemodes.Ranked;
import com.tint.specular.game.powerups.AddLife;
import com.tint.specular.game.powerups.BoardshockPowerUp;
import com.tint.specular.game.powerups.BulletBurst;
import com.tint.specular.game.powerups.FireRateBoost;
import com.tint.specular.game.powerups.LaserPowerup;
import com.tint.specular.game.powerups.PowerUp;
import com.tint.specular.game.powerups.PushAway;
import com.tint.specular.game.powerups.Ricochet;
import com.tint.specular.game.powerups.ScoreMultiplier;
import com.tint.specular.game.powerups.ShieldUpgrade;
import com.tint.specular.game.powerups.SlowdownEnemies;
import com.tint.specular.game.spawnsystems.ParticleSpawnSystem;
import com.tint.specular.game.spawnsystems.PlayerSpawnSystem;
import com.tint.specular.game.spawnsystems.PowerUpSpawnSystem;
import com.tint.specular.input.AnalogStick;
import com.tint.specular.input.GameInputProcessor;
import com.tint.specular.input.GameOverInputProcessor;
import com.tint.specular.input.PauseInputProcessor;
import com.tint.specular.map.Map;
import com.tint.specular.map.MapHandler;
import com.tint.specular.states.NativeAndroid.RequestCallback;
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
	protected PlayerSpawnSystem pss;
	protected PowerUpSpawnSystem puss;
	protected ParticleSpawnSystem pass;
	protected ComboSystem cs;
	protected WaveManager waveManager;
	protected Wave currentWave;
	private int waveNumber;
	
	// Boolean fields for start and end of game
	protected boolean ready;
	
	// Other
	protected GameMode gameMode;
	protected Player player;
	private Stage stage;
	
	// Fields related to game time
	public static float TICK_LENGTH = 1000000000 / 60f; //1 sec in nanos
	public static float TICK_LENGTH_MILLIS = 1000 / 60f;
	private float unprocessed;
	private int ticks;
	private long lastTickTime = System.nanoTime();
	private boolean isPaused = false;
	private int powerUpSpawnTime = 400;		// 10 sec in updates / ticks
	private float scoreMultiplierTimer = 0; // 6 sec in updates / ticks
	private float boardshockCharge = 0; //Value between 0 and 1
	
	// Fields that affect score or gameplay
	private int scoreMultiplier = 1;
	private int enemiesKilled;
	private boolean enablePowerUps = true;
	private boolean particlesEnabled;
	private boolean soundsEnabled;
//	private int comboToNextScoreMult = 2;
	
	// Lists for keeping track of entities in the world
	private Array<Entity> entities = new Array<Entity>(false, 128);
	private Array<Enemy> enemies = new Array<Enemy>(false, 64);
	private Array<Bullet> bullets = new Array<Bullet>(false, 64);
	private Array<Particle> particles = new Array<Particle>(false, 64);
	private Array<PowerUp> powerups = new Array<PowerUp>(false, 64);
	
	// Map control
	private Map currentMap;
	private MapHandler mapHandler;
	
	// Input related fields
	private Input input;
	private GameInputProcessor gameInputProcessor;
	private GameOverInputProcessor ggInputProcessor;
	private PauseInputProcessor pauseInputProcessor;
	
	// Custom and default fonts
	private BitmapFont arial15 = new BitmapFont();
	private BitmapFont scoreFont, multiplierFont;
	private static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"�`'<>";
	
	// Art
	private HUD hud;
	private Texture gameOverTex;
	private Texture pauseTex;
	private Music music;
	private final String[] musicFileNames = new String[]{"01.ogg","02.ogg","03.ogg","04.ogg","05.ogg","06.ogg"};
	private int currentMusic = -1;
	
	public GameState(Specular game) {
		super(game);
		
		// Loading map texture from a internal directory
		Texture mapTexture = new Texture(Gdx.files.internal("graphics/game/maps/Level.png"));
		Texture parallax = new Texture(Gdx.files.internal("graphics/game/maps/Parallax.png"));
		
		// Loading gameover texture
		gameOverTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Background.png"));
		
		// Loading pause menu texture
		pauseTex = new Texture(Gdx.files.internal("graphics/menu/pausemenu/Pause.png"));
		
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
		Bullet.init(this);
		Particle.init();
		EnemyWanderer.init();
		EnemyCircler.init();
		EnemyStriver.init();
		EnemyBooster.init();
		EnemyWorm.init();
		EnemyVirus.init();
		EnemyShielder.init();
		EnemyExploder.init();
		EnemyDasher.init();
		EnemyTanker.init();
		AnalogStick.init();
		
		// Initializing power-ups
		AddLife.init();
		BulletBurst.init();
		FireRateBoost.init();
		ScoreMultiplier.init();
		ShieldUpgrade.init();
		SlowdownEnemies.init();
		BoardshockPowerUp.init();
		Ricochet.init();
		PushAway.init();
		LaserPowerup.init();
		
		Laser.init(this);
		
		pss = new PlayerSpawnSystem(this);
		puss = new PowerUpSpawnSystem(this);
		pass = new ParticleSpawnSystem(this);
		cs = new ComboSystem();
		waveManager = new WaveManager(this);
		
		input = Gdx.input;
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
		if(!isPaused) {
			// Adding played time
			if(gameMode.isGameOver())
				ticks++;
			
			// Updating combos
			cs.update();
			
			BoardShock.update();
			
			if(scoreMultiplier > 1) {
				if(scoreMultiplierTimer < 360) {
					float enemySizeDecrease = (float) scoreMultiplier / 2;
					
					scoreMultiplierTimer += enemySizeDecrease;
				} else {
					scoreMultiplierTimer = 0;
					scoreMultiplier--;
				}
			}
			
			if(cs.getCombo() > 7) {
				setScoreMultiplier(scoreMultiplier + 1);
				cs.resetCombo();
			}
			
			if(!gameMode.isGameOver()) {
				// Update game mode, enemy spawning and player hit detection
				gameMode.update(TICK_LENGTH / 1000000);
				
				
				// So that they don't spawn while death animation is playing
				if(!player.isSpawning() && !player.isDying() && !player.isDead()) {
					player.updateHitDetection();
					if(currentWave.update()) {
						waveNumber++;
						currentWave = waveManager.getWave(waveNumber);
					}
				}
			}
			
			// Update power-ups
			powerUpSpawnTime--;
			if(powerUpSpawnTime < 0) {
				if(enablePowerUps) {
					puss.spawn();
					powerUpSpawnTime = 400;
				}
			}
					
			if(!gameMode.isGameOver()) {
				updateHitDetections();
				if(!player.isDying() && player.isDead() && player.getLife() > 0) {
		        	pss.spawn(player.getLife(), true);
		        }
			}
			
			boolean playerKilled = false;		// A variable to keep track of player status
			
			if(!gameMode.isGameOver() && player.update()) {
				gameMode.playerKilled();
				playerKilled = true;
			}
			
			// Removing destroyed entities
			for(Iterator<Entity> it = entities.iterator(); it.hasNext();) {
				Entity ent = it.next();
				if(ent.update()) {
					if(ent instanceof Particle)
						pass.getPool().free((Particle) ent);
					else if(ent instanceof Enemy)
						enemies.removeIndex(enemies.indexOf((Enemy) ent, true));
					else if(ent instanceof PowerUp)
						powerups.removeIndex(powerups.indexOf((PowerUp) ent, true));
					else if(ent instanceof Bullet) {
						bullets.removeIndex(bullets.indexOf((Bullet) ent, true));
						Bullet.free((Bullet) ent);
					}
					
					it.remove();
				}
			}

			for(Iterator<Particle> it = particles.iterator(); it.hasNext();) {
				if(it.next().update())
					it.remove();
			}
			// Resets a few things in bullet burst
			BulletBurst.updateBulletBursts();
			
			// Enemy Slowdown
			SlowdownEnemies.setUpdatedSlowdown(false);
			
			if(playerKilled) {
				// Time attack
				if(!gameMode.isGameOver()) {
					clearLists();
					resetGameTime();
					pss.spawn(3, false);
				}
				// Ranked
				else {
					saveStats();
					input.setInputProcessor(ggInputProcessor);
					
					if(!Specular.nativeAndroid.isLoggedIn()) {
						Specular.nativeAndroid.login(new RequestCallback() {
							@Override
							public void success() {
								Specular.nativeAndroid.postHighscore(player.getScore());
							}
							
							public void failed() {
							}
						});
					} else {
						Specular.nativeAndroid.postHighscore(player.getScore());
					}
				}
			}
			
			Camera.update(this);
		}
	}
	
	protected void renderGame() {
		// Clearing screen, positioning camera, rendering map and entities
		//Positioning camera to the player		
		Specular.camera.zoom = 1;
		Camera.setPosition();
		Specular.camera.update();
		
		// Rendering map and entities
		game.batch.setProjectionMatrix(Specular.camera.combined);
		game.batch.begin();
		
		game.batch.setColor(1, 0, 0, 1);
		game.batch.draw(currentMap.getParallax(), -1024 + Camera.getCameraX() / 2, -1024 +  Camera.getCameraY() / 2, 4096, 4096);
		game.batch.setColor(1, 1, 1, 1);
		
		Camera.setZoom();
		BoardShock.setZoom();
		
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		
		currentMap.render(game.batch);
		
		for(Entity ent : entities) {
			if(!(ent instanceof Enemy))
				ent.render(game.batch);
		}
		
		game.batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		
		for(Particle p : particles)
			p.render(game.batch);
		
		for(Entity ent : entities) {
			if(ent instanceof Enemy)
				ent.render(game.batch);
		}
		
		if(!gameMode.isGameOver())
			player.render(game.batch);
		
		game.batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		
		// Re-positioning camera for HUD
		Specular.camera.position.set(0, 0, 0);
		Specular.camera.zoom = 1;
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		
		// Game over screen
		if(gameMode.isGameOver()) {
			game.batch.setColor(Color.WHITE);
			game.batch.draw(gameOverTex, -gameOverTex.getWidth() / 2, -gameOverTex.getHeight() / 2);
			ggInputProcessor.getRetryBtn().render();
			ggInputProcessor.getMenuBtn().render();
			ggInputProcessor.getHighscoreBtn().render();
		}
		// Pause menu
		else if(isPaused) {
			game.batch.draw(pauseTex, -pauseTex.getWidth() / 2, 100);
			pauseInputProcessor.getResumeButton().render();
			pauseInputProcessor.getToMenuButton().render();
		}
		// In-game
		else {
			gameInputProcessor.getShootStick().render(game.batch);
			gameInputProcessor.getMoveStick().render(game.batch);

			//Drawing HUD
			hud.render(game.batch, scoreMultiplierTimer);
			
			// Drawing SCORE in the middle top of the screen
			Util.writeCentered(game.batch, scoreFont, String.valueOf(player.getScore()), 0,
					Specular.camera.viewportHeight / 2 - 36);
			// Drawing MULTIPLIER on screen
			Util.writeCentered(game.batch, multiplierFont, "x" + Math.round(scoreMultiplier), 0,
					Specular.camera.viewportHeight / 2 - 98);
			
			multiplierFont.draw(game.batch, String.valueOf(currentWave.getID()), -Specular.camera.viewportWidth / 2 + 20, Specular.camera.viewportHeight / 2 - 20);
			gameMode.render(game.batch);
		}
		
		game.batch.end();
	}
	
	public void updateHitDetections() {
		// Checking if any bullet hit an enemy
		for(Bullet b : bullets) {
			for(Enemy e : enemies) {
				if(e.hasSpawned() && e.getLife() > 0 && (b.getX() - e.getX()) * (b.getX() - e.getX()) + (b.getY() - e.getY()) * (b.getY() - e.getY()) <
						e.getOuterRadius() * e.getOuterRadius() + b.getWidth() * b.getWidth() * 4) {
					
					e.hit(Bullet.damage);
					b.hit();
					
					//Adding a small camerashake
					Camera.shake(0.1f, 0.05f);
					
					// Rewarding player depending on game mode
					enemyHit(e);
					if(e.getLife() <= 0) {
						break;
					}
				}
			}
		}
	}
	
	public void enemyHit(Enemy e) {
		if(e.getLife() <= 0) {
			gameMode.enemyKilled(e);
			
			cs.activate(enemies.size);
			
			enemiesKilled++;
			
			Camera.shake(0.3f, 0.1f);
		}
	}
	
	public void addEntity(Entity entity) {
		if(entity instanceof Particle) {
			particles.add((Particle) entity);
			return;
		} if(entity instanceof Bullet)
			bullets.add((Bullet) entity);
		else if(entity instanceof Enemy)
			enemies.add((Enemy) entity);
		else if(entity instanceof PowerUp)
			powerups.add((PowerUp) entity);
		else if(entity instanceof Player) {
			player = (Player) entity;
			return;
		}

		entities.add(entity);
	}
	
	public void clearEnemies() {
		// Removing all enemies from lists
    	for(Iterator<Enemy> it = enemies.iterator(); it.hasNext(); ) {
        	Enemy e = it.next();
        	if(!(e instanceof EnemyVirus)) {
            	e.hit(e.getLife());
            	entities.removeValue(e, true);
            	it.remove();
        	}
    	}
	}
	
	public void setScoreMultiplier(int multiplier) {
		scoreMultiplier = multiplier;
		scoreMultiplierTimer = 0;
	}
	
	public void setPaused(boolean paused) {
		this.isPaused = paused;
		Gdx.input.setInputProcessor(getPauseProcessor());
	}
	
	public boolean isPaused() { return isPaused; }
	
	public Specular getGame() {	return game; }
	public GameInputProcessor getGameProcessor() { return gameInputProcessor; }
	public PauseInputProcessor getPauseProcessor() { return pauseInputProcessor; }
	public Stage getStage() { return stage;	}
	
	public Array<Bullet> getBullets() {	return bullets;	}
	public Array<PowerUp> getPowerUps() { return powerups; }
	public Array<Enemy> getEnemies() { return enemies; }
	public Array<Entity> getEntities() { return entities; }
	public MapHandler getMapHandler() { return mapHandler; }

	public Player getPlayer() {	return player; }
	
	public ParticleSpawnSystem getParticleSpawnSystem() { return pass; }
	public Map getCurrentMap() { return currentMap;	}
	
	public float getBoardshockCharge() {
		return boardshockCharge;
	}
	
	public void addBoardshockCharge(float x) {
		boardshockCharge += x;
		if(boardshockCharge > 1)
			boardshockCharge = 1;
	}

	public boolean particlesEnabled() {
		return particlesEnabled;
	}
	
	public boolean soundsEnabled() {
		return soundsEnabled;
	}
	
	public int getScoreMultiplier() { return scoreMultiplier; }
	
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

	// Reset stuff
	/**
	 * Reset all entities
	 */
	private void clearLists() {
		// Entity reset
		entities.clear();
		enemies.clear();
		powerups.clear();
		bullets.clear();
		particles.clear();
	}
	
	/**
	 * Reset only game time
	 */
	private void resetGameTime() {
		lastTickTime = System.nanoTime();
	}
	
	/**
	 * Total reset
	 */
	public void reset() {
		clearLists();
		
		ticks = 0;
		
		soundsEnabled = Specular.prefs.getBoolean("SoundsMuted");
		particlesEnabled = Specular.prefs.getBoolean("Particles");
		
		gameMode = new Ranked(this);
		enemiesKilled = 0;
		// Adding player and setting up input processor
		pss.spawn(3, false);
		
		gameInputProcessor = new GameInputProcessor(this);
		gameInputProcessor.reset();
		input.setInputProcessor(gameInputProcessor);
		ggInputProcessor = new GameOverInputProcessor(game, this);
		pauseInputProcessor = new PauseInputProcessor(game, this);

		resetGameTime();
		
		// Disable or enable virus spawn in start, > 0 = enable & < 0 = disable
		EnemyVirus.virusAmount = 0;
		
		cs.resetCombo();
		scoreMultiplier = 1;
		scoreMultiplierTimer = 0;
		
		boardshockCharge = 0;
		Bullet.maxBounces = 0;
		
		waveNumber = 0;
		currentWave = waveManager.getWave(waveNumber);
	}

	@Override
	public void show() {
		super.show();
		// Sets "first" time play to false
		if(Specular.prefs.getBoolean("FirstTime")) {
			Specular.prefs.putBoolean("FirstTime", false);
			Specular.prefs.flush();
		}
		
		if(!Specular.prefs.getBoolean("MusicMuted")) {
			randomizeMusic();
					
			// Creating Array containing music file paths
			music.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(Music music) {
					randomizeMusic();
				}
			});
		}
		
		ticks = 0;
		
		reset();
	}
	private Random rand = new Random();

	private void randomizeMusic() {
		int random;
		//If its not the first time randomizing
		if(currentMusic != -1) {
			 random = rand.nextInt(musicFileNames.length - 1);
			
			//To make sure the same music dosen't play twice
			if(currentMusic <= random) {
				random++;
			}
		} else {
			random = rand.nextInt(musicFileNames.length);
		}
		
		if(music != null)
			music.dispose();
		
		currentMusic = random;
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/" + musicFileNames[random]));
		music.play();
	}
	
	@Override
	public void resume() {
		super.resume();
		lastTickTime = System.nanoTime();
		isPaused = false;
	}

	@Override
	public void pause() {
		super.pause();
		setPaused(true);
	}

	@Override
	public void dispose() {
		super.dispose();
		music.dispose();
		
		for(Entity ent : entities)
			ent.dispose();
		
		enemies.clear();
		powerups.clear();
		bullets.clear();
	}

	public void boardshock() {
		if(boardshockCharge >= 1 && !player.isDying() && !player.isSpawning()) {
			BoardShock.activate(this);
			boardshockCharge = 0;
		}
	}

	@Override
	public void hide() {
		super.hide();
		music.stop();
		if(music != null)
			music.dispose();
		music = null;
		
		if(!gameMode.isGameOver()) {
			saveStats();
		}
		
		isPaused = false;
	}
	
	private void saveStats() {
		Specular.prefs.putInteger("Time Played Ticks", Specular.prefs.getInteger("Time Played Ticks") + ticks);
		Specular.prefs.putInteger("Bullets Fired", Specular.prefs.getInteger("Bullets Fired") + Bullet.bulletsFired);
		Bullet.bulletsFired = 0;
		
		Specular.prefs.putInteger("Bullets Missed", Specular.prefs.getInteger("Bullets Missed") + Bullet.bulletsMissed);
		Bullet.bulletsMissed = 0;
		
		Specular.prefs.putInteger("Enemies Killed", Specular.prefs.getInteger("Enemies Killed") + enemiesKilled);
		Specular.prefs.putInteger("Games Played", Specular.prefs.getInteger("Games Played") + 1);
		
		Specular.prefs.flush();
	}

	public Wave getCurrentWave() {
		return currentWave;
	}
}