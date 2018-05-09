package RainbowReef.rrgame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author jmendo12
 */
public class Player extends GameObject implements Observer {
    
    //Key tracking, player can only move horizontally
    private boolean rightPressed;
    private boolean leftPressed;
    private final int SPEED;
    
    Player(int x, int y, String img){
        super(x,y,img);
        this.SPEED = 1;
        
    }
    //Getters, setters, and togglers
    public void toggleRightPressed() {
        this.rightPressed = true; 
    }

    public void toggleLeftPressed() {
        this.leftPressed = true;
    }
    
    public void unToggleRightPressed() {
        this.rightPressed = false;
    }

    public void unToggleLeftPressed() {
        this.leftPressed = false;
    }
    //Update and movement methods
    @Override
    public void update(Observable obv, Object o) {
        if(rightPressed){
            moveRight();
        }
        if(leftPressed){
            moveLeft();
        }
        //The newly created Rectangle is just a filler argument - real arg can
        //Be filled in later
        checkCollision(new Rectangle());
    }
    
    private void moveRight(){
        x += SPEED;
        checkBorder();
    }
    
    private void moveLeft(){
        x -= SPEED;
        checkBorder();
    }

    @Override
    public boolean checkCollision(Rectangle hitBox) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return false;
    }
    //Equality and identification methods
    
    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
    //Rendering related methods
    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if(state){
            g2.drawImage(sprite, null, x, y);
            System.out.println(toString());
        }
    }
    //toString for debugging
    @Override
    public String toString(){
        return " x1 is " + x + " x2 is " + (x + getWidth());
    }
}
