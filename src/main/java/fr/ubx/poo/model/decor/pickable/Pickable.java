package fr.ubx.poo.model.decor.pickable;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Player;

public abstract class Pickable extends Decor {

    @Override
    public void traitement(Player player) {
    }

    @Override
    public boolean canWalk(Player player) {
        return true;
    }

    @Override
    public boolean isDestroyable() {
        return true;
    }

}
