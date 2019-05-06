package com.eugenestudio.flappybird.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.eugenestudio.flappybird.GdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(1280, 720);
		config.setTitle("Flappy Bird");
		config.setWindowIcon("icon.png");
		new Lwjgl3Application(new GdxGame(), config);
	}
}
