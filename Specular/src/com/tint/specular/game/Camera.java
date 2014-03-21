package com.tint.specular.game;

import java.util.Random;

import com.tint.specular.Specular;
import com.tint.specular.game.entities.enemies.Enemy;

public class Camera {
	private static final float CAMERA_SPEED = 5, CAMERA_CENTER_RATIO = 1.45f, PLAYER_SPEED_OFFSET = 8;
	
	private static float shakeIntensity;
	private static float shakeDecay;
	private static Random rand = new Random();
	private static float cameraX, cameraY, cameraZoom = 1, enemyOffsetX, enemyOffsetY;
	
	public static void update(GameState gs) {
		//The position where the camera should smoothly move to, initially calculated based on the players distance from the center
		float targetX = (float) (Math.sin((gs.getPlayer().getX() - gs.getCurrentMap().getWidth() / 2) / 700) / CAMERA_CENTER_RATIO * 1024);
		targetX += gs.getPlayer().getDeltaX() * PLAYER_SPEED_OFFSET;
		
		float targetY = (float) (Math.sin((gs.getPlayer().getY() - gs.getCurrentMap().getHeight() / 2) / 700) / CAMERA_CENTER_RATIO* 1024);
		targetY += gs.getPlayer().getDeltaY() * PLAYER_SPEED_OFFSET;
		
		
		float targetCameraZoom = 1;
		if(gs.getEnemies().size > 0) {
			float lowestDeltaX = gs.getEnemies().get(0).getX() - cameraX;
			float lowestDeltaY = gs.getEnemies().get(0).getY() - cameraY;
			
			float delta;
			
			//All enemies positions added together
			float totalX = 0, totalY = 0;
			
			for(Enemy e : gs.getEnemies()) {
				totalX += e.getX();
				totalY += e.getY();
				
				//Calculate delta and compare it to other deltas
				delta = e.getX() - cameraX;
				if(Math.abs(lowestDeltaX) > Math.abs(delta)) {
					lowestDeltaX = delta;
				}
				
				delta = Math.abs(e.getY() - cameraY);
				if(Math.abs(lowestDeltaY) > Math.abs(delta)) {
					lowestDeltaY = delta;
				}
			}
			
			//Calculate zoom - distance divided by half of the screen, basically how many screen halves away the enemy is
			float zoomX = Math.abs(lowestDeltaX) / (Specular.camera.viewportWidth / 2);
			float zoomY = Math.abs(lowestDeltaY) / (Specular.camera.viewportHeight / 2);
			
			//Clamping the zoom value
			targetCameraZoom = zoomX > zoomY ? zoomX : zoomY;
			targetCameraZoom = targetCameraZoom < 1 ? 1 : (targetCameraZoom > 1.5f ? 1.5f : targetCameraZoom);
			
			//Moving the camera in the direction of the average of all enemies positions
			enemyOffsetX += ((totalX / gs.getEnemies().size - cameraX) / 4 - enemyOffsetX) / 80;
			enemyOffsetY += ((totalY / gs.getEnemies().size - cameraY) / 4 - enemyOffsetY) / 80;
			
			targetX += enemyOffsetX;
			targetY += enemyOffsetY;
		}
		
		cameraX += ((targetX + gs.getCurrentMap().getWidth() / 2) - cameraX) / CAMERA_SPEED;
		cameraY += ((targetY + gs.getCurrentMap().getHeight() / 2) - cameraY) / CAMERA_SPEED;
		
		cameraZoom += (targetCameraZoom - cameraZoom) / 70;
		
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
