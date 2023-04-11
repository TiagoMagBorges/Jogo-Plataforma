package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entities.Player;
import levels.LevelManeger;
import main.Game;

public class Playing extends State implements Statemethods{
    private Player player;
    private LevelManeger levelManeger;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        levelManeger = new LevelManeger(game);
        player = new Player(200, 200, (int)(64 * Game.SCALE), (int)(40 * Game.SCALE));
        player.setLevelData(levelManeger.getCurrentLevel().getLevelData());
    }

    public void windowFocusLost(){
        player.resetDirBools();
    }

    public Player getPlayer(){
        return player;
    }

    @Override
    public void draw(Graphics g) {
        levelManeger.draw(g);
        player.render(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE:
                Gamestate.state = Gamestate.MENU;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            player.setAttacking(true);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update() {
        levelManeger.update();
        player.update();
    }

    
}
