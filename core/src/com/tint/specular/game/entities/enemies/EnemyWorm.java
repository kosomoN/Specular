package com.tint.specular.game.entities.enemies;

import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class EnemyWorm extends Enemy {
	
	private static final int DIST_BETWEEN_PARTS = 50, LENGTH = 6;
	
	private static AtlasRegion body1Tex, body2Tex, headTex, tailTex;
	
	private Array<Part> parts = new Array<Part>();
	private Part head;
	private float time;
	private float speed;
	private int ticks;
	private double angle;
	
	public EnemyWorm(float x, float y, GameState gs) {
		super(x, y, gs, 1);
		head = new Part(0, 0, 0, false, null);
		parts.add(head);
		for(int i = 0; i < LENGTH; i++) {
			Part p = parts.get(i);
			parts.add(new Part(0, 0, i + 1, i == LENGTH - 1, p));
		}
		
		setWormSpeed(5);
		hasSpawned = true;
	}
	
	private EnemyWorm(Array<Part> parts, GameState gs) {
		super(parts.get(0).x, parts.get(0).y, gs, 1);
		head = parts.get(0);
		this.parts = parts;
		
		setWormSpeed(5);
		hasSpawned = true;
	}
	
	@Override
	public void hit(float damage) {
		head.hit(damage);
	}

	private void setWormSpeed(float speed) {
		this.speed = speed;
		ticks = (int) (DIST_BETWEEN_PARTS / speed);
	}

	@Override
	public boolean update() {
		time += 1;
		this.x = head.x;
		this.y = head.y;
		int i = -1;
		for(Iterator<Part> it = parts.iterator(); it.hasNext();) {
			i++;
			Part p = it.next();
			
			//If its just one part, remove it
			if(parts.size <= 1) {
				p.hit(p.getLife());
				it.remove();
				continue;
			}
			if(p.update((int) (time % ticks))) {
				//If a part is destroyed
				if(p == head) {
					//If its the head just remove it and update the others
					head = parts.get(1);
					it.remove();
					for(int j = 0; j < parts.size; j++)
						parts.get(j).partIndex--;
				} else if(p.isLast) {
					//Even easier if its the last one, just remove it
					parts.get(i - 1).isLast = true;
					it.remove();
				} else {
					//If its in the middle
					Array<Part> newWormParts = new Array<Part>();
					
					//If its the second last just remove it and the one after it
					if(i == parts.size - 2) {
						it.remove();
						it.next();
						it.remove();
						parts.get(i - 1).isLast = true;
					} else {
						//Construct a new worm with the parts behind the destroyed one
						for(int j = i + 1; j < parts.size; j++) {
							Part pp = parts.get(j);
							pp.partIndex = newWormParts.size;
							newWormParts.add(pp);
						}
						
						for(Part pp : newWormParts) {
							parts.removeValue(pp, true);
						}
						
						parts.get(parts.size - 1).isLast = true;
						
						gs.addEntity(new EnemyWorm(newWormParts, gs));
					}
				}
			}
		}

		return parts.size <= 0;
	}

	@Override
	public void updateMovement() {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		renderEnemy(batch);
	}
	
	public static void init(TextureAtlas ta) {
		body1Tex = ta.findRegion("game1/Body 1");
		body2Tex = ta.findRegion("game1/Body 2");
		tailTex = ta.findRegion("game1/Tail");
		headTex = ta.findRegion("game1/Head");
	}

	public class Part {
		private float x, y, oldX, oldY, goingToX, goingToY;
		private Part nextPart;
		private int partIndex;
		private boolean isLast;
		private float rotation;
		private float partLife = 6;
		
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
		
		public float getLife() {
			return partLife;
		}

		public void render(SpriteBatch batch) {
			if(partIndex == 0)
				Util.drawCentered(batch, headTex, x, y, (float) Math.toDegrees(angle) - 90);
			else if(isLast)
				Util.drawCentered(batch, tailTex, x, y, this.rotation);
			else if(partIndex % 2 == 0) {
				Util.drawCentered(batch, body1Tex, x, y, this.rotation);
			} else {
				Util.drawCentered(batch, body2Tex, x, y, this.rotation);
			}
		}

		private boolean update(int tick) {
			if(partIndex != 0) {
				x = linearInterpolate(oldX, goingToX, (float) tick / ticks);
				y = linearInterpolate(oldY, goingToY, (float) tick / ticks);
				if(tick == ticks - 1) {
					oldX = goingToX; 
					oldY = goingToY;
					goingToX = nextPart.oldX;
					goingToY = nextPart.oldY;
				}
				
				this.rotation = (float) Math.toDegrees(Math.atan2(y - nextPart.y, x - nextPart.x)) + 90;
			} else {
				angle = Math.atan2(gs.getPlayer().getY() - y, gs.getPlayer().getX() - x);
				/*
				if(deltaAngle > Math.PI)
					deltaAngle += deltaAngle > 0 ? -Math.PI * 2 : Math.PI * 2;
				
				if(deltaAngle < 0)
					angle += TURN_RATE;
				else
					angle -= TURN_RATE;*/
				x += Math.cos(angle) * speed * slowdown;
				y += Math.sin(angle) * speed * slowdown;
				oldX = x;
				oldY = y;
			}
			return partLife <= 0;
		}

		private float linearInterpolate(float y1, float y2, float mu) {
		   return (y1 * (1 - mu) + y2 * mu);
		}

		public float getX() {
			return x;
		}
		
		public float getY() {
			return y;
		}

		public int getOuterRadius() {
			return 30;
		}

		public void hit(float damage) {
			partLife -= damage;
			
			if(life <= 0)
				gs.getParticleSpawnSystem().spawn(getParticleType(), x, y, dx * slowdown, dy * slowdown, 15, true);
			else
				gs.getParticleSpawnSystem().spawn(getParticleType(), x, y, dx * slowdown, dy * slowdown, 6, false);
		}

		public float getInnerRadius() {
			return 16;
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
		head.render(batch);
		for(Part p : parts)
			p.render(batch);
	}

	@Override
	protected Animation getSpawnAnim() {
		return null;
	}

	@Override
	protected AtlasRegion getWarningTex() {
		return null;
	}

	@Override
	protected float getRotationSpeed() {
		return 0;
	}

	public Array<Part> getParts() {
		return parts;
	}

	@Override
	public Enemy copy() {
		return new EnemyWorm(0, 0, gs);
	}
}
