package com.tint.specular.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;

public class BeamUpgrade extends Upgrade {

	private Texture tex;
	
	public BeamUpgrade(GameState gs) {
		super(gs, Specular.prefs.getInteger("Beam Upgrade Grade"), 10);
	}
	
	public BeamUpgrade(int grade, int maxGrade) {
		super(null, grade, maxGrade);
		tex = new Texture(Gdx.files.internal("graphics/menu/upgrademenu/UpgradeIcon.png"));
	}
	
	@Override
	public void refresh() {
		// Specific to every upgrade
		gs.getPlayer().setLaserArc((float) (30 + Math.sqrt(getGrade()) * 5));
	}
	
	public Texture getTexture() {
		return tex;
	}
}
