package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.decor.Decor;

import java.util.Random;

public class Monster extends Character {
    private final World world;
    private final Random rand = new Random();
    private long randomSeconds;
    private long moveTimer = -1;


    public Monster(Game game, Position position, World world) {
        super(game, position);
        this.world = world;
        resetRandom();
    }

    @Override
    public void update(long now) {
        if (moveTimer < 0) {
            moveTimer = now;
        }
        long localTime = ((long) (1000000)) * randomSeconds;
        if (now - moveTimer > localTime) {
            this.direction = Direction.random();
            resetRandom();
            moveRequested = true;
            moveTimer = -1;
        }

        super.update(now);
    }

    private void resetRandom() {
        randomSeconds = (long) (rand.nextInt(2000) + 1000);
    }

    @Override
    public boolean canMove(Direction direction) {
        Dimension dimension = world.dimension;
        Position nextPos = direction.nextPosition(getPosition());
        if (nextPos.inside(dimension)) {
            Decor decor = world.get(nextPos);
            if (decor == null) {
                return true;
            }
            return decor.canWalk(this);
        }
        return false;
    }

}
