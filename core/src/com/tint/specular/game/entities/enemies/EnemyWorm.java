package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class EnemyWorm extends Enemy {
	
	private static Texture body1Tex, body2Tex, headTex, tailTex;
	
	private Array<Part> parts = new Array<Part>();
	private Part head;
	private float time;
	private float speed;
	private int ticks;
	private double angle;
	
	public EnemyWorm(float x, float y, GameState gs) {
		super(x, y, gs, 1);
		head = new Part(0, 100, 0, false, null);
		for(int i = 0; i < 6; i++) {
			Part p = i > 0 ? parts.get(i - 1) : head;
			parts.add(new Part(i * 100, 100, i + 1, i == 5, p));
		}
		
		setWormSpeed(5);
	}
	
	private void setWormSpeed(float speed) {
		this.speed = speed;
		ticks = (int) (150 / speed);
	}

	@Override
	public boolean update() {
		time += 1;
		head.update((int) (time % ticks));
		for(Part p : parts)
			p.update((int) (time % ticks));
		return false;
	}

	@Override
	public void updateMovement() {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		head.render(batch);
		for(Part p : parts)
			p.render(batch);
	}
	
	public static void init() {
		body1Tex = new Texture(Gdx.files.internal("graphics/game/enemies/Body 1.png"));
		body2Tex = new Texture(Gdx.files.internal("graphics/game/enemies/Body 2.png"));
		tailTex = new Texture(Gdx.files.internal("graphics/game/enemies/Tail.png"));
		headTex = new Texture(Gdx.files.internal("graphics/game/enemies/Head.png"));
	}

	public class Part {
		private float x, y, oldX, oldY, goingToX, goingToY;
		private Part nextPart;
		private int partIndex;
		private boolean isLast;
		
		public Part(float x, float y, int partIndex, boolean isLast, Part nextPart) {
			this.nextPart = nextPart;
			oldX = x;
			oldY = y;
			this.partIndex = partIndex;
			this.isLast = isLast;
			if(partIndex != 0) {
				goingToX = nextPart.oldX;
				goingToX = nextPart.oldY;
			}
		}
		
		public void render(SpriteBatch batch) {
			if(partIndex == 0)
				batch.draw(headTex, x - headTex.getWidth() / 2, y - headTex.getHeight() / 2);
			else if(isLast)
				batch.draw(tailTex, x - tailTex.getWidth() / 2, y - tailTex.getHeight() / 2);
			else if(partIndex % 2 == 0) {
				batch.draw(body1Tex, x - body1Tex.getWidth() / 2, y - body1Tex.getHeight() / 2);
			} else {
				batch.draw(body2Tex, x - body2Tex.getWidth() / 2, y - body2Tex.getHeight() / 2);
			}
		}

		private void update(int tick) {
			if(partIndex != 0) {
				x = linearInterpolate(oldX, goingToX, (float) tick / ticks);
				y = linearInterpolate(oldY, goingToY, (float) tick / ticks);
				if(tick == ticks - 1) {
					oldX = goingToX; 
					oldY = goingToY;
					goingToX = nextPart.oldX;
					goingToY = nextPart.oldY;
				}
			} else {
				angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);/*
				if(deltaAngle > Math.PI)
					deltaAngle += deltaAngle > 0 ? -Math.PI * 2 : Math.PI * 2;
				
				if(deltaAngle < 0)
					angle += TURN_RATE;
				else
					angle -= TURN_RATE;*/
				
				x += Math.cos(angle) * speed;
				y += Math.sin(angle) * speed;
				
				oldX = x;
				oldY = y;
			}
		}

		private float linearInterpolate(float y1, float y2, float mu) {
		   return (y1 * (1 - mu) + y2 * mu);
		}
	}
	
	public int getValue() {
		return 10;
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
	public Type getParticleType() {
		return Type.ENEMY_BOOSTER;
	}	

	@Override
	protected void renderEnemy(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Animation getSpawnAnim() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Texture getWarningTex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected float getRotationSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}
}