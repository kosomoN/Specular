package com.tint.specular.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.tint.specular.Specular;
import com.tint.specular.utils.Util;

public class ShockWaveRenderer {
	private static Texture[] rings = new Texture[6];
	private static Texture waveMask;
	private static Vector3 scissorVector = new Vector3();
	
	public static void renderShockwave(SpriteBatch batch, float x, float y, float time) {
		
		Gdx.gl.glColorMask(false, false, false, true);
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
		int size = waveMask.getWidth() / 2;
//		batch.draw(waveMask, x - size / 2, y - size / 2, size, size);
		
		batch.flush();
		
		Gdx.gl.glColorMask(true, true, true, true);
		batch.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);
		
		Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
		scissorVector.x = 100;
		scissorVector.y = 100;
		Specular.camera.project(scissorVector);
		Gdx.gl.glScissor((int) (Gdx.graphics.getWidth() / 2), (int) (Gdx.graphics.getHeight() / 2 - size / 2), 500, 500);
		
//		Util.drawCentered(batch, rings[0], x, y, time * 0);
//		Util.drawCentered(batch, rings[1], x, y, time * -27);
		Util.drawCentered(batch, rings[2], x, y, time * 0);
//		Util.drawCentered(batch, rings[3], x, y, time * -15);
//		Util.drawCentered(batch, rings[4], x, y, time * 0);
//		Util.drawCentered(batch, rings[5], x, y, 0);
		
		batch.flush();
		
		Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
	}
	
	public static void renderRepulsor() {
		
	}
	
	public static void init() {
		for(int i = 0; i < 6; i++) {
			rings[i] = new Texture(Gdx.files.internal("graphics/game/shockwave/Ring " + (i + 1) + ".png"));
		}
		
		waveMask = new Texture(Gdx.files.internal("graphics/game/shockwave/WaveMask.png"));
	}
}
