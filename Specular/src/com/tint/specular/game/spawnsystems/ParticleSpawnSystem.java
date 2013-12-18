package com.tint.specular.game.spawnsystems;

import java.util.Random;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Particle;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyFast;
import com.tint.specular.game.entities.enemies.EnemyNormal;

public class ParticleSpawnSystem extends SpawnSystem {
	
	public ParticleSpawnSystem(GameState gs) {
		super(gs);
		this.gs = gs;
		rand = new Random();
	}
	
	public void spawn(Entity ent, int amount, boolean shouldSpawnLarge) {
		float direction = 360 / amount;
		int offset;
		
		Particle p;
		Type type = null;
		for(int i = 0; i < amount; i++) {
			offset = rand.nextInt(50) - 25;
			
			if(ent instanceof Enemy) {
				//Change the type
				if(ent instanceof EnemyNormal) {
					type = Type.ENEMY_NORMAL;
				} else if(ent instanceof EnemyFast) {
					type = Type.ENEMY_FAST;
				} else if(ent instanceof EnemyBooster) {
					type = Type.ENEMY_BOOSTER;
				}
				Enemy e = (Enemy) ent;
				//Particle
				//TODO Remember to uncomment if u are to use particles
				p = new Particle(e.getX(), e.getY(),
						i * direction + offset, e.getDx(), e.getDy(),
						e.getInnerRadius(), (i % 2 == 0) && shouldSpawnLarge, type, gs);
				
				//Adding particle to the game
				gs.addEntity(p);
			} else if(ent instanceof Player) {
				Player pl = (Player) ent;
				//Particle
				p = new Particle(pl.getCenterX(), pl.getCenterY(),
						i * direction + offset, pl.getDeltaX(), pl.getDeltaY(),
						Player.getRadius(), (i % 2 == 0) && shouldSpawnLarge, Type.PLAYER, gs);
				
				//Adding particle to the game
				gs.addEntity(p);
			} else {
				System.err.println("Entity is no Player or Enemy - ParticleSpawnSystem [spawn()]");
				break;
			}
		}
	}
}
