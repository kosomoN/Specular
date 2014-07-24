package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

public class BoardshockPowerUp extends PowerUp {

	private static AtlasRegion texture;
	private static Texture levelTex;

	public BoardshockPowerUp(float x, float y, GameState gs) {
		super(x, y, gs, -1);
	}

	@Override
	protected void affect(Player p) {
		gs.addBoardshockCharge(1f);
	}

	@Override
	public AtlasRegion getTexture() {
		return texture;
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/BoardShock");
	}
	
	public static void reloadLevelTextures() {
		int grade = Specular.prefs.getInteger("Boardshock Upgrade Grade");
		
		if(grade > 10) { // Infinity
			levelTex = new Texture(Gdx.files.internal("graphics/game/powerups/level inf.png"));
		} else if(grade == 10) { // Max
			levelTex = new Texture(Gdx.files.internal("graphics/game/powerups/level 5.png"));
		} else if(grade >= 5) {
			levelTex = new Texture(Gdx.files.internal("graphics/game/powerups/level 4.png"));
		} else if(grade >= 3) {
			levelTex = new Texture(Gdx.files.internal("graphics/game/powerups/level 3.png"));
		} else if(grade >= 2) {
			levelTex = new Texture(Gdx.files.internal("graphics/game/powerups/level 2.png"));
		} else if(grade >= 1) {
			levelTex = new Texture(Gdx.files.internal("graphics/game/powerups/level 1.png"));
		}
	}

	@Override
	public Texture getLevelTexture() {
		return levelTex;
	}
}
