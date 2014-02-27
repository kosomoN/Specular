package com.tint.specular.game.spawnsystems;

import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.powerups.AddLife;
import com.tint.specular.game.powerups.BoardshockPowerUp;
import com.tint.specular.game.powerups.BulletBurst;
import com.tint.specular.game.powerups.FireRateBoost;
import com.tint.specular.game.powerups.PowerUp;
import com.tint.specular.game.powerups.PushAway;
import com.tint.specular.game.powerups.Ricochet;
import com.tint.specular.game.powerups.ScoreMultiplier;
import com.tint.specular.game.powerups.ShieldUpgrade;
import com.tint.specular.game.powerups.SlowdownEnemies;

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
		
		/* First it adds a random factor and sets how common a powerup is, and lastly it takes in to account the affecting powerups
		 * Example:
		 * Affecting powerups ~ 1f - player.getLife() / 4f; to get importance in percentage
		 * How common ~ 0.7f; ranging from 0% to 100%
		 * Random factor ~ rand.nextInt(10) / 10f; returning a float casted double
		 */
		float importance = 0;
		Player player = gs.getPlayer();
		float life = rand.nextInt(10) / 10f * 0.7f - (player.getLife() - 1) / 4f;
		if(life > importance)
			importance = life;
		
		float fireRate = rand.nextInt(10) / 10f * 0.8f - (10 - player.getFireRate()) / 10f;
		if(fireRate > importance)
			importance = fireRate;
		
		float burst = rand.nextInt(10) / 10f * 0.8f - (3 - player.getBulletBurstLevel()) / 3f;
		if(burst > importance)
			importance = burst;
		
		float score = rand.nextInt(10) / 10f * 0.75f;
		if(score > importance)
			importance = score;
		
		float slow = rand.nextInt(10) / 10f * 0.75f - (1 - Enemy.getSlowdown()) / 1f;
		if(slow > importance)
			importance = slow;
		
		float shield = rand.nextInt(10) / 10f * 0.65f - (player.getShields() - 1) / 3;
		if(shield > importance)
			importance = shield;
		
		float board = rand.nextInt(10) / 10f * 0.7f - gs.getBoardshockCharge() + 0.15f; 
		if(board > importance)
			importance = board;
		
		float ricochet = rand.nextInt(10) / 10f * 0.6f;
		if(ricochet > importance)
			importance = ricochet;
		
		float pushAway = rand.nextInt(10) / 10f * 0.7f;
		if(pushAway > importance)
			importance = pushAway;
		
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
			gs.addEntity(new PushAway(x, y, gs));
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
