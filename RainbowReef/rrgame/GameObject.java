/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.Graphics;
import java.awt.Rectangle;
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
    
    protected int x, y;
    protected int width, height;
    protected boolean state;
    protected boolean breakable;
    protected BufferedImage sprite;
    protected Rectangle hitBox;
    protected Game game;
    private static HashMap<String, BufferedImage> spritesMap;
    
    static{
        spritesMap = new HashMap<>();
        try{
            BufferedImage bigleg = ImageIO.read(new File("rrresources/Bigleg.gif"));
            BufferedImage block1 = ImageIO.read(new File("rrresources/Block1.gif"));
            BufferedImage block2 = ImageIO.read(new File("rrresources/Block2.gif"));
            BufferedImage block3 = ImageIO.read(new File("rrresources/Block3.gif"));
            BufferedImage block4 = ImageIO.read(new File("rrresources/Block4.gif"));
            BufferedImage block5 = ImageIO.read(new File("rrresources/Block5.gif"));
            BufferedImage block6 = ImageIO.read(new File("rrresources/Block6.gif"));
            BufferedImage block7 = ImageIO.read(new File("rrresources/Block7.gif"));
            BufferedImage blockDouble = ImageIO.read(new File("rrresources/Block_double.gif"));
            BufferedImage blockLife = ImageIO.read(new File("rrresources/Block_life.gif"));
            BufferedImage blockSolid = ImageIO.read(new File("rrresources/Block_solid.gif"));
            BufferedImage blockSplit = ImageIO.read(new File("rrresources/Block_split.gif"));
            BufferedImage katch = ImageIO.read(new File("rrresources/Katch.gif"));
                BufferedImage pop = ImageIO.read(new File("rrresources/pop.png"));
            spritesMap.put("BigLeg", bigleg);
            spritesMap.put("Block1", block1);
            spritesMap.put("Block2", block2);
            spritesMap.put("Block3", block3);
            spritesMap.put("Block4", block4);
            spritesMap.put("Block5", block5);
            spritesMap.put("Block6", block6);
            spritesMap.put("Block7", block7);
            spritesMap.put("Block_double", blockDouble);
            spritesMap.put("Block_life", blockLife);
            spritesMap.put("Block_solid", blockSolid);
            spritesMap.put("Block_split", blockSplit);
            spritesMap.put("Katch", katch);
            spritesMap.put("Pop", pop);
        }catch(IOException e){
            e.getMessage();
            System.out.println(e);
        }
    }
       
    GameObject(int x, int y, String img, Game currentGame){
        this.x = x;
        this.y = y;
        this.sprite = GameObject.getSprite(img);
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
        state = true;
        breakable = false;
        game = currentGame;
        this.hitBox = new Rectangle(x,y, this.width, this.height);
        
    }
    
  
    public static BufferedImage getSprite(String token){
        return spritesMap.get(token);
    }
    
    @Override
    public abstract void update(Observable obv, Object o);
    public abstract void checkCollision();
    public abstract void render(Graphics g);    
    
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
    final Rectangle getBounds(){
        return hitBox.getBounds();
    }
    
    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }
    
    public boolean isBreakable() {
        return breakable;
    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }
    
    void checkBorder(){
        if(x + getWidth() >= Game.SCREEN_WIDTH){
            x = Game.SCREEN_WIDTH - getWidth();
        }
        else if(x <= 0){
            x = 0;
        }
        
        if(y + getHeight() >= Game.SCREEN_HEIGHT){
            y = Game.SCREEN_HEIGHT - getHeight();
        }
        else if(y <= 0){
            y = 0;
        }
    }
}
