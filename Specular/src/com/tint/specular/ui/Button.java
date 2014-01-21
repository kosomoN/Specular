package com.tint.specular.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.tint.specular.Specular;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class Button {
	
	//FIELDS
	private Rectangle hitbox;
	private Texture texture;
	
	public Button() {
		hitbox = new Rectangle();
	}
	
	public Button(float x, float y, float width, float height) {
		hitbox = new Rectangle(x, y, width, height);
	}
	
	//RENDER&UPDATE loop
/*_____________________________________________________________________*/
	public void renderTexture(SpriteBatch batch) {
		batch.draw(texture, hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
	}
	
	public void renderTexture(SpriteBatch batch, float x, float y, float width, float height) {
		batch.draw(texture, x, y, width, height);
	}
	
	public void renderShape(ShapeRenderer shape) {
		shape.begin(ShapeType.Filled);
		shape.setColor(Color.RED);
		shape.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
		shape.end();
	}
	
/*_____________________________________________________________________*/
	
	public boolean isOver(float x, float y, boolean topLeftCorner) {
		if(topLeftCorner) {
			return hitbox.contains(x, Specular.camera.viewportHeight - y);
		} else {
			return hitbox.contains(x, y);
		}
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
	
	public void dispose() {
		texture.dispose();
	}
}
