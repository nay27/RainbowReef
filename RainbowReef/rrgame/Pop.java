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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmendo12
 */
public class Pop extends GameObject{
    
    private int lives;
    private double angle;
    private double yVel;
    private double xVel;
    private final int SPAWN_Y;
    private final int SPAWN_X;
    private final int MAX_SPEED = 20;
    private final int START_SPEED = 10;
    boolean collide = false;
    private final double speed = 1;
    boolean isDouble = false;
    
    Pop(int x, int y, String img, Game game){
        
        super(x, y, img, game);
        yVel = START_SPEED;
        xVel = 0;
        angle = 0;
        SPAWN_Y = y;
        SPAWN_X = x;
        lives = 3;
    }
    
    public int getLives(){
        return lives;
    }
    
    @Override
    public void update(Observable obv, Object o) {
        move();
        checkLives();
    }
    
    private void move(){
        
        checkCollision();
        this.getHitBox().setBounds(x, y, width, height);
        y += yVel;
        x += xVel;
        this.getHitBox().setBounds(x, y, width, height);
        checkBorder();
    }
    
    @Override
    public void checkCollision() {
        int size = game.getObjectListSize();
        
        for(int i =0; i< size; i++){
            GameObject temp = game.getObject(i);
            if(this.getBounds().intersects(temp.getBounds())){
                
                if(temp instanceof Player){
                  
                    if(this.getY() > temp.getY()){
                        this.setyVel(yVel);
                        break;
                    }else if(this.getX() == (temp.getX() / 2)){
                        this.setxVel(0);
                    }else if(this.getX() < temp.getWidth() / 2 + temp.getX()){
                        this.setxVel(-1);
                    }else if(this.getX() > temp.getWidth() / 2 + temp.getX()){
                        this.setxVel(1);
                    }
                    /*if(yVel < MAX_SPEED){
                      yVel = yVel + speed;
                    }*/
                    /*System.out.println("Testing yVel: " + this.yVel);
                    int zone1 = temp.getX() + 15;
                    int zone2 = zone1 + 16;
                    int zone3 = zone2 + 18;
                    int zone4 = zone3 + 16;
                    int zone5 = zone4 + 15;

                    if(this.getX() < zone1){
                        this.setxVel(-3);
                        System.out.println("testing zone 1");
                    }
                    else if(this.getX() > zone1 && this.getX()< zone2){
                        this.setxVel(-2);        
                        System.out.println("testing zone 2");                        
                    }  
                    else if(this.getX() > zone2 && this.getX()< zone3){
                        this.setxVel(0);
                        System.out.println("testing zone 3");
                    }
                    else if(this.getX() > zone3 && this.getX()< zone4){
                        this.setxVel(2);        
                        System.out.println("testing zone 4");
                    }
                    else if(this.getX() > zone4 && this.getX()< zone5){
                        this.setxVel(3);          
                        System.out.println("testing zone 5");
                    }*/
                    this.setyVel(-yVel);
                    break;
                }                
                else if(temp instanceof Bricks){
                    
                    Rectangle intersectionP = 
                            this.getBounds().intersection(temp.getBounds());
                    
                    if(intersectionP.width >= intersectionP.height){
                        this.setyVel(-yVel);
                    }else if(intersectionP.height >= intersectionP.width){
                        this.setxVel(-xVel);
                    }
                    
                    if(temp.isBreakable()){
                        temp.setState(false);
                        Bricks brick = (Bricks) temp;
                        
                        if(brick.getId() == 9){
                            game.setDoublePoints(true);
                        }
                        if(game.isDoublePoints()){
                            game.updateScore(brick.getScore() * 2);
                        }else{
                            game.updateScore(brick.getScore());
                        }
                    }
                    break;
                        /*if(((Bricks) temp).powerup){
                            double tempt =  System.currentTimeMillis();
                            double limit = tempt + 5000;
                                Pop temppop = new Pop(this.x, this.y, "Pop", game);
                                temppop.isDouble = true;
                                GameEvents temppopE = new GameEvents();
                            Game.gameObjects.add(temppop);
                            Game.gameEvents.add(temppopE);
                            if(Game.lastUpdateTime > limit){
                                temppop.setState(false);
                            }    
                        } else if (((Bricks) temp).heal){
                            this.lives ++;
                            
                            
                        } else{
                            ((Bricks) temp).game.updateScore(((Bricks) temp).id * 5);
                        }
                    }*/
                }
                else if(temp instanceof BigLeg){
                    
                    Rectangle intersectionP = 
                            this.getBounds().intersection(temp.getBounds());
                    if(intersectionP.width >= intersectionP.height)
                        this.setyVel(-yVel);
                    if(intersectionP.height >= intersectionP.width)
                        this.setxVel(-xVel);
                    
                    temp.setState(false);
                    game.updateScore(50);
                    game.decrementBigLeg();
                    
                    if(game.getNumBigLegs() == 0){
                        System.out.println("You win!");
                        
                        System.out.println("Score: " + game.getScore());
                    }
                    
                    break;
                }
            }
        }
    }
    
    private void checkLives(){
        if(lives == 0){
            state = false;
        }else{}
    }
    
    @Override
    void checkBorder(){
        if(x + getWidth() >= Game.MAX_X){
            x = (int)Game.MAX_X - getWidth();
        }
        else if(x <= Game.MIN_X){
            x = (int)Game.MIN_X;
        }
        
        if(y >= Game.SCREEN_HEIGHT){
            y = SPAWN_Y;
            x = SPAWN_X;
            this.setxVel(0);
            this.setyVel(START_SPEED);
            //--lives;
        }
        else if(y <= Game.MIN_Y){
            y = (int) Game.MIN_Y;
            this.setyVel(-yVel);
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
        return "y is " + y + " lives left " + lives;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getyVel() {
        return yVel;
    }

    public void setyVel(double yVel) {
        this.yVel = yVel;
    }

    public double getxVel() {
        return xVel;
    }

    public void setxVel(double xVel) {
        this.xVel = xVel;
    }
}