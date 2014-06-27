package com.tint.specular.states;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Player;
import com.tint.specular.input.AnalogStick;
import com.tint.specular.map.Map;
import com.tint.specular.utils.Util;
import com.badlogic.gdx.audio.Music;


public class TutorialState extends State {

	private DummyPlayer player = new DummyPlayer();
	private Array<Bullet> bullets = new Array<Bullet>();
	private GameState gs;
	private Map map;
	private double unprocessed;
	private long lastTickTime;
	private float sensitivity;
	private boolean tilt;
	public ControlInputProcessor inputProcessor;
	private Stage stage;
	private boolean staticSticks;
	private WidgetGroup hideableUI;
	private final String[] musicFileNames = new String[]{"01.ogg","02.ogg","03.ogg","05.ogg","06.ogg"};
	private Music music;
	private int currentMusic = -1;

	
	public TutorialState(Specular game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();
		//Map
		gs = (GameState) game.getState(States.SINGLEPLAYER_GAMESTATE);
		map = gs.getMapHandler().getMap("Map");
		
		//Player
		player.x = map.getWidth() / 2;
		player.y = map.getHeight() / 2;
		player.anim = Player.anim;
		player.barrelTexture = Player.barrelTexture[0];
		
		//Input
		stage = new Stage(new ExtendViewport(1920, 1080), game.batch);
		
		//Music
		randomizeMusic();
		
		lastTickTime = System.nanoTime();
		
		inputProcessor = new ControlInputProcessor();
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, inputProcessor));	
		
		TextureRegion backButton = new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/highscore/Back.png")));
		TextureRegion backButtonDown = new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/highscore/Back Pressed.png")));
		Button backBtn = new Button(new TextureRegionDrawable(backButton), new TextureRegionDrawable(backButtonDown));
		
		backBtn.setPosition((stage.getWidth() - Specular.camera.viewportWidth) / 2 + 47, (stage.getHeight() - Specular.camera.viewportHeight) / 2);
		
		backBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.enterState(States.SETTINGSMENUSTATE);
			}
		});

		hideableUI = new WidgetGroup();
		hideableUI.addActor(backBtn);
		stage.addActor(hideableUI);
	}
	
	private void renderGame() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		Specular.camera.position.set(player.x, player.y, 0);
		Specular.camera.update();
		
		game.batch.setProjectionMatrix(Specular.camera.combined);
		game.batch.begin();
		
		game.batch.setColor(1, 0, 0, 1);
		game.batch.draw(map.getParallax(), -1024 + player.x / 2, -1024 +  player.y / 2, 4096, 4096);
		game.batch.setColor(1, 1, 1, 1);
		
		map.render(game.batch, false);
		
		player.render(game.batch);
		
		for(Bullet b : bullets)
			b.render(game.batch);
		
		Specular.camera.position.set(0, 0, 0);
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		
		// Drawing analogsticks
		inputProcessor.shoot.render(game.batch);
		inputProcessor.move.render(game.batch);
		
		game.batch.end();
		
		stage.act();
		stage.draw();
	}

	private void update() {
		player.update();
		
		for(Iterator<Bullet> it = bullets.iterator(); it.hasNext();) {
			if(it.next().update())
				it.remove();
		}
	}
	
	private class DummyPlayer {
		public float x, y, dx, dy;
		private Animation anim;
		private float animFrameTime;
		private float direction;
		private Texture barrelTexture;
		private int timeSinceLastFire;
		private int fireRate = 10;
		
		private void render(SpriteBatch batch) {
			animFrameTime += Gdx.graphics.getDeltaTime();
			TextureRegion baseAnimFrame = anim.getKeyFrame(animFrameTime, true);
			Util.drawCentered(batch, barrelTexture, x, y, direction);
			batch.draw(baseAnimFrame, x - baseAnimFrame.getRegionWidth() / 2, y - baseAnimFrame.getRegionHeight() / 2);
		}

		private void update() {
			if(tilt) {
				changeSpeed(Gdx.input.getAccelerometerX() * 0.1f * 0.6f, Gdx.input.getAccelerometerY() * 0.1f * 0.6f);
			} else {
				float maxSpeedAreaSquared = (Specular.camera.viewportWidth / 8 * sensitivity) * (Specular.camera.viewportWidth / 8 * sensitivity);
				
				AnalogStick moveStick = inputProcessor.move;
				float moveDx = moveStick.getXHead() - moveStick.getXBase();
				float moveDy = moveStick.getYHead() - moveStick.getYBase();
				float distBaseToHead = moveDx * moveDx + moveDy * moveDy;
				
				if(moveStick.isActive() && distBaseToHead != 0) {
					//calculating the angle with delta x and delta y
					double angle = Math.atan2(moveDy, moveDx);
					
					changeSpeed(
							(float) Math.cos(angle) * Player.MAX_DELTA_SPEED *
							(distBaseToHead >= maxSpeedAreaSquared ? 1 : distBaseToHead / maxSpeedAreaSquared),
							
							(float) Math.sin(angle) * Player.MAX_DELTA_SPEED *
							(distBaseToHead >= maxSpeedAreaSquared ? 1 : distBaseToHead / maxSpeedAreaSquared)
							);
				}
			}
			
			timeSinceLastFire += 1;
			AnalogStick shootStick = inputProcessor.shoot;
			direction = (float) (Math.toDegrees(Math.atan2(shootStick.getYHead() - shootStick.getYBase(), shootStick.getXHead() - shootStick.getXBase())));
			if(shootStick.isActive()) {
				if(timeSinceLastFire >= fireRate) {
					bullets.add(Bullet.obtainBullet(x, y, direction + 8, 0, dx, dy));
					bullets.add(Bullet.obtainBullet(x, y, direction, 0, dx, dy));
					bullets.add(Bullet.obtainBullet(x, y, direction - 8, 0, dx, dy));
					
					timeSinceLastFire = 0;
				}
			}
			
			dx *= Player.FRICTION;
	        dy *= Player.FRICTION;
	        
	        if(x - Player.radius + dx < 23)
	        	dx = -dx * 0.6f;
	        else if(x + Player.radius + dx > gs.getCurrentMap().getWidth() - 23)
	        	dx = -dx * 0.6f;
	        
	        if(y - Player.radius + dy < 23)
	        	dy = -dy * 0.6f;
	        else if(y + Player.radius + dy > gs.getCurrentMap().getHeight() - 23)
	        	dy = -dy * 0.6f;
	        
	        x += dx;
	        y += dy;
		}

		private void changeSpeed(float dx, float dy) {
			this.dx += dx;
			this.dy += dy;
		}

	}
	
	private class ControlInputProcessor extends InputAdapter {
		private AnalogStick shoot = new AnalogStick(), move = new AnalogStick();
		
		public ControlInputProcessor() {
			if(staticSticks) {
				move.setBasePos(Specular.prefs.getFloat("Move Stick Pos X"), Specular.prefs.getFloat("Move Stick Pos Y"));
				shoot.setBasePos(Specular.prefs.getFloat("Shoot Stick Pos X"), Specular.prefs.getFloat("Shoot Stick Pos Y"));
			} else {
				//Just a hack to stop it from rendering before the user touches the screen
				move.setBasePos(-3000, -3000);
			}
				
		}
		
		@Override
		public boolean keyUp(int keycode) {
			if(keycode == Keys.BACK)
				game.enterState(States.SETTINGSMENUSTATE);
			return super.keyUp(keycode);
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			float viewportx = (float) screenX / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
			float viewporty = (float) screenY / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
			
			// Sticks
			
			//If NOT tilt controls
	
					//If touching left half
					if(viewportx <= Specular.camera.viewportWidth / 2) {
						
						//If sticks are static set it to the right position
						if(!staticSticks)
							move.setBasePos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
			
						move.setHeadPos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
						move.setPointer(pointer);
						
					} else {//Touching right half
						
						//If sticks are static set it to the right position
						if(!staticSticks)
							shoot.setBasePos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
						
						
						shoot.setHeadPos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
						shoot.setPointer(pointer);
					}
				
				if(viewportx > Specular.camera.viewportWidth / 2) {
					if(!staticSticks)
						shoot.setBasePos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
					
					shoot.setHeadPos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
					shoot.setPointer(pointer);
				}
			
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {

				if(move.getPointer() == pointer) {
					move.setPointer(-1);
				} else if(shoot.getPointer() == pointer) {
					shoot.setPointer(-1);
				}
			
			
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			float viewportx = (float) screenX / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
			float viewporty = (float) screenY / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;

				if(viewportx <= Specular.camera.viewportWidth / 2)
					move.setBasePos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
				else
					shoot.setBasePos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
			 
				if(pointer == shoot.getPointer())
					shoot.setHeadPos(viewportx - Specular.camera.viewportWidth / 2,
						- (viewporty - Specular.camera.viewportHeight / 2));
				
				else if(pointer == move.getPointer())
					move.setHeadPos(viewportx - Specular.camera.viewportWidth / 2,
						- (viewporty - Specular.camera.viewportHeight / 2));
			
			return false;
		}
		
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
	public void render(float delta) {
		long currTime = System.nanoTime();
        unprocessed += (currTime - lastTickTime) / GameState.TICK_LENGTH;
        lastTickTime = currTime;
        while(unprocessed >= 1) {
        	unprocessed--;
        	update();
        }
        renderGame();
	}

	@Override
	public void hide() {
		super.hide();
		dispose();
		if(music != null) {
			music.stop();
			music.dispose();
		}
		music = null;
	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
	}
	
	
}
