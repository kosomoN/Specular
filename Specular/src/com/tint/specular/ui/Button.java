package com.tint.specular.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class Button {
	
	//FIELDS
	private Rectangle hitbox;
	private Texture texture;
	
	private boolean isPressed;
	
	//CONSTRUCTOR
	public Button() {
		hitbox = new Rectangle(0, 0, 1, 1);
	}
	
	public Button(float x, float y) {
		hitbox.setPosition(x, y);
	}
	
	public Button(float x, float y, float width, float height) {
		hitbox.setPosition(x, y);
		hitbox.setSize(width, height);
	}
	
	//RENDER&UPDATE loop
/*_____________________________________________________________________*/
	public void renderTexture(SpriteBatch batch) {
		batch.draw(texture, hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
	}
	
	public void renderShape(ShapeRenderer shape) {
		shape.begin(ShapeType.Filled);
		shape.setColor(Color.YELLOW);
		shape.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
		shape.end();
	}
	
	public void update(float delta) {
		if(Gdx.input.justTouched()) {
			if(hitbox.contains(Gdx.input.getX(), Gdx.input.getY())) {
				isPressed = true;
				return;
			}
		}
				
		isPressed = false;
	}
/*_____________________________________________________________________*/
	
	public boolean isOver(float x, float y) {
		return hitbox.contains(x, Gdx.graphics.getHeight() - y);
	}
	
	//SETTERS
	public void setTexture(Texture texture) { this.texture = texture; }
	public void setPosition(float x, float y) { hitbox.setPosition(x, y); }
	public void setSize(float width, float height) { hitbox.setSize(width, height); }
	
	//GETTERS
	public float getX() { return hitbox.getX();	}
	public float getY() { return hitbox.getY(); }
	public float getWidth() { return hitbox.getWidth(); }
	public float getHeight() { return hitbox.getHeight(); }
	public Rectangle getHitbox() { return hitbox; }
	public Texture getTexture() { return texture; }
	public boolean isPressed() { return isPressed; }
	
	public void dispose() {
		texture.dispose();
	}
}
