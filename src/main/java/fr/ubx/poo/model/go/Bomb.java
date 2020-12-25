package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.game.World;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.character.Player;

public class Bomb extends GameObject {
    private final int bombRange;
    private final World world;
    private final int bombLevel;
    private long armTime;
    private int state = 0;
    private boolean isRemovable = false;
    private boolean traitement = true;

    public Bomb(Game game, Position position, long armTime, int bombLevel, int bombRange) {
        super(game, position);
        this.armTime = armTime;
        this.bombRange = bombRange;
        world = game.getWorld();
        this.bombLevel = bombLevel;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

    @Override
    public void traitement(Player player) {
        Decor decor = world.get(getPosition());
        if (decor != null) {
            if (decor.isDestroyable()) {
                world.clear(getPosition());
            }
            if (decor.isStoppingBlast()) {
                world.ChangeRequest();
                traitement = false;
                return;
            }
        }
        for (Direction direction : Direction.values()) {
            Position nextPosition = direction.nextPosition(getPosition());
            for (int i = 0; i < bombRange; i++) {
                decor = world.get(nextPosition);

                if (decor != null) {
                    if (decor.isDestroyable()) {
                        world.clear(nextPosition);
                    }
                    if (decor.isStoppingBlast()) {
                        break;
                    }
                }

                if (player.getPosition().equals(nextPosition) && game.getCurrentLevel() == bombLevel) {
                    player.decreaseHeart();
                }

                nextPosition = direction.nextPosition(nextPosition);
            }
        }
        world.ChangeRequest();
        player.decreaseActualBombNumber();
        traitement = false;
    }

    public int getState() {
        return state;
    }

    public void stateCalculator(long now) {
        boolean secondePassed = now - armTime >= (long) 1000000000;
        if (state == 4 && traitement) {
            this.traitement(game.getPlayer());
        }
        if (state == 4 && secondePassed) {
            isRemovable = (true);
            game.getWorld().ChangeRequest();
            return;
        }
        if (state < 4 && secondePassed) {
            state = state + 1;
            armTime = now;
        }
    }


}
