package com.tint.specular.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShockWaveRenderer {
	private static Texture shockWave, ring, waveMask;
	private static ShaderProgram shader;
	
	public static void renderShockwave(SpriteBatch batch, float x, float y, float time, boolean renderRing) {
		batch.setShader(shader);
		shader.setUniformf("mask_size", time);
		batch.draw(shockWave, x - shockWave.getWidth() * time / 2, y -shockWave.getHeight() * (1 - time / 2),  shockWave.getWidth() * time / 2, shockWave.getHeight() * (1 - time / 2), shockWave.getWidth(), shockWave.getHeight(), 1, 1, time * 180, 0, 0, (int) shockWave.getWidth(), (int) shockWave.getHeight(), false, false);
		batch.setShader(null);
		if(renderRing)
			batch.draw(ring, x - ring.getWidth() / 2, y - ring.getHeight() / 2);
	}
	
	public static void init() {
		shockWave = new Texture(Gdx.files.internal("graphics/game/effects/Shockwave.png"));
		ring = new Texture(Gdx.files.internal("graphics/game/effects/Ring.png"));
		
		waveMask = new Texture(Gdx.files.internal("graphics/game/effects/ShockwaveMask.png"));
		
		ShaderProgram.pedantic = false;
		 
		shader = new ShaderProgram(VERT, FRAG);
		if (!shader.isCompiled()) {
			System.err.println(shader.getLog());
			Gdx.app.exit();
		}
		if (shader.getLog().length()!=0)
			System.out.println(shader.getLog());
		
		shader.begin();
		shader.setUniformi("u_mask", 2);
		shader.end();
		
		//Bind the mask to index 1
		waveMask.bind(2);
		
		//Reset bound texture
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
	}
	
	public static void setShockwaveTexture(Texture texture) {
		shockWave = texture;
	}
	
	public static Texture getShockwaveTexture() { return shockWave; }
	
	private final static String VERT =  
			"attribute vec4 "+ShaderProgram.POSITION_ATTRIBUTE+";\n" +
			"attribute vec4 "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
			"attribute vec2 "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
			
			"uniform mat4 u_projTrans;\n" + 
			" \n" + 
			"varying vec2 vTexCoord;\n" +
			
			"void main() {\n" +  
			"	vTexCoord = "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
			"	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
			"}";
	
	private final static String FRAG = 
			  "#ifdef GL_ES\n"
			+ "#define LOWP lowp\n"
			+ "precision mediump float;\n"
			+ "#else\n"
			+ "#define LOWP \n"
			+ "#endif\n" +
			"varying vec2 vTexCoord;\n" + 
			"uniform sampler2D u_mask;\n" +	
			"uniform sampler2D u_texture;\n" +	
			"uniform float mask_size;\n" +	
			"void main(void) {\n" + 
			"	vec4 texColor = texture2D(u_texture, vTexCoord + 0.5 - mask_size / float(2));\n" + 
			"	vec4 maskColor = texture2D(u_mask, vTexCoord / mask_size);\n" + 
			"	gl_FragColor = texColor * maskColor;\n" + 
			"}";

	public static Texture getMaskTexture() {
		return waveMask;
	}
}
