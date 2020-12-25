package fr.ubx.poo.model.decor.pickable;

import fr.ubx.poo.model.go.character.Player;

public class Princess extends Pickable {

    public void traitement(Player player){
        player.GameWon();
    }

    @Override
    public boolean isDestroyable() {
        return false;
    }

}
