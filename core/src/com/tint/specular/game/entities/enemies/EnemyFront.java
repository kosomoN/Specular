package com.tint.specular.game.entities.enemies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

public class EnemyFront extends Enemy {

	private static Animation spawnAnim;
	private static Texture tex, warningTex;
	
	private double angle;
	private Random random = new Random();
	private static final double MAX_TURNANGLE = Math.PI / 256;
	
	public EnemyFront(float x, float y, GameState gs) {
		super(x, y, gs, 2);
		speed = 3;
		angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
	}

	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Booster.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);		
		
		warningTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Booster Warning.png"));
		warningTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture animTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Booster Anim.png"));
		animTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		spawnAnim = Util.getAnimation(animTex, 64, 64, 1 / 15f, 0, 0, 3, 1);
	}
	
	@Override
	protected void renderEnemy(SpriteBatch batch) {
		if(hasSpawned())
			Util.drawCentered(batch, tex, x, y, rotation - 90);
	}
	
	@Override
	public boolean update() {
		return super.update();
	}
	
	@Override
	public void updateMovement() {
		double targetDir = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
		double turn = 0;
		double deltaDir = targetDir - angle;
		
		//Small turnrate
		if(deltaDir >= Math.PI)
			turn = deltaDir - 2 * Math.PI;
		else if(deltaDir <= -Math.PI)
			turn = deltaDir + 2 * Math.PI;
		else
			turn = deltaDir;
		
		// There is a limit of turning angle
		angle += turn < -MAX_TURNANGLE  ? -MAX_TURNANGLE : turn > MAX_TURNANGLE ? MAX_TURNANGLE : turn;
		
		angle = angle % (2 * Math.PI);
		if(angle < 0)
			angle += 2 * Math.PI;
			
		
		rotation = (float) Math.toDegrees(angle);
		
		dx = (float) (Math.cos(angle) * speed);
		dy = (float) (Math.sin(angle) * speed);
		x += dx * slowdown;
		y += dy * slowdown;
		
		//Checking so that the enemy will not get outside the map
		// Left edge. 20 for width and 18 for map border
		if(x - 20 - 18 < 0) {
			x = 20 + 18;
			
			angle = (random.nextInt(90) - 45) / 180 * Math.PI;
		}
		// Right edge
		else if(x + 20 + 18 > gs.getCurrentMap().getWidth()){
			x = gs.getCurrentMap().getWidth() - 20 - 18;
			
			angle = (random.nextInt(90) + 135) / 180 * Math.PI;
		}
		// Upper edge
		if(y - 20 - 18 < 0) {
			y = 20 + 18;
			
			angle = (random.nextInt(90) + 45) / 180 * Math.PI;
		}
		// Lower edge
		else if(y + 20 + 18 > gs.getCurrentMap().getHeight()){
			y = gs.getCurrentMap().getHeight() - 20 - 18;
			
			angle = (random.nextInt(90) + 225) / 180 * Math.PI;
		}
	}

	@Override
	protected Animation getSpawnAnim() {
		return spawnAnim;
	}

	@Override
	protected float getRotationSpeed() {
		return 2;
	}

	@Override
	protected Texture getWarningTex() {
		return warningTex;
	}

	@Override
	public int getValue() {
		return 0;
	}
	
	public double getDirection() { return angle; }

	@Override
	public Type getParticleType() {
		return Type.ENEMY_BOOSTER;
	}

	@Override
	public float getInnerRadius() {
		return 0;
	}

	@Override
	public float getOuterRadius() {
		return 0;
	}
	
	@Override
	public Enemy copy() {
		return new EnemyFront(0, 0, gs);
	}
}
