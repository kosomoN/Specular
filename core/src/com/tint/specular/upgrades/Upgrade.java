package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;

public abstract class Upgrade {

	protected GameState gs;
	private float grade;
	private int maxGrade;
	private int cost = 1;
	
	public Upgrade(GameState gs, float grade, int maxGrade) {
		this.gs = gs;
		this.grade = grade;
		this.maxGrade = maxGrade;
	}
	
	public void upgrade() {
		grade = (grade >= maxGrade ? maxGrade : grade + 0.1f);
	}

	public void degrade() {
		grade = grade <= 0 ? 0 : --grade;
	}
	
	public void setMaxGrade(int maxGrade) {
		if(maxGrade >= 0) {
			this.maxGrade = maxGrade;
			grade = grade > maxGrade ? maxGrade : grade;
		}
	}

	public float getGrade() {
		return grade;
	}
	
	public int getMaxGrade() {
		return maxGrade;
	}
	
	public int getCost() {
		return cost;
	}
	
	public abstract void refresh();
	public abstract AtlasRegion getTexture();
	public abstract String getDescription();

	public void setGrade(int grade) {
		this.grade = grade;
	}
}
