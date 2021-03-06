package com.tint.specular.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tint.specular.Specular;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Onni Kosomaa (Daniel Riissanen)
 *
 */

public class Button {
	
	//FIELDS
	public static boolean btnSoundsEnabled = !Specular.prefs.getBoolean("SoundsMuted");
	
	private Rectangle hitbox;
	private Texture upTexture, downTexture;
	private SpriteBatch batch;
	private boolean touched;
	private Sound btnSound = Gdx.audio.newSound(Gdx.files.internal("audio/fx/ButtonPress.ogg"));
	private float scale = 1.0f;
	
	public Button(float x, float y, float width, float height, SpriteBatch batch, Texture upTexture, Texture downTexture) {
		this.batch = batch;
		this.upTexture = upTexture;
		this.downTexture = downTexture;
		hitbox = new Rectangle(x, y, width, height);
	}
	
	//RENDER&UPDATE loop
/*_____________________________________________________________________*/
	public void render() {
		if(touched)
			Util.drawCentered(batch, downTexture, hitbox.x + hitbox.width / 2, hitbox.y + hitbox.height / 2, scale, 0);
		else
			Util.drawCentered(batch, upTexture, hitbox.x + hitbox.width / 2, hitbox.y + hitbox.height / 2, scale, 0);
	}
	
	public void touchOver(float x, float y) {
		if(!touched && btnSoundsEnabled)
			btnSound.play();
		touched = true;
	}
	
	public void touchUp() {
		touched = false;
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
	public void setPosition(float x, float y) { hitbox.setPosition(x, y); }
	public void setSize(float width, float height) { hitbox.setSize(width, height); }
	public void setScale(float scale) { this.scale = scale; }
	public void setTouch(boolean touched) { this.touched = touched; }
	
	//GETTERS
	public float getX() { return hitbox.getX();	}
	public float getY() { return hitbox.getY(); }
	public float getWidth() { return hitbox.getWidth(); }
	public float getHeight() { return hitbox.getHeight(); }
	public Rectangle getHitbox() { return hitbox; }
	
	public void dispose() {
		upTexture.dispose();
		downTexture.dispose();
	}
}
