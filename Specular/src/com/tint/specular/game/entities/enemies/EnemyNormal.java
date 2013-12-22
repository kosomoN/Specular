package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

public class EnemyNormal extends Enemy {

	private static Texture tex;
	private float rotation;
	private float offset;
	private double angle;
	
	public EnemyNormal(float x, float y, GameState gs) {
		super(x, y, gs, 5);
		
	    if(Math.random() < 0.5) {
	    	offset = (float) Math.toRadians(60);
	    } else {
            offset = (float) Math.toRadians(-60);
	    }
	}

	@Override
	public void render(SpriteBatch batch) {
		rotation += Gdx.graphics.getDeltaTime();
		Util.drawCentered(batch, tex, x, y, rotation * 90 % 360);
	}
	
	@Override
	public boolean update() {
		if(gs.getPlayer() != null)
			angle = Math.atan2(gs.getPlayer().getCenterY() - y, gs.getPlayer().getCenterX() - x) + offset;
		
		dx = (float) (Math.cos(angle) * 2);
		dy = (float) (Math.sin(angle) * 2);
		x += dx * (1 - slowdown);
		y += dy * (1 - slowdown);
		
		//Checking map boundaries
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
	
	@Override
	public float getInnerRadius() { return 16; }
	@Override
	public float getOuterRadius() { return 30; }

	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/Enemy Normal.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	public void hit(Player shooter) {
		super.hit(shooter);
		if(life <= 0)
			shooter.addScore(10);
	}

	@Override
	public void dispose() {
		tex.dispose();
	}
}
