/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;

/**
 * All game objects will inherit from this abstract class
 * @author jmendo12
 */
 public abstract class GameObject implements Observer{
    
    int x, y;
    int width, height;
    boolean state;
    BufferedImage sprite;
    private static HashMap<String, BufferedImage> spritesMap;
    
    GameObject(int x, int y, BufferedImage img){
        this.x = x;
        this.y = y;
        this.sprite = img;
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
        state = true;
    }
    
    static{
        spritesMap = new HashMap<>();
        try{
            BufferedImage bigleg = ImageIO.read(new File("rrresourcers/Bigleg.gif"));
            BufferedImage block1 = ImageIO.read(new File("rrresourcers/Block1.gif"));
            BufferedImage block2 = ImageIO.read(new File("rrresourcers/Block2.gif"));
            BufferedImage block3 = ImageIO.read(new File("rrresourcers/Block3.gif"));
            BufferedImage block4 = ImageIO.read(new File("rrresourcers/Block4.gif"));
            BufferedImage block5 = ImageIO.read(new File("rrresourcers/Block5.gif"));
            BufferedImage block6 = ImageIO.read(new File("rrresourcers/Block6.gif"));
            BufferedImage block7 = ImageIO.read(new File("rrresourcers/Block7.gif"));
            spritesMap.put("BigLeg", bigleg);
            spritesMap.put("Block1", block1);
            spritesMap.put("Block2", block2);
            spritesMap.put("Block3", block3);
            spritesMap.put("Block4", block4);
            spritesMap.put("Block5", block5);
            spritesMap.put("Block6", block6);
            spritesMap.put("Block7", block7);
            /*Ill fill in the rest later...the idea is just to populate a hashmap
            with images for every gameObject - note gameObjects are blocks,
            the player (Katch), Pop, and Bigleg*/
        }catch(IOException e){
            e.getMessage();
        }
    }
    
    @Override
    public abstract void update(Observable obv, Object o);
    public abstract boolean checkCollision();
    
    final void setX(int a){
        x = a;
    }
    
    final void setY(int a){
        y = a;
    }
    
    final void setWidth(int w){
        width = w;
    }
    
    final void setHeight(int h){
        height = h;
    }
    
    final void setSprite(BufferedImage img){
        sprite = img;
    }
    
    final void setState(boolean truth){
        state = truth;
    }
    
    final int getX(){
        return x;
    }
    
    final int getY(){
        return y;
    }
    
    final int getWidth(){
        return width;
    }
    
    final int getHeight(){
        return height;
    }
    
    final BufferedImage getSprite(){
        return sprite;
    }
    
    final boolean getState(){
        return state;
    }
    
}
