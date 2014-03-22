package com.tint.specular.game.entities.enemies;

import static com.tint.specular.game.entities.enemies.EnemyVirus.Behavior.POINTLESS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class EnemyVirus extends Enemy {

	public enum Behavior {
		POINTLESS, FOLLOW;
	}

	private static final float BASE_GROWTH_RATE = 0.0025f;
	public static int virusAmount;
	
	private static Animation anim;
	private static Texture tex, warningTex;
	private float size = 1;
	private float growthRate = (float) (BASE_GROWTH_RATE + BASE_GROWTH_RATE * Math.random());
	private Behavior behavior;
	private double angle;
	
	public EnemyVirus(float x, float y, GameState gs, boolean spawnedFromOtherVirus) {
		super(x, y, gs, 1);
		virusAmount++;
		
		double rand = Math.random();
		if(rand > 0.333) {
			behavior = POINTLESS;
		} else {
			behavior = POINTLESS;
		}
		
		angle = Math.random() * Math.PI * 2;
		dx = (float) (Math.cos(angle) * 3);
		dy = (float) (Math.sin(angle) * 3);
		
		if(spawnedFromOtherVirus)
			hasSpawned = true;
	}

	@Override
	public void renderEnemy(SpriteBatch batch) {
		Util.drawCentered(batch, tex, (float) x - tex.getWidth() / 2 * size, y - tex.getHeight() / 2 * size, tex.getWidth() * size, tex.getHeight() * size, rotation * 80 % 360);
	}
	
	@Override
	public boolean update() {
		boolean isDead = super.update();
		if(isDead) {
			virusAmount--;
			return true;
		}
		return false;
	}

	@Override
	public void updateMovement() {
		switch(behavior) {
		case POINTLESS:
			if(x - 20 - 18 < 0) {
				angle = Math.PI - angle + Math.random() * 0.2;
				dx = (float) (Math.cos(angle) * 3);
				dy = (float) (Math.sin(angle) * 3);
				x = 20 + 18;
			}
			 
			if(x + 20 + 18 > gs.getCurrentMap().getHeight()) {
				angle = Math.PI - angle + Math.random() * 0.2;
				dx = (float) (Math.cos(angle) * 3);
				dy = (float) (Math.sin(angle) * 3);
				x = gs.getCurrentMap().getHeight() - 20 - 18;
			}
			
			if(y - 20 - 18 < 0) {
				angle = Math.PI * 2 - angle;
				dx = (float) (Math.cos(angle) * 3);
				dy = (float) (Math.sin(angle) * 3);
				y = 20 + 18;
			}
			
			if(y + 20 + 18 > gs.getCurrentMap().getHeight()){
				angle = Math.PI * 2 - angle;
				dx = (float) (Math.cos(angle) * 3);
				dy = (float) (Math.sin(angle) * 3);
				y = gs.getCurrentMap().getHeight() - 20 - 18;
			}
			break;
		case FOLLOW:
			double angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
			dx = (float) (Math.cos(angle) * 3);
			dy = (float) (Math.sin(angle) * 3);
			break;
		}

		
		x += dx * slowdown;
		y += dy * slowdown;
		
		size += growthRate * 10 / virusAmount;
		if(size >= 1.5f) {
			size = 1;
			gs.addEntity(new EnemyVirus(x, y, gs, true));
		}
	}

	@Override
	public int getValue() {
		return 1;
	}

	@Override
	public float getInnerRadius() {
		return 16 * size;
	}

	@Override
	public float getOuterRadius() {
		return 30 * size;
	}
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Virus.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);		
		
		warningTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Striver Warning.png"));
		warningTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture animTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Striver Anim.png"));
		animTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		anim = Util.getAnimation(animTex, 64, 64, 1 / 15f, 0, 0, 3, 3);
	}

	@Override
	public Type getParticleType() {
		return Type.ENEMY_VIRUS;
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
		return 80;
	}
}
