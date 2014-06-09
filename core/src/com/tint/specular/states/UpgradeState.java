package com.tint.specular.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.ui.UpgradeList;
import com.tint.specular.upgrades.BeamUpgrade;
import com.tint.specular.upgrades.BoardshockUpgrade;
import com.tint.specular.upgrades.BurstUpgrade;
import com.tint.specular.upgrades.FirerateUpgrade;
import com.tint.specular.upgrades.LifeUpgrade;
import com.tint.specular.upgrades.MultiplierUpgrade;
import com.tint.specular.upgrades.PDSUpgrade;
import com.tint.specular.upgrades.RepulsorUpgrade;
import com.tint.specular.upgrades.SlowdownUpgrade;
import com.tint.specular.upgrades.SwarmUpgrade;
import com.tint.specular.upgrades.Upgrade;
import com.tint.specular.utils.Util;

public class UpgradeState extends State {
	
	private Stage stage;
	private UpgradeList list;
	private Upgrade[] upgrades = new Upgrade[10];
	private Preferences prefs;
	private Button confirmBtn, resetBtn;
	private com.tint.specular.ui.Button okBtn, cancelBtn;
	private Texture promtTex, darkenTex;
	private float upgradePoints;
	private boolean promtVisible;
	
	public UpgradeState(Specular game) {
		super(game);
		promtTex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Promt.png"));
		darkenTex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Dark.png"));
		okBtn = new com.tint.specular.ui.Button(640, 100, 192, 100, game.batch, new Texture(Gdx.files.internal("graphics/menu/upgrademenu/OkButton.png")),
				new Texture(Gdx.files.internal("graphics/menu/upgrademenu/OkButton.png")));
		cancelBtn = new com.tint.specular.ui.Button(1200, 100, 192, 100, game.batch, new Texture(Gdx.files.internal("graphics/menu/upgrademenu/CancelButton.png")),
				new Texture(Gdx.files.internal("graphics/menu/upgrademenu/CancelButton.png")));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		
		// Promt window
		if(promtVisible) {
			game.batch.begin();
			game.batch.draw(darkenTex, 0, 0, Specular.camera.viewportWidth, Specular.camera.viewportHeight);
			Util.drawCentered(game.batch, promtTex, Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight / 2, 0);
			okBtn.render();
			cancelBtn.render();
			game.batch.end();
		}
	}
	
	@Override
	public void show() {
		super.show();
		prefs = Specular.prefs;
		upgradePoints = (float) Math.floor(prefs.getFloat("Upgrade Points"));
		resetUpgrades();
		createUpgradeList();
		
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
			@Override
			public boolean keyUp(int keycode) {
				if(keycode == Keys.BACK || keycode == Keys.ESCAPE) {
					if(confirmBtn.isVisible()) {
						// Promt user
						Gdx.input.setInputProcessor(new PromtInputAdapter());
						promtVisible = true;
					} else {
						game.enterState(States.PROFILE_STATE);
					}
				}
				return super.keyUp(keycode);
			}
		}));
	}
	
	private void createUpgradeList() {
		stage = new Stage(new ExtendViewport(1920, 1080), game.batch);
		list = new UpgradeList(getUpgrades());
		list.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				int upgradeNum = (int) Math.floor(y / UpgradeList.rowHeight());
				if(upgrades[9 - upgradeNum].getCost() <= (int) Math.floor(getUpgradePoints())) {
					upgrades[9 - upgradeNum].upgrade();
					upgradePoints -= upgrades[9 - upgradeNum].getCost();
					confirmBtn.setVisible(true);
					resetBtn.setVisible(true);
				}
			}
		});
		
		ScrollPane sp = new ScrollPane(list);
		sp.setSize(Specular.camera.viewportWidth * (1240 / 1920f), Specular.camera.viewportHeight * 0.6f);
		sp.setPosition(100, Specular.camera.viewportHeight * 0.2f);
		
		stage.addActor(sp);
		
		TextureRegionDrawable trd = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/highscore/Back.png"))));
		TextureRegionDrawable trdPressed = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/highscore/Back Pressed.png"))));
		ButtonStyle style = new ButtonStyle(trd, trdPressed, trd);
		Button backBtn = new Button(style);
		backBtn.setPosition(47, 0);
		backBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if(confirmBtn.isVisible()) {
					// Promt user
					Gdx.input.setInputProcessor(new PromtInputAdapter());
					promtVisible = true;
				} else {
					game.enterState(States.PROFILE_STATE);
				}
			}
		});
		stage.addActor(backBtn);
		
		TextureRegionDrawable trd2 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/upgrademenu/ConfirmButton.png"))));
		style = new ButtonStyle(trd2, trd2, trd2);
		confirmBtn = new Button(style);
		confirmBtn.setVisible(false);
		confirmBtn.setPosition(700, 100);
		confirmBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				saveUpgrades();
				confirmBtn.setVisible(false);
				resetBtn.setVisible(false);
			}
		});
		stage.addActor(confirmBtn);
		
		TextureRegionDrawable trd3 = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("graphics/menu/upgrademenu/ResetButton.png"))));
		style = new ButtonStyle(trd3, trd3, trd3);
		resetBtn = new Button(style);
		resetBtn.setVisible(false);
		resetBtn.setPosition(1200, 100);
		resetBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				resetUpgrades();
				confirmBtn.setVisible(false);
				resetBtn.setVisible(false);
			}
		});
		stage.addActor(resetBtn);
		
		Image title = new Image(new Texture(Gdx.files.internal("graphics/menu/upgrademenu/Upgrades Title.png")));
		title.setPosition(500, Specular.camera.viewportHeight - 150);
		stage.addActor(title);
		
		Image info = new Image(new Texture(Gdx.files.internal("graphics/menu/upgrademenu/UpgradeInfo.png")));
		info.setPosition(Specular.camera.viewportWidth - 550, 200);
		stage.addActor(info);
	}

	public void saveUpgrades() {
		prefs.putInteger("Life Upgrade Grade", upgrades[0].getGrade());
		prefs.putInteger("Firerate Upgrade Grade", upgrades[1].getGrade());
		prefs.putInteger("Burst Upgrade Grade", upgrades[2].getGrade());
		prefs.putInteger("Beam Upgrade Grade", upgrades[3].getGrade());
		prefs.putInteger("Multiplier Upgrade Grade", upgrades[4].getGrade());
		prefs.putInteger("PDS Upgrade Grade", upgrades[5].getGrade());
		prefs.putInteger("Swarm Upgrade Grade", upgrades[6].getGrade());
		prefs.putInteger("Repulsor Upgrade Grade", upgrades[7].getGrade());
		prefs.putInteger("Slowdown Upgrade Grade", upgrades[8].getGrade());
		prefs.putInteger("Boardshock Upgrade Grade", upgrades[9].getGrade());
		prefs.putFloat("Upgrade Points", upgradePoints);
		
		prefs.flush();
	}
	
	public void resetUpgrades() {
		upgrades[0] = new LifeUpgrade(prefs.getInteger("Life Upgrade Grade"), 10);
		upgrades[1] = new FirerateUpgrade(prefs.getInteger("Firerate Upgrade Grade"), 10);
		upgrades[2] = new BurstUpgrade(prefs.getInteger("Burst Upgrade Grade"), 10);
		upgrades[3] = new BeamUpgrade(prefs.getInteger("Beam Upgrade Grade"), 10);
		upgrades[4] = new MultiplierUpgrade(prefs.getInteger("Multiplier Upgrade Grade"), 10);
		upgrades[5] = new PDSUpgrade(prefs.getInteger("PDS Upgrade Grade"), 10);
		upgrades[6] = new SwarmUpgrade(prefs.getInteger("Swarm Upgrade Grade"), 10);
		upgrades[7] = new RepulsorUpgrade(prefs.getInteger("Repulsor Upgrade Grade"), 10);
		upgrades[8] = new SlowdownUpgrade(prefs.getInteger("Slowdown Upgrade Grade"), 10);
		upgrades[9] = new BoardshockUpgrade(prefs.getInteger("Boardshock Upgrade Grade"), 10);
		
		upgradePoints = prefs.getFloat("Upgrade Points");
	}
	
	public Upgrade[] getUpgrades() {
		return upgrades;
	}

	public float getUpgradePoints() {
		return upgradePoints;
	}
	
	private class PromtInputAdapter extends InputAdapter {

		@Override
		public boolean keyUp(int keycode) {
			return super.keyUp(keycode);
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			// Translate screen coordinates to viewport coordinates
			float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
			float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
			
			if(okBtn.isOver(touchpointx, touchpointy, true)) {
				okBtn.touchOver(touchpointx, touchpointy);
			} else if(cancelBtn.isOver(touchpointx, touchpointy, true)) {
				cancelBtn.touchOver(touchpointx, touchpointy);
			}
			return super.touchDown(screenX, screenY, pointer, button);
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			// Translate screen coordinates to viewport coordinates
			float touchpointx = (float) Gdx.input.getX() / Gdx.graphics.getWidth() * Specular.camera.viewportWidth;
			float touchpointy = (float) Gdx.input.getY() / Gdx.graphics.getHeight() * Specular.camera.viewportHeight;
			
			if(okBtn.isOver(touchpointx, touchpointy, true)) {
				okBtn.touchUp();
				promtVisible = false;
				game.enterState(States.PROFILE_STATE);
			} else if(cancelBtn.isOver(touchpointx, touchpointy, true)) {
				cancelBtn.touchUp();
				Gdx.input.setInputProcessor(stage);
				promtVisible = false;
			}
			return super.touchUp(screenX, screenY, pointer, button);
		}
	}
}
