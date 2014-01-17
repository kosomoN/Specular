package com.tint.specular.game;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
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
import com.tint.specular.game.entities.enemies.EnemyStupid;
import com.tint.specular.game.entities.enemies.EnemyVirus;
import com.tint.specular.game.entities.enemies.EnemyWorm;
import com.tint.specular.game.gamemodes.GameMode;
import com.tint.specular.game.gamemodes.Ranked;
import com.tint.specular.game.powerups.AddLife;
import com.tint.specular.game.powerups.BulletBurst_3;
import com.tint.specular.game.powerups.BulletBurst_5;
import com.tint.specular.game.powerups.ComboDamageBooster;
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
import com.tint.specular.map.Map;
import com.tint.specular.map.MapHandler;
import com.tint.specular.states.Facebook.LoginCallback;
import com.tint.specular.states.State;
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
	
	// Game events
	private boolean preparationDone;
	private boolean deathDone;
	
	// Fields related to game time
	public static float TICK_LENGTH = 1000000000 / 60f; //1 sec in nanos
	private float unprocessed;
	private float deathTimer;
	private int ticks;
	private long lastTickTime = System.nanoTime();
	private boolean paused = false;
	
	// Fields that affect score or gameplay
	private double scoreMultiplier = 1;
	private float damageBooster = 1f;
	private boolean enablePowerUps = true;
	private int enemiesKilled;
	
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
	
	// Custom and default fonts
	private BitmapFont arial15 = new BitmapFont();
	private BitmapFont font50;
	private static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	
	// Art
	private Texture gameOverTex;
	private Music music;
	
	public GameState(Specular game) {
		super(game);
		
		// Loading map texture from a internal directory
		Texture mapTexture = new Texture(Gdx.files.internal("graphics/game/Level2.png"));
		Texture parallax = new Texture(Gdx.files.internal("graphics/game/Parallax.png"));
		
		// Loading gameover texture
		gameOverTex = new Texture(Gdx.files.internal("graphics/menu/gameover/Game Over Title.png"));
		
		// Initializing map handler for handling many maps
		mapHandler = new MapHandler();
		mapHandler.addMap("Map", mapTexture, parallax, mapTexture.getWidth(), mapTexture.getHeight());
		currentMap = mapHandler.getMap("Map");
		
		// Initializing font
		FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Battlev2l.ttf"));
		font50 = fontGen.generateFont(50, FONT_CHARACTERS, false);
		font50.setColor(Color.RED);
		fontGen.dispose();
		
		// Initializing entities and analogstick statically
		Player.init();
		Bullet.init();
		Particle.init();
		EnemyStupid.init();
		EnemyNormal.init();
		EnemyFast.init();
		EnemyBooster.init();
		EnemyWorm.init();
		EnemyVirus.init();
		AnalogStick.init();
		
		// Initializing power-ups
		AddLife.init();
		BulletBurst_3.init();
		BulletBurst_5.init();
		ComboDamageBooster.init();
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
		music = Gdx.audio.newMusic(Gdx.files.internal("audio/02.ogg"));
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
			if(!preparationDone) {
				// Code for things before actual game start
				preparationDone = true;
			} else {
				// Adding played time
				if(entities.contains(player, true))
					ticks++;
							
				if(gameMode.isGameOver()) {
					// Code for death animation
					deathTimer -= TICK_LENGTH / 1000000;
					deathDone = deathTimer <= 0;
				} else {
					// Update game mode, enemy spawning and player hit detection
					gameMode.update(TICK_LENGTH / 1000000);
					ess.update(ticks);
					player.updateHitDetection();
				}
				
				// Updating combos and score multiplier
				cs.update();
				setScoreMultiplier(cs.getCombo());
						
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
									
									CameraShake.shake(0.2f, 0.1f);
									
									// Chance every kill to generate a power-up decreases as the amount of enemies on screen increases
									Random r = new Random();
									if(enablePowerUps)
										if(r.nextInt(100) < 10 / (enemies.size % 100 > 0 ? Math.floor(enemies.size) : 1))
											puss.spawn(e);
									
									break;
								}
							}
						}
					}
					
					if(player.isHit() && player.getSpawnTimer() <= 0) {
			        	pss.spawn(player.getLife(), true);
			        	player.setHit(false);
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
						deathTimer = 2000;
						deathDone = false;
					}
				}
				
				CameraShake.update();
			}
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
		
		// Re-positioning camera for HUD
		Specular.camera.position.set(0, 0, 0);
		Specular.camera.zoom = 1;
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		
		// Drawing analogsticks
		if(!gameMode.isGameOver()) {
			gameInputProcessor.getShootStick().render(game.batch);
			gameInputProcessor.getMoveStick().render(game.batch);
		/*
			// Debugging information
			arial15.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), -Specular.camera.viewportWidth / 2 + 10, Specular.camera.viewportHeight / 2 - 10);
			arial15.draw(game.batch, "Enities: " + entities.size, -Specular.camera.viewportWidth / 2 + 10, Specular.camera.viewportHeight / 2 - 30);
			arial15.draw(game.batch, "Player Life: " + player.getLife(), -Specular.camera.viewportWidth / 2 + 10, Specular.camera.viewportHeight / 2 - 50);
			arial15.draw(game.batch, "Memory Usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024f / 1024,
							-Specular.camera.viewportWidth / 2 + 10, Specular.camera.viewportHeight / 2 - 70);*/
			
			gameMode.render(game.batch);
		}
		
		// Drawing score in the middle top of the screen
		Util.writeCentered(game.batch, font50, "SCORE: " + player.getScore(), 0,
				Specular.camera.viewportHeight / 2 - font50.getCapHeight() - 10);
		// Drawing combo on screen
		Util.writeCentered(game.batch, font50, cs.getCombo() + "x", 0,
				Specular.camera.viewportHeight / 2 - font50.getCapHeight() * 2 - 30);

		game.batch.end();
		
		
		if(gameMode.isGameOver()) {
			if(deathDone) {
				game.batch.begin();
				game.batch.draw(gameOverTex, -Gdx.graphics.getWidth() * 3 / 4, 30);
				font50.draw(game.batch, String.valueOf(player.getScore()), -50, 100);
				game.batch.end();
				
				stage.act();
				stage.draw();
			}
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
	public void setDamageBooster(float damageBooster) { this.damageBooster = damageBooster; }
	
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
	
	/** Get a custom BitmapFont based on its size. If ther is no font with that size it returns default font.
	 * @param size - The size of the wanted font
	 * @return The custom or default font
	 */
	public BitmapFont getCustomFont(int size) {
		if(size == 50)
			return font50;
		
		return arial15;
	}
	
	public int getEnemiesKilled() {
		return enemiesKilled;
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
	private void reset() {
		clearLists();
		
		gameMode = new Ranked(this);
		
		// Adding player and setting up input processor
		pss.spawn(3, false);
		input.setInputProcessor(gameInputProcessor);
		
		resetGameTime();
		
		enemiesKilled = 0;
		
		// Disable or enable virus spawn in start, > 0 = enable & < 0 = disable
		EnemyVirus.virusAmount = 0;
	}

	@Override
	public void show() {
		super.show();
		
		/*
		 * All the positions are made after the resolution 1280x720
		 */
		// Scene2d stuff
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		// Setting up the style for the retry button
		ImageButtonStyle retryStyle = new ImageButtonStyle();
		retryStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/gameover/Retry 600 550.png"))));
		retryStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/gameover/Retry pressed.png"))));
		
		ImageButton retry = new ImageButton(retryStyle);
		retry.setSize(520, 169);
		retry.setPosition(400, 150);
		
		retry.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				reset();
			}
		});
		stage.addActor(retry);
		
		// Setting up the style for the back to main menu button
		ImageButtonStyle menuStyle = new ImageButtonStyle();
		menuStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/gameover/Main menu button pressed.png"))));
		menuStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/gameover/Main menu button 90 820.png"))));
		
		ImageButton menu = new ImageButton(menuStyle);
		menu.setSize(590, 126.5f);
		menu.setPosition(65, -40);
		
		menu.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.enterState(States.MAINMENUSTATE);
			}

		});
		stage.addActor(menu);
		
		// Setting up the style for the post highscore button
		ImageButtonStyle postStyle = new ImageButtonStyle();
		postStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/gameover/Post 1250 790.png"))));
		postStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/gameover/Post 1250 790.png"))));
		
		ImageButton postScore = new ImageButton(postStyle);
		postScore.setSize(340, 170);
		postScore.setPosition(835, -30);
		
		postScore.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(!Specular.facebook.isLoggedIn()) {
					Specular.facebook.login(new LoginCallback() {
						@Override
						public void loginSuccess() {
							Specular.facebook.postHighscore(player.getScore());
							game.enterState(States.MAINMENUSTATE);
						}

						@Override
						public void loginFailed() {
							game.enterState(States.MAINMENUSTATE);
						}
					});
				} else {
					Specular.facebook.postHighscore(player.getScore());
					game.enterState(States.MAINMENUSTATE);
				}
			}
			
		});
		stage.addActor(postScore);
		
		reset();
		
		music.play();
		music.setLooping(true);
		music.setVolume(0.3f);
		gameInputProcessor = new GameInputProcessor(game);
		input.setInputProcessor(gameInputProcessor);
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