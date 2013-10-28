package com.tint.specular.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Entity {
	public boolean update();
	public void render(SpriteBatch batch);
}
