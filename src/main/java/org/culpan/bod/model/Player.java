package org.culpan.bod.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.culpan.bod.FontManager;

public class Player extends Combatant {
    int xp = 0;

    BitmapFontCache bitmapFontCache20;

    BitmapFontCache bitmapFontCache32;

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public Player(GameState gameState, String name) {
        this(0, 0, gameState, name);
    }

    public Player(int x, int y, GameState gameState, String name) {
        super(x, y, gameState);
        this.name = name;
        this.hp = 10;
        this.maxHp = 10;
        this.ac = 3;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public void act() {

    }

    public void addXp(int xp) {
        this.xp += xp;
    }

    @Override
    public void renderSprite(Batch batch) {
        if (sprite == null) {
            Texture characterTexture = new Texture(Gdx.files.internal("dungeon/0x72_16x16DungeonTileset.v4.png"));
            sprite = new Sprite(characterTexture, 128, 240, 16, 16);
            sprite.setScale(3);
            bitmapFontCache20 = new BitmapFontCache(FontManager.getFont(FontManager.IMMORTAL_20));
            bitmapFontCache20.addText("Level:", 40, Gdx.graphics.getHeight() - 45);
            bitmapFontCache20.addText("XP:", 200, Gdx.graphics.getHeight() - 45);
            bitmapFontCache20.addText("Armor Points:", 200, Gdx.graphics.getHeight() - 72);
            bitmapFontCache20.addText("Weapons:", 40, Gdx.graphics.getHeight() - 99);

            bitmapFontCache32 = new BitmapFontCache(FontManager.getFont(FontManager.IMMORTAL_32));
            bitmapFontCache32.addText(name, 10, Gdx.graphics.getHeight() - 10);
        }

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

    public String findValidMoves() {
        StringBuilder stringBuilder = new StringBuilder();

        if (gameState.canMoveTo(x, y + 2)) {
            stringBuilder.append("Up ");
        }
        if (gameState.canMoveTo(x, y - 2)) {
            stringBuilder.append("Down ");
        }
        if (gameState.canMoveTo(x - 2, y)) {
            stringBuilder.append("Left ");
        }
        if (gameState.canMoveTo(x + 2, y)) {
            stringBuilder.append("Right ");
        }

        return stringBuilder.toString();
    }

    public String findValidAttacks() {
        StringBuilder stringBuilder = new StringBuilder();

        if (gameState.isNpcAt(x, y + 2)) {
            stringBuilder.append("Up ");
        }
        if (gameState.isNpcAt(x, y - 2)) {
            stringBuilder.append("Down ");
        }
        if (gameState.isNpcAt(x - 2, y)) {
            stringBuilder.append("Left ");
        }
        if (gameState.isNpcAt(x + 2, y)) {
            stringBuilder.append("Right ");
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean moveTo(int newX, int newY) {
        Combatant c = gameState.getCombatantAt(newX, newY);
        if (c != null && !c.isPlayer()) {
            target = c;
            attackTarget();
            return true;
        }

        if (gameState.canMoveTo(newX, newY)) {
            this.x = newX;
            this.y = newY;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void renderInfo(Batch batch) {
        BitmapFont font = FontManager.getFont(FontManager.IMMORTAL_20);
        bitmapFontCache32.draw(batch);
        bitmapFontCache20.draw(batch);
        font.draw(batch, Integer.toString(level), 105, Gdx.graphics.getHeight() - 45);
        font.draw(batch, Integer.toString(xp),  245, Gdx.graphics.getHeight() - 45);

        FontManager.getFont(FontManager.IMMORTAL_20).draw(batch, String.format("HP: %d / %d", hp, maxHp), 40, Gdx.graphics.getHeight() - 72);
        font.draw(batch, Integer.toString(ac),  345, Gdx.graphics.getHeight() - 72);

        int currentY = Gdx.graphics.getHeight() - 99;
        for (int i = 0; i < (attacks.size() > 3 ? 3 : attacks.size()); i++) {
            Attack attack = attacks.get(i);
            font.draw(batch, attack.getWeaponName(), 150, currentY);
            font.draw(batch, String.format("%d%%", attack.toHit), 290, currentY);
            font.draw(batch, attack.getDamage(), 380, currentY);
            if (attack == getActiveParry() && attack == getActiveAttack()) {
                font.draw(batch, "(a+p)", 445, currentY);
            } else if (attack == getActiveAttack()) {
                font.draw(batch, "(a)", 445, currentY);
            } else if (attack == getActiveParry()) {
                font.draw(batch, "(p)", 445, currentY);
            }
            currentY = currentY - 22;
        }

        if (spells.size() > 0) {
            font.draw(batch, "Spells:", 40, currentY);
            font.draw(batch, String.format("Magic Points: %d", getMagicPoints()), 200, currentY);
            currentY = currentY - 22;
            for (int i = 0; i < spells.size(); i++) {
                Spell spell = spells.get(i);
                font.draw(batch, String.format("%s (%d mp, %d%%)", spell.getName(), spell.getMpCost(), spell.getSkillLevel()), 100, currentY);
                currentY = currentY - 22;
            }
        }
    }
}
