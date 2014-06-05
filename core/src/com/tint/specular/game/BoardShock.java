package com.tint.specular.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.tint.specular.Specular;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyVirus;

public class BoardShock {
	
	private static Sound soundBs1 = Gdx.audio.newSound(Gdx.files.internal("audio/BoardSHOCK2.wav"));	
	private static float timeActivated;
	private static boolean activated;
	private static GameState gs;
	private static float zoom = 0;
	private static float activationX, activationY;
	private static float efficiency = 0.25f;//Specular.prefs.getFloat("Boardshock Efficiency");
	
	public static void activate(GameState gs) {
		activated = true;
		timeActivated = 0;
		BoardShock.gs = gs;
		
		soundBs1.play();
		
		activationX = gs.getPlayer().getX();
		activationY = gs.getPlayer().getY();
	}

	public static void update() {
		if(activated) {
			timeActivated += 1;
			
			if(timeActivated >= 120) {
				activated = false;
			} else if(timeActivated == 18) {
				Camera.shake(2.5f, 0.1f);
				zoom = 0;
				for(Enemy e : gs.getEnemies()) {
					if(!(e instanceof EnemyVirus) || Math.random() > efficiency) {
						e.hit(e.getLife());
						gs.gameMode.enemyKilled(e);
					}
				}
			} else if(timeActivated < 18) {
				zoom = 1;
				if(timeActivated > 13) {
					zoom = 1 - 1 - ((1 - ((timeActivated - 13) / 5)) * (1 - ((timeActivated - 13) / 5))) * 0.3f;
				} else {
					zoom = 1 - 1 - (1 - (1 - ((timeActivated) / 13)) * (1 - ((timeActivated) / 13))) * 0.3f;
				}
			}
		}
	}
	
	public static float getShockWaveProgress() {
		return (timeActivated - 18) / 102;
	}
	
	public static void setZoom() {
		Specular.camera.zoom += zoom;
	}
	
	public static void setEfficiency(float efficiency) {
		BoardShock.efficiency = efficiency;
	}

	public static boolean isActivated() {
		return activated;
	}
	
	public static float getEfficiency() {
		return efficiency;
	}

	public static float getActivationX() {
		return activationX;
	}

	public static float getActivationY() {
		return activationY;
	}
}
