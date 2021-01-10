/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.Entity;
import fr.ubx.poo.model.go.character.Character;
import fr.ubx.poo.model.go.character.Player;

/***
 * A decor is an element that does not know its own position in the grid.
 */
public abstract class Decor extends Entity {
    public boolean isDestroyable() {
        return false;
    }

    public boolean isStoppingBlast() {
        return true;
    }

    public boolean canWalk(Character character) {
        return false;
    }

    public void traitement(Player player) {
    }

    public void traitement() {
    }
}
