/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;

import java.util.*;
import java.util.function.BiConsumer;

public class World {
    public final Dimension dimension;
    public final int level;
    private final Map<Position, Decor> grid;
    private final WorldEntity[][] raw;
    protected Position doorNextOpenedPosition = null;
    protected Position doorPrevOpenedPosition = null;
    private boolean changed = false;
    private final List<Bomb> listBomb = new LinkedList<>();
    private final List<Monster> listMonster = new ArrayList<>();

    public World(WorldEntity[][] raw, int level) {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.build(raw, dimension);
        this.level = level;
    }

    public void setRaw(WorldEntity w, Position pos) {
        raw[pos.y][pos.x] = w;
    }

    public List<Bomb> getListBomb() {
        return listBomb;
    }

    public void setBombRequest(Bomb bomb) {
        listBomb.add(bomb);
        ChangeRequest();
    }

    public Position getNextLevelPosition() {
        if (doorPrevOpenedPosition == null) {
            doorPrevOpenedPosition = getNextLevelPositionBis();
        }
        return doorPrevOpenedPosition;
    }

    public List<Monster> initMonsters(Game game) {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Monster) {
                    this.listMonster.add(new Monster(game, new Position(x, y), this));
                }
            }
        }
        return this.listMonster;
    }

    public List<Monster> getListMonster() {
        return listMonster;
    }

    public Position getPrevLevelPosition() {
        if (doorNextOpenedPosition == null) {
            doorNextOpenedPosition = getPrevLevelPositionBis();
        }
        return doorNextOpenedPosition;
    }

    private Position getPrevLevelPositionBis() {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.DoorNextOpened) {
                    return new Position(x, y);
                }
            }
        }
        return null;
    }

    private Position getNextLevelPositionBis() {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.DoorPrevOpened) {
                    return new Position(x, y);
                }
            }
        }
        return null;
    }

    public boolean hasChanged() {
        return changed;
    }

    public Position findPlayer() throws PositionNotFoundException {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.Player) {
                    return new Position(x, y);
                }
            }
        }
        throw new PositionNotFoundException("Player");
    }

    public Decor get(Position position) {
        return grid.get(position);
    }

    public void set(Position position, Decor decor) {
        grid.put(position, decor);
    }

    public void clear(Position position) {
        grid.remove(position);
    }

    public void forEach(BiConsumer<Position, Decor> fn) {
        grid.forEach(fn);
    }

    public Collection<Decor> values() {
        return grid.values();
    }

    public boolean isInside(Position position) {
        return true; // to update
    }

    public boolean isEmpty(Position position) {
        return grid.get(position) == null;
    }

    public void ChangeRequest() {
        changed = true;
    }

    public void ChangeRequestDone() {
        changed = false;
    }

}
