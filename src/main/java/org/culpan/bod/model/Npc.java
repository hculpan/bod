package org.culpan.bod.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.culpan.bod.FontManager;

public class Npc extends Combatant {
    BitmapFontCache bitmapFontCache20;

    BitmapFontCache bitmapFontCache32;

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public Npc(String name) {
        this.name = name;
        bitmapFontCache20 = new BitmapFontCache(FontManager.getFont(FontManager.IMMORTAL_20));
        bitmapFontCache32 = new BitmapFontCache(FontManager.getFont(FontManager.IMMORTAL_32));
    }

    public Npc(int x, int y, GameState gameState, int level, String name) {
        super(x, y, gameState);
        this.name = name;
        this.hp = 10;
        this.maxHp = 10;
        this.level = level;

        bitmapFontCache20 = new BitmapFontCache(FontManager.getFont(FontManager.IMMORTAL_20));
        bitmapFontCache32 = new BitmapFontCache(FontManager.getFont(FontManager.IMMORTAL_32));
    }

    @Override
    public void renderSprite(Batch batch) {
        positionOnTileMap(sprite);
        sprite.draw(batch);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.0f, 0.9f, 0.1f, 1f);
        long length = Math.round((sprite.getWidth() * 3 - 5) * (double)hp / (double)maxHp);
        shapeRenderer.rect(sprite.getX() + 593, sprite.getY() + 6, length,  4);
        shapeRenderer.end();

        batch.begin();
    }

    @Override
    public void act() {
        if (target == null) {
            target = gameState.getNearestPlayerTo(getX(), getX());
        }

        if (gameState.isPlayerAt(x, y + 2)) {
            attackTarget();
        } else if (gameState.isPlayerAt(x, y - 2)) {
            attackTarget();
        } else if (gameState.isPlayerAt(x - 2, y)) {
            attackTarget();
        } else if (gameState.isPlayerAt(x + 2, y)) {
            attackTarget();
        } else {
            moveToTarget();
        }
    }

    private void moveToTarget() {
        double lowestDistance = target.distanceTo(this.getX(), this.getY());
        int moveX = 0;
        int moveY = 0;

        double distance;
        if (gameState.canMoveTo(x, y + 2)) {
            distance = target.distanceTo(x, y + 2);
            if (distance < lowestDistance) {
                moveX = 0;
                moveY = 2;
                lowestDistance = distance;
            }
        }

        if (gameState.canMoveTo(x, y - 2)) {
            distance = target.distanceTo(x, y - 2);
            if (distance < lowestDistance) {
                moveX = 0;
                moveY = -2;
                lowestDistance = distance;
            }
        }

        if (gameState.canMoveTo(x - 2, y)) {
            distance = target.distanceTo(x - 2, y);
            if (distance < lowestDistance) {
                moveX = -2;
                moveY = 0;
                lowestDistance = distance;
            }
        }

        if (gameState.canMoveTo(x + 2, y)) {
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
