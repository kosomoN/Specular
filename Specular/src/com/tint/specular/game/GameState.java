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
import com.tint.specular.game.entities.enemies.EnemyShielder;
import com.tint.specular.game.entities.enemies.EnemyVirus;
import com.tint.specular.game.entities.enemies.EnemyWanderer;
import com.tint.specular.game.entities.enemies.EnemyWorm;
import com.tint.specular.game.gamemodes.GameMode;
import com.tint.specular.game.gamemodes.Ranked;
import com.tint.specular.game.powerups.AddLife;
import com.tint.specular.game.powerups.BulletBurst;
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
import com.tint.specular.states.Facebook.LoginCallback;
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
	private int powerUpSpawnTime = 400;		// 10 sec in updates / ticks
	private float scoreMultiplierTimer = 0; // 6 sec in updates / ticks
	
	// Fields that affect score or gameplay
	private int scoreMultiplier = 1;
	private int enemiesKilled;
	private boolean enablePowerUps = true;
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
		gameOverTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Background.png"));
		
		// Creating Array containing music file paths
		String[] musicFileNames = new String[]{"01.mp3","02.mp3","03.mp3","04.mp3","05.mp3","06.mp3"};
		
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
		EnemyShielder.init();
		AnalogStick.init();
		
		// Initializing power-ups
		AddLife.init();
		BulletBurst.init();
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
		
		
		//randomize music, sorry merg, remade by Mental
		
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/" + musicFileNames[(int)(Math.random()*musicFileNames.length)]));
			
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
			
			// Updating combos
			cs.update();
			
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
					powerUpSpawnTime = 400;
				}
			}
					
			if(player != null && !gameMode.isGameOver()) {
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
				
				if(!player.isHit() && player.isDead() && gameMode.isGameOver()) {
					player.respawn();
		        	pss.spawn(player.getLife(), true);
		        }
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
					
					if(!Specular.facebook.isLoggedIn()) {
						Specular.facebook.login(new LoginCallback() {
							@Override
							public void loginSuccess() {
								Specular.facebook.postHighscore(player.getScore());
							}
							
							public void loginFailed() {
							}
						});
					} else {
						Specular.facebook.postHighscore(player.getScore());
					}
				}
			}
			
			CameraShake.update();
		}
	}
	
	protected void renderGame() {
		// Clearing screen, positioning camera, rendering map and entities
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
//		game.batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE ); //Amazing stuff happens xD; Answer(Daniel): Jup xD

		if(gameMode.isGameOver()) {
			game.batch.setColor(0.6f, 0.6f, 0.6f, 1);
		}
   
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
			hud.render(game.batch, scoreMultiplierTimer);
			
			// Drawing SCORE in the middle top of the screen
			Util.writeCentered(game.batch, scoreFont, String.valueOf(player.getScore()), 0,
					Specular.camera.viewportHeight / 2 - 36);
			// Drawing MULTIPLIER on screen
			Util.writeCentered(game.batch, multiplierFont, Math.round(scoreMultiplier) + "x", 0,
					Specular.camera.viewportHeight / 2 - 98);
			gameMode.render(game.batch);
		} else {
				game.batch.setColor(Color.WHITE);
				game.batch.draw(gameOverTex, -gameOverTex.getWidth() / 2, -gameOverTex.getHeight() / 2);
			ggInputProcessor.getRetryBtn().render();
			ggInputProcessor.getMenuBtn().render();
			ggInputProcessor.getHighscoreBtn().render();
		}
		
		game.batch.end();
		//Required for the algorithm stopping the enemies from spawning on-screen
		Specular.camera.position.set(player.getCenterX(), player.getCenterY(), 0);
	}
	
	public void addEntity(Entity entity) {

		if(entity instanceof Bullet)
			bullets.add((Bullet) entity);
		else if(entity instanceof Enemy)
			enemies.add((Enemy) entity);
		else if(entity instanceof Player)
			player = (Player) entity;

		entities.add(entity);
	}
	
	public void setScoreMultiplier(int multiplier) {
		scoreMultiplier = multiplier;
		scoreMultiplierTimer = 0;
	}
	
	public Specular getGame() {	return game; }
	public GameInputProcessor getGameProcessor() { return gameInputProcessor; }
	public Stage getStage() { return stage;	}
	
	public Array<Bullet> getBullets() {	return bullets;	}
	public Array<Enemy> getEnemies() { return enemies; }
	public Array<Entity> getEntities() { return entities; }
	public Player getPlayer() {	return player; }
	
	public ParticleSpawnSystem getParticleSpawnSystem() { return pass; }
	public Map getCurrentMap() { return currentMap;	}
	
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
		
		resetGameTime();
		
		// Disable or enable virus spawn in start, > 0 = enable & < 0 = disable
		EnemyVirus.virusAmount = 0;
		
		cs.resetCombo();
		scoreMultiplier = 1;
		scoreMultiplierTimer = 0;
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