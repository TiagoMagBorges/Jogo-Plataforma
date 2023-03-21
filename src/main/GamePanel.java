package main;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import static utilz.Constants.Directions.*;
import static utilz.Constants.PlayerConstants.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel{

    private MouseInputs mouseInputs;
    private float xDelta = 100;
    private float yDelta = 100;
    private BufferedImage animations[][];
    private int aniTick, aniIndex, aniSpeed = 17;
    private int playerAction = IDLE;
    private int playerDir = -1;
    private boolean moving = false;

    public GamePanel() {
        mouseInputs = new MouseInputs(this);
        loadAnimation();
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void loadAnimation(){
        animations = new BufferedImage[9][6];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 6; j++){
                if((i == 0 && j < 5) || (i == 1) || (i == 2 && j < 3) || (i == 3 && j == 0) || (i == 4 && j < 2) || (i == 5 && j < 4) || (i == 6 && j < 3) || (i == 7 && j < 3) || (i == 8 && j < 3)){
                    InputStream is = getClass().getResourceAsStream(String.format("/player%d%d.png", i, j));
                    try{
                        animations[i][j] = ImageIO.read(is);
                    }catch(IOException e){
                        e.printStackTrace();
                    }finally{
                        try {
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void setPanelSize(){
        Dimension size = new Dimension(1280, 800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void setDirection(int direction){
        this.playerDir = direction;
        moving = true;
    }

    public void setMoving(boolean moving){
        this.moving = moving;
    }

    private void updateAnimationTick() {
        aniTick++;
        if(aniTick >= aniSpeed){
            aniTick = 0;
            aniIndex++;
            if(aniIndex >= GetSpriteAmount(playerAction)){
                aniIndex = 0;
            }
        }
    }

    private void setAnimation() {
        if(moving){
            playerAction = RUNNING;
        }else{
            playerAction = IDLE;
        }
    }

    private void updatePos() {
        if(moving){
            switch (playerDir) {
                case LEFT:
                    xDelta -= 5;
                    break;
                case UP:
                    yDelta -= 5;
                    break;
                case RIGHT:
                    xDelta += 5;
                    break;
                case DOWN:
                    yDelta += 5;
                    break;
            }
        }
    }

    public void updateGame(){
        updateAnimationTick();
        setAnimation();
        updatePos();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        g.drawImage(animations[playerAction][aniIndex], (int)xDelta, (int)yDelta, 128, 80, null);
    }
}
