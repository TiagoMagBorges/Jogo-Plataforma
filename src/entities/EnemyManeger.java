package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
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

    public void update(int lvlData[][], Player player){
        for(Crabby c : crabbies){
            if(c.isActive()){
                c.update(lvlData, player);
            }
        }
    }

    public void draw(Graphics g, int xLvelOffset){
        drawCrabs(g, xLvelOffset);
    }

    private void drawCrabs(Graphics g, int xLvelOffset) {
        for(Crabby c : crabbies){
            if(c.isActive()){
                g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()], (int)(c.getHitbox().x - CRABBY_DRAWOFFSET_X - xLvelOffset + c.flipX()), (int)(c.getHitbox().y - CRABBY_DRAWOFFSET_Y), (CRABBY_WIDTH * c.flipW()), CRABBY_HEIGHT, null);
                //c.drawHitbox(g, xLvelOffset);
                //c.drawAttackBox(g, xLvelOffset);
            }
        }
    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas("crabby_sprite.png");
        for(int j = 0; j < crabbyArr.length; j++){
            for(int i = 0; i < crabbyArr[j].length; i++){
                crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox){
        for(Crabby c : crabbies){
            if(c.isActive()){
                if(attackBox.intersects(c.getHitbox())){
                    c.hurt(10);
                    return;
                }
            }
        }
    }

    public void resetAllEnemies() {
        for(Crabby c : crabbies){
            c.resetEnemy();
        }
    }   
}
