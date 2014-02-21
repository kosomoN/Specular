package com.tint.specular.game.entities.enemies;

import static com.tint.specular.game.entities.enemies.EnemyVirus.Behavior.POINTLESS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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
	
	private static Texture tex;
	private float size = 0.5f;
	private float growthRate = (float) (BASE_GROWTH_RATE + BASE_GROWTH_RATE * Math.random());
	private Behavior behavior;
	private double angle;
	private float rotation;
	
	public EnemyVirus(float x, float y, GameState gs) {
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
	}

	@Override
	public void render(SpriteBatch batch) {
		rotation += Gdx.graphics.getDeltaTime();
		Util.drawCentered(batch, tex, (float) x - tex.getWidth() / 2 * size, y - tex.getHeight() / 2 * size, tex.getWidth() * size, tex.getHeight() * size, rotation * 90 % 360);
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
			if(x - 20 < 0) {
				angle = Math.PI - angle + Math.random() * 0.2;
				dx = (float) (Math.cos(angle) * 3);
				dy = (float) (Math.sin(angle) * 3);
				x = 20;
			}
			
			if( x + 20 > gs.getCurrentMap().getHeight()) {
				angle = Math.PI - angle + Math.random() * 0.2;
				dx = (float) (Math.cos(angle) * 3);
				dy = (float) (Math.sin(angle) * 3);
				x = gs.getCurrentMap().getHeight() - 20;
			}
			
			if(y - 20 < 0) {
				angle = Math.PI * 2 - angle;
				dx = (float) (Math.cos(angle) * 3);
				dy = (float) (Math.sin(angle) * 3);
				y = 20;
			}
			
			if(y + 20 > gs.getCurrentMap().getHeight()){
				angle = Math.PI * 2 - angle;
				dx = (float) (Math.cos(angle) * 3);
				dy = (float) (Math.sin(angle) * 3);
				y = gs.getCurrentMap().getHeight() - 20;
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
		if(size >= 1) {
			size = 0.5f;
			gs.addEntity(new EnemyVirus(x, y, gs));
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
	}

	@Override
	public Type getParticleType() {
		return Type.ENEMY_VIRUS;
	}
	
	@Override
	public int getSpawnTime() {
		return 0;
	}
}
