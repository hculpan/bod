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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    String mapName;

    public BodGameScreen(Game game, String mapName, List<Combatant> players) {
        init(game, mapName, players);
    }

    private void init(Game game, String mapName, List<Combatant> players) {
        this.game = game;
        this.mapName = mapName;

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

        tiledMap = new TmxMapLoader().load(mapName);

        Object startx = tiledMap.getProperties().get("entry-x");
        Object starty = tiledMap.getProperties().get("entry-y");
        int charX = Integer.parseInt(startx.toString());
        int charY = Integer.parseInt(starty.toString());

        gameMaster = new GameMaster(this);
        players.forEach(p -> {
            p.setX(charX);
            p.setY(charY);
            p.setGameState(this);
            gameMaster.addCombatants(p);
        });
        loadNpcs();

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1.5f);
        Gdx.input.setInputProcessor(this);
    }

    public BodGameScreen(Game game) {
        Player p = new Player( this, "Ardelson Bitemunch");
        p.setLevel(1);
        p.setMagicPoints(10);
        p.addAttack("Long sword", 60, 8, 1, true, false);
        p.addAttack("Dagger", 40, 4, 0, false, false);
        p.addAttack( "Shield", 50, 3, 0, false, true);
        p.addSpell("Minor Heal", 1, 40);
        p.addSpell("Bladesharp", 1, 30);
        List<Combatant> players = new ArrayList<>();
        players.add(p);

        init(game, "maps/first-room.tmx", players);
    }

    private void loadNpcs() {
        TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get("Monsters");
        int dimx = layer.getWidth();
        int dimy = layer.getHeight();
        for (int i = 0; i < dimx; i++) {
            for (int j = 0; j < dimy; j++) {
                TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                if (cell != null) {
                    Object prop = cell.getTile().getProperties().get("monster-name");
                    if (prop != null && prop instanceof String) {
                        Npc npc = NpcFactory.createNpc(prop.toString(), this);
                        npc.setX(i);
                        npc.setY(j);
                        gameMaster.addCombatants(npc);
                    }
                }
            }
        }
    }

    private void loadFonts() {
        BitmapFont bitmapFont;

        bitmapFont = FontManager.addFontFromFile("fonts/immortal.ttf", 32, FontManager.IMMORTAL_32);
        bitmapFont.setColor(Color.WHITE);
        bitmapFont = FontManager.addFontFromFile("fonts/immortal.ttf", 20, FontManager.IMMORTAL_20);
        bitmapFont.setColor(Color.WHITE);
        bitmapFont = FontManager.addFontFromFile("fonts/immortal.ttf", 14, FontManager.IMMORTAL_14);
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
    public boolean exitAt(int x, int y) {
        boolean result = false;

        TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get("Exits");
        TiledMapTileLayer.Cell cell = layer.getCell(x, y);
        if (cell != null) {
            Object prop = cell.getTile().getProperties().get("door");
            if (prop != null && prop instanceof Boolean) {
                result = ((Boolean)prop).booleanValue();
            }
        }

        return result;
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
    public void addMessage(String message) {
        gameMaster.addMessage(message);
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

    private void handleGmAction() {
        gameMaster.act();
        if (gameMaster.allPlayersDead()) {
            game.setScreen(new PartyDeadScreen(game));
        }
    }

    @Override
    public Combatant getCombatantAt(int x, int y) {
        List<Combatant> combatants = gameMaster.getCombatants().stream().filter(c -> c.getX() == x && c.getY() == y).collect(Collectors.toList());
        if (combatants.size() == 1) {
            return combatants.get(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        Combatant c = gameMaster.getActivePlayer();
        if (gameMaster.isCastingSpell()) {
            if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.S) {
                gameMaster.cancelSpell();
            } else if ((keycode >= Input.Keys.NUM_0) && (keycode <= Input.Keys.NUM_9)) {
                if (gameMaster.castSpell(keycode - Input.Keys.NUM_0)) {
                    handleGmAction();
                }
            }
        } else {
            if (keycode == Input.Keys.UP && gameMaster.canAct() && c.moveTo(c.getX(), c.getY() + 2)) {
                handleGmAction();
            } else if (keycode == Input.Keys.DOWN && gameMaster.canAct() && c.moveTo(c.getX(), c.getY() - 2)) {
                handleGmAction();
            } else if (keycode == Input.Keys.LEFT && gameMaster.canAct() && c.moveTo(c.getX() - 2, c.getY())) {
                handleGmAction();
            } else if (keycode == Input.Keys.RIGHT && gameMaster.canAct() && c.moveTo(c.getX() + 2, c.getY())) {
                handleGmAction();
            } else if (keycode == Input.Keys.P && gameMaster.canAct()) {
                handleGmAction();
            } else if (keycode == Input.Keys.ESCAPE) {
                game.setScreen(new PauseGameScreen(game, this));
            } else if (keycode == Input.Keys.E && exitAt(c.getX(), c.getY())) {
                exitRoom();
            } else if (keycode == Input.Keys.S && gameMaster.canAct()) {
                gameMaster.castSpell();
            }
        }

        return false;
    }

    private void exitRoom() {
        game.setScreen(new BodGameScreen(game, "maps/corridor1.tmx", gameMaster.getPlayers()));
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
