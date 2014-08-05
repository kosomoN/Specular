package com.tint.specular.tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.tutorial.Tutorial.TutorialEvent;

public class TutorialWave {
	
	private TutorialEvent event;
	private boolean completed;
	
	public TutorialWave(TutorialEvent event) {
		this.event = event;
	}
	
	public void start() {
		completed = false;
	}
	
	public void render(SpriteBatch batch) {}
	
	public void complete() {
		completed = true;
	}
	
	public TutorialEvent getEvent() {
		return event;
	}
	
	public boolean isCompleted() {
		return completed;
	}
}
