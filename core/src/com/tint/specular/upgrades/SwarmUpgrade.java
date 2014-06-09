package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.powerups.Swarm;

public class SwarmUpgrade extends Upgrade {

	private Texture tex;
	
	public SwarmUpgrade(GameState gs) {
		super(gs, Specular.prefs.getInteger("Swarm Upgrade Grade"), 10);
	}
	
	public SwarmUpgrade(int grade, int maxGrade) {
		super(null, grade, maxGrade);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/UpgradeIcon.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		Swarm.setEffect((float) (2 * (1 + Math.sqrt(getGrade() / getMaxGrade()))));
	}
	
	public Texture getTexture() {
		return tex;
	}
}
