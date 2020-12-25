package fr.ubx.poo.model.decor.pickable;

import fr.ubx.poo.model.go.character.Player;

public class DoorPrevOpened extends Pickable{

    @Override
    public void traitement(Player player) {
        player.prevLevelRequest();
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
