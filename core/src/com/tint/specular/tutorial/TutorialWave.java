package com.tint.specular.tutorial;

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
