package fr.ubx.poo.model.decor.pickable.bonus;

import fr.ubx.poo.model.go.character.Player;

public class BombNumberInc extends Bonus {
    @Override
    public void traitement(Player player) {
        player.BombNumberInc();
        super.traitement(player);
    }
}
