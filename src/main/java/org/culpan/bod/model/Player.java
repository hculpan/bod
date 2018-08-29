package org.culpan.bod.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import org.culpan.bod.FontManager;

public class Player extends Combatant {
    int xp = 0;

    int level = 1;

    String name;

    BitmapFontCache bitmapFontCache20;

    BitmapFontCache bitmapFontCache32;

    public Player(int x, int y, GameState collisionChecker, String name) {
        super(x, y, collisionChecker);
        this.name = name;
        this.hp = 10;

        Texture characterTexture = new Texture(Gdx.files.internal("dungeon/0x72_16x16DungeonTileset.v4.png"));
        sprite = new Sprite(characterTexture, 128, 240, 16, 16);
        sprite.setScale(3);
        bitmapFontCache20 = new BitmapFontCache(FontManager.getFont(FontManager.IMMORTAL_20));
        bitmapFontCache20.addText("Level:", 40, Gdx.graphics.getHeight() - 45);
        bitmapFontCache20.addText("XP:", 200, Gdx.graphics.getHeight() - 45);

        bitmapFontCache32 = new BitmapFontCache(FontManager.getFont(FontManager.IMMORTAL_32));
        bitmapFontCache32.addText(name, 10, Gdx.graphics.getHeight() - 10);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public void act() {

    }

    @Override
    public void renderSprite(Batch batch) {
        positionOnTileMap(sprite);
        sprite.draw(batch);
    }

    public String findValidMoves() {
        StringBuilder stringBuilder = new StringBuilder();

        if (collisionChecker.canMoveTo(x, y + 2)) {
            stringBuilder.append("Up ");
        }
        if (collisionChecker.canMoveTo(x, y - 2)) {
            stringBuilder.append("Down ");
        }
        if (collisionChecker.canMoveTo(x - 2, y)) {
            stringBuilder.append("Left ");
        }
        if (collisionChecker.canMoveTo(x + 2, y)) {
            stringBuilder.append("Right ");
        }

        return stringBuilder.toString();
    }

    public String findValidAttacks() {
        StringBuilder stringBuilder = new StringBuilder();

        if (collisionChecker.isNpcAt(x, y + 2)) {
            stringBuilder.append("Up ");
        }
        if (collisionChecker.isNpcAt(x, y - 2)) {
            stringBuilder.append("Down ");
        }
        if (collisionChecker.isNpcAt(x - 2, y)) {
            stringBuilder.append("Left ");
        }
        if (collisionChecker.isNpcAt(x + 2, y)) {
            stringBuilder.append("Right ");
        }

        return stringBuilder.toString();
    }

    @Override
    public void renderInfo(Batch batch) {
        BitmapFont font = FontManager.getFont(FontManager.IMMORTAL_20);
        bitmapFontCache32.draw(batch);
        bitmapFontCache20.draw(batch);
        font.draw(batch, Integer.toString(level), 105, Gdx.graphics.getHeight() - 45);
        font.draw(batch, Integer.toString(xp),  245, Gdx.graphics.getHeight() - 45);

        FontManager.getFont(FontManager.IMMORTAL_20).draw(batch, String.format("HP: %d", hp), 40, Gdx.graphics.getHeight() - 72);
    }
}
