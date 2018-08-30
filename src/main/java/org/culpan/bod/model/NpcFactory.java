package org.culpan.bod.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import org.culpan.bod.Utils;

public class NpcFactory {
    public static Npc createNpc(String monsterName, GameState gameState) {
        Npc npc = new Npc(monsterName);
        if (monsterName.equals("goblin-warrior")) {
            npc.setGameState(gameState);
            npc.setLevel(1);
            npc.setHp(Utils.random.nextInt(6) + 3);
            npc.addAttack("Short sword", 50, 6, 0);
            Texture characterTexture = new Texture(Gdx.files.internal("dungeon/0x72_16x16DungeonTileset.v4.png"));
            npc.sprite = new Sprite(characterTexture, 16, 160, 16, 16);
            npc.sprite.setScale(3);
            npc.setAc(1);
        } else if (monsterName.equals("imp")) {
            npc.setGameState(gameState);
            npc.setLevel(1);
            npc.setHp(Utils.random.nextInt(4) + 1);
            npc.addAttack("Claw", 75, 4, 1, true, false);
            npc.addAttack("Evade", 45, 4, 1, false, true);
            Texture characterTexture = new Texture(Gdx.files.internal("dungeon/0x72_16x16DungeonTileset.v4.png"));
            npc.sprite = new Sprite(characterTexture, 48, 176, 16, 16);
            npc.sprite.setScale(3);
            npc.setAc(2);
        }
        npc.setMaxHp(npc.getHp());

        return npc;
    }
}
