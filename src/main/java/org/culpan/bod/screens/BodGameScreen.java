package org.culpan.bod.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import org.culpan.bod.FontManager;
import org.culpan.bod.Utils;
import org.culpan.bod.model.*;

public class BodGameScreen implements Screen, InputProcessor, GameState {
    Game game;

    OrthographicCamera camera;

    TiledMap tiledMap;

    TiledMapRenderer tiledMapRenderer;

    ShapeRenderer shapeRenderer;

    SpriteBatch mapBatch;

    SpriteBatch hudBatch;

    GameMaster gameMaster;

    BitmapFont bitmapFont;

    public BodGameScreen(Game game) {
        this.game = game;

        loadFonts();

        mapBatch = new SpriteBatch();
        hudBatch = new SpriteBatch();

        shapeRenderer = new ShapeRenderer();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.position.set(70, 360, 0);
        camera.update();

        tiledMap = new TmxMapLoader().load("maps/first-room.tmx");

        Object startx = tiledMap.getProperties().get("entry-x");
        Object starty = tiledMap.getProperties().get("entry-y");
        int charX = Integer.parseInt(startx.toString());
        int charY = Integer.parseInt(starty.toString());

        gameMaster = new GameMaster();
        gameMaster.addCombatants(new Player(charX, charY, this, "Ardelson Bitemunch"));
        gameMaster.addCombatants(new Npc(11, 20, this, "Goblin Jason"));

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1.5f);
        Gdx.input.setInputProcessor(this);
    }

    private void loadFonts() {
        BitmapFont bitmapFont;

        bitmapFont = FontManager.addFontFromFile("fonts/immortal.ttf", 32, FontManager.IMMORTAL_32);
        bitmapFont.setColor(Color.WHITE);
        bitmapFont = FontManager.addFontFromFile("fonts/immortal.ttf", 20, FontManager.IMMORTAL_20);
        bitmapFont.setColor(Color.WHITE);
    }

    @Override
    public void show() {

    }

    @Override
    public Combatant getNearestPlayerTo(int x, int y) {
        double lowestDistance = Double.MAX_VALUE;
        Combatant result = null;
        for (Combatant c : gameMaster.getPlayers()) {
            double distance = Utils.distance(x, y, c.getX(), c.getY());
            if (distance < lowestDistance) {
                lowestDistance = distance;
                result = c;
            }
        }

        return result;
    }

    @Override
    public boolean isNpcAt(int x, int y) {
        return gameMaster.getCombatants().stream().filter( c ->  !c.isPlayer() && c.getY() == y && c.getX() == x ).count() > 0;
    }

    @Override
    public boolean isPlayerAt(int x, int y) {
        return gameMaster.getCombatants().stream().filter( c ->  c.isPlayer() && c.getY() == y && c.getX() == x ).count() > 0;
    }

    @Override
    public boolean canMoveTo(int x, int y) {
        boolean result = false;

        TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        TiledMapTileLayer.Cell cell = layer.getCell(x, y);
        if (cell != null) {
            Object prop = cell.getTile().getProperties().get("floor");
            if (prop != null && prop instanceof Boolean) {
                result = ((Boolean)prop).booleanValue();
            }
        }

        for (Combatant c : gameMaster.getCombatants()) {
            if (c.getX() == x && c.getY() == y) {
                result = false;
            }
        }

        return result;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0.5f, 0.1f, 0.5f);
        shapeRenderer.rect(0, 0, camera.viewportWidth - 633, camera.viewportHeight - 40);
        shapeRenderer.end();

        mapBatch.setProjectionMatrix(camera.combined);
        mapBatch.begin();
        gameMaster.getCombatants().forEach( c -> {
            c.renderSprite(mapBatch);
        });
        mapBatch.end();

        hudBatch.begin();
        gameMaster.render(hudBatch);
        Combatant c = gameMaster.getActivePlayer();
        c.renderInfo(hudBatch);
        hudBatch.end();
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
    public void dispose () {
        bitmapFont.dispose();
        FontManager.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        Combatant c = gameMaster.getActivePlayer();
        if (keycode == Input.Keys.UP && gameMaster.canAct() && c.moveTo(c.getX(), c.getY() + 2)) {
            gameMaster.act();
        } else if (keycode == Input.Keys.DOWN && gameMaster.canAct() && c.moveTo(c.getX(), c.getY() - 2)) {
            gameMaster.act();
        } else if (keycode == Input.Keys.LEFT && gameMaster.canAct() && c.moveTo(c.getX() - 2, c.getY())) {
            gameMaster.act();
        } else if (keycode == Input.Keys.RIGHT && gameMaster.canAct() && c.moveTo(c.getX() + 2, c.getY())) {
            gameMaster.act();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
