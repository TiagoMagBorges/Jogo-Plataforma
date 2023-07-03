package objects;

import gamestates.Playing;
import levels.Level;
import main.Game;
import utilz.LoadSave;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import entities.Player;
import static utilz.Constants.ObjectConstant.*;
import static utilz.HelpMethods.CanCannonSeePlayer;

public class ObjectManeger {
    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage[] cannonImgs;
    private BufferedImage spikeImg;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;

    public ObjectManeger(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    public void update(int[][] lvlData, Player player){
        for(Potion p : potions){
            if(p.isActive()){
                p.update();
            }
        }
        for(GameContainer gc : containers){
            if(gc.isActive()){
                gc.update();
            }
        }
        updateCannons(lvlData, player);
    }

    public void draw(Graphics g, int xLvlOffset){
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
    }

    private void updateCannons(int[][] lvlData, Player player) {
        for(Cannon c : cannons){
            if(!c.doAnimation){
                if(c.getTileY() == player.getTileY()){
                    if(isPlayerInRange(c, player)){
                        if(isPlayerInFrontOfCannon(c, player)){
                            if(CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY())){
                                shootCanon(c);
                            }
                        }
                    }
                }
            }
            c.update();  
        }
    }
    
    private void shootCanon(Cannon c) {
        c.setAnimation(true);
    }

    private boolean isPlayerInFrontOfCannon(Cannon c, Player player) {
        if(c.getObjType() == CANNON_LEFT){
            if(c.getHitbox().x > player.getHitbox().x){
                return true;
            }
        }else if(c.getHitbox().x < player.getHitbox().x){
            return true;
        }
        return false;
    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue = (int)Math.abs(player.getHitbox().x - c.getHitbox().x);
        return absValue <= Game.TILES_SIZE * 5;
    }

    public void checkSpikeTouched(Player p){
        for(Spike s : spikes){
            if(s.getHitbox().intersects(p.getHitbox())){
                p.kill();
            }
        }
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox){
        for(Potion p : potions){
            if(p.isActive()){
                if(hitbox.intersects(p.getHitbox())){
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
        }
    }

    public void applyEffectToPlayer(Potion p){
        if(p.getObjType() == RED_POTION){
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        }else{
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
        }
    }

    public void checkObjectHit(Rectangle2D.Float attackbox){
        for(GameContainer gc : containers){
            if(gc.isActive() && !gc.doAnimation){
                if(gc.getHitbox().intersects(attackbox)){
                    gc.setAnimation(true);
                    int type = 0;
                    if(gc.getObjType() == BARREL){
                        type = 1;
                    }
                    potions.add(new Potion((int)(gc.getHitbox().x + gc.getHitbox().width / 2), (int)(gc.getHitbox().y - gc.getHitbox().height / 2), type));
                    return;
                }
            }
        }
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for(GameContainer gc : containers){
            if(gc.isActive()){
                int type = 0;
                if(gc.getObjType() == BARREL){
                    type = 1;
                }
                g.drawImage(containerImgs[type][gc.getAniIndex()], (int)(gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset), (int)(gc.getHitbox().y - gc.getyDrawOffset()), CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
            }
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for(Potion p : potions){
            if(p.isActive()){
                int type = 0;
                if(p.getObjType() == RED_POTION){
                    type = 1;
                }
                g.drawImage(potionImgs[type][p.getAniIndex()], (int)(p.getHitbox().x - p.getxDrawOffset() - xLvlOffset), (int)(p.getHitbox().y - p.getyDrawOffset()), POTION_WIDTH, POTION_HEIGHT, null);
            }
        }
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for(Spike s : spikes){
            g.drawImage(spikeImg, (int)(s.getHitbox().x - xLvlOffset), (int)(s.getHitbox().y - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
        }
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for(Cannon c : cannons){
            int x = (int)(c.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;
            if(c.getObjType() == CANNON_RIGHT){
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImgs[c.getAniIndex()], x, (int)(c.getHitbox().y), width, CANNON_HEIGHT, null);
        }
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas("potions_sprites.png");
        potionImgs = new BufferedImage[2][7];
        for(int i = 0; i < potionImgs.length; i++){
            for(int j = 0; j < potionImgs[i].length; j++){
                potionImgs[i][j] = potionSprite.getSubimage(12 * j, 16 * i, 12, 16);
            }
        }

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas("objects_sprites.png");
        containerImgs = new BufferedImage[2][8];
        for(int i = 0; i < containerImgs.length; i++){
            for(int j = 0; j < containerImgs[i].length; j++){
                containerImgs[i][j] = containerSprite.getSubimage(40 * j, 30 * i, 40, 30);
            }
        }

        spikeImg = LoadSave.GetSpriteAtlas("trap_atlas.png");

        cannonImgs = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas("cannon_atlas.png");
        for(int i = 0; i < cannonImgs.length; i++){
            cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);
        }
    }

    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
        cannons = newLevel.getCannons();
    }

    public void resetAllObjects() {
        loadObjects(playing.getLevelManeger().getCurrentLevel());
        for(Potion p : potions)
            p.reset();
        for(GameContainer gc : containers)
            gc.reset();
        for(Cannon c : cannons)
            c.reset();
    }
}
