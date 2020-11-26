/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import static fr.ubx.poo.game.WorldEntity.*;


import fr.ubx.poo.model.go.character.Player;

public class Game {
    // liste de monde (world)
    private final List<World> worlds;
    // monde courant (en cours)
    private World world;
    private final Player player;
    private final String worldPath;
    public int initPlayerLives;
    // nombre du niveau courant
    private int level=1;
    // nombre de levels (world) dans le game
    private int levels;
    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public Game(String worldPath) {
        //load la config pour avoir le nombres de levels
        loadConfig(worldPath);
        // créer la liste des worlds
        worlds=new ArrayList<World>(levels);
        for(int i=1;i<=levels;i++){
            worlds.add(new WorldFromFile(worldPath,i));
        }
        // commence pas le level 1
        world=worlds.get(0);
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

    public int getLevels(){return levels;}

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

    public void updatelevelrequest() {
        if(this.getLevels()>=this.getLevel())
            this.world = worlds.get(this.level-1);
    }
}
