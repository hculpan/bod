package org.culpan.bod;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.culpan.bod.screens.BodGameScreen;

public class BodGame extends Game {
	public static Skin skin;

	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
        skin = new Skin(Gdx.files.internal("skins/pixthulhu/skin/pixthulhu-ui.json"));

//TODO Change to TitleScreen
        //        this.setScreen(new TitleScreen(this));
		this.setScreen(new BodGameScreen(this));
	}

	@Override
	public void render () {
	    super.render();
	}
	
	@Override
	public void dispose () {
	    super.dispose();
	}
}
