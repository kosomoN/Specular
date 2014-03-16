package com.tint.specular.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.Specular;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyVirus;

public class BoardShock {
	
	private static Sound soundBs1 = Gdx.audio.newSound(Gdx.files.internal("audio/BoardSHOCK2.wav"));	
	private static float timeActivated;
	private static boolean activated;
	private static GameState gs;
	private static float zoom = 0;
	
	public static void activate(GameState gs) {
		activated = true;
		timeActivated = 0;
		BoardShock.gs = gs;
		
		soundBs1.play();
	}

	public static void update() {
		if(activated) {
			timeActivated += GameState.TICK_LENGTH_MILLIS;
			zoom = 1;
			if(timeActivated > 225) {
				zoom = 1 - 1 - ((1 - ((timeActivated - 225) / 75)) * (1 - ((timeActivated - 225) / 75))) * 0.3f;
			} else {
				zoom = 1 - 1 - (1 - (1 - ((timeActivated) / 225)) * (1 - ((timeActivated) / 225))) * 0.3f;
			}
			
			if(timeActivated >= 300) {
				Camera.shake(2.5f, 0.1f);
				activated = false;
				zoom = 0;
				for(Enemy e : gs.getEnemies()) {
					if(!(e instanceof EnemyVirus) || Math.random() > 0.25) {
						e.hit(e.getLife());
						gs.gameMode.enemyKilled(e);
					}
				}
			}
		}
	}
	
	public static void render(SpriteBatch batch) {
		if(activated)
			batch.setColor(0, 0, 0, 1);
		else
			batch.setColor(1, 1, 1, 1);
	}
	
	
	public static void setZoom() {
		Specular.camera.zoom += zoom;
	}

	public static boolean isActivated() {
		return activated;
	}
}
