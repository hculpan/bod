package org.culpan.bod.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.culpan.bod.BodGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Bunch O' Dungeons!";
		config.width = 1351;
		config.height = 760;
		new LwjglApplication(new BodGame(), config);
	}
}
