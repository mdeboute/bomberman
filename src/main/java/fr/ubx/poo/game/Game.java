/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Game {
    private static int currentLevel = 1;
    private static int levels;
    private final List<World> worlds;
    private final Player player;
    private final List<List<Bomb>> listBombs = new LinkedList<>();
    private final List<List<Monster>> listMonsters = new ArrayList<>();
    public int initPlayerLives;
    private World world;
    private boolean levelChange = false;

    public Game(String worldPath) {
        loadConfig(worldPath);
        worlds = new ArrayList<>(levels);
        for (int i = 1; i <= levels; i++) {
            World Level_i = new WorldFromFile(worldPath, i);
            worlds.add(Level_i);
            listBombs.add(Level_i.getListBomb());
        }
        this.world = worlds.get(0);
        Position positionPlayer;
        try {
            positionPlayer = world.findPlayer();
            player = new Player(this, positionPlayer);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        // initialisation listMonster
        for (World world : worlds) {
            this.listMonsters.add(world.initMonsters(this));
        }
    }

    public List<List<Monster>> getListMonsters() {
        return listMonsters;
    }

    public List<List<Bomb>> getListBombs() {
        return listBombs;
    }

    public boolean hasLevelChanged() {
        return levelChange;
    }

    public void levelChangeRequest() {
        levelChange = true;
    }

    public void levelChangedRequestDone() {
        levelChange = false;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }

    public int getLevels() {
        return levels;
    }

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            //test
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "2"));
            //nombre de levels
            levels = Integer.parseInt(prop.getProperty("levels", "1"));

        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Position updateLevelRequestNext() throws PositionNotFoundException {
        if (this.getLevels() >= this.getCurrentLevel() + 1 && this.getCurrentLevel() + 1 >= 1) {
            World worldTmp = worlds.get((currentLevel - 1) + 1);
            Position newPosition = worldTmp.getNextLevelPosition();
            if (newPosition != null) {
                this.world = worldTmp;
                currentLevel = currentLevel + 1;
                return newPosition;
            }
        }
        throw new PositionNotFoundException("CHANGEMENT DE MONDE IMPOSSIBLE");
    }

    public Position updateLevelRequestPrev() throws PositionNotFoundException {
        if (this.getLevels() >= this.getCurrentLevel() - 1 && this.getCurrentLevel() - 1 >= 1) {
            World worldTmp = worlds.get((currentLevel - 1) - 1);
            Position newPosition = worldTmp.getPrevLevelPosition();
            if (newPosition != null) {
                this.world = worldTmp;
                currentLevel = currentLevel - 1;
                return newPosition;
            }
        }
        throw new PositionNotFoundException("CHANGEMENT DE MONDE IMPOSSIBLE");
    }
}
