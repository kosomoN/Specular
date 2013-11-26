package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;

public class Device {
	
	private boolean hasKeyboard;
	private boolean hasMultiTouch;
	private boolean hasVibrator;
	private boolean hasAccelerometer;
	private boolean move, shoot;
	
	public float moveX, moveY, shootX, shootY;
	public int touches = 0;
	
	/*public enum Controls {
		KEYBOARD, TWINSTICKS, KEYBOARD_AND_STICK, ACCELEROMETER_AND_STICK;
	}*/
	
	public Device() {
		hasKeyboard = Gdx.input.isPeripheralAvailable(Peripheral.HardwareKeyboard);
		hasMultiTouch = Gdx.input.isPeripheralAvailable(Peripheral.MultitouchScreen);
		hasVibrator = Gdx.input.isPeripheralAvailable(Peripheral.Vibrator);
		hasAccelerometer = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
	}
	
	public boolean hasKeyboard() { return hasKeyboard; }
	public boolean hasAccelerometer() { return hasAccelerometer; }
	public boolean supportsMultiTouch() { return hasMultiTouch; }
	public boolean canVibrate() { return hasVibrator; }
	public boolean isMoving() { return move; }
	public boolean isShooting() { return shoot; }
}
