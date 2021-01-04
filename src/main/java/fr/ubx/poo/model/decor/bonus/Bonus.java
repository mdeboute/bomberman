package fr.ubx.poo.model.decor.bonus;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Character;
import fr.ubx.poo.model.go.character.Player;

public abstract class Bonus extends Decor {
    @Override
    public boolean isStoppingBlast() {
        return false;
    }

    @Override
    public boolean isDestroyable() {
        return true;
    }

    @Override
    public boolean canWalk(Character character) {
        return true;
    }

    @Override
    public void traitement(Player player) {
        player.clear();
    }
}
