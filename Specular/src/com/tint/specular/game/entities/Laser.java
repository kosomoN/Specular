package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;

public class Laser implements Entity, Poolable {

	private static final int FADE_DELAY = 10;
	private static Texture laserTexture, overlayLaserTexture;
	private static Pool<Laser> laserPool;
	
	private float[] verticies = new float[4 * 5];

	private float timer;
	private Vector2 normal;
	private GameState gs;
	private float x2, y2;
	private int barrelIndex;
	
	private Laser(GameState gs) {
		this.gs = gs;
	}

	@Override
	public boolean update() {
		timer += 1;
		calculateVerticies(gs.getPlayer().getBarrelPosX(barrelIndex), gs.getPlayer().getBarrelPosY(barrelIndex), x2, y2);
		return timer > FADE_DELAY;
	}

	@Override
	public void render(SpriteBatch batch) {
		
		float color = Color.toFloatBits(1, 1, 1, (1 - timer / FADE_DELAY) * (1 - timer / FADE_DELAY));
		
		verticies[2] = color;
		verticies[7] = color;
		verticies[12] = color;
		verticies[17] = color;
		
		batch.draw(laserTexture, verticies, 0, 20);
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
		laserTexture.dispose();
		overlayLaserTexture.dispose();
	}
	
	public static void init(final GameState gs) {
		laserTexture = new Texture(Gdx.files.internal("graphics/game/Laser.png"));
		laserTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		overlayLaserTexture = new Texture(Gdx.files.internal("graphics/game/Laser Overlay.png"));
		overlayLaserTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		laserPool = new Pool<Laser>() {

			@Override
			protected Laser newObject() {
				return new Laser(gs);
			}
		};
	}
	
	public static Laser obtainLaser(float x, float y, float x2, float y2, int barrelIndex) {
		return laserPool.obtain().reUse(x, y, x2, y2, barrelIndex);
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
	
	private Laser reUse(float x, float y, float x2, float y2, int barrelIndex) {
//		x2 = MathUtils.clamp(x2, 18, gs.getCurrentMap().getWidth() - 18);
//		y2 = MathUtils.clamp(y2, 18, gs.getCurrentMap().getHeight() - 18);
		this.x2 = x2;
		this.y2 = y2;
		this.barrelIndex = barrelIndex;
		
		normal = new Vector2(x2 - x, y2 - y);
		normal.nor();
		normal.setAngle(normal.angle() + 90);
		normal.scl(8);
		
		verticies[0] = x - normal.x;
		verticies[1] = y - normal.y;
		verticies[2] = Color.WHITE.toFloatBits();
		verticies[3] = 0;
		verticies[4] = 1;
		
		verticies[5] = x + normal.x;
		verticies[6] = y + normal.y;
		verticies[7] = Color.WHITE.toFloatBits();
		verticies[8] = 0;
		verticies[9] = 0;
		
		verticies[10] = x2 + normal.x;
		verticies[11] = y2 + normal.y;
		verticies[12] = Color.WHITE.toFloatBits();
		verticies[13] = 1;
		verticies[14] = 0;
		
		verticies[15] = x2 - normal.x;
		verticies[16] = y2 - normal.y;
		verticies[17] = Color.WHITE.toFloatBits();
		verticies[18] = 1;
		verticies[19] = 1;
		
		timer = 0;
		
		gs.getParticleSpawnSystem().spawn(Type.BULLET, x2, y2, 0, 0, 3, false);
		
		return this;
	}

	@Override
	public void reset() {}
}
