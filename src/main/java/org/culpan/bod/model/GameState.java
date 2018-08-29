package org.culpan.bod.model;

public interface GameState {
    boolean canMoveTo(int x, int y);

    boolean isNpcAt(int x, int y);

    boolean isPlayerAt(int x, int y);

    Combatant getNearestPlayerTo(int x, int y);
}
