/*package com.tint.specular.game.spawnsystems;

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

public class ParticleSpawnSystem {
	
	private GameState gs;
	private Random rand;
	public ParticleSpawnSystem(GameState gs) {
		this.gs = gs;
		rand = new Random();
	}
	
	public void spawn(Entity ent, int amount, int times) {
		float direction = 360 / amount;
		int offset;
		int particlesAdded = 0;
		
		Particle p;
		Type type = null;
		for(int i = 0; i < times; i++) {
			System.out.println(i);
			while(particlesAdded < amount) {
				offset = - 10 + rand.nextInt(20);
				
				if(ent instanceof Enemy) {
					//Change the type
					if(ent instanceof EnemyNormal) {
						type = Type.ENEMY_NORMAL;
					} else if(ent instanceof EnemyFast) {
						type = Type.ENEMY_FAST;
					} else if(ent instanceof EnemyBooster) {
						type = Type.ENEMY_BOOSTER;
					}
					
					//Particle
					//TODO Remember to uncomment if u are to use particles
					/*p = new Particle(((Enemy) ent).getX(), ((Enemy) ent).getY(),
							(1 + particlesAdded) * direction + offset, ((Enemy) ent).getDeltaX(), ((Enemy) ent).getDeltaY(),
							((Enemy) ent).getInnerRadius(), particlesAdded % 2 == 0, type, gs);
					
					//Adding particle to the game
					gs.addEntity(p);*/
				/*} else if(ent instanceof Player) {
					
					//Particle
					p = new Particle(((Player) ent).getCenterX(), ((Player) ent).getCenterY(),
							(1 + particlesAdded) * direction + offset, ((Player) ent).getDeltaX(), ((Player) ent).getDeltaY(),
							Player.getRadius(), particlesAdded % 2 == 0, Type.PLAYER, gs);
					
					//Adding particle to the game
					gs.addEntity(p);
				} else {
					System.err.println("Entity is no Player or Enemy - ParticleSpawnSystem [spawn()]");
					break;
				}
				particlesAdded++;
			}	
		}
	}
}*/
