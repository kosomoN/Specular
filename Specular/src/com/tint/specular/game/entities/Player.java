package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tint.specular.Util;
import com.tint.specular.game.GameState;

public class Player implements Entity {

	private static Animation innerAnim, outerAnim;
	private float animFrameTime;
	private float x, y, dx, dy;
	private GameState gs;
	private float timeSinceLastFire, fireRate = 6f;
	
	public Player(GameState gs) {
		this.gs = gs;
	}

	@Override
	public boolean update() {
		
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			dy += 0.8f;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			dy -= 0.8f;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			dx += 0.8f;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			dx -= 0.8f;
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			System.gc();
		}
		timeSinceLastFire += 1;
		if(Gdx.input.isTouched()) {
			if(timeSinceLastFire >= fireRate) {
				gs.addEntity(new Bullet(x, y, (float) (Math.toDegrees(Math.atan2(Gdx.graphics.getHeight() / 2 - Gdx.input.getY(), Gdx.input.getX() - Gdx.graphics.getWidth() / 2)) + Math.random() * 20 - 10), dx, dy, gs));
				timeSinceLastFire = 0;
			}
		}
		
        dx *= 0.95f;
        dy *= 0.95f;
        
        if(x - 31 + dx < 0)
        	dx = -dx * 0.6f;
        else if(x + 31 + dx > gs.getMapWidth())
        	dx = -dx * 0.6f;
        
        if(y - 31 + dy < 0)
        	dy = -dy * 0.6f;
        else if(y + 31 + dy > gs.getMapHeight())
        	dy = -dy * 0.6f;
        
        x += dx;
        y += dy;
        
        return false;
	}

	@Override
	public void render(SpriteBatch batch) {
		animFrameTime += Gdx.graphics.getDeltaTime();
		TextureRegion frame = outerAnim.getKeyFrame(animFrameTime, true);
		batch.draw(frame, x - frame.getRegionWidth() / 2, y - frame.getRegionHeight() / 2);
		
		frame = innerAnim.getKeyFrame(animFrameTime, true);
		batch.draw(frame, x - frame.getRegionWidth() / 2, y - frame.getRegionHeight() / 2);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public static void init() {
		Texture texture  = new Texture(Gdx.files.internal("graphics/game/Player.png"));
		innerAnim = Util.getAnimation(texture, 128, 128, 0.1f, 0, 0, 6, 2);
		outerAnim = Util.getAnimation(texture, 128, 128, 0.1f, 0, 4, 2, 6);
	}
}
