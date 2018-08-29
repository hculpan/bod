package org.culpan.bod.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import org.culpan.bod.Utils;

public abstract class Combatant {
    GameState collisionChecker;

    int x;

    int y;

    Sprite sprite;

    int hp;

    public Combatant(int x, int y, GameState collisionChecker) {
        this.x = x;
        this.y = y;
        this.collisionChecker = collisionChecker;
    }

    public boolean isPlayer() {
        return false;
    }

    public abstract void renderSprite(Batch batch);

    public abstract void renderInfo(Batch batch);

    public abstract void act();

    public void takeDamage(int damage) {
        this.hp -= damage;
    }

    public double distanceTo(int x, int y) {
        return Utils.distance(this.x, this.y, x, y);
    }

    public boolean moveTo(int newX, int newY) {
        if (collisionChecker.canMoveTo(newX, newY)) {
            this.x = newX;
            this.y = newY;
            return true;
        } else {
            return false;
        }
    }

    public Rectangle getBoundingRectangle() {
        Rectangle result = new Rectangle();
        result.x = x;
        result.y = y;
        result.width = 2;
        result.height = 2;

        return result;
    }

    protected void positionOnTileMap(Sprite sprite) {
        sprite.setPosition(24 * x + 16, 24 * y + 16);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
