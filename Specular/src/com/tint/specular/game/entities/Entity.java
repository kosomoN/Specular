package com.tint.specular.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Entity {
	public boolean update(float delta);
	public void render(SpriteBatch batch);
	public void dispose();
}
