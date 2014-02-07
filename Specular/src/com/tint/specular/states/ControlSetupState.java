package com.tint.specular.states;

import static com.tint.specular.input.GameInputProcessor.STATIC_STICKS;
import static com.tint.specular.input.GameInputProcessor.STATIC_TILT;
import static com.tint.specular.input.GameInputProcessor.TILT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.input.AnalogStick;
import com.tint.specular.map.Map;
import com.tint.specular.utils.Util;

public class ControlSetupState extends State {

	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private DummyPlayer player = new DummyPlayer();
	private GameState gs;
	private Map map;
	private double unprocessed;
	private long lastTickTime;
	private float sensitivity;
	private int controls;
	public ControlInputProcessor inputProcessor;
	private Stage stage;
	
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
		
		controls = Specular.prefs.getInteger("Controls");

		//Slider ui
		SliderStyle sliderStyle = new SliderStyle(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Slider Background.png"))))
			, new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Slider Knob.png")))));
		
		
		// Slider
		final Slider sensitivitySlider = new Slider(0.2f, 2f, 0.1f, false, sliderStyle);
		sensitivitySlider.setSize(1000, 50);
		sensitivitySlider.setPosition(-sensitivitySlider.getWidth() / 2, -Specular.camera.viewportHeight / 2 + 20);
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
	}
	
	private void renderGame() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Specular.camera.position.set(player.x, player.y, 0);
		Specular.camera.update();
		
		game.batch.setProjectionMatrix(Specular.camera.combined);
		game.batch.begin();
		
		game.batch.draw(map.getParallax(), -1024 + player.x / 2, -1024 +  player.y / 2, 4096, 4096);
		
		map.render(game.batch);
		
		player.render(game.batch);
		
		Specular.camera.position.set(0, 0, 0);
		Specular.camera.update();
		game.batch.setProjectionMatrix(Specular.camera.combined);
		
		// Drawing analogsticks
		inputProcessor.shoot.render(game.batch);
		inputProcessor.move.render(game.batch);
			
		game.batch.end();
		
		shapeRenderer.setProjectionMatrix(Specular.camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.circle(inputProcessor.move.getXBase(), inputProcessor.move.getYBase(), Specular.camera.viewportWidth / 8 * sensitivity);
		shapeRenderer.end();
		
		stage.act();
		stage.draw();
	}

	private void update() {
		player.update();
	}


	private class DummyPlayer {
		public float x, y, dx, dy;
		private Animation anim;
		private float animFrameTime;
		private float direction;
		private Texture barrelTexture;
		
		private void render(SpriteBatch batch) {
			animFrameTime += Gdx.graphics.getDeltaTime();
			TextureRegion baseAnimFrame = anim.getKeyFrame(animFrameTime, true);
			Util.drawCentered(batch, barrelTexture, x, y, direction);
			batch.draw(baseAnimFrame, x - baseAnimFrame.getRegionWidth() / 2, y - baseAnimFrame.getRegionHeight() / 2);
		}

		private void update() {
			if(controls == 0) {
				changeSpeed(Gdx.input.getAccelerometerX() * 0.1f * 0.6f, Gdx.input.getAccelerometerY() * 0.1f * 0.6f);
			} else if(controls == 2 || controls == 3){
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
			if(controls == STATIC_TILT || controls == STATIC_STICKS) {
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
			if(controls != TILT && controls != STATIC_TILT) {
				
				//If touching left half
				if(viewportx <= Specular.camera.viewportWidth / 2) {
					
					//If sticks are static set it to the right position
					if(controls == STATIC_STICKS)
						move.setBasePos(-1 / 4f * Specular.camera.viewportWidth, 0);
					else
						move.setBasePos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
		
					move.setHeadPos(viewportx - Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
					move.setPointer(pointer);
					
				} else {//Touching right half
					
					//If sticks are static set it to the right position
					if(controls == STATIC_STICKS)
						shoot.setBasePos(1 / 4f * Specular.camera.viewportWidth, 0);
					else
						shoot.setBasePos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
					
					
					shoot.setHeadPos(viewportx- Specular.camera.viewportWidth / 2, - (viewporty - Specular.camera.viewportHeight / 2));
					shoot.setPointer(pointer);
				}
			}
			if(viewportx > Specular.camera.viewportWidth / 2) {
				if(controls == STATIC_STICKS || controls == STATIC_TILT)
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
			if(controls != 0) {
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
			if(controls != 0) {
				float viewportx = (float) screenX / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
				float viewporty = (float) screenY / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
				
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
}
