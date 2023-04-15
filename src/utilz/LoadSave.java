package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Crabby;
import main.Game;

import static utilz.Constants.EnemyConstants.*;

public class LoadSave {
    public static BufferedImage[][] GetPlayerAtlas(){
        BufferedImage animations[][] = new BufferedImage[9][6];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 6; j++){
                if((i == 0 && j < 5) || (i == 1) || (i == 2 && j < 3) || (i == 3 && j == 0) || (i == 4 && j < 2) || (i == 5 && j < 4) || (i == 6 && j < 3) || (i == 7 && j < 3) || (i == 8 && j < 3)){
                    InputStream is = LoadSave.class.getResourceAsStream(String.format("/player%d%d.png", i, j));
                    try{
                        animations[i][j] = ImageIO.read(is);
                    }catch(IOException e){
                        e.printStackTrace();
                    }finally{
                        try {
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return animations;
    }

    public static BufferedImage GetSpriteAtlas(String fileName){
        BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
    }

    public static ArrayList<Crabby> GetCrabs(){
        BufferedImage img = GetSpriteAtlas("level_one_data_long.png");
        ArrayList<Crabby> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++){
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == CRABBY)
					list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
			}
        }
        return list;
    }

    public static int[][] GetLevelData(){
		BufferedImage img = GetSpriteAtlas("level_one_data_long.png");
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];
		for (int j = 0; j < img.getHeight(); j++){
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if (value >= 48)
					value = 0;
				lvlData[j][i] = value;
			}
        }
		return lvlData;
    }
}
