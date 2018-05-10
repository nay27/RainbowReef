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
    
    Bricks(int x, int y, String img, Game game){
        super(x,y,img,game);
        
        switch (img){
            case "Block1": this.id = 1;
                    break;
            case "Block2": this.id = 2;
                    break;
            case "Block3": this.id = 3;
                    break;
            case "Block4": this.id = 4;
                    break;
            case "Block5": this.id = 5;
                    break;
            case "Block6": this.id = 6;
                    break;
            case "Block7": this.id = 7;
                    break;
            
        }
        
    }

    @Override
    public void update(Observable obv, Object o) {
         
    }

    public boolean checkCollision(Rectangle rec) {
        return this.hitBox.intersects(rec);
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
