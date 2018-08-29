package org.culpan.bod.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import org.culpan.bod.Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class Combatant {
    GameState gameState;

    int level;

    int x;

    int y;

    Sprite sprite;

    int maxHp;

    int hp;

    int ac = 10;

    String name;

    Combatant target;

    List<Attack> attacks = new ArrayList<>();

    public Combatant(int x, int y, GameState gameState) {
        this.x = x;
        this.y = y;
        this.gameState = gameState;
    }

    public boolean isPlayer() {
        return false;
    }

    public abstract void renderSprite(Batch batch);

    public abstract void renderInfo(Batch batch);

    public abstract void act();

    public int takeDamage(int damage) {
        if (ac >= damage) {
            return 0;
        } else {
            int value = damage - ac;
            this.hp -= value;
            return value;
        }
    }

    public double distanceTo(int x, int y) {
        return Utils.distance(this.x, this.y, x, y);
    }

    private String doDamage(Combatant target, int damage) {
        int finalDamage;
        finalDamage = target.takeDamage(damage);
        if (finalDamage == 0) {
            return String.format("%s attacks and hits, but armor blocks all damage!", name);
        } else {
            return String.format("%s attacks and hits for %d damage!", name, finalDamage);
        }
    }

    protected void attackTarget() {
        String message = "";
        int damage, damage1, damage2, finalDamage;

        Attack attack = getActiveAttack();
        Attack.AttackResult attackResult = attack.makeAttack(target);
        switch (attackResult) {
            case critcal_hit:
                damage1 = attack.damage();
                damage2 = attack.damage();
                damage = (damage1 > damage2 ? damage1 : damage2);
                message = doDamage(target, damage);
                break;
            case hit:
                damage = attack.damage();
                message = doDamage(target, damage);
                break;
            case miss:
                message = String.format("%s attacks and misses!", name);
                break;
            case parried:
                message = String.format("%s attacks and hits but is parried!", name);
                break;
            case critical_fumble:
                message = String.format("%s attacks and fumbles!", name);
                break;
        }

        gameState.addMessage(message);
    }

    public boolean moveTo(int newX, int newY) {
        if (gameState.canMoveTo(newX, newY)) {
            this.x = newX;
            this.y = newY;
            return true;
        } else {
            return false;
        }
    }

    public int getDamage() {
        return attacks.get(0).damage();
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

    public int getAc() {
        return ac;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<Attack> attacks) {
        this.attacks = attacks;
    }

    public void addAttack(String name, int skill, int damageDie, int damageBonus) {
        attacks.add(new Attack(name, skill, damageDie, damageBonus));
    }

    public void addAttack(String name, int damageDie) {
        attacks.add(new Attack(name, level, damageDie, 0));
    }

    public Attack getActiveAttack() {
        return attacks.get(0);
    }

    public Attack getActiveParry() {
        return attacks.get(0);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }
}
