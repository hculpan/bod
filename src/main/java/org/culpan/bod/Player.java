package org.culpan.bod;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Combatant {
    Sprite sprite;

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public void render(Batch batch) {

    }
}
