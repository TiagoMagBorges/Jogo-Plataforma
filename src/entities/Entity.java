package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected Rectangle2D.Float attackBox;
    protected int aniIndex, aniTick;
    protected int state;
    protected float airSpeed;
    protected boolean inAir;
    protected int maxHealth;
    protected int currentHealth;
    protected float walkSpeed;

    public Entity(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
		this.height = height;
    }

    protected void drawHitbox(Graphics g, int xLvlOffset){
        g.setColor(Color.GREEN);
        g.drawRect((int)(hitbox.x - xLvlOffset), (int)(hitbox.y), (int)(hitbox.width), (int)(hitbox.height));
    }

    protected void drawAttackBox(Graphics g, int lvlOffset) {
        g.setColor(Color.RED);
        g.drawRect((int)(attackBox.x - lvlOffset), (int)(attackBox.y), (int)(attackBox.width), (int)(attackBox.height));
    }

    protected void initHitbox(int width, int height){
        hitbox = new Rectangle2D.Float(x, y, (int)(width * Game.SCALE), (int)(height * Game.SCALE));
    }

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    public int getState(){
        return state;
    }

    public int getAniIndex(){
        return this.aniIndex;
    }
}
