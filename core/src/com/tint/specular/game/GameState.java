package com.tint.specular.game;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Laser;
import com.tint.specular.game.entities.PDS;
import com.tint.specular.game.entities.Particle;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.UpgradeOrb;
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
import com.tint.specular.game.powerups.PDSPowerUp;
import com.tint.specular.game.powerups.PowerUp;
import com.tint.specular.game.powerups.Repulsor;
import com.tint.specular.game.powerups.Ricochet;
import com.tint.specular.game.powerups.ScoreMultiplier;
import com.tint.specular.game.powerups.ShieldPowerUp;
import com.tint.specular.game.powerups.SlowdownEnemies;
import com.tint.specular.game.powerups.Swarm;
import com.tint.specular.game.spawnsystems.OrbSpawnSystem;
import com.tint.specular.game.spawnsystems.ParticleSpawnSystem;
import com.tint.specular.game.spawnsystems.PlayerSpawnSystem;
import com.tint.specular.game.spawnsystems.PowerUpSpawnSystem;
import com.tint.specular.input.AnalogStick;
import com.tint.specular.input.GameInputProcessor;
import com.tint.specular.input.GameOverInputProcessor;
import com.tint.specular.input.MenuInputProcessor;
import com.tint.specular.input.PauseInputProcessor;
import com.tint.specular.map.Map;
import com.tint.specular.map.MapHandler;
import com.tint.specular.states.State;
import com.tint.specular.tutorial.Tutorial;
import com.tint.specular.tutorial.Tutorial.TutorialEvent;
import com.tint.specular.ui.HUD;
import com.tint.specular.upgrades.BoardshockUpgrade;
import com.tint.specular.upgrades.BurstUpgrade;
import com.tint.specular.upgrades.FirerateUpgrade;
import com.tint.specular.upgrades.LaserUpgrade;
import com.tint.specular.upgrades.MultiplierUpgrade;
import com.tint.specular.upgrades.PDSUpgrade;
import com.tint.specular.upgrades.RepulsorUpgrade;
import com.tint.specular.upgrades.SlowdownUpgrade;
import com.tint.specular.upgrades.SwarmUpgrade;
import com.tint.specular.upgrades.Upgrade;
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
	protected OrbSpawnSystem oss;
	protected ComboSystem cs;
	protected WaveManager waveManager;
	protected Wave currentWave;
	private int waveNumber;
	
	private TextureAtlas textureAtlas;
	
	// Boolean fields for start and end of game
	protected boolean ready, shaken;
	
	// Other
	protected GameMode gameMode;
	protected Player player;
	private Stage stage;
	private Tutorial tutorial;
	private static int PARTICLE_LIMIT;
	private static int ORB_LIMIT = 50;
	private float scoreFontAlpha = 0;
	
	// Fields related to game time
	public static float TICK_LENGTH = 1000000000 / 60f; //1 sec in nanos
	public static float TICK_LENGTH_MILLIS = 1000 / 60f;
	public static final int MULTIPLIER_COOLDOWN_TIME = 360; // ticks
	private float unprocessed;
	private int ticks;
	private long lastTickTime = System.nanoTime();
	private boolean isPaused = false;
	private int powerUpSpawnTime = 400;		// 10 sec in updates / ticks
	private float scoreMultiplierTimer = 0; // 6 sec in updates / ticks
	private float boardshockCharge = 0; //Value between 0 and 1
	private int tutorialTicks;
	private long gameOverTicks;
	
	// Fields that affect score or gameplay
	private int lastHighscore = Specular.prefs.getInteger("Highscore");
	private int scoreMultiplier = 1;
	private int enemiesKilled;
	private boolean enablePowerUps = true;
	private boolean particlesEnabled;
	private boolean tutorialOnGoing;
	private boolean showTutorialEnd;
	
	// Sound timers
	private int GAMEOVERSOUND_TIMER = 105; //ticks from entering gamoverstate to start playing sound
	
	// Lists for keeping track of entities in the world
	private Array<Entity> entities = new Array<Entity>(false, 128);
	private Array<Enemy> enemies = new Array<Enemy>(false, 64);
	private Array<Bullet> bullets = new Array<Bullet>(false, 64);
	private Array<Particle> particles = new Array<Particle>(false, PARTICLE_LIMIT);
	private Array<UpgradeOrb> orbs = new Array<UpgradeOrb>(false, ORB_LIMIT);
	private Array<PowerUp> powerups = new Array<PowerUp>(false, 64);
	
	// Upgrades
	private Upgrade[] upgrades = {new LaserUpgrade(this), new BoardshockUpgrade(this), new BurstUpgrade(this), new FirerateUpgrade(this),
			new MultiplierUpgrade(this), new PDSUpgrade(this), new RepulsorUpgrade(this), new SlowdownUpgrade(this), new SwarmUpgrade(this)};
	
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
	private BitmapFont scoreFont, multiplierFont, gameOverScoreFont;
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"`'<>";
	
	// Art
	private HUD hud;
	private Texture gameOverTex;
	private Texture newHighscore;
	private Texture pauseTex, greyPixel;
	private Music music;
	private final String[] musicFileNames = new String[]{"01.ogg","02.ogg","04.ogg","05.ogg","06.ogg"};
	private int currentMusic = -1;
	private Rectangle scissors = new Rectangle();
	private Rectangle clipBounds;
	
	private Sound gameOverSound = Gdx.audio.newSound(Gdx.files.internal("audio/fx/GameOver.ogg"));
	private Sound killSound = Gdx.audio.newSound(Gdx.files.internal("audio/fx/Destruction.ogg"));
	private Sound multiplierUpSound = Gdx.audio.newSound(Gdx.files.internal("audio/fx/MultiplierUp.ogg"));
	private Sound multiplierDownSound = Gdx.audio.newSound(Gdx.files.internal("audio/fx/MultiplierDown.ogg"));
	private boolean soundEffects;	
	
	public GameState(Specular game) {
		super(game);
		
		// Loading map texture from a internal directory
		Texture mapTexture = new Texture(Gdx.files.internal("graphics/game/packed/Level.png"));
		Texture shockLight = new Texture(Gdx.files.internal("graphics/game/packed/ShockLight.png"));
		Texture parallax = new Texture(Gdx.files.internal("graphics/game/packed/Parallax.png"));
		
		// Loading gameover textures
		gameOverTex = new Texture(Gdx.files.internal("graphics/game/packed/Background.png"));
		newHighscore = new Texture(Gdx.files.internal("graphics/menu/gameover/New Highscore.png"));
		
		// Loading pause menu textures
		pauseTex = new Texture(Gdx.files.internal("graphics/menu/pausemenu/Pause.png"));
		greyPixel = new Texture(Gdx.files.internal("graphics/menu/pausemenu/Grey Pixel.png"));
		
		textureAtlas = new TextureAtlas(Gdx.files.internal("graphics/game/packed/Specular.atlas"));
		
		// Loading HUD
		hud = new HUD(this);
		
		// Initializing map handler for handling many maps
		mapHandler = new MapHandler();
		mapHandler.addMap("Map", mapTexture, shockLight, parallax, mapTexture.getWidth(), mapTexture.getHeight(), this);
		currentMap = mapHandler.getMap("Map");
		
		clipBounds = new Rectangle(0, 0, currentMap.getWidth(), currentMap.getHeight());
		
		// Initializing font
		FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Battlev2l.ttf"));
		FreeTypeFontParameter ftfp = new FreeTypeFontParameter();
		ftfp.size = 96; // MAX SIZE
		ftfp.characters = "1234567890,";
		gameOverScoreFont = fontGen.generateFont(ftfp);
		gameOverScoreFont.setColor(Color.RED);
		
		ftfp.characters = "1234567890,tapocniue"; //Characters for "Tap to continue"
		ftfp.size = 64;
		scoreFont = fontGen.generateFont(ftfp);
		scoreFont.setColor(Color.RED);
		
		ftfp.characters = "1234567890,x";
		ftfp.size = 40;
		multiplierFont = fontGen.generateFont(ftfp);
		multiplierFont.setColor(Color.RED);
		
		// Tutorial (Must be initialized before fontGen.dispose())
		tutorial = new Tutorial(this, fontGen);
		
		fontGen.dispose();
		//Graphics Settings
		GfxSettings.init();
		
		// Tutorial
		Tutorial.init(textureAtlas);
		
		// Initializing entities and analogstick statically
		Player.init(textureAtlas);
		Bullet.init(this);
		Particle.init(textureAtlas);
		UpgradeOrb.init(textureAtlas);
		EnemyWanderer.init(textureAtlas);
		EnemyCircler.init(textureAtlas);
		EnemyStriver.init(textureAtlas);
		EnemyBooster.init(textureAtlas);
		EnemyWorm.init(textureAtlas);
		EnemyVirus.init(textureAtlas);
		EnemyShielder.init(textureAtlas);
		EnemyExploder.init(textureAtlas);
		EnemyDasher.init(textureAtlas);
		EnemyTanker.init(textureAtlas);
		AnalogStick.init(hud);
		
		// Initializing power-ups
		AddLife.init(textureAtlas);
		BulletBurst.init(textureAtlas);
		FireRateBoost.init(textureAtlas);
		ScoreMultiplier.init(textureAtlas);
		ShieldPowerUp.init(textureAtlas);
		SlowdownEnemies.init(textureAtlas);
		BoardshockPowerUp.init(textureAtlas);
		Ricochet.init(textureAtlas);
		Repulsor.init(textureAtlas);
		LaserPowerup.init(textureAtlas);
		ShockWaveRenderer.init(textureAtlas);
		Laser.init(this);
		Swarm.init(textureAtlas);
		PDSPowerUp.init(textureAtlas);
		
		pss = new PlayerSpawnSystem(this);
		puss = new PowerUpSpawnSystem(this);
		pass = new ParticleSpawnSystem(this);
		oss = new OrbSpawnSystem(this);
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
			
			if(tutorialOnGoing)
				tutorial.update();
			
			// Remove random particles if there are too many
			while(particles.size >= PARTICLE_LIMIT)
				particles.removeIndex(rand.nextInt(particles.size));
			
			// Remove random orbs if there are too many
			while(orbs.size >= ORB_LIMIT)
				orbs.removeIndex(rand.nextInt(orbs.size));
			
			// Adding played time
			if(!gameMode.isGameOver())
				ticks++;
				
			// Updating combos
			cs.update();
			
			BoardShock.update();
			
			if(scoreMultiplier > 1) {
				if(scoreMultiplierTimer < MULTIPLIER_COOLDOWN_TIME) {
					float decreaseRate = (float) scoreMultiplier / 3;
					
					scoreMultiplierTimer += decreaseRate;
				} else {
					if(isSoundEnabled())
						multiplierDownSound.play(1f, (float) Math.sqrt(scoreMultiplier / 3f), 0);
					
					scoreMultiplierTimer = 0;
					scoreMultiplier--;
				}
			}
			
			if(cs.getCombo() > 7) {
				if(isSoundEnabled())
					multiplierUpSound.play(1f, (float) Math.sqrt(scoreMultiplier / 3f), 0);
				
				setScoreMultiplier(scoreMultiplier + 1);
				if(getScoreMultiplier() % 10 == 0) {
				    float curScale = multiplierFont.getScaleX();
					multiplierFont.getData().setScale(curScale * 2);
				}
				
				cs.resetCombo();
			}
			
			if(multiplierFont.getScaleX() > 1) {
                float curScale = multiplierFont.getScaleX();
				multiplierFont.getData().setScale(curScale - 0.05f);
				if(multiplierFont.getScaleX() <= 1) {
					multiplierFont.getData().setScale(1);
				}
			}
			
			boolean playerKilled = false;		// A variable to keep track of player status
			if(!gameMode.isGameOver()) {
				// Update game mode, enemy spawning and player hit detection
				gameMode.update(TICK_LENGTH / 1000000);
				
				if(!tutorialOnGoing) {
					// Update power-ups
					powerUpSpawnTime--;
					if(powerUpSpawnTime < 0) {
						if(enablePowerUps) {
							puss.spawn();
							powerUpSpawnTime = 300;
						}
					}
				}
				
				updateHitDetections();
				
				// So that they don't spawn while death animation is playing
				if(!player.isDying() && !player.isSpawning()) {
					if(player.isDead() && !tutorialOnGoing) {
						pss.spawn(true);
			        	waveNumber++;
						currentWave = waveManager.getWave(waveNumber);
					} else {
						player.updateHitDetection();
						if(!tutorialOnGoing) {
							if(currentWave.update()) {
								waveNumber++;
								currentWave = waveManager.getWave(waveNumber);
							}
						}
					}
				}
			
				if(player.update() && !player.isDying()) {
					if(!tutorialOnGoing) {
						gameMode.playerKilled();
						playerKilled = true;
					}
				}
			} else {
				clearLists();
				gameOverTicks++;
			}
			
			// Removing destroyed entities
			for(Iterator<Entity> it = entities.iterator(); it.hasNext();) {
				Entity ent = it.next();
				if(ent.update()) {
					if(ent instanceof Particle)
						pass.getPool().free((Particle) ent);
					else if(ent instanceof UpgradeOrb)
						oss.getPool().free((UpgradeOrb) ent);  
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

			for(Iterator<UpgradeOrb> it = orbs.iterator(); it.hasNext();) {
				if(it.next().update())
					it.remove();
			}
			
			for(Iterator<Particle> it = particles.iterator(); it.hasNext();) {
				if(it.next().update())
					it.remove();
			}
			// Enemy Slowdown
			SlowdownEnemies.setUpdatedSlowdown(false);
			
			if(playerKilled && tutorialHasEnded()) {
				if(!gameMode.isGameOver()) {
					clearLists();
					resetGameTime();
					pss.spawn(false);
				}
				else {
					saveStats();
					input.setInputProcessor(ggInputProcessor);
					gameOverScoreFont.getData().setScale(14);
					
					if(Specular.nativeAndroid.isLoggedIn()) {
						Specular.nativeAndroid.postHighscore(player.getScore(), true);
					}
				}
			}
			
			Camera.update(this);
			
		}
	}
	
	protected void renderGame() {
		
		Gdx.gl.glClearDepthf(1f);
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
		
		// Clearing screen, positioning camera, rendering map and entities
		//Positioning camera to the player
		Specular.camera.zoom = 1;
		Camera.setPosition();
		Specular.camera.update();
		
		// Rendering map and entities
		game.batch.setProjectionMatrix(Specular.camera.combined);
		game.batch.begin();
		
		game.batch.setColor(1, 1, 1, 1);
		game.batch.draw(currentMap.getParallax(), -1024 + Camera.getCameraX() / 2, -1024 +  Camera.getCameraY() / 2, 4096, 4096);
		game.batch.setColor(1, 1, 1, 1);
		
		Camera.setZoom();
		BoardShock.setZoom();
		
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		
		currentMap.render(game.batch, true);
		
		ScissorStack.calculateScissors(Specular.camera, game.batch.getTransformMatrix(), clipBounds, scissors);
		ScissorStack.pushScissors(scissors);
		
		if(tutorialOnGoing && tutorial.getCurrentWave().getEvent() == TutorialEvent.POWER_UPS_SHOWN) {
			if(!tutorial.enemiesHasSpawned()) {
				Util.writeCentered(game.batch, tutorial.getFont(), "these are power-ups", tutorial.getTextX(), tutorial.getTextY() + 200);

				if(tutorial.allPowerUpsActivated())
					Util.writeCentered(game.batch, tutorial.getFont(), "some can be combined", tutorial.getTextX(), tutorial.getTextY());
			}
		}
		
		for(Entity ent : entities) {
			if(!(ent instanceof Enemy))
				ent.render(game.batch);
		}
		
        game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        
        for(UpgradeOrb orb : orbs)
        	orb.render(game.batch);
        
        for(Particle p : particles)
           p.render(game.batch);
       
        game.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		for(Entity ent : enemies) {
			ent.render(game.batch);
		}
		
		if(!gameMode.isGameOver())
			player.render(game.batch);
		
		game.batch.flush();
		ScissorStack.popScissors();
		
		// Re-positioning camera for HUD
		Specular.camera.position.set(0, 0, 0);
		Specular.camera.zoom = 1;
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		
		if(isPaused) { // Pause menu
			game.batch.draw(greyPixel, -Specular.camera.viewportWidth / 2, -Specular.camera.viewportHeight / 2, Specular.camera.viewportWidth, Specular.camera.viewportHeight);
			game.batch.draw(pauseTex, -pauseTex.getWidth() / 2, 100);
			pauseInputProcessor.getResumeButton().render();
			pauseInputProcessor.getToMenuButton().render();
		} else {
			if(!gameMode.isGameOver()) {
				//Drawing HUD
				hud.render(game.batch, scoreMultiplierTimer);
				gameInputProcessor.getShootStick().render(game.batch);
				gameInputProcessor.getMoveStick().render(game.batch);

				// Tutorial
				if(tutorialOnGoing && !showTutorialEnd)
					tutorial.render(game.batch);

				// Drawing SCORE in the middle top of the screen
				Util.writeCentered(game.batch, scoreFont, player.getFormattedScore(), 0,
						Specular.camera.viewportHeight / 2 - 36);
				// Drawing MULTIPLIER on screen
				Util.writeCentered(game.batch, multiplierFont, "x" + Math.round(scoreMultiplier), 0,
						Specular.camera.viewportHeight / 2 - 98);
				
				// Tutorial end
				if(showTutorialEnd) {
					tutorialTicks++;
					
					if(tutorialTicks > 120) {
						scoreFontAlpha = (tutorialTicks - 120) / 180f;
						scoreFontAlpha = scoreFontAlpha > 1 ? 1 : scoreFontAlpha;
						
						game.batch.setColor(1, 1, 1, scoreFontAlpha);
						game.batch.draw(greyPixel, -Specular.camera.viewportWidth / 2, -Specular.camera.viewportHeight / 2, Specular.camera.viewportWidth, Specular.camera.viewportHeight);
						game.batch.setColor(Color.WHITE);
						
						scoreFont.setColor(1, 0, 0, scoreFontAlpha);
						Util.writeCentered(game.batch, scoreFont, "tap to continue", 0, -100);
					}
				}
					
				gameMode.render(game.batch);
			} else if(gameMode.isGameOver()) { // Game over screen
				// Manual camera shake
				Specular.camera.position.set(0, 0, 0);
				Specular.camera.position.add(rand.nextFloat() * 100 * Camera.getShakeIntensity(), rand.nextFloat() * 100 * Camera.getShakeIntensity(), 0);
				Specular.camera.update();
				game.batch.setProjectionMatrix(Specular.camera.combined);
				
				game.batch.draw(greyPixel, -Specular.camera.viewportWidth / 2, -Specular.camera.viewportHeight / 2, Specular.camera.viewportWidth, Specular.camera.viewportHeight);
				game.batch.draw(gameOverTex, -gameOverTex.getWidth() / 2f, -gameOverTex.getHeight() / 2f);
				
				// Game Over effects [fade in, camera shake]
				if(gameOverScoreFont.getScaleX() > 1f) {
				    float curScale = gameOverScoreFont.getScaleX();
					gameOverScoreFont.getData().setScale(curScale - 0.1f);
					gameOverScoreFont.setColor(1, 0, 0, Math.max((10 - gameOverScoreFont.getScaleX()) / 10f, 0));
				} else {
					gameOverScoreFont.getData().setScale(1);
					if(!shaken) {
						Camera.shake(0.5f, 0.02f);
						shaken = true;
					}
					
					if(player.getScore() >= lastHighscore) {
						Util.drawCentered(game.batch, newHighscore, 0, 0, 0);
						lastHighscore = player.getScore();
					}
				}
				
				// Drawing final score and buttons
				if(music != null)
					music.setVolume(0.25f);
				if(gameOverTicks == GAMEOVERSOUND_TIMER && isSoundEnabled()) {
					gameOverSound.play(1f, 1, 0);
				}
				Util.writeCentered(game.batch, gameOverScoreFont, player.getFormattedScore(), 0, 100);
				
				game.batch.setColor(Color.WHITE);
				ggInputProcessor.getRetryBtn().render();
				ggInputProcessor.getMenuBtn().render();
				ggInputProcessor.getHighscoreBtn().render();
				
				if(player.getUpgradePoints() >= 1) {
					if(!ggInputProcessor.isTouchingUpgradeBtn()) {
						if(gameOverTicks % 90 < 40) {
							ggInputProcessor.getUpgradeBtn().setScale(1.00f);
							ggInputProcessor.getUpgradeBtn().setTouch(true);
						} else {
							ggInputProcessor.getUpgradeBtn().setScale(1.0f);
							ggInputProcessor.getUpgradeBtn().setTouch(false);
						}
					}
				}
				
				ggInputProcessor.getUpgradeBtn().render();
			}
		}
		
		game.batch.end();
	}
	
	public void updateHitDetections() {
		// Checking if any bullet hit an enemy
		for(Bullet b : bullets) {
			for(Enemy e : enemies) {
				if(e.hasSpawned() && e.getLife() > 0) {
					//Custom hitdetection for the worm
					//It just uses the parts as they were enemies
					if(e instanceof EnemyWorm) {
						for(EnemyWorm.Part p : ((EnemyWorm) e).getParts()) {
							if((b.getX() - p.getX()) * (b.getX() - p.getX()) + (b.getY() - p.getY()) * (b.getY() - p.getY()) <
									p.getOuterRadius() * p.getOuterRadius() + b.getWidth() * b.getWidth() * 4) {
								p.hit(Bullet.damage);
								b.hit();
								
								//Adding a small camerashake
									Camera.shake(0.1f, 0.05f);

								// Rewarding player depending on game mode
								enemyHit(e);
								break;
							}
						}
					} else if((b.getX() - e.getX()) * (b.getX() - e.getX()) + (b.getY() - e.getY()) * (b.getY() - e.getY()) <
							e.getOuterRadius() * e.getOuterRadius() + b.getWidth() * b.getWidth() * 4) {
						
						e.hit(Bullet.damage);
						b.hit();
					
						// Rewarding player depending on game mode
						enemyHit(e);
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
			oss.spawn(e.getX(), e.getY(), e.getDx() * Enemy.getSlowdown(), e.getDy() * Enemy.getSlowdown(), 2);
			
			Camera.shake(0.3f, 0.1f);
			
			if(isSoundEnabled())
				killSound.play(0.75f, (float) (1 + Math.random() / 3 - 0.16), 0);
		}
	}
	
	public void addEntity(Entity entity) {
		if(entity instanceof Particle) {
			particles.add((Particle) entity);
			return;
		} else if(entity instanceof UpgradeOrb) {
			orbs.add((UpgradeOrb) entity);
			return;
		}
		if(entity instanceof Bullet)
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
	
	public void clearEnemies(Array<Enemy> enemiesToSave) {
		// Removing all enemies from lists
    	for(Iterator<Enemy> it = enemies.iterator(); it.hasNext(); ) {
        	Enemy e = it.next();
        	if(!(e instanceof EnemyVirus) && !(enemiesToSave.contains(e, false))) {
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
		if(!gameMode.isGameOver()) {
			this.isPaused = paused;
			if(paused) {
				if(Gdx.input.getInputProcessor() == getGameProcessor())
					Gdx.input.setInputProcessor(getPauseProcessor());
			} else if(Gdx.input.getInputProcessor() == getPauseProcessor())
				Gdx.input.setInputProcessor(getGameProcessor());
		}
	}
	
	public void startTutorial() {
		tutorialOnGoing = true;
		tutorialTicks = 0;
		tutorial.startWaves(MenuInputProcessor.helpPressed() ? States.SINGLEPLAYER_GAMESTATE : States.MAINMENUSTATE);
	}
	
	public void endTutorial() {
		tutorialOnGoing = false;
		showTutorialEnd = false;
		tutorial.end();
		if(!(tutorial.getReturnState() == States.SINGLEPLAYER_GAMESTATE)) {
			game.enterState(tutorial.getReturnState());
		}
	}
	
	public void showTutorialEnd() {
		showTutorialEnd = true;
	}
	
	public boolean isPaused() { return isPaused; }
	public boolean tutorialHasEnded() { return !tutorialOnGoing; }
	public boolean tutorialEndIsShowing() { return showTutorialEnd; }
	
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
	public OrbSpawnSystem getOrbSpawnSystem() { return oss; }
	public Map getCurrentMap() { return currentMap;	}
	
	public Tutorial getTutorial() { return tutorial; }
	
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
	
	public float getScoreFontAlpha() { return scoreFontAlpha; }
	
	public int getScoreMultiplier() { return scoreMultiplier; }
	
	/** Get a custom BitmapFont based on its size. If there is no font with that index it returns default font.
	 * - index 0 = scoreFont
	 * - index 1 = multiplierFont
	 * - index 2 = gameOverScoreFont
	 * @param index - The index of the wanted font (See above)
	 * @return The custom or default font
	 */
	public BitmapFont getCustomFont(int index) {
		if(index == 0)
			return scoreFont;
		if(index == 1)
			return multiplierFont;
		if(index == 2)
			return gameOverScoreFont;
		
		return arial15;
	}
	
	public int getEnemiesKilled() {
		return enemiesKilled;
	}
	
	public ComboSystem getComboSystem() {
		return cs;
	}
	
	public void refreshUpgrades() {
		for(Upgrade u : upgrades) {
			u.refresh();
		}
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
		orbs.clear();
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
		gameOverTicks = 0;
		
		soundEffects = !Specular.prefs.getBoolean("SoundsMuted");
		particlesEnabled = Specular.prefs.getBoolean("Particles");
		
		if(music != null)
			music.setVolume(1f);
		
		gameMode = new Ranked(this);
		enemiesKilled = 0;
		// Adding player and setting up input processor
		pss.spawn(false);
		
		shaken = false;
		
		gameInputProcessor = new GameInputProcessor(this);
		gameInputProcessor.reset();
		input.setInputProcessor(gameInputProcessor);
		ggInputProcessor = new GameOverInputProcessor(game, this);
		pauseInputProcessor = new PauseInputProcessor(game, this);

		resetGameTime();
		
		cs.resetCombo();
		scoreMultiplier = 1;
		scoreMultiplierTimer = 0;
		
		boardshockCharge = 0;
		Bullet.maxBounces = 0;
		Bullet.setTwist(false);
		Bullet.setDamage(1);
		FireRateBoost.stacks = 0;
		
		// Refreshing upgrades
		refreshUpgrades();
		
		waveNumber = 0;
		waveManager.resetGame();
		currentWave = waveManager.getWave(waveNumber);
	}

	@Override
	public void show() {
		super.show();
		// Sets "first" time play to false
		if(Specular.prefs.getBoolean("First Time")) {
			// Start tutorial
			startTutorial();
			
			Specular.prefs.putBoolean("First Time", false);
			Specular.prefs.flush();
		} else if(MenuInputProcessor.helpPressed()) {
			Specular.prefs.putBoolean("First Time", false);
			startTutorial();
		}
		
		if(!Specular.prefs.getBoolean("MusicMuted")) {
			randomizeMusic();

		}
		
		if(GfxSettings.ReturnSetting() == GfxSettings.LOW){
			PARTICLE_LIMIT = 50;
		} else if(GfxSettings.ReturnSetting() == GfxSettings.MEDIUM){
			PARTICLE_LIMIT = 150;
		} else {
			PARTICLE_LIMIT = 300;
		}
		
		scoreFontAlpha = 0;
		
		// Initializing power-up levels
		//AddLife.reloadLevelTextures(Specular.prefs.getFloat("Life Upgrade Grade"));
		BulletBurst.reloadLevelTextures(Specular.prefs.getFloat("Burst Upgrade Grade"));
		FireRateBoost.reloadLevelTextures(Specular.prefs.getFloat("Firerate Upgrade Grade"));
		ScoreMultiplier.reloadLevelTextures(Specular.prefs.getFloat("Multiplier Upgrade Grade"));
		SlowdownEnemies.reloadLevelTextures(Specular.prefs.getFloat("Slowdown Upgrade Grade"));
		//BoardshockPowerUp.reloadLevelTextures(Specular.prefs.getFloat("Board Upgrade Grade"));
		Ricochet.reloadLevelTextures(Specular.prefs.getFloat("Ricochet Upgrade Grade"));
		Repulsor.reloadLevelTextures(Specular.prefs.getFloat("Repulsor Upgrade Grade"));
		LaserPowerup.reloadLevelTextures(Specular.prefs.getFloat("Beam Upgrade Grade"));
		Swarm.reloadLevelTextures(Specular.prefs.getFloat("Swarm Upgrade Grade"));
		PDSPowerUp.reloadLevelTextures(Specular.prefs.getFloat("PDS Upgrade Grade"));
		
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
		
		if(music != null) {
			music.stop();
			music.dispose();
		}
		
		currentMusic = random;
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/" + musicFileNames[random]));
		music.play();
		music.setVolume(1);
		
		music.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(Music music) {
				randomizeMusic();
				music.dispose();
			}
		});
	}
	
	@Override
	public void resume() {
		super.resume();
		lastTickTime = System.nanoTime();
		setPaused(true);
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
		if(music != null) {
			music.stop();
			music.dispose();
		}
		music = null;
		
		if(!gameMode.isGameOver()) {
			saveStats();
		}
		
		setPaused(false);
	}
	
	private void saveStats() {
		Specular.prefs.putInteger("Time Played Ticks", Specular.prefs.getInteger("Time Played Ticks") + ticks);
		Specular.prefs.putInteger("Bullets Fired", Specular.prefs.getInteger("Bullets Fired") + Bullet.bulletsFired);
		Bullet.bulletsFired = 0;
		
		Specular.prefs.putInteger("Bullets Missed", Specular.prefs.getInteger("Bullets Missed") + Bullet.bulletsMissed);
		Bullet.bulletsMissed = 0;
		
		Specular.prefs.putInteger("Enemies Killed", Specular.prefs.getInteger("Enemies Killed") + enemiesKilled);
		Specular.prefs.putInteger("Games Played", Specular.prefs.getInteger("Games Played") + 1);
		
		Specular.prefs.putFloat("Freeze Time", SlowdownEnemies.getFreezeTime());
		//Specular.prefs.putFloat("Boardshock Efficiency", BoardShock.getEfficiency());
		Specular.prefs.putFloat("Burst Max Time", BulletBurst.getMaxActiveTime());
		Specular.prefs.putFloat("Firerate Boost", FireRateBoost.getBoost());
		Specular.prefs.putFloat("Swarm Effect", Swarm.getEffect());
		Specular.prefs.putFloat("Repulsor Max Time", Repulsor.getMaxActiveTime());
		Specular.prefs.putFloat("PDS Damage", PDS.getDamage());
		Specular.prefs.putFloat("Laser Aiming Arc", getPlayer().getLaserArc());
		Specular.prefs.putFloat("Upgrade Points", getPlayer().getUpgradePoints());
		
		if(player.getScore() > Specular.prefs.getInteger("Highscore"))
			Specular.prefs.putInteger("Highscore", player.getScore());
		
		Specular.prefs.flush();
	}

	public Wave getCurrentWave() {
		return currentWave;
	}

	public Array<Particle> getParticles() {
		return particles;
	}
	
	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}
	
	public int getGsTicks(){
		return ticks;
	}

	public boolean isSoundEnabled() {
		return soundEffects;
	}
	
	public Music gameMusic() {
		return music;
	}
	
	public Array<UpgradeOrb> getOrbs() {
		return orbs;
	}
	
}