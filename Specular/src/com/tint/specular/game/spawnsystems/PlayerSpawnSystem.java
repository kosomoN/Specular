package com.tint.specular.game.spawnsystems;

import java.util.Random;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Entity;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.utils.Util;

public class PlayerSpawnSystem {
	
	//FIELDS
	private GameState gs;
	private Random rand;
	
	public PlayerSpawnSystem(GameState gs) {
		this.gs = gs;
		rand = new Random();
	}
	
	public void spawn(int playerAmount) {
		int x, y;
		int playersSpawned = 0;
		
		outer :
		do {
			x = (int) (rand.nextInt(gs.getCurrentMap().getWidth() - (int) (Player.getRadius() * 2)) + Player.getRadius());
			y = (int) (rand.nextInt(gs.getCurrentMap().getHeight() - (int) (Player.getRadius() * 2)) + Player.getRadius());
			
			for(Entity ent : gs.getEntities()) {
				if(ent instanceof Enemy) {
					if(Util.getDistanceSquared(x, y, ((Enemy) ent).getX(), ((Enemy) ent).getY()) < Math.pow(500, 2)) {
						continue outer;
					}
				} else if(ent instanceof Player) {
					if(Util.getDistanceSquared(x, y, ((Player) ent).getCenterX(),
							((Player) ent).getCenterY()) < Math.pow(Player.getRadius() * 2, 2)) {
						continue outer;
					}
				}
			}
			
			gs.addEntity(new Player(gs, x, y));
			playersSpawned++;
		} while(playersSpawned < playerAmount);
	}
}
