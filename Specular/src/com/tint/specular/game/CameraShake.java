package com.tint.specular.game;

import java.util.Random;

import com.tint.specular.Specular;

public class CameraShake {
	
	private static float shakeIntensity;
	private static float shakeDecay;
	private static Random rand = new Random();

	public static void update() {
      if(shakeIntensity > 0){
          shakeIntensity -= shakeDecay;
       } else {
    	   shakeIntensity = 0;
       }
	}
	
	public static void moveCamera() {
		Specular.camera.position.add(rand.nextFloat() * 100 * shakeIntensity, rand.nextFloat() * 100 * shakeIntensity, 0);
	}
	
	public static void shake(float intensity, float decay) {
		shakeIntensity = intensity;
		shakeDecay = decay;
	}
}
