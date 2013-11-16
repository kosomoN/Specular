package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

public class EnemyNormal extends Enemy {

	//FIELDS
	private static Texture tex;
	private float rotation;
	private float offset;
	
	//CONSTRUCTOR
	public EnemyNormal(float x, float y, GameState gs) {
		super(x, y, gs);
		this.width = tex.getWidth();
		this.height = tex.getHeight();
		
		speedUtilization = 1;
		life = 2;
		
	    if(Math.random() < 0.5) {
	    	offset = (float) Math.toRadians(60);
	    } else {
            offset = (float) Math.toRadians(-60);
	    }
	}

	//RENDER&UPDATE loop
/*_______________________________________________________________________________*/
	@Override
	public void render(SpriteBatch batch) {
		rotation += Gdx.graphics.getDeltaTime();
		Util.drawCentered(batch, tex, x, y, rotation * 90 % 360);
	}
	
	@Override
	public boolean update() {
		double angle = Math.atan2(getClosestPlayer().getCenterY() - y, getClosestPlayer().getCenterX() - x) + offset;
		x += Math.cos(angle) * 2 * speedUtilization;
		y += Math.sin(angle) * 2 * speedUtilization;
		
		if(x - 20 < 0) {
			offset = -offset;
		} else if(x + 20 > gs.getCurrentMap().getHeight()){
			offset = -offset;
		}
		
		if(y - 20 < 0) {
			offset = -offset;
		} else if(y + 20 > gs.getCurrentMap().getHeight()){
			offset = -offset;
		}
		
		
		return super.update();
	}
/*_______________________________________________________________________________*/
	
	//GETTERS
	@Override
	public float getInnerRadius() {
		return tex.getWidth() / 4;
	}
	
	public float getOuterRadius() {
		return tex.getWidth() / 2;
	}
		
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/Enemy Normal.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	public void hit(Player shooter) {
		super.hit(shooter);
		if(life <= 0)
			shooter.addScore(20);
	}

	@Override
	public void dispose() {
		tex.dispose();
	}
}
