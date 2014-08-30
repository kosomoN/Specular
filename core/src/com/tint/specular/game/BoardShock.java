package com.tint.specular.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.tint.specular.Specular;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyVirus;
import com.tint.specular.game.entities.enemies.EnemyWorm;

public class BoardShock {
	
	private static Sound boardshockSound = Gdx.audio.newSound(Gdx.files.internal("audio/fx/BoardShock.ogg"));	
	private static float timeActivated;
	private static boolean activated;
	private static GameState gs;
	private static float zoom = 0;
	private static float activationX, activationY;
	private static float efficiency = Specular.prefs.getFloat("Boardshock Efficiency");



	
	public static void activate(GameState gs) {
		activated = true;
		timeActivated = 0;
		BoardShock.gs = gs;
		
		activationX = gs.getPlayer().getX();
		activationY = gs.getPlayer().getY();
	}

	public static void update() {		
		if(activated) {
			timeActivated += 1;
			
			if(timeActivated >= 180) {
				activated = false;
			}
			if(timeActivated >= 60) {
				if(gs.gameMusic() != null){
					gs.gameMusic().setVolume(0.05f + ((timeActivated-60) / 180));
				}
			} else if(timeActivated == 18) {
				if(gs.isSoundEnabled())
					boardshockSound.play(1f, 1, 0);
				Camera.shake(2.5f, 0.1f);
				if(gs.gameMusic() != null){
					gs.gameMusic().setVolume(0.05f);
				}
				zoom = 0;
				for(Enemy e : gs.getEnemies()) {
					if(!(e instanceof EnemyVirus) || Math.random() > efficiency) {
						if(e instanceof EnemyWorm) {
							e.kill();
							continue;
						}
						
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
