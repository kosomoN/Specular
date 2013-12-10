package com.tint.specular.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.utils.Util;

public class AnalogStick {
	
	public static Texture base, head;
	
	private float xBase, yBase, xHead, yHead;
	private int pointer;
	
	public AnalogStick() {
		pointer = -1;
	}
	
	public static void init() {
		base = new Texture(Gdx.files.internal("graphics/game/Aiming Pad.png"));
		head = new Texture(Gdx.files.internal("graphics/game/Aiming Pad.png"));
	}
	
	public void render(SpriteBatch batch) {
		if(isActive()) {
			Util.drawCentered(batch, base, xBase, yBase, 0);
			Util.drawCentered(batch, head, xHead, yHead, 0);
		}
	}
	
	public void setBasePos(float xBase, float yBase) {
		this.xBase = xBase;
		this.yBase = yBase;
	}
	
	public void setHeadPos(float xHead, float yHead) {
		this.xHead = xHead;
		this.yHead = yHead;
	}
	
	public void setPointer(int pointer) {
		this.pointer = pointer;
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
	
	public int getPointer() {
		return pointer;
	}
	
	public boolean isActive() {
		return pointer != -1;
	}
}
