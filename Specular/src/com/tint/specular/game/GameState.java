package com.tint.specular.game;

import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

public class GameState extends State {
	
	private static float TICK_LENGTH = 1000000000 / 60f; //1 sec in nanos
	private float unprocessed;
	private long lastTickTime = System.nanoTime();
	private long timePlayedInMillis;
	
	private float enemiesOnScreen = 0;
	private float multiplierTime;
	private double scoreMultiplier = 1;
	
	private Array<Entity> entities = new Array<Entity>();
	private Array<Enemy> enemies = new Array<Enemy>();
	private Array<Bullet> bullets = new Array<Bullet>();
	private Array<Particle> particles = new Array<Particle>();
	
	private MapHandler mapHandler;
	private Map currentMap;
	
	private Music music;
	private Input input;
	
	private BitmapFont arial15 = new BitmapFont();
	private BitmapFont font50;
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"�`'<>";
	
	protected Player player;
	
	protected EnemySpawnSystem ess;
	protected PlayerSpawnSystem pss;
	protected PowerUpSpawnSystem puss;
	protected ParticleSpawnSystem pass;
	
	protected HashMap<String, Setting> settings;
	protected boolean ready;
	protected boolean gameover;
	
	private Stage stage;
	private Skin skin;
	private Table table;
	private GameInputProcessor gameInputProcessor;
	
	public GameState(Specular game) {
		super(game);
		
		//Loading map texture from a internal directory
		Texture mapTexture = new Texture(Gdx.files.internal("graphics/game/Level.png"));
		
		//Initializing map handler for handling many maps
		mapHandler = new MapHandler();
		mapHandler.addMap("Map", mapTexture, mapTexture.getWidth(), mapTexture.getHeight());
		currentMap = mapHandler.getMap("Map");
		
		//Initializing font
		FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Battlev2l.ttf"));
		font50 = fontGen.generateFont(50, FONT_CHARACTERS, false);
		font50.setColor(Color.RED);
		fontGen.dispose();
		
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
		pass = new ParticleSpawnSystem(this);
		
		input = Gdx.input;
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
	
	protected void update() {
		//Adding played time
		timePlayedInMillis += 10;
		
		//Updating score multiplier
		multiplierTime += 10;
		if(multiplierTime % 1000 == 0)
			setScoreMultiplier(getScoreMultiplier() - 0.1);
		
		
		enemiesOnScreen += 0.01;
		if(player != null && player.getLife() > 0) {
			//Checking if any bullet hit an enemy
			for(Bullet b : bullets) {
				for(Enemy e : enemies) {
					if(e.getLife() > 0) {
						if((b.getX() - e.getX()) * (b.getX() - e.getX()) + (b.getY() - e.getY()) * (b.getY() - e.getY()) <
								e.getOuterRadius() * e.getOuterRadius() + b.getWidth() * b.getWidth() * 4) {
							
							e.hit(b.getShooter());
							b.hit();
							
							//5% chance every hit to generate a power-up
							/*Random r = new Random();
							if(r.nextInt(100) < 5) {
								puss.spawn(e);
							}*/
						}
					}
				}
			}
		}
		
		//Removing destroyed entities
		for(Iterator<Entity> it = entities.iterator(); it.hasNext();) {
			Entity ent = it.next();
			if(ent.update()) {
				if(ent instanceof Enemy)
					enemies.removeIndex(enemies.indexOf((Enemy) ent, true));
				else if(ent instanceof Bullet)
					bullets.removeIndex(bullets.indexOf((Bullet) ent, true));
				else if(ent instanceof Player) {
					gameover = true;
//					table.setVisible(true);
					input.setInputProcessor(stage);
				}
				it.remove();
			}
		}
		
		//Spawning new enemies
		if(timePlayedInMillis >= 2000)
			if(enemies.size < Math.floor(enemiesOnScreen))
				ess.spawn((int) Math.floor(enemiesOnScreen) - enemies.size);
		
		//Player hit detection
		if(!player.isDead())
			player.updateHitDetection();
		
		CameraShake.update();
	}
	
	protected void renderGame() {
		//Clearing screen, positioning camera, rendering map and entities
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
//		game.batch.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE ); //Amazing stuff happens xD
		
		Specular.camera.position.set(player.getCenterX() / 2, player.getCenterY() / 2, 0);
		Specular.camera.zoom = 1;
		Specular.camera.update();
		
		game.batch.setProjectionMatrix(Specular.camera.combined);
		CameraShake.moveCamera();
		game.batch.begin();
		game.batch.draw(currentMap.getTexture(), -2048, -2048, 4096, 4096);
		
		Specular.camera.position.set(player.getCenterX(), player.getCenterY(), 0);
		CameraShake.moveCamera();
		/*float zoom = 1;
		float time = timePlayedInMillis % 3000;
		if(time > 2500) {
			if(time == 2990) {
				CameraShake.shake(1, 0.04f);
			} else if(time > 2950) {
				zoom = 1 - ((1 - ((time - 2950) / 50)) * (1 - ((time - 2950) / 50))) * 0.25f;
			} else {
				zoom = 1 - (1 - (1 - ((time - 2500) / 450)) * (1 - ((time - 2500) / 450))) * 0.25f;
				System.out.println(zoom);
			}
		}
		Specular.camera.zoom = zoom;*/
		Specular.camera.update();
		
		game.batch.setProjectionMatrix(Specular.camera.combined);
		CameraShake.moveCamera();
		currentMap.render(game.batch);
		for(Entity ent : entities) {
			ent.render(game.batch);
		}
		
		//Positioning camera
		Specular.camera.position.set(0, 0, 0);
		Specular.camera.zoom = 1;
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		
		//Drawing analogsticks
		if(!gameover) {
			gameInputProcessor.getShootStick().render(game.batch);
			gameInputProcessor.getMoveStick().render(game.batch);
		
//			player.renderLifebar(game.batch);
		
			//Drawing score

			//Debugging
			/*
			arial15.draw(game.batch, "Enities: " + entities.size, -Specular.camera.viewportWidth / 2 + 10, Specular.camera.viewportHeight / 2 - 30);
			arial15.draw(game.batch, "Player Life: " + player.getLife(), -Specular.camera.viewportWidth / 2 + 10, Specular.camera.viewportHeight / 2 - 50);
			arial15.draw(game.batch, "Memory Usage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024f / 1024,
							-Specular.camera.viewportWidth / 2 + 10, Specular.camera.viewportHeight / 2 - 70);/**/
		}
		Util.writeCentered(game.batch, font50, "SCORE: " + player.getScore(), 0,
					Specular.camera.viewportHeight / 2 - font50.getCapHeight() - 10);
		game.batch.end();
		
		if(gameover) {
			stage.act();
			stage.draw();
		}
		
		Specular.camera.position.set(player.getCenterX(), player.getCenterY(), 0);
	}
/*____________________________________________________________________*/
	
	//SETTERS
	public void addEntity(Entity entity) {
		if(entity instanceof Player)
			player = (Player) entity;
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
	
	//GETTERS
	public Specular getGame() {
		return game;
	}
	
	public GameInputProcessor getProcessor() {
		return gameInputProcessor;
	}
	
	public HashMap<String, Setting> getSettings() {
		return settings;
	}
	
	public Array<Enemy> getEnemies() {
		return enemies;
	}
	
	public Array<Entity> getEntities() {
		return entities;
	}
	
	public Player getPlayer() {
		return player;
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
	
	/** Get a custom BitmapFont based on its size. If ther is no font with that size it returns null.
	 * @param size - The size of the wanted font
	 * @return The custom font
	 */
	public BitmapFont getCustomFont(int size) {
			if(size == 30)
				return font50;
			
			System.err.println("No font with size " + size);
			return null;
	}
	
	/**
	 * Gets the default font which is the arial font with size 15
	 * @return Arial font
	 */
	public BitmapFont getDefaultFont() {
		return arial15;
	}
	
	private void reset() {
		gameover = false;
		timePlayedInMillis = 0;
		
		//Reset table
//		table.clear();
		
		//Entity reset
		entities.clear();
		enemies.clear();
		bullets.clear();
		
		//Wave reset
		enemiesOnScreen = 0;
		
		//Adding player and setting up input processor
		pss.spawn(1);
		input.setInputProcessor(gameInputProcessor);
	}
	
	@Override
	public void show() {
		super.show();
		
		//Scene2d stuff
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		/*
		skin = new Skin();
		skin.add("font30", font50);
		skin.add("font15", arial15);
		skin.add("background", new Texture(Gdx.files.internal("graphics/")));
		skin.add("default", new LabelStyle(font50, Color.RED));
		
		table = new Table();
		table.setVisible(false);
		table.center();*/
		TextButton tb = new TextButton("Post highscore!", skin);
		tb.setSize(300, 150);
		tb.setPosition((Gdx.graphics.getWidth() - 300) / 2, (Gdx.graphics.getHeight() - 150) / 2);
		tb.addListener(new ChangeListener() {
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
//		table.add(tb).fill();
		
		stage.addActor(tb);
		/*
		table.setSize(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		table.setPosition((Gdx.graphics.getWidth() - table.getWidth()) / 2, (Gdx.graphics.getHeight() - table.getHeight()) / 2);
		table.setSkin(skin);
		
		table.setBackground("background");
		

		
		TextButtonStyle btnStyle = new TextButtonStyle(skin.getDrawable("background"),
				skin.getDrawable("background"), skin.getDrawable("background"), skin.getFont("font15"));
		TextButton playAgain = new TextButton("Play Again", btnStyle);
		playAgain.setSize(100, 30);
		playAgain.setPosition(table.getX() + 10, table.getY() + 10);
		playAgain.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				reset();
				System.out.println("helo");
			}
		});
		stage.addActor(playAgain);
		
		TextButton highscores = new TextButton("Highscores", btnStyle);
		highscores.setSize(100, 30);
		highscores.setPosition(table.getX() + table.getWidth() - highscores.getWidth() - 10, table.getY() + 10);
		highscores.addListener(new EventListener() {
			
			@Override
			public boolean handle(Event event) {
				System.out.println("hn");
				return false;
			}
		});
		stage.addActor(highscores);*/
		
		reset();
		music.play();
		gameInputProcessor = new GameInputProcessor(game);
		input.setInputProcessor(gameInputProcessor);
		
		timePlayedInMillis = 0;
	}
	
	@Override
	public void resume() {
		super.resume();
		
		lastTickTime = System.nanoTime();
	}

	@Override
	public void pause() {
		super.pause();
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

	public ParticleSpawnSystem getPass() {
		return pass;
	}
}