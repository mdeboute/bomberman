package fr.ubx.poo.model.decor.pickable.bonus;

import fr.ubx.poo.model.go.character.Player;

public class BombRangeDec extends Bonus {

    @Override
    public void traitement(Player player) {
        player.bombRangeDec();
        super.traitement(player);
    }
}
