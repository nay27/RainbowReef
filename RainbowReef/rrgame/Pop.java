/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Observable;

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
    private final int MAX_SPEED = 25;
    private final int START_SPEED = 8;
    private double deadTime, spawnTime;
    boolean collide = false, spawn = false;
    boolean isDouble = false;
    
    Pop(int x, int y, String img, Game game){
        
        super(x, y, img, game);
        yVel = START_SPEED;
        xVel = 0;
        angle = 0;
        SPAWN_Y = y;
        SPAWN_X = x;
        lives = 10;
        spawn = true;
        spawnTime = System.nanoTime()/1000000000.0;
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
        if(spawn){
            System.out.println("testing spawntime: " + spawnTime);
            System.out.println("testing currtime: " + game.getTime());
            while((spawnTime+2 > game.getTime())){
            }
        }
        checkCollision();
        checkBorder();
        y += yVel;
        x += xVel;
        this.getHitBox().setBounds(x, y, width, height);
        spawn = false;
        
    }
    
    @Override
    public void checkCollision() {
        int size = game.getObjectListSize();
        
        for(int i =0; i< size; i++){
            GameObject temp = game.getObject(i);
            
            if(temp instanceof Pop){
                continue;
            }
            
            if(this.getBounds().intersects(temp.getBounds())){
                
                if(temp instanceof Player){
                    
                    SoundPlayer effect = game.getSoundEffects();
                    effect.setFile("rrresources/Sound_katch.wav");
                    Thread sound = new Thread(effect);
                    sound.start();
                    try{
                        sound.join();
                    }catch(InterruptedException e){
                        System.out.println(e.getMessage());
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                    
                    if(this.getY() >= 600){
                        this.setyVel(yVel);
                        break;
                    }
                    else if(getX() > temp.getX() + (temp.getWidth() / 2 - 15) 
                            && getX() < temp.getX() + (temp.getWidth() / 2 + 15))
                    {   
                            this.setxVel((xVel + 0));
                    }
                    else if(getX() < temp.getX() + (temp.getWidth() / 2 - 15))
                    {   
                            this.setxVel((xVel - 2));
                            collide = true;
                        
                    }
                    else if(getX() > temp.getX() + (temp.getWidth() / 2 + 15))
                    {   
                            this.setxVel((xVel + 2));
                            collide = true;

                    }
                    if(yVel < MAX_SPEED){
                    this.setyVel(-(yVel+1));
                    }
                    break;
                }else if(temp instanceof Bricks){
                    
                    SoundPlayer effect = game.getSoundEffects();
                    effect.setFile("rrresources/Sound_block.wav");
                    Thread sound = new Thread(effect);
                    sound.start();
                    try{
                        sound.join();
                    }catch(InterruptedException e){
                        System.out.println(e.getMessage());
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                    
                    Rectangle intersectionP = 
                            this.getBounds().intersection(temp.getBounds());
                    
                    if(intersectionP.width >= intersectionP.height){
                        this.setyVel(-yVel);
                    }
                    if(intersectionP.height >= intersectionP.width){
                        this.setxVel(-xVel);
                    }
                    
                    if(temp.isBreakable()){
                        temp.setState(false);
                        Bricks brick = (Bricks) temp;
                        //Check for special bricks
                        if(brick.getId() == 9){
                            game.setDoublePoints(true);
                        }else if(brick.getId() == 8){
                            this.lives += 1;
                        }
                        
                        //Check for double points, then assign points
                        if(game.isDoublePoints()){
                            game.updateScore(brick.getScore() * 2);
                        }else{
                            game.updateScore(brick.getScore());
                        }
                    }
                    break;
                }else if(temp instanceof BigLeg){
                    
                    SoundPlayer effect = game.getSoundEffects();
                    effect.setFile("rrresources/Sound_bigleg.wav");
                    Thread sound = new Thread(effect);
                    sound.start();
                    try{
                        sound.join();
                    }catch(InterruptedException e){
                        System.out.println(e.getMessage());
                        System.out.println(Arrays.toString(e.getStackTrace()));
                    }
                    
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
            spawnTime = System.nanoTime()/1000000000.0;
            this.setxVel(0);
            this.setyVel(START_SPEED);
            playLiveLost();
            lives--;
            collide = false;
            spawn = true;
            
        }
        else if(y <= Game.MIN_Y - 40){
            this.setyVel(-yVel);
        }
    }
    
    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if(state){
            g2.drawImage(sprite, null, x, y);
            //System.out.println(toString());
        }
    }
    private void playLiveLost(){
        
        SoundPlayer effect = game.getSoundEffects();
        effect.setFile("rrresources/Sound_lost.wav");
        Thread sound = new Thread(effect);
        sound.start();
        try{
            sound.join();
        }catch(InterruptedException e){
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
    @Override
    public String toString(){
        return "y is " + y + " x is " + x + " collide: " + collide;
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