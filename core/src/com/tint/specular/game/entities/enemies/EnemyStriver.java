package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class EnemyStriver extends Enemy {

	private static Animation anim;
	private static AtlasRegion tex, warningTex;

	public EnemyStriver(float x, float y, GameState gs) {
		super(x, y, gs, 2);
		
		targetSpeed = (float) (Math.random() * 2 + 2);
	}

	@Override
	public void renderEnemy(SpriteBatch batch) {
		Util.drawCentered(batch, tex, x, y, rotation * 90 % 360);
	}
	
	@Override
	public void updateMovement() {
		//Calculating angle of movement based on closest player
		double angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
		
		dx = (float) (Math.cos(angle) * speed);
		dy = (float) (Math.sin(angle) * speed);
		x += dx * slowdown;
		y += dy * slowdown;
	}
	
	public static void init(TextureAtlas ta) {
		tex = ta.findRegion("game1/Enemy Striver");
		
		warningTex = ta.findRegion("game1/Enemy Striver Warning");
		
		AtlasRegion animTex = ta.findRegion("game1/Enemy Striver Anim");
		anim = Util.getAnimation(animTex, 64, 64, 1 / 15f, 0, 0, 3, 1);
	}

	@Override
	public int getValue() {
		return 3;
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public float getInnerRadius() { return 16; }
	@Override
	public float getOuterRadius() { return 30; }

	@Override
	public Type getParticleType() {
		return Type.ENEMY_STRIVER;
	}	
	
	@Override
	protected Animation getSpawnAnim() {
		return anim;
	}

	@Override
	protected AtlasRegion getWarningTex() {
		return warningTex;
	}

	@Override
	protected float getRotationSpeed() {
		return 90;
	}
	
	@Override
	public Enemy copy() {
		return new EnemyStriver(0, 0, gs);
	}
}
