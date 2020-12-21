package fr.ubx.poo.model.decor.pickable;

import fr.ubx.poo.model.go.character.Player;

public class Monster extends Pickable {

    public void traitement(Player player){
        player.decreaseHeart();
    }
}
