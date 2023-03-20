package main;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import static utils.Constants.PlayerConstants.*;

public class GamePanel extends JPanel{

    private MouseInputs mouseInputs;
    private float xDelta = 100;
    private float yDelta = 100;
    private BufferedImage img;
    private BufferedImage animations[][];
    private int aniTick, aniIndex, aniSpeed = 17;
    private int playerAction = IDLE;

    public GamePanel() {
        mouseInputs = new MouseInputs(this);

        importImg();
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

    private void importImg(){
        InputStream is = getClass().getResourceAsStream("/player00.png");

            try{
                img = ImageIO.read(is);
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

    private void setPanelSize(){
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    public void changeXDelta(float x){
        this.xDelta += x;
    }

    public void changeYDelta(float y){
        this.yDelta += y;
    }

    public void setRectPos(float x, float y){
        this.xDelta = x;
        this.yDelta = y;
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

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        updateAnimationTick();

        g.drawImage(animations[playerAction][aniIndex], (int)xDelta, (int)yDelta, 128, 80, null);
    }
}
