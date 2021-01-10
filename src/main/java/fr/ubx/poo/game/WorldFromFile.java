package fr.ubx.poo.game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static fr.ubx.poo.game.WorldEntity.Empty;
import static fr.ubx.poo.game.WorldEntity.Player;

public class WorldFromFile extends World {

    public WorldFromFile(String path,int n){
        super( TabFromFile(path,n), n );
    }

    public static WorldEntity [][] TabFromFile(String path, int n){
        WorldEntity[][] World_building = {{Player},{Empty}};
        try(InputStream input = new FileInputStream(new File(path, "config.properties"))){
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            String prefix =prop.getProperty("prefix","level");

            BufferedReader br = new BufferedReader(new FileReader( new File(path, prefix+n +".txt")));
            List<char[]> L = new ArrayList<>();
            String s;

            while((s=br.readLine()) != null){
                L.add( s.toCharArray());
            }
            World_building= new WorldEntity[L.size()][L.get(0).length];
            for(int i =0;i<L.size();i++){
                for(int j=0;j<L.get(0).length;j++)
                    World_building[i][j]=WorldEntity.fromCode(L.get(i)[j]).orElse(Empty);
            }
            br.close();
        }catch(IOException ex){
            System.err.println("Error loading file");
        }
        return World_building;
    }


}
