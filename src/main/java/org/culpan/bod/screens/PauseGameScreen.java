package org.culpan.bod.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.culpan.bod.BodGame;

public class PauseGameScreen implements Screen {
    private Stage stage;
    private Game game;
    private Screen callingScreen;

    public PauseGameScreen(Game aGame, Screen callingScreen) {
        this.game = aGame;
        this.stage = new Stage(new ScreenViewport());
        this.callingScreen = callingScreen;

        Label title = new Label("Bunch O' Dungeons!", BodGame.skin,"title");
        title.setAlignment(Align.center);
        title.setY(Gdx.graphics.getHeight()*2/3);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);

        TextButton quitButton = new TextButton("Quit",BodGame.skin);
        quitButton.setWidth(Gdx.graphics.getWidth()/4);
        quitButton.setPosition(Gdx.graphics.getWidth() * 0.25f - quitButton.getWidth()/2,Gdx.graphics.getHeight()/2-quitButton.getHeight()/2);
        quitButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(quitButton);

        TextButton playButton = new TextButton("Resume",BodGame.skin);
        playButton.setWidth(Gdx.graphics.getWidth()/4);
        playButton.setPosition(Gdx.graphics.getWidth() * .75f - playButton.getWidth()/2,Gdx.graphics.getHeight()/2 - playButton.getHeight()/2);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (callingScreen instanceof InputProcessor) {
                    Gdx.input.setInputProcessor((InputProcessor)callingScreen);
                }
                game.setScreen(callingScreen);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(playButton);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BodGame.SCREEN_BACKGROUND.r, BodGame.SCREEN_BACKGROUND.g, BodGame.SCREEN_BACKGROUND.b, BodGame.SCREEN_BACKGROUND.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
