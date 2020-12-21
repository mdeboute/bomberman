/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.DoorNextClosed;
import fr.ubx.poo.model.decor.movable.Box;
import fr.ubx.poo.model.decor.pickable.DoorNextOpened;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.GameObject;

public class Player extends GameObject implements Movable {

    private boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private boolean DoorOpenRequested = false;
    private boolean BombRequested = false;
    private int lives = 1;
    private boolean winner;
    private final int BombRange = 1;
    private final int BombNumber = 1;
    private int KeyValue = 0;


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

    public void levelChangeRequest() {
        game.levelChangeRequest();
    }


    @Override
    public boolean canMove(Direction direction) {
        Dimension dimension = game.getWorld().dimension;
        Position nextPos = direction.nextPosition(getPosition());
        if (nextPos.inside(dimension)) {
            Decor decor = game.getWorld().get(nextPos);
            if (decor == null) {
                return true;
            }
            decor.traitement(this);
            return decor.canWalk(this);
        }
        return false;
    }

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
        if (DoorOpenRequested) {
            Position DoorPosition = direction.nextPosition(getPosition());
            if (game.getWorld().get(DoorPosition) instanceof DoorNextClosed && getKeyValue() > 0) {
                clear();
                game.getWorld().set(DoorPosition, new DoorNextOpened());
                KeyValue -= 1;
            }
            DoorOpenRequested = false;
        }
        if (BombRequested) {
            if(BombNumber > game.getNumberBombs()) {
                game.getWorld().setBombRequest(new Bomb(game, getPosition(), now, World.level));
            }
            BombRequested = false;
        }

    }

    public void clear() {
        game.getWorld().clear(direction.nextPosition(getPosition()));
        game.getWorld().ChangeRequest();
    }

    public Dimension getDimension() {
        return game.getWorld().dimension;
    }

    public void prevLevelRequest() {
        try {
            if (game.updateLevelRequest(-1)) {
                Position newPosition = game.getWorld().getPrevLevelPosition();
                this.setPosition(new Position(newPosition));
                direction = Direction.S;
            }
        } catch (Exception e) {
            game.updateLevelRequest(1);
        } finally {
            game.getWorld().ChangeRequest();
        }

    }

    public void nextLevelRequest() {

        try {
            if (game.updateLevelRequest(1)) {
                Position newPosition = game.getWorld().getNextLevelPosition();
                this.setPosition(new Position(newPosition));
                direction = Direction.S;
            }
        } catch (Exception e) {
            game.updateLevelRequest(-1);
        } finally {
            game.getWorld().ChangeRequest();
        }
    }

    public void moveBoxRequest() {
        Position playerPos = this.getPosition();
        Position boxPosition = direction.nextPosition(playerPos);
        Position boxNextPosition = direction.nextPosition(boxPosition);
        Dimension dimension = this.getDimension();
        if (boxNextPosition.inside(dimension) && game.getWorld().isEmpty(boxNextPosition)) {
            game.getWorld().set(boxNextPosition, new Box());
            this.clear();
        }
    }

    public void GameWon() {
        this.winner = true;
    }

    public void increaseHeart() {
        this.lives++;
    }

    public void decreaseHeart() {
        this.lives--;
        this.alive = (lives > 0);
    }

    public boolean isWinner() {
        return this.winner;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public int getBombRange() {
        return this.BombRange;
    }

    public int getBombNumber() {
        return this.BombNumber;
    }

    public int getKeyValue() {
        return this.KeyValue;
    }

    public void increaseKey() {
        this.KeyValue++;
    }

    public void requestDoorOpen() {
        DoorOpenRequested = true;
    }

    public void BombRequest() {
        BombRequested = true;
    }
}
