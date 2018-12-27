package com.tint.specular.states;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
	private boolean soundEffects;
	private ControlInputProcessor inputProcessor;
	private Stage stage;
	private Button staticBtn;
	private WidgetGroup hideableUI;
	private Button stickPositionBtn;
	private Sound btnSound = Gdx.audio.newSound(Gdx.files.internal("audio/fx/ButtonPress.ogg"));
	
	public ControlSetupState(Specular game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();
		//Map
		gs = (GameState) game.getState(States.SINGLEPLAYER_GAMESTATE);
		map = gs.getMapHandler().getMap("Map");
		
		//Player
		player.x = map.getWidth() / 2f;
		player.y = map.getHeight() / 2f;
		player.anim = Player.anim;
		player.barrelTexture = Player.barrelTexture[0];
		
		//Input
		stage = new Stage(new ExtendViewport(1920, 1080), game.batch);
		
		lastTickTime = System.nanoTime();
		
		sensitivity = Specular.prefs.getFloat("Sensitivity");
		
		tilt = Specular.prefs.getBoolean("Tilt");
		staticSticks = Specular.prefs.getBoolean("Static");
		
		soundEffects = !Specular.prefs.getBoolean("SoundsMuted");
		
		inputProcessor = new ControlInputProcessor();
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, inputProcessor));
		
		circleTex = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Circle.png"));
		circleTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		selectedTex = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Selected.png"));
		TextureRegionDrawable knobTex = new TextureRegionDrawable(new TextureRegion(selectedTex));
		knobTex.setMinWidth(256);
		//Slider ui
		SliderStyle sliderStyle = new SliderStyle(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Slider.png"))))
			, knobTex);
		
		// Slider
		final Slider sensitivitySlider = new Slider(0.2f, 2f, 0.01f, false, sliderStyle);
		sensitivitySlider.setSize(1280, 200);
		sensitivitySlider.setPosition((stage.getWidth() - sensitivitySlider.getWidth()) / 2 + 250, (stage.getHeight() - Specular.camera.viewportHeight) / 2 + 8);
		sensitivitySlider.setValue(Specular.prefs.getFloat("Sensitivity"));
		
		sensitivitySlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sensitivity = sensitivitySlider.getValue();
			}
		});
		
		TextureRegion backButton = new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/highscore/Back.png")));
		TextureRegion backButtonDown = new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/highscore/Back Pressed.png")));
		Button backBtn = new Button(new TextureRegionDrawable(backButton), new TextureRegionDrawable(backButtonDown));
		
		backBtn.setPosition((stage.getWidth() - Specular.camera.viewportWidth) / 2 + 47, (stage.getHeight() - Specular.camera.viewportHeight) / 2);
		
		backBtn.addListener(new ClickListener() {
			boolean entered;
			boolean touched;
			
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				entered = true;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				entered = false;
				touched = false;
			}
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(soundEffects)
					btnSound.play();
				touched = true;
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if(entered)
					saveAndExit();
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				if(entered && !touched && soundEffects) {
					btnSound.play();
				}
				touched = entered;
			}
		});
		
		TextureRegion testBtnTex = new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Test.png")));
		TextureRegion testBtnTexPressed = new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Test Pressed.png")));
		final Button testBtn = new Button(new TextureRegionDrawable(testBtnTex), new TextureRegionDrawable(testBtnTexPressed));
		
		testBtn.setPosition((stage.getWidth() - testBtn.getWidth()) / 2, (stage.getHeight() + Specular.camera.viewportHeight) / 2 - testBtnTex.getRegionHeight());
		
		testBtn.addListener(new ClickListener() {
			boolean entered;
			boolean touched;
			
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				entered = true;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				entered = false;
				touched = false;
			}
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(soundEffects)
					btnSound.play();
				touched = true;
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if(entered) {
					hideableUI.setVisible(!hideableUI.isVisible());
					stickPositionBtn.setVisible(hideableUI.isVisible() && staticSticks);
				}
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				if(entered && !touched && soundEffects) {
					btnSound.play();
				}
				touched = entered;
			}
		});
		
		stage.addActor(testBtn);
		
		TextureRegion stickPositionTex = new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Positions.png")));
		TextureRegion stickPositionTexDown = new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Positions Pressed.png")));
		stickPositionBtn = new Button(new TextureRegionDrawable(stickPositionTex), new TextureRegionDrawable(stickPositionTexDown));
		
		stickPositionBtn.setPosition((stage.getWidth() - stickPositionBtn.getWidth()) / 2, testBtn.getY() - testBtn.getHeight());//(stage.getHeight() + Specular.camera.viewportHeight) / 2 - 250);
		
		stickPositionBtn.setVisible(Specular.prefs.getBoolean("Static"));
		
		stickPositionBtn.addListener(new ClickListener() {
			boolean entered;
			boolean touched;
			
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				entered = true;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				entered = false;
				touched = false;
			}
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(soundEffects)
					btnSound.play();
				touched = true;
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				hideableUI.setVisible(!stickPositionBtn.isChecked());
				testBtn.setVisible(!stickPositionBtn.isChecked());
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				if(entered && !touched && soundEffects) {
					btnSound.play();
				}
				touched = entered;
			}
		});
		
		stage.addActor(stickPositionBtn);
		
		TextureRegion controlButtonsTex = new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Controls Checks.png")));
		
		staticBtn = new Button(new TextureRegionDrawable(controlButtonsTex));
		
		staticBtn.setPosition((stage.getWidth() - Specular.camera.viewportWidth) / 2 - 50, (stage.getHeight() + Specular.camera.viewportHeight) / 2 - 205);
		staticBtn.setChecked(Specular.prefs.getBoolean("Static"));
		staticBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Specular.prefs.putBoolean("Static", staticBtn.isChecked());
				staticSticks = staticBtn.isChecked();
				inputProcessor.move.setStatic(staticSticks);
				inputProcessor.shoot.setStatic(staticSticks);
				stickPositionBtn.setVisible(staticBtn.isChecked());
				if(staticBtn.isChecked()) {
				    float movePosX = Specular.prefs.getFloat("Move Stick Pos X");
				    float movePosY = Specular.prefs.getFloat("Move Stick Pos Y");
                    float shootPosX = Specular.prefs.getFloat("Shoot Stick Pos X");
                    float shootPosY = Specular.prefs.getFloat("Shoot Stick Pos Y");
					inputProcessor.move.setBasePos(movePosX, movePosY);
					inputProcessor.shoot.setBasePos(shootPosX, shootPosY);
				} else {
					Specular.prefs.putFloat("Move Stick Pos X", inputProcessor.move.getXBase());
					Specular.prefs.putFloat("Move Stick Pos Y", inputProcessor.move.getYBase());
					Specular.prefs.putFloat("Shoot Stick Pos X", inputProcessor.shoot.getXBase());
					Specular.prefs.putFloat("Shoot Stick Pos Y", inputProcessor.shoot.getYBase());
					Specular.prefs.flush();
				}
			}
		});
		
		hideableUI = new WidgetGroup();
		hideableUI.addActor(sensitivitySlider);
		hideableUI.addActor(staticBtn);
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
		if(staticBtn.isChecked() && hideableUI.isVisible()) {
			game.batch.begin();
			game.batch.draw(selectedTex, staticBtn.getX() + 47, staticBtn.getY() + 78);
			game.batch.end();
		}
		
		if(stickPositionBtn.isChecked()) {
		    float stickBaseW = AnalogStick.base.getRegionWidth();
		    float stickBaseH = AnalogStick.base.getRegionHeight();
		    float moveStickX = stage.getWidth() / 2 + inputProcessor.move.getXBase() - stickBaseW / 2f;
		    float moveStickY = stage.getHeight() / 2 + inputProcessor.move.getYBase() - stickBaseH / 2f;
            float shootStickX = stage.getWidth() / 2 + inputProcessor.shoot.getXBase() - stickBaseW / 2f;
            float shootStickY = stage.getHeight() / 2 + inputProcessor.shoot.getYBase() - stickBaseH / 2f;
			game.batch.begin();
			game.batch.draw(AnalogStick.base, moveStickX, moveStickY);
			game.batch.draw(AnalogStick.base, shootStickX, shootStickY);
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
	
	private void saveAndExit() {
		Specular.prefs.putFloat("Sensitivity", sensitivity);
		if(staticSticks) {
			Specular.prefs.putFloat("Move Stick Pos X", inputProcessor.move.getXBase());
			Specular.prefs.putFloat("Move Stick Pos Y", inputProcessor.move.getYBase());
			Specular.prefs.putFloat("Shoot Stick Pos X", inputProcessor.shoot.getXBase());
			Specular.prefs.putFloat("Shoot Stick Pos Y", inputProcessor.shoot.getYBase());
			Specular.prefs.flush();
		}

		game.enterState(States.SETTINGSMENUSTATE);
	}

	private class DummyPlayer {
		public float x, y, dx, dy;
		private Animation<TextureRegion> anim;
		private float animFrameTime;
		private float direction;
		private AtlasRegion barrelTexture;
		private int timeSinceLastFire;
		private int fireRate = 10;
		
		private void render(SpriteBatch batch) {
			animFrameTime += Gdx.graphics.getDeltaTime();
			TextureRegion baseAnimFrame = anim.getKeyFrame(animFrameTime, true);
			Util.drawCentered(batch, barrelTexture, x, y, direction);
			batch.draw(baseAnimFrame, x - baseAnimFrame.getRegionWidth() / 2f, y - baseAnimFrame.getRegionHeight() / 2f);
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
					float speed = (distBaseToHead >= maxSpeedAreaSquared ? 1 : distBaseToHead / maxSpeedAreaSquared);
					changeSpeed(
							(float) Math.cos(angle) * Player.MAX_DELTA_SPEED * speed,
							(float) Math.sin(angle) * Player.MAX_DELTA_SPEED * speed
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
		private AnalogStick shoot = new AnalogStick(staticSticks), move = new AnalogStick(staticSticks);
		
		public ControlInputProcessor() {
			if(staticSticks) {
				move.setBasePos(Specular.prefs.getFloat("Move Stick Pos X"), Specular.prefs.getFloat("Move Stick Pos Y"));
				shoot.setBasePos(Specular.prefs.getFloat("Shoot Stick Pos X"), Specular.prefs.getFloat("Shoot Stick Pos Y"));
			} else {
				//Just a hack to stop it from rendering before the user touches the screen
				move.setPointer(-1);
			}
				
		}
		
		@Override
		public boolean keyUp(int keycode) {
			if(keycode == Keys.BACK)
				saveAndExit();
			return super.keyUp(keycode);
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			float viewportx = (float) screenX / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
			float viewporty = (float) screenY / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
			
			// Sticks
			
			//If NOT tilt controls
			if(stickPositionBtn.isChecked()) {
				if(viewportx <= Specular.camera.viewportWidth / 2)
					move.setBasePos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
				else
					shoot.setBasePos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
			} else {
				if(!tilt) {
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
				}
				if(viewportx > Specular.camera.viewportWidth / 2) {
					if(!staticSticks)
						shoot.setBasePos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
					
					shoot.setHeadPos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
					shoot.setPointer(pointer);
				}
			}
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			if(!stickPositionBtn.isChecked()) {
				if(move.getPointer() == pointer) {
					move.setPointer(-1);
				} else if(shoot.getPointer() == pointer) {
					shoot.setPointer(-1);
				}
			}
			
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			float viewportx = (float) screenX / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
			float viewporty = (float) screenY / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
			if(stickPositionBtn.isChecked()) {
				if(viewportx <= Specular.camera.viewportWidth / 2)
					move.setBasePos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
				else
					shoot.setBasePos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
			} else {
				if(pointer == shoot.getPointer())
					shoot.setHeadPos(viewportx - Specular.camera.viewportWidth / 2,
						- (viewporty - Specular.camera.viewportHeight / 2));
				
				else if(pointer == move.getPointer())
					move.setHeadPos(viewportx - Specular.camera.viewportWidth / 2,
						- (viewporty - Specular.camera.viewportHeight / 2));
			}
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
		dispose();
		Specular.prefs.flush();
	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
		circleTex.dispose();
		selectedTex.dispose();
	}
	
	
}
