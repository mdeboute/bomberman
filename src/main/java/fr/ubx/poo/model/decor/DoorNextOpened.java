package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class DoorNextOpened extends Decor {
    @Override
    public void traitement(Player player) {
        player.nextLevelRequest();
        player.levelChangeRequest();
    }
}
