package com.tint.specular.game.spawnsystems;

import java.util.Random;

import com.badlogic.gdx.utils.Pool;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Particle;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyFast;
import com.tint.specular.game.entities.enemies.EnemyNormal;
import com.tint.specular.game.entities.enemies.EnemyStupid;
import com.tint.specular.game.entities.enemies.EnemyVirus;

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
		particlePool = new Pool<Particle>() {
			@Override
			protected Particle newObject() {
				return new Particle(gs);
			}
		};
	}
	
	public void spawn(Entity ent, int amount, boolean shouldSpawnLarge) {
		float direction = 360f / amount;
		int offset;
		
		Particle p;
		Type type = null;
		for(int i = 0; i < amount; i++) {
			offset = rand.nextInt(50) - 25;
			
			if(ent instanceof EnemyStupid) {
				System.err.println("EnemyStupid doesn't have particles");
			} else if(ent instanceof Enemy) {
				//Change the type
				if(ent instanceof EnemyNormal) {
					type = Type.ENEMY_NORMAL;
				} else if(ent instanceof EnemyFast || ent instanceof EnemyVirus) {
					type = Type.ENEMY_FAST;
				} else if(ent instanceof EnemyBooster) {
					type = Type.ENEMY_BOOSTER;
				}
				Enemy e = (Enemy) ent;
				//Particle
				p = particlePool.obtain();
				p.reUse(e.getX(), e.getY(),
						i * direction + offset, e.getDx(), e.getDy(),
						e.getInnerRadius(), (i % 2 == 0) && shouldSpawnLarge, type);
				
				//Adding particle to the game
				gs.addEntity(p);
			} else if(ent instanceof Player) {
				Player pl = (Player) ent;
				//Particle
				p = particlePool.obtain();
				p.reUse(pl.getCenterX(), pl.getCenterY(),
						i * direction + offset, pl.getDeltaX(), pl.getDeltaY(),
						Player.getRadius(), (i % 2 == 0) && shouldSpawnLarge, Type.PLAYER);
				
				//Adding particle to the game
				gs.addEntity(p);
			} else {
				break;
			}
		}
	}

	public Pool<Particle> getPool() {
		return particlePool;
	}
}
