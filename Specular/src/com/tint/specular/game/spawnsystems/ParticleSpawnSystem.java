package com.tint.specular.game.spawnsystems;

import java.util.Random;

import com.badlogic.gdx.utils.Pool;
import com.tint.specular.Specular;
import com.tint.specular.game.Camera;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle;
import com.tint.specular.game.entities.Particle.Type;

/**
 * 
 * @author Onni Kosomaa
 *
 */

public class ParticleSpawnSystem extends SpawnSystem {
	
	private Pool<Particle> particlePool;
	
	public ParticleSpawnSystem(final GameState gs) {
		super(gs);
		this.gs = gs;
		rand = new Random();
		particlePool = new Pool<Particle>(0, 10) {
			@Override
			protected Particle newObject() {
				return new Particle(gs);
			}
		};
	}
	
	public void spawn(Type type, float x, float y, float dx, float dy, int amount, boolean shouldSpawnLarge) {
		if(gs.particlesEnabled() && Camera.getCameraX() - Specular.camera.viewportWidth / 2 * Camera.getZoom() - 100 < x &&
				Camera.getCameraX() + Specular.camera.viewportWidth / 2 * Camera.getZoom() + 100 > x &&
				Camera.getCameraY() - Specular.camera.viewportHeight / 2 * Camera.getZoom() - 100 < y &&
				Camera.getCameraY() + Specular.camera.viewportHeight / 2 * Camera.getZoom() + 100 > y) {
			float direction = 360f / amount;
			int offset;
			
			Particle p;
			for(int i = 0; i < amount; i++) {
				offset = rand.nextInt(50) - 25;
				
				p = particlePool.obtain();
				p.reUse(x, y, i * direction + offset, dx, dy, (i % 2 == 0) && shouldSpawnLarge, type);
				
				//Adding particle to the game
				gs.addEntity(p);
			}
		}
	}

	public Pool<Particle> getPool() {
		return particlePool;
	}
}
