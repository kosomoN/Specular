package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.game.GameState;

public class EnemyWorm extends Enemy {
	
	private static Texture middleTex, headTex;
	private static final double TURN_RATE = 0.04;
	
	private Array<Part> parts = new Array<Part>();
	private Part head;
	private float time;
	private float speed;
	private int ticks;
	private double angle;
	
	public EnemyWorm(float x, float y, GameState gs) {
		super(x, y, gs);
		head = new Part(0, 100, true, null);
		for(int i = 0; i < 6; i++) {
			Part p = i > 0 ? parts.get(i - 1) : head;
			parts.add(new Part(i * 100, 100, false, p));
		}
		
		setSpeed(5);
	}
	
	private void setSpeed(float speed) {
		this.speed = speed;
		ticks = (int) (90 / speed);
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
	public void render(SpriteBatch batch) {
		head.render(batch);
		for(Part p : parts)
			p.render(batch);
	}
	
	public static void init() {
//		middleTex = new Texture(Gdx.files.internal("graphics/game/Enemy Worm Middle.png"));
//		headTex = middleTex;
	}

	public class Part {
		private float x, y, oldX, oldY, goingToX, goingToY;
		private Part nextPart;
		private boolean isHead;
		
		public Part(float x, float y, boolean isHead, Part nextPart) {
			this.nextPart = nextPart;
			oldX = x;
			oldY = y;
			this.isHead = isHead;
			if(!isHead) {
				goingToX = nextPart.oldX;
				goingToX = nextPart.oldY;
			}
		}
		
		public void render(SpriteBatch batch) {
			if(isHead)
				batch.draw(headTex, x - headTex.getWidth() / 2, y - headTex.getHeight() / 2);
			else
				batch.draw(middleTex, x - middleTex.getWidth() / 2, y - middleTex.getHeight() / 2);
		}

		private void update(int tick) {
			if(!isHead) {
				x = linearInterpolate(oldX, goingToX, (float) tick / ticks);
				y = linearInterpolate(oldY, goingToY, (float) tick / ticks);
				if(tick == ticks - 1) {
					oldX = goingToX; 
					oldY = goingToY;
					goingToX = nextPart.oldX;
					goingToY = nextPart.oldY;
				}
			} else {
				double deltaAngle = angle - Math.atan2(getClosestPlayer().getCenterX() - y, getClosestPlayer().getCenterY() - x);
				if(deltaAngle > Math.PI)
					deltaAngle += deltaAngle > 0 ? -Math.PI * 2 : Math.PI * 2;
				
				if(deltaAngle < 0)
					angle += TURN_RATE;
				else
					angle -= TURN_RATE;
				
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
	
	@Override
	public float getInnerRadius() {
		return 0;
	}

	@Override
	public float getOuterRadius() {
		return 0;
	}

	/*@Override
	public float getDeltaX() {
		return dx;
	}

	@Override
	public float getDeltaY() {
		return dy;
	}*/
}
