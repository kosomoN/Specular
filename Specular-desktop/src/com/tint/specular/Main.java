package com.tint.specular;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Specular";
		cfg.useGL20 = false;
		cfg.width = 1080;
		cfg.height = 720;
		
		new LwjglApplication(new Specular(), cfg);
	}
}
