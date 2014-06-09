package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class LifeUpgrade extends Upgrade {

	private Texture tex;

	public LifeUpgrade(GameState gs) {
		super(gs, Specular.prefs.getInteger("Life Upgrade Grade"), 10);
	}
	
	public LifeUpgrade(int grade, int maxGrade) {
		super(null, grade, maxGrade);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/UpgradeIcon.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		if(getGrade() == getMaxGrade())
			Player.setStartingLife(4);
	}
	
	public Texture getTexture() {
		return tex;
	}
}
