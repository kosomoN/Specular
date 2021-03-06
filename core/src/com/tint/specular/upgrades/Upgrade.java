package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;

public abstract class Upgrade {

	protected boolean infinity;
	protected GameState gs;
	private float grade;
	private float maxGrade;
	private float cost = 1;
	
	public Upgrade(GameState gs, float grade, float maxGrade) {
		this.gs = gs;
		this.grade = grade;
		this.maxGrade = maxGrade;
	}
	
	public boolean upgrade() {
		if(grade >= maxGrade)
			return false;
		
		grade += (3 * 0.01f / (grade + 1));
		return true;
	}

	public void degrade() {
		grade = grade <= 0 ? 0 : --grade;
	}
	
	public void setMaxGrade(float maxGrade) {
		if(maxGrade >= 0) {
			this.maxGrade = maxGrade;
			grade = grade > maxGrade ? maxGrade : grade;
		}
	}
	
	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getGrade() {
		if(grade > maxGrade && !infinity)
			grade = maxGrade;

		return grade;
	}
	
	public float getMaxGrade() {
		return maxGrade;
	}
	
	public float getCost() {
		return cost;
	}
	
	public abstract void refresh();
	public abstract AtlasRegion getTexture();
	public abstract String getDescription();

	public void setGrade(float grade) {
		this.grade = grade;
	}
}
