package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.go.character.Player;

public class Bomb extends GameObject {
    private long armTime;
    private int state = 0;
    private int bombRange = 1;
    private boolean isRemovable = false;
    private static int level;
    private boolean traitement = true;

    public boolean isRemovable() {
        return isRemovable;
    }

    @Override
    public void traitement(Player player) {
        //TODO


        traitement=false;

    }

    public int getState() {
        return state;
    }

    public Bomb(Game game, Position position, long armTime,int level) {
        super(game, position);
        this.armTime = armTime;
        this.level=level;
    }

    public void stateCalculator(long now) {
        int m = (int) (now - armTime) / 900000000;
        if(state == 4 && traitement){
            this.traitement(game.getPlayer());
        }
        if (state == 4 && m == 1) {
            isRemovable =(true);
            game.getWorld().ChangeRequest();
            return;
        }

        if (m == 1 && m > 0 && state <= 4) {
            state = state + m;
            armTime = now;
        }
    }


}
