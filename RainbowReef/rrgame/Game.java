/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author jmendo12
 */
public class Game extends JPanel implements Runnable{
       private boolean isRunning = false;
    private int fps = 60;
    private int frameCount = 0;
    private final double GAME_HERTZ = 30.0;
    public static final int SCREEN_WIDTH = 960;
    public static final int SCREEN_HEIGHT = 620;
    private BufferedImage gameWorld;
    private ArrayList<GameObject> gameObjects;
    private ArrayList<GameEvents> gameEvents;
    
    static HashMap<String, BufferedImage> spritesMap;
    
       
    //Constructor
    Game(){
        super();
        gameWorld = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        gameObjects = new ArrayList<>();
        gameEvents = new ArrayList<>();
        init();
    }
    //Thread method
    @Override
    public void run(){
        new Window(SCREEN_WIDTH, SCREEN_HEIGHT ,"Rainbow Reef", this);
        this.isRunning = true;
        gameLoop();
        
    }
    //Game loop, rendering, and updating methods
    private void gameLoop(){
        final double TIME_BTWN_UPDATES = 1000000000 / GAME_HERTZ;

        final int MAX_UPDATES_TO_RENDER = 1;

        double lastUpdateTime = System.nanoTime();

        double lastRenderTime = System.nanoTime();

        final double TARGET_FPS = 60;
        final double TARGET_RENDER_TIME = 1000000000 / TARGET_FPS;

        int lastSecondTime = (int) (lastUpdateTime / 1000000000);
      
        double now;

        while (isRunning) {

            now = System.nanoTime();
            int updateCount = 0;

            while (now - lastUpdateTime > TIME_BTWN_UPDATES
                    && updateCount < MAX_UPDATES_TO_RENDER) {
                update();
                lastUpdateTime += TIME_BTWN_UPDATES;
                updateCount++;
            }

            if (now - lastUpdateTime > TIME_BTWN_UPDATES) {
                lastUpdateTime = now - TIME_BTWN_UPDATES;
            }
            
             int tempnow = (int) (now/1000000000.0);
             
                    
                      
            

            //float interpolation = (float) Math.min(1.0f,
            //         (now - lastUpdateTime) / TIME_BTWN_UPDATES );
            this.repaint();
            this.setFocusable(isRunning);
            this.requestFocusInWindow(isRunning);

            lastRenderTime = now;

            int thisSecond = (int) (lastUpdateTime / 1000000000);
            if (thisSecond > lastSecondTime) {
                //System.out.println("New Second " + thisSecond + " " + frameCount);
                fps = frameCount;
                //System.out.println("FPS is :" + fps);
                frameCount = 0;
                lastSecondTime = thisSecond;
            }

            while (now - lastRenderTime < TARGET_RENDER_TIME
                    && now - lastUpdateTime < TIME_BTWN_UPDATES) {
                Thread.yield();
                try {
                    Thread.sleep(1000 / 144);
                } catch (Exception e) {

                }

                now = System.nanoTime();
            }

        }
    }
    
    private void init(){
        Player player = new Player(SCREEN_WIDTH/2,500,"Katch");
        GameEvents playerE = new GameEvents();
        playerE.addObserver(player);
        Controls playerControl = new Controls (player, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT);
        this.addKeyListener(playerControl);
        gameObjects.add(player);
        gameEvents.add(playerE);
        
        
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = (Graphics2D) gameWorld.createGraphics();
        
        for(int i = 0; i < gameObjects.size(); i++){
            GameObject temp = gameObjects.get(i);
            temp.render(buffer);
        }
        g2.drawImage(gameWorld, null, 0, 0);
        g2.dispose();
    }
    
    private void update(){
        for(int i = 0; i<gameObjects.size(); i++){
            GameObject temp = gameObjects.get(i);
            GameEvents tempE = gameEvents.get(i);
            
            tempE.addObserver(temp);
            tempE.setChanged();
            tempE.notifyObservers();
        }
    }
    
    public static void main(String [] args){
        Game glt = new Game();
        Thread me = new Thread(glt);
        me.start();
    }
}
