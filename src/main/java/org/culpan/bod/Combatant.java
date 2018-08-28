package org.culpan.bod;

import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class Combatant {
    public boolean isPlayer() {
        return false;
    }

    public abstract void render(Batch batch);
}
