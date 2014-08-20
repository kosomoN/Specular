package com.tint.specular.game;

import com.badlogic.gdx.Gdx;
import com.tint.specular.Specular;

public class GfxSettings {

	public static final int LOW = 0, MEDIUM = 1, HIGH = 2;
	public static boolean PlayerTrail;
	public static boolean EnemyTrail;
	public static boolean BulletTrail;
	public static boolean ParticleTrail;
	public static boolean BoardShockFx;
	public static int setting;
	public static int orbSize;


	public static void init() {
		setting = Specular.prefs.getInteger("Graphics");
		if(setting == LOW) {
			Gdx.app.log("Game", "Low Graphics");
			PlayerTrail = false;
			EnemyTrail = false;
			BulletTrail = false;
			ParticleTrail = false;
			BoardShockFx = true;
		}
		
		if(setting == MEDIUM) {
			Gdx.app.log("Game", "Med Graphics");
			PlayerTrail = true;
			EnemyTrail = true;
			BulletTrail = false;
			ParticleTrail = false;
			BoardShockFx = true;
			orbSize = 200;
		}
		
		if(setting == HIGH) {
			Gdx.app.log("Game", "High Graphics");
			PlayerTrail = true;
			EnemyTrail = true;
			BulletTrail = true;
			ParticleTrail = true;
			BoardShockFx = true;
			orbSize = 250;
		}
	}
	
	//Returning these potatoes
	public static boolean ReturnBs () {return BoardShockFx;}
	public static boolean ReturnPt () {return PlayerTrail;}
	public static boolean ReturnEt () {return EnemyTrail;}
	public static boolean ReturnBt () {return BulletTrail;}
	public static boolean ReturnPtr () {return ParticleTrail;}
	public static int ReturnSetting () {return setting;}
	public static int getOrbSize () {return orbSize;}
}

