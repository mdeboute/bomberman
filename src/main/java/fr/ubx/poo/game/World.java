/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.Bomb;

import java.util.*;
import java.util.function.BiConsumer;

public class World {
    private final Map<Position, Decor> grid;
    private final WorldEntity[][] raw;
    public final Dimension dimension;
    public static int level;
    private boolean changed = false;
    protected Position doorNextOpenedPosition;
    protected Position doorPrevOpenedPosition;
    private LinkedList<Bomb> listBomb=new LinkedList<Bomb>();

    public World(WorldEntity[][] raw, int level) {
        this.raw = raw;
        dimension = new Dimension(raw.length, raw[0].length);
        grid = WorldBuilder.build(raw, dimension);
        this.level = level;
        doorNextOpenedPosition = getPrevLevelPositionBis();
        doorPrevOpenedPosition = getNextLevelPositionBis();
    }

    public List<Bomb> getListBomb() {
        return listBomb;
    }

    public void setBombRequest(Bomb bomb){
        listBomb.add(bomb);
        ChangeRequest();
    }

    public Position getNextLevelPosition() {
        return doorPrevOpenedPosition;
    }

    public Position getPrevLevelPosition() {
        return doorNextOpenedPosition;
    }


    public Position getPrevLevelPositionBis() {
        for (int x = 0; x < dimension.width; x++) {
            for (int y = 0; y < dimension.height; y++) {
                if (raw[y][x] == WorldEntity.DoorNextOpened) {
                    return new Position(x, y);
                }
            }
        }
        return null;
    }

    public Position getNextLevelPositionBis() {
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
        changed = !changed;
    }

}
