package com.tint.specular.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.tint.specular.Specular;
import com.tint.specular.effects.TrailPart;
import com.tint.specular.game.BoardShock;
import com.tint.specular.game.GameState;
import com.tint.specular.game.ShockWaveRenderer;
import com.tint.specular.game.entities.Bullet;
import com.tint.specular.game.entities.Particle;
import com.tint.specular.game.entities.enemies.Enemy;
import com.tint.specular.game.entities.enemies.EnemyCircler;
import com.tint.specular.game.entities.enemies.EnemyDasher;
import com.tint.specular.game.entities.enemies.EnemyShielder;
import com.tint.specular.game.entities.enemies.EnemyStriver;
import com.tint.specular.game.entities.enemies.EnemyWanderer;

public class Map {
	private static ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	private int width, height;
	private Texture texture, shockLight, mask, parallax;
	private FrameBuffer fbo;
	private ShaderProgram shader;
	private String name;
	private GameState gs;
	private Matrix4 matrix = new Matrix4();
	
	
	public Map(Texture texture, Texture shockLight, Texture parallax, String name, int width, int height, GameState gs) {
		this.name = name;
		this.texture = texture;
		this.parallax = parallax;
		this.shockLight = shockLight;
		this.width = width;
		this.height = height;
		this.gs = gs;
		
		matrix.setToOrtho2D(0, 0, texture.getWidth(), texture.getHeight());
		
		mask = new Texture(Gdx.files.internal("graphics/game/Mask.png"));
		
		fbo = new FrameBuffer(Format.RGBA4444, shockLight.getWidth(), shockLight.getHeight(), false);
		
		//Required for custom variables in the shader
		ShaderProgram.pedantic = false;
		
		shader = new ShaderProgram(VERT, FRAG);
		
		if (!shader.isCompiled()) {
			System.err.println(shader.getLog());
			Gdx.app.exit();
		}
		if (shader.getLog().length()!=0)
			System.out.println(shader.getLog());
		
		//Set the "u_mask" variable to one
		shader.begin();
		shader.setUniformi("u_mask", 1);
		shader.end();
		
		//Bind the mask to index 1
		fbo.getColorBufferTexture().bind(1);
		
		//Reset bound texture
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		
	}
	
	public Map() {
		
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(texture, 0, 0);
		batch.end();
		
		
		//Render masks to the framebuffer
		fbo.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(matrix);
		
//		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		batch.begin();
		batch.setColor(Color.RED);
		for(TrailPart tp : gs.getPlayer().getTrail())
			batch.draw(mask, tp.getX() - tp.getSize() / 2, fbo.getHeight() - tp.getY() - tp.getSize() / 2, tp.getSize(), tp.getSize());
		
		for(Bullet b : gs.getBullets()) {
			batch.draw(mask, b.getX() - 80, fbo.getHeight() - b.getY() - 80, 160, 160);
		}
		
		if(BoardShock.isActivated()) {
			float sizee = BoardShock.getShockWaveProgress() * 6144;
			batch.draw(ShockWaveRenderer.getMaskTexture(), BoardShock.getActivationX() - sizee / 2, fbo.getHeight() - BoardShock.getActivationY() - sizee / 2, sizee, sizee);
		}
		
		for(Particle e : gs.getParticles()) {
			switch(e.getType()) {
			case BULLET:
				batch.setColor(1, 0, 0, 1);
				break;
			case ENEMY_BOOSTER:
				break;
			case ENEMY_DASHER:
				batch.setColor(0, 1, 0, 1);
				break;
			case ENEMY_NORMAL:
				batch.setColor(0.3f, 1f, 1f, 1);
				break;
			case ENEMY_SHIELDER:
				batch.setColor(1, 1, 1, 1);
				break;
			case ENEMY_STRIVER:
				batch.setColor(1, 0, 1, 1);
				break;
			case ENEMY_VIRUS:
				batch.setColor(0, 1, 0, 1);
				break;
			case ENEMY_WANDERER:
				batch.setColor(1, 0.9f, 0.5f, 1);
				break;
			}
			
			float size = e.getLifetimePercent() * 160 + 80;
			batch.draw(mask, e.getX() - size / 2, fbo.getHeight() - e.getY() - size / 2, size, size);
		}
		
		for(Enemy e : gs.getEnemies()) {
			if(e instanceof EnemyWanderer)
				batch.setColor(1, 0.9f, 0.5f, 1);
			else if(e instanceof EnemyCircler)
				batch.setColor(0.3f, 1f, 1f, 1);
			if(e instanceof EnemyStriver)
				batch.setColor(1, 0, 1, 1);
			if(e instanceof EnemyShielder)
				batch.setColor(1, 1, 1, 1);
			if(e instanceof EnemyDasher)
				batch.setColor(0, 1, 0, 1);
			batch.draw(mask, e.getX() - 80, fbo.getHeight() - e.getY() - 80, 160, 160);
		}
		
		batch.setColor(Color.WHITE);
		batch.end();
		fbo.end();
		
		//Enable depth masking to improve performance
		Gdx.gl.glDepthFunc(GL20.GL_LESS);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthMask(true);
		Gdx.gl.glColorMask(false, false, false, false);
		
		shapeRenderer.setProjectionMatrix(Specular.camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		
		for(TrailPart tp : gs.getPlayer().getTrail())
			shapeRenderer.circle(tp.getX(), tp.getY(), tp.getSize());
		
		for(Bullet b : gs.getBullets()) {
			shapeRenderer.circle(b.getX(), b.getY(), 160);
		}
		
		if(BoardShock.isActivated()) {
			float size = BoardShock.getShockWaveProgress() * 6144;
			shapeRenderer.circle(BoardShock.getActivationX(), fbo.getHeight() - BoardShock.getActivationY(), size);
		}
		
		for(Particle e : gs.getParticles()) {
			shapeRenderer.circle(e.getX(), e.getY(), 160);
		}
		
		for(Enemy e : gs.getEnemies()) {
			shapeRenderer.circle(e.getX(), e.getY(), 160);
		}
		shapeRenderer.end();
		
		
		//Render using depth masking and shader
		Gdx.gl.glColorMask(true, true, true, true);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
		
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.setProjectionMatrix(Specular.camera.combined);
		batch.begin();
		batch.setShader(shader);
		batch.draw(shockLight, 0, 0);
		batch.setShader(null);
		
		batch.flush();
		
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
	}

	public int getWidth() { return width; }
	public int getHeight() { return height;	}
	public Texture getTexture() { return texture; }
	public String getName() { return name; }
	public void setTexture(Texture texture) { this.texture = texture; }
	public Texture getParallax() { return parallax;	}
	
	
	private final String VERT =  
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
	
	private final String FRAG = 
			  "#ifdef GL_ES\n"
			+ "#define LOWP lowp\n"
			+ "precision mediump float;\n"
			+ "#else\n"
			+ "#define LOWP \n"
			+ "#endif\n" +
			"varying vec2 vTexCoord;\n" + 
			"uniform sampler2D u_mask;\n" +	
			"uniform sampler2D u_texture;\n" +	
			"void main(void) {\n" + 
			"	vec4 texColor = texture2D(u_texture, vTexCoord);\n" + 
			"	vec4 maskColor = texture2D(u_mask, vTexCoord);\n" + 
			"	gl_FragColor = texColor * maskColor;\n" + 
			"}";
}
