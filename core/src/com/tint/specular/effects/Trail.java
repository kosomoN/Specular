package com.tint.specular.effects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Trail {

	public enum TrailType {
		PLAYER
	}
	
	private List<TrailPart> parts = new ArrayList<TrailPart>();
	private float updateRate;
	private float update;
	
	public Trail(float updateRate) {
		this.updateRate = updateRate;
	}
	
	public static void init() {
		TrailPart.init();
	}
	
	public void render(SpriteBatch batch) {
		for(TrailPart part : parts) {
			part.render(batch);
		}
	}
	
	public void update() {
		update += Gdx.graphics.getDeltaTime();
		
		if(update >= updateRate) {
			for(Iterator<TrailPart> it = parts.iterator(); it.hasNext(); ) {
				TrailPart part = it.next();
				if(part.update())
					it.remove();
			}
			update = 0;
		}
	}
	
	public void addTrailPart(TrailPart part) {
		parts.add(part);
	}
}
