package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.go.character.Player;

public class DoorPrevOpened extends Decor {
    @Override
    public void traitement(Player player) {
        player.prevLevelRequest();
        player.levelChangeRequest();
    }
}
