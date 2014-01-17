package com.tint.specular.game;

import java.util.Random;

import com.tint.specular.Specular;

/**
 * 
 * @author Onni Kosomaa
 *
 */

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
}
