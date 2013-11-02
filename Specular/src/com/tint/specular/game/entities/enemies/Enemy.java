package com.tint.specular.game.entities.enemies;

import com.tint.specular.game.entities.Entity;

public abstract class Enemy implements Entity {
	protected float x, y;
	protected float direction;
	protected float useOfSpeed;
	
	public void setUseOfSpeed(float speedSlowDown) {
		useOfSpeed = speedSlowDown;
		useOfSpeed = useOfSpeed > 1 ? 1 : useOfSpeed;
		useOfSpeed = useOfSpeed < 0 ? 0 : useOfSpeed;
	}
}
