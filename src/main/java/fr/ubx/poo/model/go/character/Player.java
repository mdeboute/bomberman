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

    public boolean godMod = false;
    private Direction direction;
    private boolean alive = true;
    private boolean moveRequested = false;
    private boolean DoorOpenRequested = false;
    private boolean BombRequested = false;
    private int lives = 1;
    private boolean winner;
    private int BombRange = 2;
    private int BombNumber = 2;
    private int KeyValue = 0;
    private int actualBombNumber = 0;
    private long godModTimer = -1;


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
                game.getWorld().setRaw(WorldEntity.DoorNextOpened, new Position(DoorPosition));
                KeyValue -= 1;
            }
            DoorOpenRequested = false;
        }
        if (BombRequested) {
            if (BombNumber > actualBombNumber) {
                game.getWorld().setBombRequest(new Bomb(game, getPosition(), now, game.getWorld().level, this.BombRange));
                actualBombNumber++;
            }
            BombRequested = false;

        }
        if (godMod) {
            if (godModTimer < 0) {
                godModTimer = now;
            }
            if (now - godModTimer > (long)1000000000) {
                godMod = false;
                godModTimer = -1;
            }
        }

    }

    public void clear() {
        game.getWorld().clear(direction.nextPosition(getPosition()));
        game.getWorld().ChangeRequest();
    }

    public void prevLevelRequest() {
        Position pos =this.getPosition();
        try {
            Position nextPosition=game.updateLevelRequestPrev();
            this.setPosition(nextPosition);
            this.direction=Direction.S;
        } catch (PositionNotFoundException e) {
            this.setPosition(pos);
            System.err.println("ERREUR CHANGEMENT DE MONDE PREV");
        } finally {
            game.getWorld().ChangeRequest();
        }
    }

    public void nextLevelRequest() {
        Position pos =this.getPosition();
        try {
            Position nextPosition=game.updateLevelRequestNext();
            this.setPosition(nextPosition);
            this.direction=Direction.S;
        } catch (PositionNotFoundException e) {
            this.setPosition(pos);
            System.err.println("ERREUR CHANGEMENT DE MONDE NEXT");
        } finally {
            game.getWorld().ChangeRequest();
        }
    }

    public void moveBoxRequest() {
        Position playerPos = this.getPosition();
        Position boxPosition = direction.nextPosition(playerPos);
        Position boxNextPosition = direction.nextPosition(boxPosition);
        Dimension dimension = game.getWorld().dimension;
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
        if (!godMod) {
            this.lives--;
            this.alive = (lives > 0);
            godMod = true;
        }
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

    public void decreaseActualBombNumber() {
        actualBombNumber--;
    }

    public int getActualBombNumber() {
        return actualBombNumber;
    }

    public void BombNumberInc() {
        BombNumber++;
    }

    public void BombNumberDec() {
        BombNumber--;
    }

    public void bombRangeDec() {
        BombRange--;
    }

    public void bombRangeInc() {
        BombRange++;
    }
}
