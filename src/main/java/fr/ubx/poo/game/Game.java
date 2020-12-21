/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import fr.ubx.poo.model.go.Bomb;
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
    private  List<World> worlds;
    private World world;
    private final Player player;
    private final String worldPath;
    public int initPlayerLives;
    private int currentLevel = 1;
    private static int levels;
    private boolean levelChange=false;
    private List<List<Bomb>> ListBombs = new ArrayList<List<Bomb>>() ;

    public List<List<Bomb>> getListBombs() {
        return ListBombs;
    }

    public boolean hasLevelChanged(){
        return levelChange;
    }

    public void levelChangeRequest(){
        levelChange=!levelChange;
    }


    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public Game(String worldPath) {
        loadConfig(worldPath);
        worlds = new ArrayList<World>(levels);
        for (int i = 1; i <= levels; i++) {
            World Level_i=new WorldFromFile(worldPath, i);
            worlds.add(Level_i);
            ListBombs.add(Level_i.getListBomb());
        }

        this.world = worlds.get(0);
        this.worldPath = worldPath;
        Position positionPlayer = null;
        try {
            positionPlayer = world.findPlayer();
            player = new Player(this, positionPlayer);
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
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

    public boolean updateLevelRequest(int i) {

        if (this.getLevels() >= this.getCurrentLevel()+i && this.getCurrentLevel()+i>=1 ) {
            this.world = worlds.get(this.currentLevel + i - 1);
            this.currentLevel=this.currentLevel+i;
            return true;
        }
        return false;
    }


    public int getNumberBombs() {
        int m= ListBombs.stream().mapToInt(List::size).sum();
        return m;
    }
}
