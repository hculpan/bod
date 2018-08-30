package org.culpan.bod.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.culpan.bod.FontManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GameMaster {
    public enum ActionState { normal, casting_spell }

    List<Combatant> combatants;

    int round = 1;

    BitmapFontCache bitmapFontCache32;

    BitmapFontCache bitmapFontCache20;

    ShapeRenderer shapeRenderer;

    List<String> messages = new LinkedList<>();

    GameState gameState;

    ActionState actionState = ActionState.normal;

    public GameMaster(GameState gameState) {
        combatants = new ArrayList<>();
        this.gameState = gameState;

        bitmapFontCache20 = new BitmapFontCache(FontManager.getFont(FontManager.IMMORTAL_20));
        bitmapFontCache20.addText("You have the following actions available:", 40, 380);
        bitmapFontCache20.addText("Pass: P", 80, 275);

        bitmapFontCache32 = new BitmapFontCache(FontManager.getFont(FontManager.IMMORTAL_32));
        bitmapFontCache32.addText("Round", 40, 420);

        shapeRenderer = new ShapeRenderer();
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

    public void addMessage(String message) {
        messages.add(0, message);
    }

    public boolean canAct() {
        return true;
    }

    public void act() {
        List<Combatant> deadNpcs = combatants.stream().filter( c -> !c.isPlayer() && c.getHp() <= 0).collect(Collectors.toList());
        int totalXp = deadNpcs.stream().collect(Collectors.summingInt(Combatant::getLevel));
        deadNpcs.forEach(c -> {
            combatants.remove(c);
        });
        getPlayers().forEach(p -> ((Player)p).addXp(totalXp));
        getNpcs().forEach(c -> c.act());
        round++;
    }

    public boolean allPlayersDead() {
        return getPlayers().stream().filter(p -> p.getHp() > 0).count() == 0;
    }

    public void addCombatants(Combatant ... combatants) {
        for (Combatant c : combatants) {
            this.combatants.add(c);
        }
    }

    public Player getActivePlayer() {
        for (Combatant c : combatants) {
            if (c.isPlayer()) {
                return (Player)c;
            }
        }

        return null;
    }

    public List<Combatant> getPlayers() {
        return combatants.stream().filter(Combatant::isPlayer).collect(Collectors.toList());
    }

    public List<Combatant> getNpcs() {
        return combatants.stream().filter(c -> !c.isPlayer()).collect(Collectors.toList());
    }

    public void render(Batch batch) {
        batch.end();

        Player p = getActivePlayer();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.5f, 0.0f, 0.1f, 0.5f);
        shapeRenderer.rect(20, 150, 565, 290);
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 0.5f);
        shapeRenderer.rect(20, 20, 565, 110);
        shapeRenderer.end();

        batch.begin();
        // Draw messages
        BitmapFont bitmap14 = FontManager.getFont(FontManager.IMMORTAL_14);
        for (int i = 0; i < (messages.size() > 6 ? 6 : messages.size()); i++) {
            bitmap14.draw(batch, messages.get(i), 30, 40 + (i * 16));
        }

        // Draw player options
        bitmapFontCache20.draw(batch);
        String moves = p.findValidMoves();
        if (moves != null && !moves.isEmpty()) {
            bitmapFontCache20.getFont().draw(batch, "Move: " + moves, 80, 345);
        }
        String attacks = p.findValidAttacks();
        if (attacks != null && !attacks.isEmpty()) {
            bitmapFontCache20.getFont().draw(batch, "Attack: " + attacks, 80, 310);
        }
        if (touchingExit(p)) {
            bitmapFontCache20.getFont().draw(batch, "Exit: E", 80, 240);
        }
        if (p.getSpells().size() > 0) {
            bitmapFontCache20.getFont().draw(batch, "Spell: S", 80, 205);
        }

        bitmapFontCache32.draw(batch);
        bitmapFontCache32.getFont().draw(batch, Integer.toString(round), 150, 420);

        if (actionState == ActionState.casting_spell) {
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1.0f);
            shapeRenderer.rect(525, 180, 300, 400);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.0f, 0.0f, 1.0f, 1.0f);
            shapeRenderer.rect(526, 181, 298, 398);
            shapeRenderer.end();

            batch.begin();
            int currentY = 560 - 40;
            bitmapFontCache32.getFont().draw(batch, "Pick spell:", 546, 560);
            for (int i = 0; i < p.getSpells().size(); i++) {
                Spell spell = p.getSpells().get(i);
                bitmapFontCache20.getFont().draw(batch, String.format("%d - %s (%d)", i + 1, spell.getName(), spell.getMpCost()), 586, currentY);
                currentY -= 24;
            }
        }

    }

    public boolean castSpell(int spellNumber) {
        boolean result = false;
        if (spellNumber - 1 >= 0 && spellNumber - 1 < getActivePlayer().getSpells().size()) {
            Spell spell = getActivePlayer().getSpells().get(spellNumber - 1);
            spell.castSpell(gameState, getActivePlayer(), getActivePlayer().getTarget());
            actionState = ActionState.normal;
            result = true;
        }
        return result;
    }

    public void castSpell() {
        if (getActivePlayer().getMagicPoints() > 0) {
            actionState = ActionState.casting_spell;
        } else {
            addMessage("No more magic points to cast spells with.");
        }
    }

    private boolean touchingExit(Combatant c) {
        return gameState.exitAt(c.getX(), c.getY()) ||
                gameState.exitAt(c.getX(), c.getY() + 1) ||
                gameState.exitAt(c.getX() + 1, c.getY() + 1) ||
                gameState.exitAt(c.getX() + 1, c.getY());
    }

    public boolean isCastingSpell() {
        return actionState == ActionState.casting_spell;
    }

    public void cancelSpell() {
        actionState = ActionState.normal;
    }
}
