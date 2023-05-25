package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import entities.EnemyManeger;
import entities.Player;
import levels.LevelManeger;
import main.Game;
import objects.ObjectManeger;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods{
    private Player player;
    private LevelManeger levelManeger;
    private EnemyManeger enemyManeger;
    private ObjectManeger objectManeger;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private boolean paused = false;
    private int xLvelOffset;
    private int leftBorder = (int)(0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.8 * Game.GAME_WIDTH);
    private int maxLvlOffsetX;
    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private int[] smallCloudsPos;
    private Random rnd = new Random();
    private boolean gameOver;
    private boolean levelCompleted;

    public Playing(Game game) {
        super(game);
        initClasses();
        backgroundImg = LoadSave.GetSpriteAtlas("playing_bg_img.png");
        bigCloud = LoadSave.GetSpriteAtlas("big_clouds.png");
        smallCloud = LoadSave.GetSpriteAtlas("small_clouds.png");
        smallCloudsPos = new int[8];
        for(int i = 0; i < smallCloudsPos.length; i++){
            smallCloudsPos[i] = (int)(90*Game.SCALE) + rnd.nextInt((int)(100*Game.SCALE));
        }
        calculateLevelOffset();
        loadStartLevel();
    }

    @Override
    public void update() {
        if(paused){
            pauseOverlay.update();
        }else if(levelCompleted){
            levelCompletedOverlay.update();
        }else if(!gameOver){
            levelManeger.update();
            objectManeger.update();
            player.update();
            enemyManeger.update(levelManeger.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawClouds(g);
        levelManeger.draw(g, xLvelOffset);
        player.render(g, xLvelOffset);
        enemyManeger.draw(g, xLvelOffset);
        objectManeger.draw(g, xLvelOffset);
        if(paused){
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }else if(gameOver){
            gameOverOverlay.draw(g);
        }else if(levelCompleted){
            levelCompletedOverlay.draw(g);
        }
    }

    private void initClasses() {
        levelManeger = new LevelManeger(game);
        enemyManeger = new EnemyManeger(this);
        objectManeger= new ObjectManeger(this);
        player = new Player(200, 200, (int)(64 * Game.SCALE), (int)(40 * Game.SCALE), this);
        player.setLevelData(levelManeger.getCurrentLevel().getLevelData());
        player.setSpawn(levelManeger.getCurrentLevel().getPlayerSpawn());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    private void calculateLevelOffset() {
        maxLvlOffsetX = levelManeger.getCurrentLevel().getLevelOffset();
    }

    private void loadStartLevel() {
        enemyManeger.loadEnemies(levelManeger.getCurrentLevel());
        objectManeger.loadObjects(levelManeger.getCurrentLevel());
    }

    public void loadNextLevel(){
        resetAll();
        levelManeger.loadNextLevel();
        player.setSpawn(levelManeger.getCurrentLevel().getPlayerSpawn());
    }

    public void unpauseGame(){
        paused = false;
    }

    public void windowFocusLost(){
        player.resetDirBools();
    }

    public Player getPlayer(){
        return player;
    }

    private void drawClouds(Graphics g) {
        for(int i = 0; i < 3; i++){
            g.drawImage(bigCloud, (i * BIG_CLOUD_WIDTH - (int)(xLvelOffset * 0.3)), (int)(204*Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }for(int i = 0; i < smallCloudsPos.length; i++){
            g.drawImage(smallCloud, (SMALL_CLOUD_WIDTH * 4 * i-(int)(xLvelOffset * 0.7)), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManeger.checkObjectTouched(hitbox);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManeger.checkObjectHit(attackBox);
    }

    public void checkSpikeTouched(Player p) {
        objectManeger.checkSpikeTouched(p);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver){
            gameOverOverlay.keyPressed(e);
        }else{
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
                case KeyEvent.VK_BACK_SPACE:
                    Gamestate.state = Gamestate.MENU;
                    break;
                case KeyEvent.VK_ESCAPE:
                    paused = !paused;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!gameOver){
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
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gameOver){
            if(e.getButton() == MouseEvent.BUTTON1){
                player.setAttacking(true);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver){
            if(paused){
                pauseOverlay.mouseMoved(e);
            }else if(levelCompleted){
                levelCompletedOverlay.mouseMoved(e);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver){
            if(paused){
                pauseOverlay.mousePressed(e);
            }else if(levelCompleted){
                levelCompletedOverlay.mousePressed(e);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver){
            if(paused){
                pauseOverlay.mouseReleased(e);
            }else if(levelCompleted){
                levelCompletedOverlay.mouseReleased(e);
            }
        }
    }

    public void mouseDragged(MouseEvent e){
        if(!gameOver){
            if(paused){
                pauseOverlay.mouseDragged(e);
            }
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int)(player.getHitbox().x);
        int diff = playerX - xLvelOffset;
        if(diff > rightBorder){
            xLvelOffset += diff - rightBorder;
        }else if(diff < leftBorder){
            xLvelOffset += diff - leftBorder;
        }
        if(xLvelOffset > maxLvlOffsetX){
            xLvelOffset = maxLvlOffsetX;
        }else if(xLvelOffset < 0){
            xLvelOffset = 0;
        }
    }

    public void resetAll(){
        gameOver = false;
        paused = false;
        levelCompleted = false;
        player.resetAll();
        enemyManeger.resetAllEnemies();
        objectManeger.resetAllObjects();
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManeger.checkEnemyHit(attackBox);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public EnemyManeger getEnemyManeger() {
        return enemyManeger;
    }

    public void setMaxLvlOffset(int maxLvlOffsetX){
        this.maxLvlOffsetX = maxLvlOffsetX;
    }

    public void setLevelCompleted(boolean levelCompleted) {
        this.levelCompleted = levelCompleted;
    }

    public ObjectManeger getObjectManeger(){
        return objectManeger;
    }

    public LevelManeger getLevelManeger() {
        return levelManeger;
    }
}
