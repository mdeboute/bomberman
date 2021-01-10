package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.go.GameObject;

public abstract class Character extends GameObject implements Movable {
    protected Direction direction = Direction.W;
    protected boolean alive = true;
    protected boolean moveRequested = false;
    protected int lives = 1;

    public Character(Game game, Position position) {
        super(game, position);
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean IsAlive() {
        return !this.alive;
    }


    public abstract boolean canMove(Direction direction);


    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }


    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
            moveRequested = false;
        }
    }

    public void decreaseHeart() {
        this.lives--;
        this.alive = (lives > 0);
        System.out.println("blew");
    }

}
