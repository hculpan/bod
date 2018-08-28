package org.culpan.bod;

import java.util.ArrayList;
import java.util.List;

public class GameMaster {
    List<Combatant> combatants;

    int round = 1;

    public GameMaster() {
        combatants = new ArrayList<Combatant>();
    }

    public GameMaster(List<Combatant> combatants) {
        this.combatants.addAll(combatants);
    }

    public GameMaster(Combatant ... combatants) {
        addCombatants(combatants);
    }

    public List<Combatant> getCombatants() {
        return combatants;
    }

    public void setCombatants(List<Combatant> combatants) {
        this.combatants = combatants;
    }

    public void addCombatants(Combatant ... combatants) {
        for (Combatant c : combatants) {
            this.combatants.add(c);
        }
    }
}
