package fr.ubx.poo.model.decor.pickable;

import fr.ubx.poo.model.go.character.Player;

public class Key extends Pickable{

    @Override
    public void traitement(Player player){
        player.increaseKey();
        player.clear();
    }
}
