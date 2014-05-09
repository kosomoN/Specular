package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Hugo Holmqvist
 *
 */

public class EnemyTanker extends Enemy {

	private static Animation anim;
	private static Texture warningTex;
	private static TextureRegion[] tex = new TextureRegion[4];
	public int hits;
	public int onetickpasses;
	


	public EnemyTanker(float x, float y, GameState gs) {
		super(x, y, gs, 20);	
		speed = (float) 1.5;
	}

	@Override
	public void renderEnemy(SpriteBatch batch) {
		//not rotating, going towards the player.
		float angle = (float) (Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX()- x));
		Util.drawCentered(batch, tex[hits / 5]/* so genius :)*/, x, y, (float) Math.toDegrees(angle) - 90);	
	}
	
	@Override
	public void updateMovement() {
		if(onetickpasses > 60) {
		//Calculating angle of movement based on closest player
		double angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX()- x);
		
		dx = (float) (Math.cos(angle) * speed);
		dy = (float) (Math.sin(angle) * speed);
		x += dx * slowdown;
		y += dy * slowdown;
		}
		onetickpasses++;
		if(onetickpasses == 240){
			onetickpasses = 0;
		}
	}


	@Override
	public void hit(float damage) {	
		speed += 1f;
		hits++;
		super.hit(damage);
	}

	public static void init() {
		Texture texture = new Texture(Gdx.files.internal("graphics/game/enemies/Tanker.png"));
		tex[0] = new TextureRegion(texture, 0, 0, 128, 128);
		tex[1] = new TextureRegion(texture, 128, 0, 128, 128);
		tex[2] = new TextureRegion(texture, 256, 0, 128, 128);
		tex[3] = new TextureRegion(texture, 378, 0, 128, 128);
		warningTex = new Texture(Gdx.files.internal("graphics/game/enemies/Tanker Warning.png"));
		warningTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture animTex = new Texture(Gdx.files.internal("graphics/game/enemies/Tanker Anim.png"));
		animTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		anim = Util.getAnimation(animTex, 64, 64, 1 / 15f, 0, 0, 3, 3);	
	}

	@Override
	public int getValue() {
		return 3;
	}
	

	@Override
	public float getInnerRadius() { return 16; }
	@Override
	public float getOuterRadius() { return 30; }

	@Override
	public Type getParticleType() {
		return Type.ENEMY_SHIELDER;
	}	
	
	@Override
	protected Animation getSpawnAnim() {
		return anim;
	}

	@Override
	protected Texture getWarningTex() {
		return warningTex;
	}

	@Override
	protected float getRotationSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
