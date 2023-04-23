package entities;

import static utilz.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;

import main.Game;

public class Crabby extends Enemy{

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(x, y, (int)(22 * Game.SCALE), (int)(19 * Game.SCALE));
    }

    protected void drawHitbox(Graphics g, int xLvlOffset){
        g.setColor(Color.RED);
        g.drawRect((int)(hitbox.x - xLvlOffset), (int)(hitbox.y), (int)(hitbox.width), (int)(hitbox.height));
    }

    public void update(int[][] lvlData, Player player){
        updateMove(lvlData, player);
        updateAnimationTick();
    }

    private void updateMove(int[][] lvlData, Player player){
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
            }
        }
    }
}
