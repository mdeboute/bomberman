/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.*;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.DoorNextClosed;
import fr.ubx.poo.model.decor.DoorNextOpened;
import fr.ubx.poo.model.decor.movable.Box;
import fr.ubx.poo.model.go.Bomb;

public class Player extends Character {
    public boolean godMod = false;
    private boolean DoorOpenRequested = false;
    private boolean BombRequested = false;
    private boolean winner;
    private int BombRange = 1;
    private int BombNumber = 1;
    private int KeyValue = 0;
    private int actualBombNumber = 0;
    private long godModTimer = -1;


    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
    }

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


    public int getLives() {
        return lives;
    }

    public void levelChangeRequest() {
        game.levelChangeRequest();
    }

    public void update(long now) {
        super.update(now);
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
            if (now - godModTimer > (long) 1000000000) {
                godMod = false;
                godModTimer = -1;
            }
        }
        for (Monster monster : game.getWorld().getListMonster()) {
            if (getPosition().equals(monster.getPosition())) {
                this.decreaseHeart();
            }
        }
    }

    public void clear() {
        game.getWorld().clear(direction.nextPosition(getPosition()));
        game.getWorld().ChangeRequest();
    }

    public void prevLevelRequest() {
        Position pos = this.getPosition();
        try {
            Position nextPosition = game.updateLevelRequestPrev();
            this.setPosition(nextPosition);
            this.direction = Direction.S;
        } catch (PositionNotFoundException e) {
            this.setPosition(pos);
            System.err.println("ERREUR CHANGEMENT DE MONDE PREV");
        } finally {
            game.getWorld().ChangeRequest();
        }
    }

    public void nextLevelRequest() {
        Position pos = this.getPosition();
        try {
            Position nextPosition = game.updateLevelRequestNext();
            this.setPosition(nextPosition);
            this.direction = Direction.S;
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
            for (Monster monster : game.getWorld().getListMonster()) {
                if (monster.getPosition().equals(boxNextPosition)) {
                    return;
                }
            }
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

    @Override
    public void decreaseHeart() {
        if (!godMod) {
            super.decreaseHeart();
            godMod = true;
        }
    }

    public boolean isWinner() {
        return this.winner;
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

    public void BombNumberInc() {
        BombNumber++;
    }

    public void BombNumberDec() {
        if (BombNumber > 1)
            BombNumber--;
    }

    public void bombRangeDec() {
        if (BombRange > 1)
            BombRange--;
    }

    public void bombRangeInc() {
        BombRange++;
    }
}
