/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.pickable.*;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;

public class Player extends GameObject implements Movable {

    private boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private boolean winner;
    private int BombRange=1;
    private int BombNumber=0;
    private int KeyValue=0;



    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
    }

    public int getLives() {
        return lives;
    }

    public Direction getDirection() {
        return direction;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        if(game.getWorld().get(direction.nextPosition(getPosition())) instanceof Pickable)
            return true;
        return direction.nextPosition(getPosition()).inside(game.getWorld().dimension)
                && game.getWorld().isEmpty(direction.nextPosition(getPosition())) ;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                if(game.getWorld().get(direction.nextPosition(getPosition())) instanceof Princess)
                    this.winner=true;

                // si le prochain move est sur une case Monster on diminue la vie
                if(game.getWorld().get(direction.nextPosition(getPosition())) instanceof Monster)
                    --lives;
                // changement sur le level de map
                if (game.getWorld().get(direction.nextPosition(getPosition())) instanceof Key) {
                    KeyValue++;
                    game.getWorld().clear(direction.nextPosition(getPosition()));
                    game.getWorld().ChangeRequest();
                }
                //changer de level
                if(game.getWorld().get(direction.nextPosition(getPosition()))  instanceof DoorNextOpened) {
                    game.setLevel(game.getLevel() + 1);
                    game.getWorld().ChangeRequest();
                    game.updatelevelrequest();
                }



                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public boolean isWinner() { return winner; }

    public boolean isAlive() { return lives>0; }

    public int getBombRange() {
        return BombRange;
    }

    public int getBombNumber() {
        return BombNumber;
    }

    public int getKeyValue() {
        return KeyValue;
    }

}
