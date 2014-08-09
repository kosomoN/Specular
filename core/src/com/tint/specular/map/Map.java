package com.tint.specular.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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
import com.tint.specular.game.entities.enemies.EnemyBooster;
import com.tint.specular.game.entities.enemies.EnemyCircler;
import com.tint.specular.game.entities.enemies.EnemyDasher;
import com.tint.specular.game.entities.enemies.EnemyExploder;
import com.tint.specular.game.entities.enemies.EnemyShielder;
import com.tint.specular.game.entities.enemies.EnemyStriver;
import com.tint.specular.game.entities.enemies.EnemyTanker;
import com.tint.specular.game.entities.enemies.EnemyVirus;
import com.tint.specular.game.entities.enemies.EnemyWanderer;
import com.tint.specular.game.entities.enemies.EnemyWorm;
import com.tint.specular.game.powerups.PowerUp;
import com.tint.specular.game.GfxSettings;

public class Map {
	private static ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	private int width, height;
	private Texture texture, shockLight, parallax;
	private AtlasRegion mask;
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
		
		mask = gs.getTextureAtlas().findRegion("game1/Mask");
		
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
	
	public void render(SpriteBatch batch, boolean light) {
		batch.draw(texture, 0, 0);
		
		if(GfxSettings.setting != GfxSettings.LOW) {
			batch.end();
			
			//Render masks to the framebuffer
			fbo.begin();
			Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.setProjectionMatrix(matrix);
			
//			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			batch.begin();
			batch.setColor(Color.RED);
			if(gs.getPlayer().getLife() > 0 && GfxSettings.ReturnPt()) {
				for(TrailPart tp : gs.getPlayer().getTrail())
					batch.draw(mask, tp.getX() - tp.getSize() / 2, fbo.getHeight() - tp.getY() - tp.getSize() / 2, tp.getSize(), tp.getSize());
			}
			
			if(GfxSettings.ReturnBt()){
				for(Bullet b : gs.getBullets()) {
					batch.draw(mask, b.getX() - 80, fbo.getHeight() - b.getY() - 80, 160, 160);
				}
			}
			
			for(PowerUp p : gs.getPowerUps()) {
				if(!p.isActivated()) {
					float alpha = p.getDespawnTime() % 100 / 100f;
					float size = (float) (500 * (1 - alpha));
					batch.setColor(1, 0, 0, alpha * 2 > 1 ? 1 : alpha * 2);
					batch.draw(mask, p.getX() - size / 2, fbo.getHeight() - p.getY() - size / 2, size, size);
				}
			}
			
			if(BoardShock.isActivated() && GfxSettings.ReturnBs()) {
				float size = BoardShock.getShockWaveProgress() * 6144;
				batch.draw(ShockWaveRenderer.getMaskTexture(), BoardShock.getActivationX() - size / 2, fbo.getHeight() - BoardShock.getActivationY() - size / 2, size, size);
			}
			
			if(GfxSettings.ReturnPtr()){
				for(Particle e : gs.getParticles()) {
					switch(e.getType()) {
					case BULLET:
						batch.setColor(1, 0, 0, e.getLifetimePercent());
						break;
					case ENEMY_BOOSTER:
						batch.setColor(1, 1, 0, e.getLifetimePercent());
						break;
					case ENEMY_DASHER:
						batch.setColor(0, 1, 0, e.getLifetimePercent());
						break;
					case ENEMY_CIRCLER:
						batch.setColor(0.3f, 1f, 1f, e.getLifetimePercent());
						break;
					case ENEMY_SHIELDER:
						batch.setColor(1, 1, 1, e.getLifetimePercent());
					case ENEMY_STRIVER:
						batch.setColor(1, 0, 1, e.getLifetimePercent());
						break;
					case ENEMY_VIRUS:
						batch.setColor(1, 0, 1, e.getLifetimePercent());
						break;
					case ENEMY_WANDERER:
						batch.setColor(1, 0.9f, 0.5f, e.getLifetimePercent());
						break;
					case ENEMY_EXPLODER:
						batch.setColor(0.8f, 0.2f, 0, e.getLifetimePercent());
						break;
					case ENEMY_TANKER:
						batch.setColor(0.8f, 0.5f, 0, e.getLifetimePercent());
						break;
					default:
						break;
					}
					
					float size = 240;//e.getLifetimePercent() * 160 + 80; //Uncomment for scaling glow size
					batch.draw(mask, e.getX() - size / 2, fbo.getHeight() - e.getY() - size / 2, size, size);
				}
			}
			
			if(GfxSettings.ReturnEt()){
				for(Enemy e : gs.getEnemies()) {
					if(e instanceof EnemyWanderer)
						batch.setColor(1, 0.9f, 0.5f, 1);
					else if(e instanceof EnemyCircler)
						batch.setColor(0.3f, 1f, 1f, 1);
					else if(e instanceof EnemyStriver)
						batch.setColor(1, 0, 1, 1);
					else if(e instanceof EnemyShielder)
						batch.setColor(1, 1, 1, 1);
					else if(e instanceof EnemyDasher)
						batch.setColor(0, 1, 0, 1);
					else if(e instanceof EnemyTanker)
						batch.setColor(0.8f, 0.5f, 0, 1);
					else if(e instanceof EnemyBooster)
						batch.setColor(1, 1, 0, 1);
					else if(e instanceof EnemyExploder)
						batch.setColor(0.8f, 0.2f, 0, 1);
					else if(e instanceof EnemyVirus)
						batch.setColor(1, 0, 1, 1);
					else if(e instanceof EnemyWorm) {
						batch.setColor(0.7f, 1, 0, 1);
						for(EnemyWorm.Part p : ((EnemyWorm) e).getParts())
							batch.draw(mask, p.getX() - 80, fbo.getHeight() - p.getY() - 80, 160, 160);
					}
					
					batch.draw(mask, e.getX() - 80, fbo.getHeight() - e.getY() - 80, 160, 160);
				}
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
			
			if(GfxSettings.ReturnPt()){
				for(TrailPart tp : gs.getPlayer().getTrail())
					shapeRenderer.rect(tp.getX() - tp.getSize() / 2, tp.getY() - tp.getSize() / 2, tp.getSize(), tp.getSize());
			
			}
			
			if(BoardShock.isActivated() && GfxSettings.ReturnBs()) {
				float size = BoardShock.getShockWaveProgress() * 6144;
				shapeRenderer.rect(BoardShock.getActivationX() - size / 2, BoardShock.getActivationY() - size / 2, size, size);
			}
			
			if(GfxSettings.ReturnPtr()){
				for(Particle e : gs.getParticles()) {
					shapeRenderer.rect(e.getX() - 80, e.getY() - 80, 160, 160);
				}
			}
			
			if(GfxSettings.ReturnBt()){
				for(Bullet b : gs.getBullets()) {
					shapeRenderer.rect(b.getX() - 80, b.getY() - 80, 160, 160);
				}
			}
			
			for(PowerUp p : gs.getPowerUps()) {
				if(!p.isActivated())
					shapeRenderer.rect(p.getX() - 250, p.getY() - 250, 500, 500);
			}

			if(GfxSettings.ReturnEt()){
				for(Enemy e : gs.getEnemies()) {
					if(e instanceof EnemyWorm) {
						for(EnemyWorm.Part p : ((EnemyWorm) e).getParts())
							shapeRenderer.rect(p.getX() - 80, p.getY() - 80, 160, 160);
					}
					shapeRenderer.rect(e.getX() - 80, e.getY() - 80, 160, 160);
				}
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
