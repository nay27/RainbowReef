/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author jmendo12
 */
public class Game extends JPanel implements Runnable{
    
    private boolean isRunning = false;
    private static final int SCREEN_WIDTH = 960;
    private static final int SCREEN_HEIGHT = 720;
    private BufferedImage gameWorld;
    
    Game(){
        super();
        gameWorld = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        init();
    }
    
    @Override
    public void run(){
        new Window(SCREEN_WIDTH, SCREEN_HEIGHT ,"Rainbow Reef", this);
        this.isRunning = true;
        gameLoop();
    }
    
    private void gameLoop(){
        while(isRunning){
            
        }
    }
    
    private void init(){
        
    }
    
    public static void main(String [] args){
        Game glt = new Game();
        Thread me = new Thread(glt);
        me.start();
    }
}
