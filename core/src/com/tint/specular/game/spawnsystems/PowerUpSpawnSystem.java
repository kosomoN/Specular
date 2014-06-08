package com.tint.specular.game.spawnsystems;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.Player.AmmoType;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.powerups.AddLife;
import com.tint.specular.game.powerups.BoardshockPowerUp;
import com.tint.specular.game.powerups.BulletBurst;
import com.tint.specular.game.powerups.FireRateBoost;
import com.tint.specular.game.powerups.LaserPowerup;
import com.tint.specular.game.powerups.PDSPowerUp;
import com.tint.specular.game.powerups.PowerUp;
import com.tint.specular.game.powerups.Repulsor;
import com.tint.specular.game.powerups.Ricochet;
import com.tint.specular.game.powerups.ScoreMultiplier;
import com.tint.specular.game.powerups.ShieldUpgrade;
import com.tint.specular.game.powerups.SlowdownEnemies;
import com.tint.specular.game.powerups.Swarm;

/**
 * 
 * @author Daniel Riissanen
 *
 */

public class PowerUpSpawnSystem extends SpawnSystem {
	
	public PowerUpSpawnSystem(GameState gs) {
		super(gs);
	}
	
	/**
	 * Spawns a random power-up at a random location
	 */
	public void spawn() {
		float x = rand.nextInt(gs.getCurrentMap().getWidth() - 100) + 50;
		float y = rand.nextInt(gs.getCurrentMap().getHeight() - 100) + 50;
		
		float importance = 0;
		Player player = gs.getPlayer();
		float life = (float) (rand.nextDouble() - player.getLife() / 4f * rand.nextDouble() * 0.5f);
		if(life > importance)
			importance = life;
		
		float fireRate = (float) (rand.nextDouble() - (1 - player.getFireRate() / 10f) * rand.nextDouble() * 0.5f);
		if(fireRate > importance)
			importance = fireRate;
		
		float burst = (float) (rand.nextDouble() - player.getBulletBurstLevel() / 3f * rand.nextDouble() * 0.55f);
		if(burst > importance)
			importance = burst;
		
		float score = (float) rand.nextDouble() * 0.8f;
		if(score > importance)
			importance = score;
		
		float slow = (float) (rand.nextDouble() - Enemy.getSlowdown() * rand.nextDouble() * 0.5f);
		if(slow > importance)
			importance = slow;
		
		float shield = (float) (rand.nextDouble() - player.getShields() / 3 * rand.nextDouble() * 0.5f);
		if(shield > importance)
			importance = shield;
		
		float board = (float) (rand.nextDouble() - gs.getBoardshockCharge()  * rand.nextDouble() * 0.5f); 
		if(board > importance)
			importance = board;
		
		float ricochet = (float) rand.nextDouble() * 0.8f;
		if(ricochet > importance)
			importance = ricochet;
		
		float pushAway = (float) rand.nextDouble() * 0.8f;
		if(pushAway > importance)
			importance = pushAway;
		
		float laser = (float) (rand.nextDouble() - (gs.getPlayer().getAmmoType().equals(AmmoType.LASER) ? rand.nextDouble() * 0.45f : 0));
		if(laser > importance)
			importance = laser;
		
		float swarm = (float) (rand.nextDouble() * 0.8f);
		if(swarm > importance)
			importance = swarm;
		
		float pds = (float) (rand.nextDouble() * 0.8f);
		if(pds > importance)
			importance = pds;
		
		if(importance == life && player.getLife() < 4)
			gs.addEntity(new AddLife(x, y, gs));
		else if(importance == fireRate)
			gs.addEntity(new FireRateBoost(x, y, gs));
		else if(importance == burst)
			gs.addEntity(new BulletBurst(x, y, gs));
		else if(importance == score)
			gs.addEntity(new ScoreMultiplier(x, y, gs));
		else if(importance == slow)
			gs.addEntity(new SlowdownEnemies(x, y, gs));
		else if(importance == shield && player.getShields() < 3)
			gs.addEntity(new ShieldUpgrade(x, y, gs));
		else if(importance == board)
			gs.addEntity(new BoardshockPowerUp(x, y, gs));
		else if(importance == ricochet)
			gs.addEntity(new Ricochet(x, y, gs));
		else if(importance == pushAway)
			gs.addEntity(new Repulsor(x, y, gs));
		else if(importance == laser)
			gs.addEntity(new LaserPowerup(x, y, gs));
		else if(importance == swarm)
			gs.addEntity(new Swarm(x, y, gs));
		else if(importance == pds)
			gs.addEntity(new PDSPowerUp(x, y, gs));
	}
	
	/**
	 * Use to spawn one specific powerup at random location
	 * @param pu - The power-up to spawn
	 */
	public void spawn(PowerUp pu) {
		float x = rand.nextInt(gs.getCurrentMap().getWidth() - 100) + 50, y = rand.nextInt(gs.getCurrentMap().getHeight() - 100) + 50;
		pu.setPosition(x, y);
		
		gs.addEntity(pu);
	}
}
