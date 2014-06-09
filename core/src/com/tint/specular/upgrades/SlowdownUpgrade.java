package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.powerups.SlowdownEnemies;

public class SlowdownUpgrade extends Upgrade {

	private Texture tex;
	
	public SlowdownUpgrade(GameState gs) {
		super(gs, Specular.prefs.getInteger("Slowdown Upgrade Grade"), 10);
	}
	
	public SlowdownUpgrade(int grade, int maxGrade) {
		super(null, grade, maxGrade);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/UpgradeIcon.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		SlowdownEnemies.setFreezeTime((float) (Math.sqrt(getGrade()) * 500));
	}
	
	public Texture getTexture() {
		return tex;
	}
}
