/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Observable;

/**
 *
 * @author jmendo12
 */
public class Pop extends GameObject{
    
    private int lives;
    private double angle;
    private final double Y_VEL;
    private final int SPAWN_Y;
    boolean collide = false;
    
    Pop(int x, int y, String img, Game game){
        
        super(x, y, img, game);
        Y_VEL = -9.81;
        angle = 0;
        SPAWN_Y = y;
        lives = 3;
    }
    
    public int getLives(){
        return lives;
    }
    
    @Override
    public void update(Observable obv, Object o) {
        move();
        checkCollision();
        checkLives();
    }
    
    private void move(){
        if(checkCollision(new Rectangle())){
            
        }else{
            y -= Y_VEL;
            checkBorder();
        }
      
    }
    
    public boolean checkCollision() {
        int size = game.getObjectListSize();
        
        for(int i =0; i< size; i++){
            GameObject temp = game.getObject(i);
            if(this.hitBox.intersects(temp.hitBox)){
                collide = true;
                if(temp instanceof Bricks){
                    ((Bricks) temp).state = false;
                }
            }
        }
        return collide;
    }
    
    private void checkLives(){
        if(lives == 0){
            state = false;
        }else{}
    }
    
    @Override
    void checkBorder(){
        if(x + getWidth() >= Game.SCREEN_WIDTH){
            x = Game.SCREEN_WIDTH - getWidth();
        }
        else if(x <= 0){
            x = 0;
        }
        
        if(y >= Game.SCREEN_HEIGHT){
            y = SPAWN_Y;
            //--lives;
        }
        else if(y <= 0){
            y = 0;
        }
    }
    
    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if(state){
            g2.drawImage(sprite, null, x, y);
            System.out.println(toString());
        }
    }
    
    @Override
    public String toString(){
        return "y is " + y + " angle is " + angle +
                " lives left " + lives;
    }
}
