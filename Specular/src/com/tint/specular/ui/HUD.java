package com.tint.specular.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.Specular;
import com.tint.specular.game.GameState;

public class HUD {
	private Texture hudTop, hudBottom, bar, multiplierBar;
	private Texture boardshockChargeTex[] = new Texture[4];
	private GameState gs;
	
	public HUD(GameState gs) {
		this.gs = gs;
		
		hudTop = new Texture(Gdx.files.internal("graphics/game/HUD/HUD top.png"));
		hudBottom = new Texture(Gdx.files.internal("graphics/game/HUD/HUD bottom.png"));
		bar = new Texture(Gdx.files.internal("graphics/game/HUD/Bar.png"));
		
		multiplierBar = new Texture(Gdx.files.internal("graphics/game/HUD/MP Bar.png"));
		for(int i = 0; i < 4; i++) {
			boardshockChargeTex[i] = new Texture(Gdx.files.internal("graphics/game/HUD/HUD Boardshock " + i + ".png"));
		}
	}

	public void render(SpriteBatch batch, float multiplierTimer) {
		float multiplierPercent = 1 - multiplierTimer / 360;
		
		
		int textureWidth = (int) (multiplierBar.getWidth() * multiplierPercent);
		int x = -textureWidth / 2;
		int textureX = (int) (multiplierBar.getWidth() * (1 - multiplierPercent) / 2);
		
		batch.draw(multiplierBar, x, Specular.camera.viewportHeight / 2 - 155, textureX, 0, textureWidth, multiplierBar.getHeight());
		
		batch.draw(hudBottom, -hudBottom.getWidth() / 2, -Specular.camera.viewportHeight / 2);
		batch.draw(hudTop, -hudTop.getWidth() / 2, Specular.camera.viewportHeight / 2 - hudTop.getHeight());
		
		//Drawing LIFE
		for(int i = 0; i < gs.getPlayer().getLife(); i++)
			batch.draw(bar, -366 - i * 49, Specular.camera.viewportHeight / 2 - 81);
		
		//Drawing SHIELDS
		for(int i = 0; i < gs.getPlayer().getShields(); i++)
			batch.draw(bar, -604 - i * 49, Specular.camera.viewportHeight / 2 - 65);
		
		//Drawing COMBO
		for(int i = 0; i < gs.getComboSystem().getCombo(); i++)//It's long cause it uses flipX
			batch.draw(bar, 239 + i * 49, Specular.camera.viewportHeight / 2 - 81, bar.getWidth(), bar.getHeight(), 0, 0, bar.getWidth(), bar.getHeight(), true, false);
		
		int chargeLevel = (int) (gs.getBoardshockCharge() / 0.25f);
		if(chargeLevel > 0)
			batch.draw(boardshockChargeTex[chargeLevel - 1], -boardshockChargeTex[chargeLevel - 1].getWidth() / 2, -Specular.camera.viewportHeight / 2);
	}
}
