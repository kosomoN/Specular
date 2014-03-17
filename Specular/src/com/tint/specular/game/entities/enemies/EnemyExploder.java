package com.tint.specular.game.entities.enemies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

public class EnemyExploder extends Enemy {

	private static final int EXPLODE_RANGE_SQUARED = 400 * 400, FULL_FUSE = 100;
	private static Texture tex, warningTex, explotionTex;
	private float explotionRadius;
	private float speed;
	private float rotation;
	private int fuse = FULL_FUSE;
	private boolean fuseStart, exploded;
	
	private Array<Enemy> enemiesHit = new Array<Enemy>();
	private int timeSinceLastDirChange;
	private int dirChangeRate = 200;
	private double angle, turnRate;
	
	private Random random = new Random();
	
	public EnemyExploder(float x, float y, GameState gs) {
		super(x, y, gs, 2);
		speed = 0.5f;
	}

	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Exploder.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		explotionTex = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Circle.png"));
		explotionTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		warningTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Exploder Warning.png"));
		warningTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	@Override
	protected void renderEnemy(SpriteBatch batch) {
		rotation += Gdx.graphics.getDeltaTime();
		
		if(fuse > 0) {
			// Normal animation
			Util.drawCentered(batch, tex, x, y, rotation * 10 % 360);
		} else {
			// Explosion animation
			explotionRadius = FULL_FUSE * (-fuse / 4f) / 2;
			batch.draw(explotionTex, x - explotionRadius, y - explotionRadius , explotionRadius * 2, explotionRadius * 2);
		}
	}
	
	@Override
	public boolean update() {
		if(super.update()) {
			fuseStart = true;
			if(fuse > 0)
				fuse = 0;
		}
		updateSuicide();
		return explotionRadius  * explotionRadius  > EXPLODE_RANGE_SQUARED; 
	}

	@Override
	public void updateMovement() {
		if(fuse > 0) {
			timeSinceLastDirChange++;
			
			if(timeSinceLastDirChange > dirChangeRate) {
				turnRate = random.nextInt(40) - 20;
				timeSinceLastDirChange = 0;
			}
			angle += turnRate / 180 * Math.PI;
			
			dx = (float) (Math.cos(angle / 180 * Math.PI) * speed);
			dy = (float) (Math.sin(angle / 180 * Math.PI) * speed);
			x += dx * slowdown;
			y += dy * slowdown;
			
			//Checking so that the enemy will not get outside the map
			// Left edge. 20 for width and 18 for map border
			if(x - 20 - 18 < 0) {
				x = 20 + 18;
				
				angle = random.nextInt(90) - 45;
			}
			// Right edge
			else if(x + 20 + 18 > gs.getCurrentMap().getWidth()){
				x = gs.getCurrentMap().getWidth() - 20 - 18;
				
				angle = random.nextInt(90) + 135;
			}
			// Upper edge
			if(y - 20 - 18 < 0) {
				y = 20 + 18;
				
				angle = random.nextInt(90) + 45;
			}
			// Lower edge
			else if(y + 20 + 18 > gs.getCurrentMap().getHeight()){
				y = gs.getCurrentMap().getHeight() - 20 - 18;
				
				angle = random.nextInt(90) + 225;
			}
		}
	}
	
	public void updateSuicide() {
		if(fuseStart) {
			fuse--;
			
			if(fuse <= 0 && !exploded)
				explode();
		} else {
			Player player = gs.getPlayer();
			
			float distX = x - player.getX();
			float distY = y - player.getY();
			if(distX * distX + distY * distY < (getInnerRadius() + Player.getRadius() + 200) * (getInnerRadius() + Player.getRadius() + 200)) {
				fuseStart = true;
			}
		}
	}
	
	private void explode() {
		float distanceSquared;
		double angle;
		
		// Enemies
		for(Enemy e : gs.getEnemies()) {
			if(!e.equals(this) && !enemiesHit.contains(e, true)) {
				distanceSquared = (e.getX() - getX()) * (e.getX() - getX()) + (e.getY() - getY()) * (e.getY() - getY());
				if(distanceSquared - 32 * 32 < EXPLODE_RANGE_SQUARED && distanceSquared + 32 * 32 > EXPLODE_RANGE_SQUARED) {
					angle = Math.atan2(e.getY() - getY(), e.getX() - getX());
					
					// explosion power is how much the enemy is pushed
					float explotionPower = 10 * (1 - distanceSquared / EXPLODE_RANGE_SQUARED);
					float damage = (1f - (distanceSquared / EXPLODE_RANGE_SQUARED));
					
					e.addDx((float) (Math.cos(angle) * explotionPower));
					e.addDy((float) (Math.sin(angle) * explotionPower));
					
					damage = damage < 0 ? 0 : damage;
					e.hit(damage);
				}
			}
		}
		
		// Player
		Player player = gs.getPlayer();
		distanceSquared = (player.getX() - getX()) * (player.getX() - getX()) + (player.getY() - getY()) * (player.getY() - getY());
		angle = Math.atan2(player.getY() - getY(), player.getX() - getX());
		
		if(distanceSquared < EXPLODE_RANGE_SQUARED) {
			player.setSpeed(player.getDeltaX() + (float) (Math.cos(angle) * 10 * (1 - distanceSquared / EXPLODE_RANGE_SQUARED)),
					player.getDeltaY() + (float) (Math.sin(angle) * 10 * (1 - distanceSquared / EXPLODE_RANGE_SQUARED)));
			player.kill();
		}
		
		exploded = true;
		kill();
	}

	@Override
	public int getValue() {
		return 4;
	}

	@Override
	public Type getParticleType() {
		return null;
	}
	
	@Override
	public void hit(float damage) {
		life -= damage;
	}

	@Override
	public float getInnerRadius() {	return 16; }

	@Override
	public float getOuterRadius() {	return 32; }

	@Override
	protected Animation getSpawnAnim() {
		return null;
	}

	@Override
	protected Texture getWarningTex() {
		return warningTex;
	}

}
