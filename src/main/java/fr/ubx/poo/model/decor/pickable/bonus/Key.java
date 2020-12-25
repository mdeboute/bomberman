package fr.ubx.poo.model.decor.pickable.bonus;

import fr.ubx.poo.model.go.character.Player;

public class Key extends Bonus {

    @Override
    public void traitement(Player player) {
        player.increaseKey();
        player.clear();
    }

    @Override
    public boolean isDestroyable() {
        return false;
    }
}
