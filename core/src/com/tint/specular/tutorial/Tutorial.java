package com.tint.specular.tutorial;

import com.badlogic.gdx.graphics.Color;
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
import com.tint.specular.game.entities.enemies.EnemyCircler;
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
	private float textX, textY;
	
	// Player movement
	int ticksGone;
	
	// Player shot
	private int bulletsFiredBefore;
	
	// Power-ups shown
	private boolean enemiesSpawned;
	private boolean allActivated;

	public Tutorial(final GameState gs, FreeTypeFontGenerator fontGen) {
		this.gs = gs;
		// Initializing font
		FreeTypeFontParameter ftfp = new FreeTypeFontParameter();
		ftfp.size = 50; // MAX SIZE
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

			@Override
			public void complete() {
				super.complete();
				ticksGone = 0;
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
				ticksGone = 0;
				enemiesSpawned = false;
			}
			
		});
		tutorialWaves.add(new TutorialWave(TutorialEvent.POWER_UPS_SHOWN) {
			@Override
			public void start() {
				super.start();
				float x1 = gs.getPlayer().getX(), x2 = x1;
				float y = gs.getPlayer().getY();

				textX = x1;
				textY = y + 300;
				
				if(x1 < 500) {
					x1 += 400;
					x2 += 800;
					textX += 600;
				} else if(x1 > gs.getCurrentMap().getWidth() - 500) {
					x1 -= 400;
					x2 -= 800;
					textX -= 600;
				} else {
					x1 += 400;
					x2 -= 400;
				}
				
				if(y > gs.getCurrentMap().getHeight() - 500) {
					y -= 400;
					textY -= 800;
				} else {
					y += 400;
				}
				
				gs.addEntity(new FireRateBoost(x1, y, gs));
				gs.addEntity(new BulletBurst(x2, y, gs));
				
				tutorialFont.setColor(1, 0, 0, 0.4f);
				
				allActivated = false;
			}

			@Override
			public void complete() {
				super.complete();
				for(PowerUp pu : gs.getPowerUps()) {
					pu.removeEffect(gs.getPlayer());
				}
				
				tutorialFont.setColor(Color.RED);
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
			if(ticksGone < 32) {
				batch.draw(redPixel, -Specular.camera.viewportWidth / 2, -Specular.camera.viewportHeight / 2, Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight);
				Util.writeCentered(batch, tutorialFont, "Slide Here", -Specular.camera.viewportWidth / 4, 0);
			}
			break;
			
		case PLAYER_SHOT :
			if(ticksGone < 32) {
				batch.draw(redPixel, 0, -Specular.camera.viewportHeight / 2, Specular.camera.viewportWidth / 2, Specular.camera.viewportHeight);
				Util.writeCentered(batch, tutorialFont, "Slide Here", Specular.camera.viewportWidth / 4, 0);
			}
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
				if(Player.distTraveledSqrd > 0) { // If player has moved
					ticksGone++;
					if(ticksGone >= 180) {
						currentWave.complete();
						next();
					}
				}
				break;
				
			case PLAYER_SHOT :
				if(Bullet.bulletsFired > 0) {
					ticksGone++;
					
					if(ticksGone >= 180 && !enemiesSpawned) {
						for(int i = 0; i < 5; i++) {
							gs.addEntity(new EnemyCircler((float) (Math.random() * (Specular.camera.viewportWidth - 100) + 50), (float) (Math.random() * (Specular.camera.viewportHeight - 100) + 50), gs));
						}
						enemiesSpawned = true;
					} else if(gs.getEnemies().size == 0 && ticksGone >= 540) {
						currentWave.complete();
						next();
					}
				}
				break;
					
			case POWER_UPS_SHOWN :
				for(PowerUp pu : gs.getPowerUps()) {
					if(!pu.isActivated()) {
						break outer;
					}
				}
				allActivated = true;
				
				if(allActivated) {
					ticksGone++;
					
					if(ticksGone >= 90) {
						float alpha = (90 + 96 - ticksGone) / 240f;
						alpha = alpha < 0 ? 0 : alpha;
					
						tutorialFont.setColor(1, 0, 0, alpha);
					}
					
					if(enemiesSpawned && gs.getEnemies().size == 0) {
						currentWave.complete();
						next();
					} else {
						if(ticksGone >= 240 && !enemiesSpawned) {
							Enemy e = null;
							for(int i = 0; i < 10; i++) {
								e = new EnemyWanderer((float) (Math.random() * (Specular.camera.viewportWidth - 100) + 50), (float) (Math.random() * (Specular.camera.viewportHeight - 100) + 50), gs);
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
		gs.getPlayer().setLife(Player.getStartingLives());
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
	
	public BitmapFont getFont() {
		return tutorialFont;
	}

	public int getTicks() {
		return ticksGone;
	}
	
	public float getTextX() {
		return textX;
	}
	
	public float getTextY() {
		return textY;
	}
	
	public boolean enemiesHasSpawned() {
		return enemiesSpawned;
	}
	
	public boolean allPowerUpsActivated() {
		return allActivated;
	}
}

