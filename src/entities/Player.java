package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;

import static utilz.Constants.PlayerConstants.*;

public class Player extends Entity{
    private BufferedImage[][] animations;
    private int aniIndex, aniTick, aniSpeed = 25;
    private int playerAction = IDLE;
    private boolean left, up, right, down;
    private boolean moving = false, attacking = false;
    private float playerSpeed = 2.0f;

    public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimation();
	}

    public void update(){
        updatePos();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g){
        g.drawImage(animations[playerAction][aniIndex], (int)x, (int)y, width, height, null);
    }
    
    private void updateAnimationTick() {
        aniTick++;
        if(aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(playerAction)){
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;
        if(moving){
            playerAction = RUNNING;
        }else{
            playerAction = IDLE;
        }
        if(attacking){
            playerAction = ATTACK_1;
        }
        if(startAni != playerAction){
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = false;
        if(left && !right){
            x -= playerSpeed;
            moving = true;
        }else if(right && !left){
            x += playerSpeed;
            moving = true;
        }
        if(up && !down){
            y -= playerSpeed;
            moving = true;
        }else if(down && !up){
            y += playerSpeed;
            moving = true;
        }
    }

    private void loadAnimation(){
        animations = LoadSave.GetPlayerAtlas();
    }

    public void resetDirBools(){
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public boolean isLeft(){
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }
}
