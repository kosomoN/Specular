package com.tint.specular.states;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Player;
import com.tint.specular.input.AnalogStick;
import com.tint.specular.map.Map;
import com.tint.specular.utils.Util;

public class ControlSetupState extends State {

	private Texture circleTex, selectedTex;
	private DummyPlayer player = new DummyPlayer();
	private Array<Bullet> bullets = new Array<Bullet>();
	private GameState gs;
	private Map map;
	private double unprocessed;
	private long lastTickTime;
	private float sensitivity;
	private boolean tilt, staticSticks;
	public ControlInputProcessor inputProcessor;
	private Stage stage;
	private Button staticBtn;
	
	public ControlSetupState(Specular game) {
		super(game);
		
		circleTex = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Circle.png"));
		circleTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
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
		player.barrelTexture = Player.barrelTexture;
		
		//Input
		stage = new Stage(Specular.camera.viewportWidth, Specular.camera.viewportHeight, false, game.batch);
		stage.setCamera(Specular.camera);
		
		inputProcessor = new ControlInputProcessor();
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, inputProcessor));
		
		lastTickTime = System.nanoTime();
		
		
		sensitivity = Specular.prefs.getFloat("Sensitivity");
		
		tilt = Specular.prefs.getBoolean("Tilt");
		staticSticks = Specular.prefs.getBoolean("Static");
		
		selectedTex = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Selected.png"));
		TextureRegionDrawable knobTex = new TextureRegionDrawable(new TextureRegion(selectedTex));
		knobTex.setMinWidth(256);;
		//Slider ui
		SliderStyle sliderStyle = new SliderStyle(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Slider.png"))))
			, knobTex);
		
		// Slider
		final Slider sensitivitySlider = new Slider(0.2f, 2f, 0.01f, false, sliderStyle);
		sensitivitySlider.setSize(1280, 50);
		sensitivitySlider.setPosition(-sensitivitySlider.getWidth() / 2 + 250, -Specular.camera.viewportHeight / 2 + 83);
		sensitivitySlider.setValue(Specular.prefs.getFloat("Sensitivity"));
		sensitivitySlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sensitivity = sensitivitySlider.getValue();
			}
		});
		
		stage.addActor(sensitivitySlider);
		
		
		TextureRegion backButton = new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/highscore/Back.png")));
		TextureRegion backButtonDown = new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/highscore/Back Pressed.png")));
		Button backBtn = new Button(new TextureRegionDrawable(backButton), new TextureRegionDrawable(backButtonDown));
		
		backBtn.setPosition(-Specular.camera.viewportWidth / 2 + 47, -Specular.camera.viewportHeight / 2);
		
		backBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Specular.prefs.putFloat("Sensitivity", sensitivity);
				game.enterState(States.SETTINGSMENUSTATE);
			}
		});
		
		stage.addActor(backBtn);
		
		
		TextureRegion controlButtonsTex = new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Controls Checks.png")));
		
		staticBtn = new Button(new TextureRegionDrawable(controlButtonsTex));
		
		staticBtn.setPosition(-Specular.camera.viewportWidth / 2 - 50, Specular.camera.viewportHeight / 2 - 205);
		
		staticBtn.setChecked(staticSticks);
		
		staticBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Specular.prefs.putBoolean("Static", staticBtn.isChecked());
				staticSticks = staticBtn.isChecked();
			}
		});
		
		stage.addActor(staticBtn);
	}
	
	private void renderGame() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Specular.camera.position.set(player.x, player.y, 0);
		Specular.camera.update();
		
		game.batch.setProjectionMatrix(Specular.camera.combined);
		game.batch.begin();
		
		game.batch.setColor(1, 0, 0, 1);
		game.batch.draw(map.getParallax(), -1024 + player.x / 2, -1024 +  player.y / 2, 4096, 4096);
		game.batch.setColor(1, 1, 1, 1);
		
		map.render(game.batch);
		
		player.render(game.batch);
		
		for(Bullet b : bullets)
			b.render(game.batch);
		
		Specular.camera.position.set(0, 0, 0);
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		
		float width = Specular.camera.viewportWidth / 4 * sensitivity;
		float height = Specular.camera.viewportWidth / 4 * sensitivity;
		game.batch.draw(circleTex, inputProcessor.move.getXBase() - width / 2, inputProcessor.move.getYBase() - height / 2,
									width, height);
		
		// Drawing analogsticks
		inputProcessor.shoot.render(game.batch);
		inputProcessor.move.render(game.batch);
		
		game.batch.end();
		
		stage.act();
		stage.draw();
		if(staticBtn.isChecked()) {
			game.batch.begin();
			game.batch.draw(selectedTex, staticBtn.getX() + 47, staticBtn.getY() + 78);
			game.batch.end();
		}
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
					bullets.add(new Bullet(x, y, direction + 8, dx, dy, gs));
					bullets.add(new Bullet(x, y, direction, dx, dy, gs));
					bullets.add(new Bullet(x, y, direction - 8, dx, dy, gs));
					
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
				move.setBasePos(-1 / 4f * Specular.camera.viewportWidth, 0);
			} else {
				//Just a hack to stop it from rendering before the user touches the screen
				move.setBasePos(-3000, -3000);
			}
				
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			float viewportx = (float) screenX / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
			float viewporty = (float) screenY / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
			
			//Checking if touching boardshock button
			if(viewporty > Specular.camera.viewportHeight - 90 &&
					viewportx - Specular.camera.viewportWidth / 2 > -350 && viewportx - Specular.camera.viewportWidth / 2 < 350) {
				gs.boardshock();
				return false;
			}
			
			// Sticks
			
			//If NOT tilt controls
			if(!tilt) {
				
				//If touching left half
				if(viewportx <= Specular.camera.viewportWidth / 2) {
					
					//If sticks are static set it to the right position
					if(staticSticks)
						move.setBasePos(-1 / 4f * Specular.camera.viewportWidth, 0);
					else
						move.setBasePos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
		
					move.setHeadPos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
					move.setPointer(pointer);
					
				} else {//Touching right half
					
					//If sticks are static set it to the right position
					if(staticSticks)
						shoot.setBasePos(1 / 4f * Specular.camera.viewportWidth, 0);
					else
						shoot.setBasePos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
					
					
					shoot.setHeadPos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
					shoot.setPointer(pointer);
				}
			}
			if(viewportx > Specular.camera.viewportWidth / 2) {
				if(staticSticks)
					shoot.setBasePos(1 / 4f * Specular.camera.viewportWidth, 0);
				else
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
			
			if(pointer == shoot.getPointer())
			shoot.setHeadPos(viewportx - Specular.camera.viewportWidth / 2,
					- (viewporty - Specular.camera.viewportHeight / 2));
			
			else if(pointer == move.getPointer())
			move.setHeadPos(viewportx - Specular.camera.viewportWidth / 2,
					- (viewporty - Specular.camera.viewportHeight / 2));
		
			return false;
		}
		
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
		Specular.prefs.flush();
	}
}
