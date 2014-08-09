package com.tint.specular.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;

public class Laser implements Entity, Poolable {

	private static final int FADE_DELAY = 10;
	private static AtlasRegion laserTexture;
	private static Pool<Laser> laserPool;
	
	private float[] verticies = new float[4 * 5];

	private float timer;
	private Vector2 normal;
	private GameState gs;
	private float x2, y2;
	private int barrelIndex;
	private boolean laserFromBarrels;
	
	private Laser(GameState gs) {
		this.gs = gs;
	}

	@Override
	public boolean update() {
		timer += 1;
		
		if(laserFromBarrels) {
			calculateVerticies(gs.getPlayer().getBarrelPosX(barrelIndex), gs.getPlayer().getBarrelPosY(barrelIndex), x2, y2);
		} else {
			calculateVerticies(gs.getPlayer().getX(), gs.getPlayer().getY(), x2, y2);
		}
		
		
		return timer > FADE_DELAY;
	}

	@Override
	public void render(SpriteBatch batch) {
		
		float color = Color.toFloatBits(1, 1, 1, (1 - timer / FADE_DELAY) * (1 - timer / FADE_DELAY));
		
		verticies[2] = color;
		verticies[7] = color;
		verticies[12] = color;
		verticies[17] = color;
		
		batch.draw(laserTexture.getTexture(), verticies, 0, 20);
	}
	
	@Override
	public float getX() {
		return -1;
	}

	@Override
	public float getY() {
		return -1;
	}

	@Override
	public void dispose() {
	}
	
	public static void init(final GameState gs) {
		laserTexture = gs.getTextureAtlas().findRegion("game1/Laser");
		
		laserPool = new Pool<Laser>() {

			@Override
			protected Laser newObject() {
				return new Laser(gs);
			}
		};
	}
	
	public static Laser obtainLaser(float x, float y, float x2, float y2, int barrelIndex, boolean fromBarrels) {
		return laserPool.obtain().reUse(x, y, x2, y2, barrelIndex, fromBarrels);
	}
	
	private void calculateVerticies(float x, float y, float x2, float y2) {
		normal.x = x2 - x;
		normal.y = y2 - y;
		normal.nor();
		normal.setAngle(normal.angle() + 90);
		normal.scl(8);
		
		verticies[0] = x - normal.x;
		verticies[1] = y - normal.y;
		
		verticies[5] = x + normal.x;
		verticies[6] = y + normal.y;
		
		verticies[10] = x2 + normal.x;
		verticies[11] = y2 + normal.y;
		
		verticies[15] = x2 - normal.x;
		verticies[16] = y2 - normal.y;
	}
	
	private Laser reUse(float x, float y, float x2, float y2, int barrelIndex, boolean fromBarrels) {
//		x2 = MathUtils.clamp(x2, 18, gs.getCurrentMap().getWidth() - 18);
//		y2 = MathUtils.clamp(y2, 18, gs.getCurrentMap().getHeight() - 18);
		this.x2 = x2;
		this.y2 = y2;
		this.barrelIndex = barrelIndex;
		laserFromBarrels = fromBarrels;
		
		normal = new Vector2(x2 - x, y2 - y);
		normal.nor();
		normal.setAngle(normal.angle() + 90);
		normal.scl(laserTexture.getRegionHeight() / 2);
		
		verticies[0] = x - normal.x;
		verticies[1] = y - normal.y;
		verticies[2] = Color.WHITE.toFloatBits();
		verticies[3] = laserTexture.getU();
		verticies[4] = laserTexture.getV2();
		
		verticies[5] = x + normal.x;
		verticies[6] = y + normal.y;
		verticies[7] = Color.WHITE.toFloatBits();
		verticies[8] = laserTexture.getU();
		verticies[9] = laserTexture.getV();
		
		verticies[10] = x2 + normal.x;
		verticies[11] = y2 + normal.y;
		verticies[12] = Color.WHITE.toFloatBits();
		verticies[13] = laserTexture.getU2();
		verticies[14] = laserTexture.getV();
		
		verticies[15] = x2 - normal.x;
		verticies[16] = y2 - normal.y;
		verticies[17] = Color.WHITE.toFloatBits();
		verticies[18] = laserTexture.getU2();
		verticies[19] = laserTexture.getV2();
		
		timer = 0;
		
		gs.getParticleSpawnSystem().spawn(Type.BULLET, x2, y2, 0, 0, 3, false);
		
		return this;
	}

	@Override
	public void reset() {}
}
