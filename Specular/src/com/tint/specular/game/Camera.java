package com.tint.specular.game;

import java.util.Random;

import com.tint.specular.Specular;
import com.tint.specular.game.entities.enemies.Enemy;

public class Camera {
	private static final float CAMERA_SPEED = 5, CAMERA_CENTER_RATIO = 1.45f, PLAYER_SPEED_OFFSET = 8;
	
	private static float shakeIntensity;
	private static float shakeDecay;
	private static Random rand = new Random();
	private static float cameraX, cameraY, cameraZoom = 1;
	
	public static void update(GameState gs) {
		
		float targetX = (float) (Math.sin((gs.getPlayer().getX() - gs.getCurrentMap().getWidth() / 2) / 700) / CAMERA_CENTER_RATIO * 1024);
		targetX += gs.getPlayer().getDeltaX() * PLAYER_SPEED_OFFSET;
		
		float targetY = (float) (Math.sin((gs.getPlayer().getY() - gs.getCurrentMap().getHeight() / 2) / 700) / CAMERA_CENTER_RATIO* 1024);
		targetY += gs.getPlayer().getDeltaY() * PLAYER_SPEED_OFFSET;
		
		cameraX += ((targetX + gs.getCurrentMap().getWidth() / 2) - cameraX) / CAMERA_SPEED;
		cameraY += ((targetY + gs.getCurrentMap().getHeight() / 2) - cameraY) / CAMERA_SPEED;
		
		float targetCameraZoom = 1;
		if(gs.getEnemies().size > 0) {
			float lowestDeltaX = Math.abs(gs.getEnemies().get(0).getX() - cameraX);
			float lowestDeltaY = Math.abs(gs.getEnemies().get(0).getY() - cameraY);
			
			float delta;
			for(Enemy e : gs.getEnemies()) {
				delta = Math.abs(e.getX() - cameraX);
				if(lowestDeltaX > delta) {
					lowestDeltaX = delta;
				}
				
				delta = Math.abs(e.getY() - cameraY);
				if(lowestDeltaY > delta) {
					lowestDeltaY = delta;
				}
			}
			lowestDeltaY += 50;
			lowestDeltaY += 50;
			
			float zoomX = lowestDeltaX / (Specular.camera.viewportWidth / 2);
			float zoomY = lowestDeltaY / (Specular.camera.viewportHeight / 2);
			
			targetCameraZoom = zoomX > zoomY ? zoomX : zoomY;
			targetCameraZoom = targetCameraZoom < 1 ? 1 : targetCameraZoom;
		}
		cameraZoom += (targetCameraZoom - cameraZoom) / 50;
		
		if(shakeIntensity > 0){
			shakeIntensity -= shakeDecay;
		} else {
			shakeIntensity = 0;
		}
	}
	
	/**
	 * High intensity low decay - much shake in a short period of time
	 * Low intensity high decay - little shake in a long period of time
	 * @param intensity Also works as timer
	 * @param decaySpeed This is subtracted from the intensity every tick
	 */
	public static void shake(float intensity, float decaySpeed) {
		shakeIntensity = intensity;
		shakeDecay = decaySpeed;
	}

	public static void setPosition() {
		Specular.camera.position.set(cameraX, cameraY, 0);
		Specular.camera.position.add(rand.nextFloat() * 100 * shakeIntensity, rand.nextFloat() * 100 * shakeIntensity, 0);
	}
	
	public static void setZoom() {
		Specular.camera.zoom = cameraZoom;
	}
	
	public static float getZoom() {
		return cameraZoom;
	}

	public static float getCameraX() {
		return cameraX;
	}

	public static float getCameraY() {
		return cameraY;
	}
}
