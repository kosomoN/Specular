package com.tint.specular.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.tint.specular.game.GameState;

public class Player implements Entity {

	private static Animation innerAnim, outerAnim;
	private float animFrameTime;
	private float x, y, dx, dy;
	private GameState gs;
	
	public Player(GameState gs) {
		this.gs = gs;
	}

	@Override
	public void update() {
		
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			dy += 1f;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			dy -= 1f;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			dx += 1f;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			dx -= 1f;
		}
		
		if(Gdx.input.justTouched()) {
			gs.addEntity(new Bullet(x, y, (float) Math.toDegrees(Math.atan2(Gdx.graphics.getHeight() / 2 - Gdx.input.getY(), Gdx.input.getX() - Gdx.graphics.getWidth() / 2)), dx, dy));
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
		TextureRegion tr = new TextureRegion(new Texture(Gdx.files.internal("graphics/game/Player.png")));
		TextureRegion[][] trArr = tr.split(128, 128);
		Array<TextureRegion> innerFrames = new Array<TextureRegion>();
		for(TextureRegion[] tr1 : trArr) {
			for(TextureRegion tr2 : tr1) {
				if(innerFrames.size < 23) {
					innerFrames.add(tr2);
				}
			}
		}
		
		innerAnim = new Animation(0.1f, innerFrames);
		
		Array<TextureRegion> outerFrames = new Array<TextureRegion>();
		for(int y = 4; y < 7; y++) {
			for(int x = 0; x < 8; x++) {
				if(y == 6 && x == 2)
					break;
				outerFrames.add(trArr[y][x]);
			}
		}
		
		outerAnim = new Animation(0.1f, outerFrames);
	}
}
