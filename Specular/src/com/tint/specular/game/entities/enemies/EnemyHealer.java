package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;

public class EnemyHealer extends Enemy {
	
	private static Texture texture;
	private float radiusOfEffect = 128f;
	private float timeSinceLastHeal;
	private float healRate = 100f;
	
	public EnemyHealer(float x, float y, GameState gs, int life) {
		super(x, y, gs, life);
		timeSinceLastHeal = 0;
	}
	
	public static void init() {
		texture = new Texture(Gdx.files.internal("graphics/game/Enemy Healer.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(texture, x, y);
	}
	
	@Override
	public boolean update() {
		float distX;
		float distY;
		
		timeSinceLastHeal++;
		
		//Healing other enemies
		if(timeSinceLastHeal >= healRate) {
			for(Enemy e : gs.getEnemies()) {
				if(!e.equals(this)) {
					distX = e.getX() - x;
					distY = e.getY() - y;
				
					if(distX * distX + distY * distY <= radiusOfEffect)
						e.addLife(1);
				}
			}
		}
		
		//Movement
		
		return super.update();
	}

	@Override
	public int getValue() {
		return 25;
	}

	@Override
	public float getInnerRadius() {
		return texture.getWidth() / 4;
	}

	@Override
	public float getOuterRadius() {
		return texture.getWidth() / 2;
	}
}
