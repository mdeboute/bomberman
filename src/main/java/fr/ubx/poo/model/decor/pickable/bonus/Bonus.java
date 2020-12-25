package fr.ubx.poo.model.decor.pickable.bonus;

import fr.ubx.poo.model.decor.pickable.Pickable;
import fr.ubx.poo.model.go.character.Player;

public abstract class Bonus extends Pickable {
    @Override
    public boolean isStoppingBlast() {
        return false;
    }

    @Override
    public void traitement(Player player) {
        player.clear();
    }
}
