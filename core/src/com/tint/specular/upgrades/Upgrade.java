package com.tint.specular.upgrades;

import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;

public abstract class Upgrade {

	protected GameState gs;
	private int grade;
	private int maxGrade;
	private int cost = 1;
	
	public Upgrade(GameState gs, int grade, int maxGrade) {
		this.gs = gs;
		this.grade = grade;
		this.maxGrade = maxGrade;
	}
	
	public void upgrade() {
		grade = (grade == maxGrade ? grade : ++grade);
	}

	public void degrade() {
		grade = grade == 0 ? grade : --grade;
	}
	
	public void setMaxGrade(int maxGrade) {
		if(maxGrade >= 0) {
			this.maxGrade = maxGrade;
			grade = grade > maxGrade ? maxGrade : grade;
		}
	}

	public int getGrade() {
		return grade;
	}
	
	public int getMaxGrade() {
		return maxGrade;
	}
	
	public int getCost() {
		return cost;
	}
	
	public abstract void refresh();
	public abstract Texture getTexture();
}
