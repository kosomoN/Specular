package com.tint.specular.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;

public class HUD {
	private AtlasRegion hudTopLeft, hudTopRight, hudBottom, bar, barFlipped, pause;
	private Texture multiplierBar;
	private AtlasRegion boardshockChargeTex[] = new AtlasRegion[4];
	private GameState gs;
	
	public HUD(GameState gs) {
		this.gs = gs;
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("graphics/game/HUD-packed/pack.atlas"));
		hudTopLeft = atlas.findRegion("HUD Top Left");
		hudTopRight = atlas.findRegion("HUD Top Right");
		hudBottom = atlas.findRegion("HUD bottom");
		pause = atlas.findRegion("Pause");
		bar = atlas.findRegion("Bar");
		barFlipped = new AtlasRegion(atlas.findRegion("Bar"));
		barFlipped.flip(true, false);
		
		multiplierBar = new Texture(Gdx.files.internal("graphics/game/HUD/MP Bar.png"));
		boardshockChargeTex[3] = atlas.findRegion("HUD Boardshock 3");
	}

	public void render(SpriteBatch batch, float multiplierTimer) {
		float multiplierPercent = 1 - multiplierTimer / GameState.MULTIPLIER_COOLDOWN_TIME;

		int textureWidth = (int) (multiplierBar.getWidth() * multiplierPercent);
		int x = -textureWidth / 2;
		int textureX = (int) (multiplierBar.getWidth() * (1 - multiplierPercent) / 2);

		batch.draw(multiplierBar, x, Specular.camera.viewportHeight / 2 - 155, textureX, 0, textureWidth, multiplierBar.getHeight());
		
		batch.draw(hudBottom, -hudBottom.getRegionWidth() / 2, -Specular.camera.viewportHeight / 2);
		
		batch.draw(hudTopLeft, -hudTopLeft.getRegionWidth(), Specular.camera.viewportHeight / 2 - hudTopLeft.getRegionHeight());
		batch.draw(hudTopRight, 0, Specular.camera.viewportHeight / 2 - hudTopRight.getRegionHeight());
		batch.draw(pause, Specular.camera.viewportWidth / 2 - pause.getRegionWidth(), Specular.camera.viewportHeight / 2 - pause.getRegionHeight());
		
		//Drawing LIFE
		for(int i = 0; i < gs.getPlayer().getLife(); i++)
			batch.draw(bar, -368 - i * 50, Specular.camera.viewportHeight / 2 - 73);
		
		//Drawing SHIELDS
		for(int i = 0; i < gs.getPlayer().getShields(); i++)
			batch.draw(bar, -606 - i * 49, Specular.camera.viewportHeight / 2 - 57);
		
		//Drawing COMBO
		for(int i = 0; i < gs.getComboSystem().getCombo(); i++)
			batch.draw(barFlipped, 238 + i * 49, Specular.camera.viewportHeight / 2 - 73);
		
		int chargeLevel = (int) (gs.getBoardshockCharge() / 0.25f);
		if(chargeLevel > 0)
			batch.draw(boardshockChargeTex[chargeLevel - 1], -boardshockChargeTex[chargeLevel - 1].getRegionWidth() / 2, -Specular.camera.viewportHeight / 2);
	}
}
