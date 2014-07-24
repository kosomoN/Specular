package com.tint.specular.game.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class FireRateBoost extends PowerUp {
	private static AtlasRegion texture;
	private static Texture levelTex;
	private static float boost = 2 / 3f;
	public static int stacks;
	
	public FireRateBoost(float x, float y, GameState gs) {
		super(x, y, gs, 800);
	}
	
	public FireRateBoost(float x, float y, GameState gs, float despawnTime) {
		super(x, y, gs, despawnTime);
	}
	
	public static void init(TextureAtlas ta) {
		texture = ta.findRegion("game1/FireRate");
//		boost = Specular.prefs.getFloat("Firerate Boost");
	}
	
	public static void reloadLevelTextures() {
		int grade = Specular.prefs.getInteger("Firerate Upgrade Grade");
		
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
	protected void affect(Player player) {
		stacks++;
		player.setFireRate((float) (10 * Math.pow(boost, stacks)));
	}
	
	@Override
	public void removeEffect(Player player) {
		stacks--;
		player.setFireRate((float) (10 * Math.pow(boost, stacks)));
	}
	
	public static void setBoost(float boost) { FireRateBoost.boost = boost; }
	public static float getBoost() { return boost; }
	
	@Override
	public AtlasRegion getTexture() {
		return texture;
	}

	@Override
	public Texture getLevelTexture() {
		return levelTex;
	}
}
