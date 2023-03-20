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

public class GamePanel extends JPanel{

    private MouseInputs mouseInputs;
    private float xDelta = 100;
    private float yDelta = 100;
    private BufferedImage img;

    public GamePanel() {
        mouseInputs = new MouseInputs(this);

        importImg();
        setPanelSize();

        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void importImg(){
        InputStream is = getClass().getResourceAsStream("/player_sprite.png");

        try{
            img = ImageIO.read(is);
        }catch(IOException e){
            e.printStackTrace();
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

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.drawImage(img, (int)xDelta, (int)yDelta, 128, 80, null);
    }
}
