package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.game.GameState;
import com.tint.specular.ui.UpgradeList;
import com.tint.specular.upgrades.BurstUpgrade;
import com.tint.specular.upgrades.FirerateUpgrade;
import com.tint.specular.upgrades.LaserUpgrade;
import com.tint.specular.upgrades.MultiplierUpgrade;
import com.tint.specular.upgrades.PDSUpgrade;
import com.tint.specular.upgrades.RepulsorUpgrade;
import com.tint.specular.upgrades.RicochetUpgrade;
import com.tint.specular.upgrades.SlowdownUpgrade;
import com.tint.specular.upgrades.SwarmUpgrade;
import com.tint.specular.upgrades.Upgrade;

public class UpgradeState extends State {
	
	private Stage stage;
	private UpgradeList list;
	private Upgrade[] upgrades = new Upgrade[9];
	private static AtlasRegion[] upgradeLevels;

	private Label pointsLeftLabel;
	
	private static BitmapFont UPFont;
	private float upgradePoints;
	protected int currentlyPressing;
	protected float waitForDragDelay;
	
	public UpgradeState(Specular game) {
		super(game);
		TextureAtlas ta = ((GameState) game.getState(States.SINGLEPLAYER_GAMESTATE)).getTextureAtlas();
		
		upgrades[0] = new FirerateUpgrade(Specular.prefs.getFloat("Firerate Upgrade Grade"), 10, ta);
		upgrades[1] = new BurstUpgrade(Specular.prefs.getFloat("Burst Upgrade Grade"), 10, ta);
		upgrades[2] = new LaserUpgrade(Specular.prefs.getFloat("Beam Upgrade Grade"), 10, ta);
		upgrades[3] = new MultiplierUpgrade(Specular.prefs.getFloat("Multiplier Upgrade Grade"), 10, ta);
		upgrades[4] = new PDSUpgrade(Specular.prefs.getFloat("PDS Upgrade Grade"), 10, ta);
		upgrades[5] = new SwarmUpgrade(Specular.prefs.getFloat("Swarm Upgrade Grade"), 10, ta);
		upgrades[6] = new RepulsorUpgrade(Specular.prefs.getFloat("Repulsor Upgrade Grade"), 10, ta);
		upgrades[7] = new RicochetUpgrade(Specular.prefs.getFloat("Ricochet Upgrade Grade"), 10, ta);
		upgrades[8] = new SlowdownUpgrade(Specular.prefs.getFloat("Slowdown Upgrade Grade"), 10, ta);
		
		UpgradeList.init();
		
		// Initializing font
		FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Battlev2l.ttf"));
		FreeTypeFontParameter ftfp = new FreeTypeFontParameter();
		ftfp.size = 40;
		ftfp.characters = GameState.FONT_CHARACTERS;
		UPFont = fontGen.generateFont(ftfp);
		UPFont.setColor(Color.RED);
		
		fontGen.dispose();
		upgradeLevels = new AtlasRegion[6];
		upgradeLevels[0] = ta.findRegion("game1/level 1");
		upgradeLevels[1] = ta.findRegion("game1/level 2");
		upgradeLevels[2] = ta.findRegion("game1/level 3");
		upgradeLevels[3] = ta.findRegion("game1/level 4");
		upgradeLevels[4] = ta.findRegion("game1/level 5");
		upgradeLevels[5] = ta.findRegion("game1/level inf");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//timer += delta;
		if(currentlyPressing != -1) {
			if(waitForDragDelay > 0.1f && upgrades[currentlyPressing].getCost() <= upgradePoints) {
				if(upgrades[currentlyPressing].upgrade()) {
					upgradePoints -= upgrades[currentlyPressing].getCost();
					upgrades[currentlyPressing].setCost((float) Math.pow(2, upgrades[currentlyPressing].getGrade() / 5));
					pointsLeftLabel.setText("points available " + (int) Math.floor(upgradePoints));
				}
		
				list.getProgressBars()[currentlyPressing].setValue(upgrades[currentlyPressing].getGrade());
			} else {
				waitForDragDelay += delta;
				
			}
		}
		
		stage.act();
		stage.draw();
		((MainmenuState) game.getState(Specular.States.MAINMENUSTATE)).startMusic();
	}
	
	@Override
	public void show() {
		super.show();
		resetUpgrades();
		createUpgradeList();
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
			@Override
			public boolean keyUp(int keycode) {
				if(keycode == Keys.BACK || keycode == Keys.ESCAPE) {
					saveUpgrades();
					game.enterState(States.MAINMENUSTATE);
				}
				return super.keyUp(keycode);
			}
		}));
		
		currentlyPressing = -1;
	}
	
	@Override
	public void hide() {
		super.hide();
		saveUpgrades();
	}

	private void createUpgradeList() {
		stage = new Stage(new ExtendViewport(1920, 1080), game.batch);
		
		Image title = new Image(new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Upgrades.png")));
		title.setPosition((Specular.camera.viewportWidth - title.getWidth()) / 2, Specular.camera.viewportHeight - title.getHeight());
		stage.addActor(title);
		
		list = new UpgradeList(getUpgrades());
		list.addListener(new InputListener() {

			private float downX, downY;
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				waitForDragDelay = 0;
				currentlyPressing = upgrades.length - 1 - (int) Math.floor(y / UpgradeList.rowHeight());
				downX = x;
				downY = y;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				currentlyPressing = -1;
				super.touchUp(event, x, y, pointer, button);
			}

			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer) {
				if(Math.abs(downX - x) > 40 || Math.abs(downY - y) > 40)
					currentlyPressing = -1;
					
				super.touchDragged(event, x, y, pointer);
			}
			
			
		});
		
		
		ScrollPane sp = new ScrollPane(list);
		sp.setSize(Specular.camera.viewportWidth - 200, Specular.camera.viewportHeight * 0.6f);
		sp.setPosition(100, Specular.camera.viewportHeight * 0.2f);
		stage.addActor(sp);
		
		TextureRegionDrawable trd = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/highscore/Back.png"))));
		TextureRegionDrawable trdPressed = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/highscore/Back Pressed.png"))));
		ButtonStyle style = new ButtonStyle(trd, trdPressed, trd);
		Button backBtn = new Button(style);
		backBtn.setPosition(50, 0);
		backBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				saveUpgrades();
				game.enterState(States.MAINMENUSTATE);
			}
		});
		stage.addActor(backBtn);
		
		LabelStyle lStyle = new LabelStyle(UPFont, UPFont.getColor());
		
		pointsLeftLabel = new Label("Points left " + (int) Math.floor(upgradePoints), lStyle);
		pointsLeftLabel.setPosition(Specular.camera.viewportWidth - UPFont.getBounds(pointsLeftLabel.getText()).width - 200, 85);
		stage.addActor(pointsLeftLabel);
		
	}

	private void saveUpgrades() {
		Specular.prefs.putFloat("Firerate Upgrade Grade", upgrades[0].getGrade());
		Specular.prefs.putFloat("Burst Upgrade Grade", upgrades[1].getGrade());
		Specular.prefs.putFloat("Beam Upgrade Grade", upgrades[2].getGrade());
		Specular.prefs.putFloat("Multiplier Upgrade Grade", upgrades[3].getGrade());
		Specular.prefs.putFloat("PDS Upgrade Grade", upgrades[4].getGrade());
		Specular.prefs.putFloat("Swarm Upgrade Grade", upgrades[5].getGrade());
		Specular.prefs.putFloat("Repulsor Upgrade Grade", upgrades[6].getGrade());
		Specular.prefs.putFloat("Ricochet Upgrade Grade", upgrades[7].getGrade());
		Specular.prefs.putFloat("Slowdown Upgrade Grade", upgrades[8].getGrade());
		Specular.prefs.putFloat("Upgrade Points", upgradePoints);
		
		Specular.prefs.flush();
	}
	
	public void resetUpgrades() {
		upgrades[0].setGrade(Specular.prefs.getFloat("Firerate Upgrade Grade"));
		upgrades[1].setGrade(Specular.prefs.getFloat("Burst Upgrade Grade"));
		upgrades[2].setGrade(Specular.prefs.getFloat("Beam Upgrade Grade"));
		upgrades[3].setGrade(Specular.prefs.getFloat("Multiplier Upgrade Grade"));
		upgrades[4].setGrade(Specular.prefs.getFloat("PDS Upgrade Grade"));
		upgrades[5].setGrade(Specular.prefs.getFloat("Swarm Upgrade Grade"));
		upgrades[6].setGrade(Specular.prefs.getFloat("Repulsor Upgrade Grade"));
		upgrades[7].setGrade(Specular.prefs.getFloat("Ricochet Upgrade Grade"));
		upgrades[8].setGrade(Specular.prefs.getFloat("Slowdown Upgrade Grade"));
		
		upgradePoints = Specular.prefs.getFloat("Upgrade Points");
	}
	
	public static AtlasRegion getUpgradeLevelTexture(float grade) {
		if(grade > 0) {
			if(grade < 2) {
				return upgradeLevels[0];
			} else if(grade < 3) {
				return upgradeLevels[1];
			} else if(grade < 5) {
				return upgradeLevels[2];
			} else if(grade < 10) {
				return upgradeLevels[3];
			} else if(grade == 10) {
				return upgradeLevels[4]; // MAX
			} else if(grade > 10) {
				return upgradeLevels[5]; // Infinity if we someday want to enable
			}
		}
		
		return null;
	}
	
	public Upgrade[] getUpgrades() {
		return upgrades;
	}

	public float getUpgradePoints() {
		return upgradePoints;
	}
}
