package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AnalogStick {
	
	public static Texture base, head;
	
	private float xBase, yBase, xHead, yHead;
	private boolean render;
	
	public AnalogStick() {
		
	}
	
	public static void init() {
		base = new Texture(Gdx.files.internal("graphics/game/Aiming Pad.png"));
		head = new Texture(Gdx.files.internal("graphics/game/Aiming Pad.png"));
	}
	
	public void render(SpriteBatch batch) {
//		if(render) {
			batch.draw(base, xBase, yBase);
			batch.draw(head, xHead, yHead);
//		}
	}
	
	public void setBasePos(float xBase, float yBase) {
		this.xBase = xBase;
		this.yBase = yBase;
	}
	
	public void setHeadPos(float xHead, float yHead) {
		this.xHead = xHead;
		this.yHead = yHead;
	}
	
	public void setRender(boolean render) {
		this.render = render;
	}

	public float getXBase() {
		return xBase;
	}

	public float getYBase() {
		return yBase;
	}

	public float getXHead() {
		return xHead;
	}

	public float getYHead() {
		return yHead;
	}
	
	public boolean shallRender() {
		return render;
	}
}
