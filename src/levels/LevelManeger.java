package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class LevelManeger {
    private Game game;
    private BufferedImage[] levelSprite;

    public LevelManeger(Game game){
        this.game = game;
        importOutsideSprites();
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas("outside_sprites.png");
        levelSprite = new BufferedImage[48];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 12; j++){
                int index = i * 12 + j;
                levelSprite[index] = img.getSubimage(j*32, i*32, 32, 32);
            }
        }
    }

    public void draw(Graphics g){
        g.drawImage(levelSprite[2], 0, 0, null);
        
    }

    public void update(){

    }
}
