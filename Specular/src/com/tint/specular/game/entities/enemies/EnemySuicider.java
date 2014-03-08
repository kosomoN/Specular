package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

public class EnemySuicider extends Enemy {

	private static final int EXPLODE_RANGE_SQUARED = 400 * 400, FULL_FUSE = 100;
	private static Texture tex, explotionTex;
	private float explotionRadius;
	private float speed;
	private float rotation;
	private int fuse = FULL_FUSE;
	private boolean fuseStart;
	
	public EnemySuicider(float x, float y, GameState gs) {
		super(x, y, gs, 2);
		speed = 10f; // Slower than the player but still very fast
	}

	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Suicider.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		explotionTex = new Texture(Gdx.files.internal("graphics/menu/settingsmenu/Circle.png"));
		explotionTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	@Override
	protected void renderEnemy(SpriteBatch batch) {
		rotation += Gdx.graphics.getDeltaTime();
		
		if(fuse > 0) {
			// Normal animation
			Util.drawCentered(batch, tex, x, y, rotation * 90 % 360);
		} else {
			// Explosion animation
			explotionRadius = FULL_FUSE * (-fuse / 8f) / 2;
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
			//Calculating angle of movement based on closest player
			double angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
			
			dx = (float) (Math.cos(angle) * speed * fuse / FULL_FUSE);
			dy = (float) (Math.sin(angle) * speed);
			x += dx * slowdown;
			y += dy * slowdown;
		}
	}
	
	public void updateSuicide() {
		if(fuseStart) {
			fuse--;
			
			if(fuse <= 0)
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
			if(!e.equals(this)) {
				distanceSquared = (e.getX() - getX()) * (e.getX() - getX()) + (e.getY() - getY()) * (e.getY() - getY());
				if(distanceSquared - 32 * 32 < explotionRadius * explotionRadius && distanceSquared + 32 * 32 > explotionRadius * explotionRadius) {
					angle = Math.atan2(e.getY() - getY(), e.getX() - getX());
					//Math.cos(angle) * distance (0-1) to make it push less if the enemy is far away * time^2 to make it smoothly disappear
					e.addDx((float) (Math.cos(angle) * 10 * (1 - distanceSquared / EXPLODE_RANGE_SQUARED)));
					e.addDy((float) (Math.sin(angle) * 10 * (1 - distanceSquared / EXPLODE_RANGE_SQUARED)));
					
					float damage = 20 * ((explotionRadius * explotionRadius + 32 * 32 - distanceSquared) / (explotionRadius * explotionRadius));
					damage = damage < 0 ? 0 : damage;
					e.hit(Math.round(damage));
				}
			}
		}
		
		// Player
		Player player = gs.getPlayer();
		distanceSquared = (player.getX() - getX()) * (player.getX() - getX()) + (player.getY() - getY()) * (player.getY() - getY());
		angle = Math.atan2(player.getY() - getY(), player.getX() - getX());
		
		if(distanceSquared < explotionRadius * explotionRadius) {
			player.setSpeed(player.getDeltaX() + (float) (Math.cos(angle) * 10 * (1 - distanceSquared / EXPLODE_RANGE_SQUARED)),
					player.getDeltaY() + (float) (Math.sin(angle) * 10 * (1 - distanceSquared / EXPLODE_RANGE_SQUARED)));
			player.kill();
		}
		
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
	public void hit(int damage) {
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
		return null;
	}

}
