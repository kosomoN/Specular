package com.tint.specular.upgrades;

import com.tint.specular.game.GameState;

public abstract class Upgrade {

	protected GameState gs;
	private int grade;
	private int maxGrade;
	private int cost;
	
	public Upgrade(GameState gs, int cost) {
		this.gs = gs;
		this.cost = cost;
	}
	
	public void upgrade() {
		grade = grade == maxGrade ? grade : ++grade;
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
}
