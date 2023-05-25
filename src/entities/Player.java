package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import gamestates.Playing;
import main.Game;
import utilz.LoadSave;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.GRAVITY;
import static utilz.Constants.ANI_SPEED;;

public class Player extends Entity{
    private Playing playing;
    private BufferedImage[][] animations;
    private boolean left, right, jump;
    private boolean moving = false, attacking = false;
    private int levelData[][];
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private BufferedImage statusBarImg;
    private int statusBarWidth = (int)(192 * Game.SCALE);
    private int statusBarHeight = (int)(58 * Game.SCALE);
    private int statusBarX = (int)(10 * Game.SCALE);
    private int statusBarY = (int)(10 * Game.SCALE);
    private int healthBarWidth = (int)(150 * Game.SCALE);
    private int healthBarHeight = (int)(4 * Game.SCALE);
    private int healthBarXStart = (int)(34 * Game.SCALE);
    private int healthBarYStart = (int)(14 * Game.SCALE);
    private int healthWidth = healthBarWidth;
    private int flipX = 0;
    private int flipW = 1;
    private boolean attackChecked;


    public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.walkSpeed = 1.0f * Game.SCALE;
		loadAnimation();
        initHitbox(20, 27);
        initAttackBox();
	}

    public void update(){
        updateHealthBar();
        if(currentHealth <= 0){
            playing.setGameOver(true);
            return;
        }
        updateAttackBox();
        updatePos();
        if(moving){
            checkPotionTouched();
            checkSpikeTouched();
        }
        if(attacking){
            checkAttack();
        }
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g, int lvlOffset){
        g.drawImage(animations[state][aniIndex], (int)(hitbox.x - xDrawOffset - lvlOffset + flipX), (int)(hitbox.y - yDrawOffset), (width * flipW), height, null);
        //drawHitbox(g, lvlOffset);
        //drawAttackBox(g, lvlOffset);
        drawUI(g);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkSpikeTouched(){
        playing.checkSpikeTouched(this);
    }

    public void setSpawn(Point spawm){
        this.x = spawm.x;
        this.y = spawm.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void checkAttack() {
        if(attackChecked || aniIndex != 1){
            return;
        }
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
    }

    public void updateAttackBox(){
        if(right){
            attackBox.x = hitbox.x;
        }else if(left){
            attackBox.x = hitbox.x - hitbox.width - 6;
        }
        attackBox.y = hitbox.y + (Game.SCALE * 10);
    }

    private void initAttackBox(){
        attackBox = new Rectangle2D.Float(x, y, (int)(45 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    private void updateHealthBar() {
        healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
    }
    
    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect((healthBarXStart + statusBarX), (healthBarYStart + statusBarY), healthWidth, healthBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if(aniTick >= ANI_SPEED){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(state)){
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = state;
        if(moving){
            state = RUNNING;
        }else{
            state = IDLE;
        }

        if(inAir){
            if(airSpeed < 0){
                state = JUMP;
            }else{
                state = FALLING;
            }
        }

        if(attacking){
            state = ATTACK;
            if(startAni != ATTACK){
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }
        if(startAni != state){
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = false;

        if(jump){
            jump();
        }
        
        if(!inAir){
            if((!left && !right) || (left && right)){
                return;
            }
        }

        float xSpeed = 0;

        if(left){
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if(right){
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if(!inAir){
            if(!IsEntityOnFloor(hitbox, levelData)){
                inAir = true;
            }
        }

        if(inAir){
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)){
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            }else{
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if(airSpeed > 0){
                    resetInAir();
                }else{
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }
        }else{
            updateXPos(xSpeed);
        }
        moving = true;
    }

    private void jump() {
        if(inAir){
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere((hitbox.x + xSpeed), hitbox.y, hitbox.width, hitbox.height, levelData)){
            hitbox.x += xSpeed;
        }else{
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    public void changeHealth(int value){
        currentHealth += value;
        if(currentHealth <= 0){
            currentHealth = 0;
        }else if(currentHealth >= maxHealth){
            currentHealth = maxHealth;
        }
    }

    public void kill(){
        currentHealth = 0;
    }

    public void changePower(int value){
        // Adiciona poder.
    }

    private void loadAnimation(){
        BufferedImage img = LoadSave.GetSpriteAtlas("player_sprites.png");
        animations = new BufferedImage[7][8];
        for(int i = 0; i < animations.length; i++){
            for(int j = 0; j < animations[i].length; j++){
                animations[i][j] = img.getSubimage(j * 64, i * 40, 64, 40);
            }
        }

        statusBarImg = LoadSave.GetSpriteAtlas("health_power_bar.png");
    }

    public void resetDirBools(){
        left = false;
        right = false;
    }

    public void resetAll() {
        resetDirBools();
        inAir = false;
        attacking = false;
        moving = false;
        state = IDLE;
        currentHealth = maxHealth;
        hitbox.x = x;
        hitbox.y = y;
        if(!IsEntityOnFloor(hitbox, levelData)){
            inAir = true;
        }
    }

    public void setLevelData(int levelData[][]){
        this.levelData = levelData;
        if(!IsEntityOnFloor(hitbox, levelData)){
            inAir = true;
        }
    }

    public boolean isLeft(){
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setAttacking(boolean attacking){
        this.attacking = attacking;
    }

    public void setJump(Boolean jump){
        this.jump = jump;
    }
}
