package utilz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

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
}
