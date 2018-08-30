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

    int magicPoints;

    String name;

    Combatant target;

    List<Attack> attacks = new ArrayList<>();

    List<Spell> spells = new ArrayList<>();

    Attack activeAttack;

    Attack activeParry;

    public Combatant() {

    }

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

    public void healDamage(int healed) {
        this.hp += healed;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }

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
            return String.format("%s hit with %s, but armor blocked damage!", name, getActiveAttack().getWeaponName());
        } else {
            return String.format("%s hit with %s for %d damage!", name, getActiveAttack().getWeaponName(), finalDamage);
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
                message = String.format("%s attacked with %s and misses!", name, getActiveAttack().getWeaponName());
                break;
            case parried:
                message = String.format("%s hits with %s but is parried by %s!", name, getActiveAttack().getWeaponName(), target.getActiveParry().getWeaponName());
                break;
            case critical_fumble:
                message = String.format("%s attacks with %s and fumbles!", name, getActiveAttack().getWeaponName());
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
        addAttack(name, skill, damageDie, damageBonus, true, true);
    }

    public void addAttack(String name, int skill, int damageDie, int damageBonus, boolean activeAttack, boolean activeParry) {
        Attack attack = new Attack(name, skill, damageDie, damageBonus);
        attacks.add(attack);
        if (activeAttack) {
            this.activeAttack = attack;
        }

        if (activeParry) {
            this.activeParry = attack;
        }
    }

    public void addAttack(String name, int damageDie) {
        attacks.add(new Attack(name, level, damageDie, 0));
    }

    public Attack getActiveAttack() {
        if (activeAttack == null) {
            return attacks.get(0);
        } else {
            return activeAttack;
        }
    }

    public Attack getActiveParry() {
        if (activeParry == null) {
            return attacks.get(0);
        } else {
            return activeParry;
        }
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

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getMagicPoints() {
        return magicPoints;
    }

    public void setMagicPoints(int magicPoints) {
        this.magicPoints = magicPoints;
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public void addSpell(String name, int mpCost, int skillLevel) {
        Spell spell = new Spell(name, mpCost, skillLevel);
        spells.add(spell);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Combatant getTarget() {
        return target;
    }

    public void setTarget(Combatant target) {
        this.target = target;
    }
}
