package com.tint.specular.game;

import com.badlogic.gdx.Gdx;
import com.tint.specular.Specular;

public class GfxSettings {

public static boolean PlayerTrail;
public static boolean EnemyTrail;
public static boolean BulletTrail;
public static boolean ParticleTrail;
public static boolean BoardShockFx;


	public static void init() {
		if(Specular.prefs.getInteger("Graphics") == 0) {
			Gdx.app.log("Game", "Low Graphics");
			PlayerTrail = false;
			EnemyTrail = false;
			BulletTrail = false;
			ParticleTrail = false;
			BoardShockFx = true;
		
		}
		
		if(Specular.prefs.getInteger("Graphics") == 1) {
			Gdx.app.log("Game", "Med Graphics");
			PlayerTrail = true;
			EnemyTrail = true;
			BulletTrail = false;
			ParticleTrail = false;
			BoardShockFx = true;
		
		}
		
		if(Specular.prefs.getInteger("Graphics") == 2) {
			Gdx.app.log("Game", "High Graphics");
			PlayerTrail = true;
			EnemyTrail = true;
			BulletTrail = true;
			ParticleTrail = true;
			BoardShockFx = true;
		
			
		}
		
	}
	
	//Returning these potatoes
	public static boolean ReturnBs () {return BoardShockFx;}
	public static boolean ReturnPt () {return PlayerTrail;}
	public static boolean ReturnEt () {return EnemyTrail;}
	public static boolean ReturnBt () {return BulletTrail;}
	public static boolean ReturnPtr () {return ParticleTrail;}
}

