package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.PDS;

public class PDSUpgrade extends Upgrade {

	private Texture tex;
	
	public PDSUpgrade(GameState gs) {
		super(gs, Specular.prefs.getInteger("PDS Upgrade Grade"), 10);
	}
	
	public PDSUpgrade(int grade, int maxGrade) {
		super(null, grade, maxGrade);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/UpgradeIcon.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		PDS.setDamage((float) (2 + Math.sqrt(getGrade())));
	}
	
	public Texture getTexture() {
		return tex;
	}
}