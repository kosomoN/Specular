package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.BoardShock;

public class BoardshockUpgrade extends Upgrade {

	private Texture tex;
	
	public BoardshockUpgrade(GameState gs) {
		super(gs, Specular.prefs.getInteger("Boardshock Upgrade Grade"), 10);
	}
	
	public BoardshockUpgrade(int grade, int maxGrade) {
		super(null, grade, maxGrade);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/UpgradeIcon.png"));
	}
	
	@Override
	public void refresh() {
		// Specfic to every upgrade
		BoardShock.setEfficiency((float) (0.25 - Math.sqrt(getGrade()) / getMaxGrade() * 0.25 / 2));
	}
	
	public Texture getTexture() {
		return tex;
	}
}
