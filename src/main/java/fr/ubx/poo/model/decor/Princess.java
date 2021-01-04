package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class Princess extends Decor {
    @Override
    public void traitement(Player player) {
        player.GameWon();
    }
}
