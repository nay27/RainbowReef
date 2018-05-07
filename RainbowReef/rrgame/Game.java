/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author jmendo12
 */
public class Game extends JPanel implements Runnable{
    
    private boolean isRunning = false;
    public static final int SCREEN_WIDTH = 960;
    public static final int SCREEN_HEIGHT = 720;
    private BufferedImage gameWorld;
    private ArrayList<GameObject> gameObjects;
    private ArrayList<GameEvents> gameEvents;
    
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
        while(isRunning){
            repaint();
            update();
        }
    }
    
    private void init(){
        Player player = new Player(0,0,"Katch");
        gameObjects.add(player);
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
        
    }
    
    private void update(){
        
    }
    
    public static void main(String [] args){
        Game glt = new Game();
        Thread me = new Thread(glt);
        me.start();
    }
}
