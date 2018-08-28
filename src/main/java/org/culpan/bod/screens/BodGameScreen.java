 package org.culpan.bod.screens;

 import com.badlogic.gdx.*;
 import com.badlogic.gdx.graphics.Color;
 import com.badlogic.gdx.graphics.GL20;
 import com.badlogic.gdx.graphics.OrthographicCamera;
 import com.badlogic.gdx.graphics.Texture;
 import com.badlogic.gdx.graphics.g2d.BitmapFont;
 import com.badlogic.gdx.graphics.g2d.Sprite;
 import com.badlogic.gdx.graphics.g2d.SpriteBatch;
 import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
 import com.badlogic.gdx.maps.tiled.TiledMap;
 import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
 import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
 import com.badlogic.gdx.maps.tiled.TmxMapLoader;
 import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
 import org.culpan.bod.FontManager;
 import org.culpan.bod.GameMaster;

 public class BodGameScreen implements Screen, InputProcessor {
     Game game;

     OrthographicCamera camera;

     TiledMap tiledMap;

     TiledMapRenderer tiledMapRenderer;

     ShapeRenderer shapeRenderer;

     Texture characterTexture;

     SpriteBatch mapBatch;

     SpriteBatch hudBatch;

     Sprite character;

     int charX = 0;

     int charY = 0;

     GameMaster gameMaster;

     BitmapFont bitmapFont;

     public BodGameScreen(Game game) {
         this.game = game;

         gameMaster = new GameMaster();

         BitmapFont bitmapFont = FontManager.addFontFromFile("fonts/immortal.ttf", 32, FontManager.IMMORTAL_32);
         bitmapFont.setColor(Color.WHITE);

         mapBatch = new SpriteBatch();
         hudBatch = new SpriteBatch();

         shapeRenderer = new ShapeRenderer();

         characterTexture = new Texture(Gdx.files.internal("dungeon/0x72_16x16DungeonTileset.v4.png"));
         character = new Sprite(characterTexture, 128, 240, 16, 16);
         character.setScale(3);

         float w = Gdx.graphics.getWidth();
         float h = Gdx.graphics.getHeight();

         camera = new OrthographicCamera();
         camera.setToOrtho(false, w, h);
         camera.position.set(70, 360, 0);
         camera.update();

         tiledMap = new TmxMapLoader().load("maps/first-room.tmx");

         Object startx = tiledMap.getProperties().get("entry-x");
         Object starty = tiledMap.getProperties().get("entry-y");
         charX = Integer.parseInt(startx.toString());
         charY = Integer.parseInt(starty.toString());
         positionOnTileMap(character, charX, charY);

         tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1.5f);
         Gdx.input.setInputProcessor(this);
     }

     @Override
     public void show() {

     }

     protected boolean canMoveTo(int x, int y) {
         TiledMapTileLayer layer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
         TiledMapTileLayer.Cell cell = layer.getCell(x, y);
         if (cell != null) {
             Object prop = cell.getTile().getProperties().get("floor");
             if (prop != null && prop instanceof Boolean) {
                 return ((Boolean)prop).booleanValue();
             }
         }
         return false;
     }

     protected void positionOnTileMap(Sprite sprite, int x, int y) {
         sprite.setPosition(24 * x + 16, 24 * y + 16);
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
         character.draw(mapBatch);
         mapBatch.end();

         hudBatch.begin();
         FontManager.getFont(FontManager.IMMORTAL_32).draw(hudBatch, "Adrelson Bitemunch", 10, Gdx.graphics.getHeight() - 10);
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
         if (keycode == Input.Keys.UP) {
             if (canMoveTo(charX, charY + 2)) {
                 charY += 2;
                 positionOnTileMap(character, charX, charY);
             }
         } else if (keycode == Input.Keys.DOWN) {
             if (canMoveTo(charX, charY - 2)) {
                 charY -= 2;
                 positionOnTileMap(character, charX, charY);
             }
         } else if (keycode == Input.Keys.LEFT) {
             if (canMoveTo(charX - 2, charY)) {
                 charX -= 2;
                 positionOnTileMap(character, charX, charY);
             }
         } else if (keycode == Input.Keys.RIGHT) {
             if (canMoveTo(charX + 2, charY)) {
                 charX += 2;
                 positionOnTileMap(character, charX, charY);
             }
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
