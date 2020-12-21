package fr.ubx.poo.model.decor.movable;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Player;

public class Box extends Decor {

    @Override
    public void traitement(Player player) {
        player.moveBoxRequest();
    }

    @Override
    public boolean canWalk(Player player) {
        return false;
    }
}
