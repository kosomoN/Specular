package com.tint.specular.game.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;

/**
 *
 * @author Hugo Holmqvist
 *
 */		

public class EnemyDasher extends Enemy {
	
	private static Animation anim;
	private static Texture tex, warningTex, dashWarningTex, boostTex;

	private double direction;
	private int boostingDelay = -1;
	
	private static ShaderProgram shader;

	public EnemyDasher(float x, float y, GameState gs) {
		super(x, y, gs, 10);
	}

	@Override
	public void renderEnemy(SpriteBatch batch) {
			
		//Render warning line
		batch.setColor(1, 1, 1, ((float) Math.cos(boostingDelay / 60f * Math.PI * 2 + Math.PI) + 1) / 4);
		
		if(direction == 0 || direction == Math.PI) {
			float width = 0;
			if(direction == 0) {
				width = gs.getCurrentMap().getWidth() - x;
				for(int i = 0, n = (int) Math.ceil(width / dashWarningTex.getWidth()); i < n; i++)
					batch.draw(dashWarningTex, x + i * dashWarningTex.getWidth(), y - dashWarningTex.getHeight() / 2);
			} else {
				width = x;
				for(int i = 0, n = (int) Math.ceil(width / dashWarningTex.getWidth()); i < n; i++)
					batch.draw(dashWarningTex, x - (i + 1) * dashWarningTex.getWidth(), y - dashWarningTex.getHeight() / 2);
			}

		} else {
			float height = y;
			if(direction == Math.PI / 2) {
				//Upwards
				height = gs.getCurrentMap().getHeight() - y;
				for(int i = 0, n = (int) Math.ceil(height / dashWarningTex.getHeight()); i < n; i++)
					Util.drawCentered(batch, dashWarningTex, x, y + i * dashWarningTex.getWidth(), 90);
			} else {
				//Downwards
				for(int i = 0, n = (int) Math.ceil(height / dashWarningTex.getHeight()); i < n; i++)
					Util.drawCentered(batch, dashWarningTex, x, y - (i + 1) * dashWarningTex.getWidth(), 90);
				
			}
		}
		
		batch.setColor(Color.WHITE);
		
		if(speed > 0) {
			batch.setShader(shader);
			shader.setUniformf("blurSize", speed / 300);
		}
		Util.drawCentered(batch, tex, x, y, (float) Math.toDegrees(direction) - 90);
		
		if(speed > 0)
			batch.setShader(null);
	}
	
	@Override
	public void updateMovement() {
		
		//Boosting and changing direction
		if(boostingDelay > 120) {
			speed += 0.5;
			
			dx = (float) (Math.cos(direction) * speed);
			dy = (float) (Math.sin(direction) * speed);
			x += dx * slowdown;
			y += dy * slowdown;
			
			if(boostingDelay > 150) {
				if (direction == 0) {
					if(gs.getPlayer().getX() < x) {
						boostingDelay = 0;
						dx = 0;
						dy = 0;
					}
				} else if (direction == Math.PI) {
					if(gs.getPlayer().getX() > x) {
						boostingDelay = 0;
						dx = 0;
						dy = 0;
					}
				} else if (direction == Math.PI / 2) {
					if(gs.getPlayer().getY() < y) {
						boostingDelay = 0;
						dx = 0;
						dy = 0;
					}
				} else if (direction == Math.PI / 2 * 3) {
					if(gs.getPlayer().getY() > y) {
						boostingDelay = 0;
						dx = 0;
						dy = 0;
					}
				}
			}
			
		} 

		if(boostingDelay == 0) {
			if(gs.getPlayer() != null) {
				int dx = (int) (gs.getPlayer().getX() - x);
				int dy = (int) (gs.getPlayer().getY() - y);
				
				if(Math.abs(dx) > Math.abs(dy)) {
					direction = (dx > 0 ? 0 : Math.PI);
				} else {
					direction = (dy > 0 ? Math.PI / 2 : Math.PI / 2 * 3 );
				}
			}
			
			dx = 0;
			dy = 0;
			speed = 0;
		}
		
		boostingDelay++;


	}

	@Override
	public float getInnerRadius() { return 16; }
	@Override
	public float getOuterRadius() { return 30; }
	
	public static void init() {
		tex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Dasher.png"));
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);	
		
		warningTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Dasher Warning.png"));
		warningTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		dashWarningTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Dasher Dash Warning.png"));
		dashWarningTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture animTex = new Texture(Gdx.files.internal("graphics/game/enemies/Enemy Dasher Anim.png"));
		animTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		anim = Util.getAnimation(animTex, 128, 128, 1 / 15f, 0, 0, 3, 3);
		

		ShaderProgram.pedantic = false;
		 
		shader = new ShaderProgram(VERT, FRAG);
		if (!shader.isCompiled()) {
			System.err.println(shader.getLog());
			Gdx.app.exit();
		}
		if (shader.getLog().length()!=0)
			System.out.println(shader.getLog());
		
	}
	
	@Override
	public int getValue() {
		return 5;
	}
	
	@Override
	public void dispose() {
		tex.dispose();
	}
	 
	@Override
	public Type getParticleType() {
		return Type.ENEMY_DASHER;
	}	
	
	@Override
	protected Animation getSpawnAnim() {
		return anim;
	}

	@Override
	protected Texture getWarningTex() {
		return warningTex;
	}

	@Override
	protected float getRotationSpeed() {
		return 0;
	}

	public static Texture getBoostTex() {
		return boostTex;
	}

	public static void setBoostTex(Texture boostTex) {
		EnemyDasher.boostTex = boostTex;
	}
	
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
			"uniform sampler2D u_texture;\n"+
			"uniform float blurSize;\n"+
			"void main(void) {\n" + 
			"   vec4 sum = vec4(0.0);\n"+
			"	sum += texture2D(u_texture, vec2(vTexCoord.x, vTexCoord.y - 4.0*blurSize)) * 0.05;\n"+
			"	sum += texture2D(u_texture, vec2(vTexCoord.x, vTexCoord.y - 3.0*blurSize)) * 0.09;\n"+
			"	sum += texture2D(u_texture, vec2(vTexCoord.x, vTexCoord.y - 2.0*blurSize)) * 0.12;\n"+
			"	sum += texture2D(u_texture, vec2(vTexCoord.x, vTexCoord.y - blurSize)) * 0.15;\n"+
			"	sum += texture2D(u_texture, vec2(vTexCoord.x, vTexCoord.y)) * 0.16;\n"+
			"	sum += texture2D(u_texture, vec2(vTexCoord.x, vTexCoord.y + blurSize)) * 0.15;\n"+
			"	sum += texture2D(u_texture, vec2(vTexCoord.x, vTexCoord.y + 2.0*blurSize)) * 0.12;\n"+
			"	sum += texture2D(u_texture, vec2(vTexCoord.x, vTexCoord.y + 3.0*blurSize)) * 0.09;\n"+
			"	sum += texture2D(u_texture, vec2(vTexCoord.x, vTexCoord.y + 4.0*blurSize)) * 0.05;\n"+
 
   			"gl_FragColor = sum;\n" + 
			"}";

}