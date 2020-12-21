package fr.ubx.poo.model.decor.pickable;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Player;

public class Heart extends Pickable {

    @Override
    public void traitement(Player player){
        player.increaseHeart();
        player.clear();
    }
}
