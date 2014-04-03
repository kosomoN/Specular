package com.tint.specular.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * 
 * @author Onni Kosomaa
 *
 */
public interface Entity {
	public boolean update();
	public void render(SpriteBatch batch);
	public void dispose();
	public float getX();
	public float getY();
}
