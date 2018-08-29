package org.culpan.bod.model;

import org.culpan.bod.Utils;

public class Attack {
    public enum AttackResult {  critcal_hit, hit, parried, miss, critical_fumble }

    String weaponName;

    String damage;

    int damageDie;

    int damageBonus;

    int toHit;

    int encumbrance;

    public Attack(String weaponName, int toHit, int damageDie, int damageBonus, int encumbrance) {
        this.weaponName = weaponName;
        this.toHit = toHit;
        this.damageDie = damageDie;
        this.damageBonus = damageBonus;
        this.encumbrance = encumbrance;
        this.damage = getDamageAsString();
    }

    public Attack(String weaponName, int toHit, int damageDie, int damageBonus) {
        this.weaponName = weaponName;
        this.toHit = toHit;
        this.damageDie = damageDie;
        this.damageBonus = damageBonus;
        this.encumbrance = 0;
        this.damage = getDamageAsString();
    }

    public String getDamageAsString() {
        return "1d" + Integer.toString(damageDie) + (damageBonus == 0 ? "" : String.format("%+d", damageBonus));
    }

    public AttackResult makeAttack(Combatant target) {
        Utils.SuccessLevel attackResult = Utils.rollSkill(toHit);
        if (Utils.isSuccess(attackResult)) {
            Utils.SuccessLevel parryResult = Utils.rollSkill(target.getActiveParry().toHit);
            if (attackResult == Utils.SuccessLevel.success && Utils.isSuccess(parryResult)) {
                return AttackResult.parried;
            } else if (attackResult == Utils.SuccessLevel.critical_success && parryResult == Utils.SuccessLevel.critical_success) {
                return AttackResult.parried;
            } else {
                return AttackResult.hit;
            }
        } else if (attackResult == Utils.SuccessLevel.critical_failure) {
            return AttackResult.critical_fumble;
        }

        return AttackResult.miss;
    }

    public int damage() {
        int damage = Utils.random.nextInt(damageDie) + 1 + damageBonus;
        return (damage < 0 ? 0 : damage);
    }

    public String getWeaponName() {
        return weaponName;
    }

    public void setWeaponName(String weaponName) {
        this.weaponName = weaponName;
    }

    public int getDamageDie() {
        return damageDie;
    }

    public void setDamageDie(int damageDie) {
        this.damageDie = damageDie;
    }

    public int getDamageBonus() {
        return damageBonus;
    }

    public void setDamageBonus(int damageBonus) {
        this.damageBonus = damageBonus;
    }

    public int getToHit() {
        return toHit;
    }

    public void setToHit(int toHit) {
        this.toHit = toHit;
    }

    public int getEncumbrance() {
        return encumbrance;
    }

    public void setEncumbrance(int encumbrance) {
        this.encumbrance = encumbrance;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }
}
