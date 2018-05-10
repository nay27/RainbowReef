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
    private final double SPEED;
    private double xPoint; 
    private final double yPoint;
    public static final double MAX_X = 960 - 40;
    public static final double MIN_X = 0 + 40;
    
    Player(int x, int y, String img, Game game){
        super(x, y, img, game);
        xPoint = (double) x;
        yPoint = (double) y;
        SPEED = .025;
        
    }
    //Getters, setters, and togglers
    public boolean isLeftPressed() {
        return leftPressed;
    }
    public boolean isRightPressed(){
        return rightPressed;
    }
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
        xPoint += SPEED;
        x = (int) xPoint;
        checkBorder();
    }
    
    private void moveLeft(){
        xPoint -= SPEED;
        x = (int) xPoint;
        checkBorder();
    }

    @Override
    public boolean checkCollision(Rectangle hitBox) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return false;
    }
    
    @Override
    public void checkBorder(){
            if(x + getWidth() >= Player.MAX_X){
                x = (int) Player.MAX_X - getWidth();
            }
            else if(x < Player.MIN_X){
                x = (int) Player.MIN_X;
            }
        
            if(y + getHeight() >= Game.SCREEN_HEIGHT){
                y = Game.SCREEN_HEIGHT - getHeight();
            }
            else if(y <= 0){
                y = 0;
            }
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
        if(state)
            g2.drawImage(sprite, null, x, y);
    }
    //toString for debugging
    @Override
    public String toString(){
        return " x1 is " + x + " x2 is " + (x + getWidth());
    }

    public double getxPoint() {
        return xPoint;
    }

    public void setxPoint(double xPoint) {
        this.xPoint = xPoint;
    }
}
