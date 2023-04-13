package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class LevelManeger {
    private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne;

    public LevelManeger(Game game){
        this.game = game;
        importOutsideSprites();
        levelOne = new Level(LoadSave.GetLevelData());
    }

    private void importOutsideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas("outside_sprites.png");
		levelSprite = new BufferedImage[48];
		for (int j = 0; j < 4; j++){
			for (int i = 0; i < 12; i++) {
				int index = j * 12 + i;
				levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
			}
        }
	}

    public void draw(Graphics g, int lvlOffset){
        for(int i = 0; i < Game.TILES_IN_HEIGHT; i++){
            for(int j = 0; j < levelOne.getLevelData()[0].length; j++){
                int index = levelOne.getSpriteIndex(j, i);
                g.drawImage(levelSprite[index], ((Game.TILES_SIZE * j) - lvlOffset), (Game.TILES_SIZE * i), Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
    }

    public void update(){

    }

    public Level getCurrentLevel(){
        return levelOne;
    }
}
