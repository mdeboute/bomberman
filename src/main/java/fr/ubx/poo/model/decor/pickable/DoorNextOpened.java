package fr.ubx.poo.model.decor.pickable;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Player;

public class DoorNextOpened extends Pickable {

    @Override
    public void traitement(Player player) {
        player.nextLevelRequest();
        player.levelChangeRequest();
    }
    @Override
    public boolean canWalk(Player player){
        return false;
    }


    @Override
    public boolean isDestroyable() {
        return false;
    }
}
