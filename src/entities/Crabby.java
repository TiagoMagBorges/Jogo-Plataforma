package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public class Crabby extends Enemy{
    private Rectangle2D.Float attackBox;
    private int attackBoxOffsetX;
    
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int)(22 * Game.SCALE), (int)(19 * Game.SCALE));
        initAttackBox();
    }

    public void update(int[][] lvlData, Player player){
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    public void drawAttackBox(Graphics g, int xLvlOffset){
        g.setColor(Color.GREEN);
        g.drawRect((int)(attackBox.x - xLvlOffset), (int)(attackBox.y), (int)(attackBox.width), (int)(attackBox.height));
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(82 * Game.SCALE), (int)(19 * Game.SCALE));
        attackBoxOffsetX = (int)(Game.SCALE * 30);
    }

    protected void drawHitbox(Graphics g, int xLvlOffset){
        g.setColor(Color.RED);
        g.drawRect((int)(hitbox.x - xLvlOffset), (int)(hitbox.y), (int)(hitbox.width), (int)(hitbox.height));
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    private void updateBehavior(int[][] lvlData, Player player){
        if(firstUpdate){
            firstUpdateCheck(lvlData);
        }
        if(inAir){
            updateinAir(lvlData);
        }else{
            switch (enemyState) {
                case IDLE:
                    newState(RUNNING);;
                    break;
                case RUNNING:
                    if(cansSeePlayer(lvlData, player)){
                        turnTowardsPlayer(player);
                    }
                    if(isPlayerCloseForAttack(player)){
                        newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if(aniIndex == 0){
                        attackChecked = false;
                    }
                    if(aniIndex == 3 && !attackChecked){
                        checkPlayerHit(player, attackBox);
                    }
                    break;
                case HIT:
                    break;
            }
        }
    }

    public int flipX(){
        if(walkDir == RIGHT){
            return width;
        }else{
            return 0;
        }
    }

    public int flipW(){
        if(walkDir == RIGHT){
            return -1;
        }else{
            return 1;
        }
    }
}
