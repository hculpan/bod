package org.culpan.bod.model;

import org.culpan.bod.Utils;

public class Spell {
    String name;

    int mpCost;

    int skillLevel;

    public Spell() {

    }

    public Spell(String name, int mpCost, int skillLevel) {
        this.name = name;
        this.mpCost = mpCost;
        this.skillLevel = skillLevel;
    }

    public void castSpell(GameState gameState, Combatant caster, Combatant target) {
        if (name.equals("Minor Heal")) {
            String message;
            Utils.SuccessLevel successLevel = Utils.rollSkill(skillLevel);
            if (successLevel != Utils.SuccessLevel.critical_success) {
                caster.setMagicPoints(caster.getMagicPoints() - 1);
            } else if (successLevel == Utils.SuccessLevel.critical_failure) {
                caster.setMagicPoints(caster.getMagicPoints() - 2);
            }

            if (Utils.isSuccess(successLevel)) {
                int healed = Utils.random.nextInt(6) + 1;
                caster.healDamage(healed);
                if (caster.getHp() == caster.getMaxHp()) {
                    message = "Minor Heal has restored " + caster.getName() + " to full health";
                } else {
                    message = "Minor Heal has healed " + caster.getName() + " for " + healed + " hit poinst";
                }
            } else {
                message = caster.getName() + " failed " + name + " skill roll.";
            }
            gameState.addMessage(message);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMpCost() {
        return mpCost;
    }

    public void setMpCost(int mpCost) {
        this.mpCost = mpCost;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }
}
