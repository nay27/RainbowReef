/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Observable;

/**
 *
 * @author curbsidece
 */
public class Bricks extends GameObject {
    
    int id;
    boolean powerup = false, heal = false;
    
    Bricks(int x, int y, String img, Game game){
        super(x,y,img,game);
        
        switch (img){
            case "Block1": 
                this.id = 1;
                breakable = true;
                break;
            case "Block2": 
                this.id = 2;
                breakable = true;
                break;
            case "Block3": 
                this.id = 3;
                breakable = true;
                break;
            case "Block4": 
                this.id = 4;
                breakable = true;
                break;
            case "Block5": 
                this.id = 5;
                breakable = true;
                break;
            case "Block6": 
                this.id = 6;
                breakable = true;
                break;
            case "Block7": 
                this.id = 7;
                breakable = true;
                break;  
            case "Block_life":
                this.id = 8;
                breakable = true;
                heal = true;
                break;
            case "Block_split":
                this.id = 9;
                breakable = true;
                powerup = true;
                break;
        }
        
    }

    @Override
    public void update(Observable obv, Object o) {
         
    }

    public void checkCollision() {
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if(state){
            g2.drawImage(sprite, x, y, null);
        }
    }

  
}
