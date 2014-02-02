package com.tint.specular.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.tint.specular.Specular;
import com.tint.specular.game.entities.enemies.Enemy;

public class BoardShock {
	
	private static Sound soundBs1 = Gdx.audio.newSound(Gdx.files.internal("audio/BoardSHOCK2.wav"));	
	private static float timeActivated;
	private static boolean activated;
	private static GameState gs;
	private static float zoom = 1;
	
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
				zoom = 1 - ((1 - ((timeActivated - 225) / 75)) * (1 - ((timeActivated - 225) / 75))) * 0.25f;
			} else {
				zoom = 1 - (1 - (1 - ((timeActivated) / 225)) * (1 - ((timeActivated) / 225))) * 0.25f;
			}
			
			Specular.camera.zoom = zoom;
			
			if(timeActivated >= 300) {
				CameraShake.shake(0.5f, 0.03f);
				activated = false;
				for(Enemy e : gs.getEnemies()) {
					e.hit(e.getLife());
					gs.gameMode.enemyKilled(e);
				}
			}
		}
	}
	
	public static void setZoom() {
		Specular.camera.zoom = zoom;
	}

	public static boolean isActivated() {
		return activated;
	}
}
