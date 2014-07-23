package com.tint.specular.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.Specular;
import com.tint.specular.Specular.States;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyWanderer;
import com.tint.specular.game.powerups.BulletBurst;
import com.tint.specular.game.powerups.FireRateBoost;
import com.tint.specular.game.powerups.PowerUp;
import com.tint.specular.utils.Util;

public class Tutorial {

	public enum TutorialEvent {
		PLAYER_MOVED, PLAYER_SHOT, POWER_UPS_SHOWN
	}

	private GameState gs;
	private States returnState;
	private Array<TutorialWave> tutorialWaves = new Array<TutorialWave>();
	private TutorialWave currentWave;
	private BitmapFont tutorialFont;
	private static AtlasRegion redPixel;
	private int currentWaveIndex;
	private int bulletsFiredBefore;
	private boolean enemiesSpawned;

	public Tutorial(final GameState gs, FreeTypeFontGenerator fontGen) {
		this.gs = gs;
		// Initializing font
		FreeTypeFontParameter ftfp = new FreeTypeFontParameter();
		ftfp.size = 80; // MAX SIZE
		ftfp.characters = GameState.FONT_CHARACTERS;
		tutorialFont = fontGen.generateFont(ftfp);
		tutorialFont.setColor(Color.RED);
	}
	
	public void reset(States returnState) {
		this.returnState = returnState;
		tutorialWaves.clear();
		currentWaveIndex = 0;
		enemiesSpawned = false;
		bulletsFiredBefore = Bullet.bulletsFired;
		
		// Initializing tutorial steps
		tutorialWaves.add(new TutorialWave(TutorialEvent.PLAYER_MOVED) {

			@Override
			public void start() {
				super.start();
				Player.distTraveledSqrd = 0;
			}
			
		});
		tutorialWaves.add(new TutorialWave(TutorialEvent.PLAYER_SHOT) {

			@Override
			public void start() {
				super.start();
				Bullet.bulletsFired = 0;
			}

			@Override
			public void complete() {
				super.complete();
				Bullet.bulletsFired = bulletsFiredBefore;
			}
			
		});
		tutorialWaves.add(new TutorialWave(TutorialEvent.POWER_UPS_SHOWN) {
			@Override
			public void start() {
				super.start();
				float x1 = gs.getPlayer().getX(), x2 = x1;
				float y = gs.getPlayer().getY();

				if(x1 < 500) {
					x1 += 400;
					x2 += 800;
				} else if(x1 > gs.getCurrentMap().getWidth() - 500) {
					x1 -= 400;
					x2 -= 800;
				} else {
					x1 += 400;
					x2 -= 400;
				}
					
				if(y > gs.getCurrentMap().getHeight() - 500) {
					y -= 400;
				} else {
					y += 400;
				}
				
				
				gs.addEntity(new FireRateBoost(x1, y, gs));
				gs.addEntity(new BulletBurst(x2, y, gs));
			}

			@Override
			public void complete() {
				super.complete();
				for(PowerUp pu : gs.getPowerUps()) {
					pu.removeEffect(gs.getPlayer());
				}
			}
		});
	}
	
	public static void init(TextureAtlas ta) {
		redPixel = ta.findRegion("game1/Red Pixel");
	}
	
	public void start(States returnState) {
		reset(returnState);
		currentWave = tutorialWaves.get(currentWaveIndex);
		currentWave.start();
	}
	
	public void render(SpriteBatch batch) {
		switch(currentWave.getEvent()) {
		case PLAYER_MOVED :
			batch.draw(redPixel, -Specular.camera.viewportWidth / 2, -Specular.camera.viewportHeight / 2, Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight);
			Util.writeCentered(batch, tutorialFont, "Slide Here", -Specular.camera.viewportWidth / 4, 0);
			break;
			
		case PLAYER_SHOT :
			batch.draw(redPixel, 0, -Specular.camera.viewportHeight / 2, Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight);
			Util.writeCentered(batch, tutorialFont, "Slide Here", Specular.camera.viewportWidth / 4, 0);
			break;
				
		case POWER_UPS_SHOWN :
			break;
		}
	}
	
	public void update() {
		if(!currentWave.isCompleted()) {
			outer:
			switch(currentWave.getEvent()) {
			case PLAYER_MOVED :
				if(Player.distTraveledSqrd > 16000) {
					currentWave.complete();
					next();
				}
				break;
				
			case PLAYER_SHOT :
				if(Bullet.bulletsFired > 30) {
					currentWave.complete();
					next();
				}
				break;
					
			case POWER_UPS_SHOWN :
				boolean allActivated = false;
				for(PowerUp pu : gs.getPowerUps()) {
					if(!pu.isActivated()) {
						break outer;
					}
				}
				allActivated = true;
				
				if(allActivated) {
					if(enemiesSpawned && gs.getEnemies().size == 0) {
						currentWave.complete();
						next();
					} else {
						if(!enemiesSpawned) {
							Enemy e = null;
							for(int i = 0; i < 10; i++) {
								e = new EnemyWanderer((float) (Math.random() * Specular.camera.viewportWidth + 50 - 100), (float) (Math.random() * Specular.camera.viewportHeight + 50 - 100), gs);
								e.setSpeed((float) Math.random());
								gs.addEntity(e);
							}
							enemiesSpawned = true;
						}
					}
				}
				break;
			}
		}
	}
	
	public void next() {
		currentWaveIndex++;
		if(currentWaveIndex < tutorialWaves.size) {
			currentWave = tutorialWaves.get(currentWaveIndex);
			currentWave.start();
		} else {
			end();
		}
	}
	
	private void end() {
		gs.showTutorialEnd();
	}
	
	public States getReturnState() {
		return returnState;
	}
	
	public Array<TutorialWave> getTutorialWaves() {
		return tutorialWaves;
	}
	
	public TutorialWave getCurrentWave() {
		return currentWave;
	}
	
	public TutorialWave getWave(TutorialEvent event) {
		for(TutorialWave wave : tutorialWaves) {
			if(wave.getEvent().equals(event)) {
				return wave;
			}
		}
		
		System.err.println("There is no wave with that event");
		return null;
	}
}

