/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.bonus.Key;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.model.*;



public class Player extends GameObject implements Movable {

    private boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives;
    private boolean winner;
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
        return this.game.getWorld().isEmpty(direction.nextPosition(getPosition())) &&
                this.game.getWorld().isInside(direction.nextPosition(getPosition()));
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                if (game.getWorld().get(direction.nextPosition(getPosition())) instanceof Key) {
                    KeyValue++;
                    game.getWorld().clear(direction.nextPosition(getPosition()));
                    game.getWorld().ChangeRequest();
                }
                doMove(direction);
                if(this.game.getWorld().findMonsters().contains(this.getPosition())){
                    this.lives--;
                } else {
                    try {
                        if (this.game.getWorld().findPrincess().equals(this.getPosition())) {
                            this.winner = true;
                        }
                    } catch (PositionNotFoundException e) {
                        System.out.println(e);
                    }
                }
                if(this.getLives() <= 0){
                    this.alive = false;
                }
            }
        }
        moveRequested = false;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return alive;
    }

}
