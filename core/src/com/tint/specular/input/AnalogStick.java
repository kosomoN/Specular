package com.tint.specular.input;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.ui.HUD;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class AnalogStick {
	
	public static AtlasRegion base, head;
	
	private float xBase, yBase, xHead, yHead;
	private int pointer;

	private boolean isStatic;
	
	public AnalogStick(boolean isStatic) {
		pointer = -1;
		this.isStatic = isStatic;
	}
	
	public static void init(HUD hud) {
		head = hud.getDpadCenter();
		base = hud.getDpadOuter();
	}
	
	public void render(SpriteBatch batch) {
		if(isStatic || isActive())
			Util.drawCentered(batch, base, xBase, yBase, 0);
		
		if(isActive()) {
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

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
}
