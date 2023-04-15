package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import utilz.LoadSave;

import static utilz.Constants.EnemyConstants.*;

public class EnemyManeger {
    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManeger(Playing playing){
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        crabbies = LoadSave.GetCrabs();
    }

    public void update(){
        for(Crabby c : crabbies){
            c.update();
        }
    }

    public void draw(Graphics g, int xLvelOffset){
        drawCrabs(g, xLvelOffset);
    }

    private void drawCrabs(Graphics g, int xLvelOffset) {
        for(Crabby c : crabbies){
            g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()], (int)(c.getHitbox().x - xLvelOffset), (int)(c.getHitbox().y), CRABBY_WIDTH, CRABBY_HEIGHT, null);
        }
    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas("crabby_sprite.png");
        for(int i = 0; i < crabbyArr.length; i++){
            for(int j = 0; j < crabbyArr[i].length; j++){
                crabbyArr[i][j] = temp.getSubimage((j * CRABBY_WIDTH_DEFAULT), (i * CRABBY_HEIGHT_DEFAULT), CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }
}
