package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Player;
import com.tint.specular.utils.Util;

public class EnemyFast extends Enemy {

	//FIELDS
	private static Texture tex;
	private float rotation;

	//CONSTRUCTOR
	public EnemyFast(float x, float y, GameState gs) {
		super(x, y, gs);
		slowdown = 0;
		life = 3;
	}

	//RENDER&UPDATE loop
/*_______________________________________________________________________*/	
	@Override
	public void render(SpriteBatch batch) {
		rotation += Gdx.graphics.getDeltaTime();
		Util.drawCentered(batch, tex, x, y, rotation * 90 % 360);
	}
	
	@Override
	public boolean update() {
		//Calculating angle of movement based on closest player
		double angle = Math.atan2(getClosestPlayer().getCenterY() - y, getClosestPlayer().getCenterX() - x);
		
		dx = (float) (Math.cos(angle) * 3);
		dy = (float) (Math.sin(angle) * 3);
		x += dx * (1 - slowdown);
		y += dy * (1 - slowdown);
		
		return super.update();
	}
/*_______________________________________________________________________*/
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/Enemy Fast.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	public void hit(Player shooter) {
		super.hit(shooter);
		if(life <= 0)
			shooter.addScore(30);
	}

	@Override
	public void dispose() {
		tex.dispose();
	}

	@Override
	public float getInnerRadius() { return tex.getWidth() / 4; }
	@Override
	public float getOuterRadius() { return tex.getWidth() / 2; }
}