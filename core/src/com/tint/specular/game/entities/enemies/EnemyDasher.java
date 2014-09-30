package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.game.GameState;
import com.tint.specular.game.GfxSettings;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

/**
 *
 * @author Hugo Holmqvist
 *
 */		

public class EnemyDasher extends Enemy {
	
	private static Animation anim;
	private static AtlasRegion tex, warningTex, dashWarningTex;

	private double direction;
	private int boostingDelay = -100;
	private boolean lastDashVertical = Math.random() > 0.5 ? true : false;
	private int maxDashDistance = (int) (Math.random() * 500 + 750);
	private float distanceMoved;
	
	public EnemyDasher(float x, float y, GameState gs) {
		super(x, y, gs, 10);
	}

	@Override
	public void renderEnemy(SpriteBatch batch) {

		if(GfxSettings.setting == GfxSettings.LOW) {
			//Render warning line
			batch.setColor(1, 1, 1, ((float) Math.cos(boostingDelay / 60f * Math.PI * 2 + Math.PI) + 1) / 4);

			if(direction == 0 || direction == Math.PI) {
				float width = 0;
				if(direction == 0) {
					width = gs.getCurrentMap().getWidth() - x;
					for(int i = 0, n = (int) Math.ceil(width / dashWarningTex.getRegionWidth()); i < n; i++)
						batch.draw(dashWarningTex, x + i * dashWarningTex.getRegionWidth(), y - dashWarningTex.getRegionHeight() / 2);
				} else {
					width = x;
					for(int i = 0, n = (int) Math.ceil(width / dashWarningTex.getRegionWidth()); i < n; i++)
						batch.draw(dashWarningTex, x - (i + 1) * dashWarningTex.getRegionWidth(), y - dashWarningTex.getRegionHeight() / 2);
				}

			} else {
				float height = y;
				if(direction == Math.PI / 2) {
					//Upwards
					height = gs.getCurrentMap().getHeight() - y;
					for(int i = 0, n = (int) Math.ceil(height / dashWarningTex.getRegionHeight()); i < n; i++)
						Util.drawCentered(batch, dashWarningTex, x, y + i * dashWarningTex.getRegionWidth(), 90);
				} else {
					//Downwards
					for(int i = 0, n = (int) Math.ceil(height / dashWarningTex.getRegionHeight()); i < n; i++)
						Util.drawCentered(batch, dashWarningTex, x, y - (i + 1) * dashWarningTex.getRegionWidth(), 90);

				}
			}

			batch.setColor(Color.WHITE);
		}
		Util.drawCentered(batch, tex, x, y, (float) Math.toDegrees(direction) - 90);
		
	}
	
	@Override
	public void updateMovement() {
		
		//Boosting and changing direction
		if(boostingDelay > 120) {
			speed += 0.5;
			

			x += dx * speed * slowdown;
			y += dy * speed * slowdown;
		
			distanceMoved += dx * speed * slowdown;
	
			distanceMoved += dy * speed * slowdown;

			if(boostingDelay > 150) {
				if (direction == 0) {
					if(distanceMoved > maxDashDistance || gs.getPlayer().getX() < x) {
						boostingDelay = 0;
						dx = 0;
						dy = 0;
					}
				} else if (direction == Math.PI) {
					if(distanceMoved > maxDashDistance || gs.getPlayer().getX() > x) {
						boostingDelay = 0;
						dx = 0;
						dy = 0;
					}
				} else if (direction == Math.PI / 2) {
					if(distanceMoved > maxDashDistance || gs.getPlayer().getY() < y) {
						boostingDelay = 0;
						dx = 0;
						dy = 0;
					}
				} else if (direction == Math.PI / 2 * 3) {
					if(distanceMoved > maxDashDistance || gs.getPlayer().getY() > y) {
						boostingDelay = 0;
						dx = 0;
						dy = 0;
					}
				}
			}
			
		} 

		if(boostingDelay == 0) {
			if(gs.getPlayer() != null) {
				
				if(lastDashVertical) {
					int dy = (int) (gs.getPlayer().getY() - y);
					direction = (dy > 0 ? Math.PI / 2 : Math.PI / 2 * 3 );
				} else {
					int dx = (int) (gs.getPlayer().getX() - x);
					direction = (dx > 0 ? 0 : Math.PI);
				}
				lastDashVertical = !lastDashVertical;
			}
			
			dx = (float) (Math.cos(direction));
			dy = (float) (Math.sin(direction));
			speed = 0;
		}
		
		boostingDelay++;

		//To stop deacceleration
		targetSpeed = speed;
	}

	@Override
	public float getInnerRadius() { return 16; }
	@Override
	public float getOuterRadius() { return 30; }
	
	public static void init(TextureAtlas ta) {
		tex = ta.findRegion("game1/Enemy Dasher");
		
		warningTex = ta.findRegion("game1/Enemy Dasher Warning");
		
		dashWarningTex = ta.findRegion("game1/Enemy Dasher Dash Warning");
		
		AtlasRegion animTex = ta.findRegion("game1/Enemy Dasher Anim");
		anim = Util.getAnimation(animTex, 128, 128, 1 / 15f, 0, 0, 3, 3);
			
	}
	
	@Override
	public int getValue() {
		return 5;
	}
	
	@Override
	public void dispose() {
	}
	 
	@Override
	public Type getParticleType() {
		return Type.ENEMY_DASHER;
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
		return 0;
	}

	@Override
	public Enemy copy() {
		return new EnemyDasher(0, 0, gs);
	}

	public double getDasherDirection() {
		return direction;
	}

	public int getBoostingDelay() {
		return boostingDelay;
	}

}