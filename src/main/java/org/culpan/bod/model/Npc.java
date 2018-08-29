package org.culpan.bod.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import org.culpan.bod.FontManager;
import org.culpan.bod.Utils;

public class Npc extends Combatant {
    String name;

    Combatant target;

    BitmapFontCache bitmapFontCache20;

    BitmapFontCache bitmapFontCache32;

    public Npc(int x, int y, GameState collisionChecker, String name) {
        super(x, y, collisionChecker);
        this.name = name;
        this.hp = 10;

        Texture characterTexture = new Texture(Gdx.files.internal("dungeon/0x72_16x16DungeonTileset.v4.png"));
        sprite = new Sprite(characterTexture, 16, 160, 16, 16);
        sprite.setScale(3);
        bitmapFontCache20 = new BitmapFontCache(FontManager.getFont(FontManager.IMMORTAL_20));
//        bitmapFontCache20.addText("Level:", 40, Gdx.graphics.getHeight() - 45);
//        bitmapFontCache20.addText("XP:", 200, Gdx.graphics.getHeight() - 45);

        bitmapFontCache32 = new BitmapFontCache(FontManager.getFont(FontManager.IMMORTAL_32));
//        bitmapFontCache32.addText(name, 10, Gdx.graphics.getHeight() - 10);
    }

    @Override
    public void renderSprite(Batch batch) {
        positionOnTileMap(sprite);
        sprite.draw(batch);
    }

    @Override
    public void act() {
        if (target == null) {
            target = collisionChecker.getNearestPlayerTo(getX(), getX());
        }

        if (collisionChecker.isPlayerAt(x, y + 2)) {
            target.takeDamage(Utils.random.nextInt(8) + 1);
        } else if (collisionChecker.isPlayerAt(x, y - 2)) {
            target.takeDamage(Utils.random.nextInt(8) + 1);
        } else if (collisionChecker.isPlayerAt(x - 2, y)) {
            target.takeDamage(Utils.random.nextInt(8) + 1);
        } else if (collisionChecker.isPlayerAt(x + 2, y)) {
            target.takeDamage(Utils.random.nextInt(8) + 1);
        } else {
            moveToTarget();
        }
    }

    private void moveToTarget() {
        double lowestDistance = target.distanceTo(this.getX(), this.getY());
        int moveX = 0;
        int moveY = 0;

        double distance;
        if (collisionChecker.canMoveTo(x, y + 2)) {
            distance = target.distanceTo(x, y + 2);
            if (distance < lowestDistance) {
                moveX = 0;
                moveY = 2;
                lowestDistance = distance;
            }
        }

        if (collisionChecker.canMoveTo(x, y - 2)) {
            distance = target.distanceTo(x, y - 2);
            if (distance < lowestDistance) {
                moveX = 0;
                moveY = -2;
                lowestDistance = distance;
            }
        }

        if (collisionChecker.canMoveTo(x - 2, y)) {
            distance = target.distanceTo(x - 2, y);
            if (distance < lowestDistance) {
                moveX = -2;
                moveY = 0;
                lowestDistance = distance;
            }
        }

        if (collisionChecker.canMoveTo(x + 2, y)) {
            distance = target.distanceTo(x + 2, y);
            if (distance < lowestDistance) {
                moveX = 2;
                moveY = 0;
            }
        }

        this.x = this.x + moveX;
        this.y = this.y + moveY;
    }

    @Override
    public void renderInfo(Batch batch) {
        BitmapFont font = FontManager.getFont(FontManager.IMMORTAL_20);
        bitmapFontCache32.draw(batch);
        bitmapFontCache20.draw(batch);
//        font.draw(batch, Integer.toString(level), 105, Gdx.graphics.getHeight() - 45);
//        font.draw(batch, Integer.toString(xp),  245, Gdx.graphics.getHeight() - 45);

        FontManager.getFont(FontManager.IMMORTAL_20).draw(batch, String.format("HP: %d", hp), 40, Gdx.graphics.getHeight() - 72);
    }
}
