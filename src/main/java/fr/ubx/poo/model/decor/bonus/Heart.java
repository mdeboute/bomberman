package fr.ubx.poo.model.decor.bonus;

import fr.ubx.poo.model.go.character.Player;

public class Heart extends Bonus {
    @Override
    public void traitement(Player player) {
        player.increaseHeart();
        player.clear();
    }
}
