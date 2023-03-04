package it.aretesoftware;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import it.aretesoftware.example.QuadtreeExample;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(0);
		config.setTitle("gdx-quadtree-example");
		config.setWindowedMode(1400, 900);
		new Lwjgl3Application(new QuadtreeExample(), config);
	}
}
