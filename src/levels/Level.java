package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import entities.Crabby;
import main.Game;
import static utilz.HelpMethods.GetLevelData;
import static utilz.HelpMethods.GetCrabs;
import static utilz.HelpMethods.GetPlayerSpawn;

public class Level {
    private BufferedImage img;
    private int[][] levelData;
    private ArrayList<Crabby> crabs;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage img){
        this.img = img;
        createLevelData();
        createEnemies();
        calculateLevelOffsets();
        calculatePlayerSpawn();
    }

    private void calculatePlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calculateLevelOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset =  lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = GetCrabs(img);
    }

    private void createLevelData() {
        levelData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y){
        return levelData[y][x];
    }

    public int[][] getLevelData(){
        return levelData;
    }

    public int getLevelOffset(){
        return maxLvlOffsetX;
    }

    public ArrayList<Crabby> getCrabs(){
        return crabs;
    }

    public Point getPlayerSpawn(){
        return playerSpawn;
    }
}
