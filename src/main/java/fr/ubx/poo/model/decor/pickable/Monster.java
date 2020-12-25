package fr.ubx.poo.model.decor.pickable;

import fr.ubx.poo.model.go.character.Player;

public class Monster extends Pickable {
    @Override
    public void traitement(Player player){
        player.decreaseHeart();
    }

    @Override
    public boolean isStoppingBlast() {
        return false;
    }
}
