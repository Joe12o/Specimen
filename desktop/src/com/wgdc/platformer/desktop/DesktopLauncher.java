package com.wgdc.platformer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.wgdc.platformer.Platformer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.backgroundFPS = 5;
		config.foregroundFPS = 60;
		config.useHDPI = true;
        config.title = "SPECIMEN";
        config.width = 1280;
        config.height = 720;
		config.samples = 8;
		new LwjglApplication(new Platformer(), config);
	}
}
