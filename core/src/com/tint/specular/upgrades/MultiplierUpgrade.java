package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.game.GameState;

public class MultiplierUpgrade extends Upgrade {

	private Texture tex;
	
	public MultiplierUpgrade(int grade, int maxGrade) {
		super(grade, maxGrade);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/UpgradeIcon.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every update
		GameState.MULTIPLIER_COOLDOWN_TIME = (int) (360 * (1 + Math.sqrt(getGrade()) / getMaxGrade() * 0.1f)) ;
	}
	
	public Texture getTexture() {
		return tex;
	}
}
