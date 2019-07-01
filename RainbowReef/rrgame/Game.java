/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author jmendo12
 */
public class Game extends JPanel implements Runnable{
    
    private boolean isRunning = false;
    private boolean atMenu = true;
    private boolean atLevel1 = false;
    private boolean atLevel2 = false;
    private boolean atLevel3 = false;
    private boolean doublePoints = false;
    private int fps = 60;
    private int frameCount = 0;
    private static int score = 0;
    private static int highScore = 0;
    private int doublePointsTicks;
    private final double GAME_HERTZ = 30.0;
    private final String gameOver = "Game Over, You Lose";
    private static String scoreString;
    public static final int SCREEN_WIDTH = 960;
    public static final int SCREEN_HEIGHT = 720;
    public static final double MAX_X = 960 - 40;
    public static final double MIN_X = 0 + 40;
    public static final double MIN_Y = 0 + 40;
    private int numBigLegs;
    private BufferedImage gameWorld;
    private Background gameBack;
    private Pop pop;
    private Player katch;
    private TiledMap level1, level2, level3;
    private MainMenu mainMenu;
    private SoundPlayer soundEffects;
    private ArrayList<GameObject> gameObjects;
    private ArrayList<GameObject> level2Objects;
    private ArrayList<GameObject> level3Objects;
    private ArrayList<GameEvents> gameEvents;
    private ArrayList<GameEvents> level2Events;
    private ArrayList<GameEvents> level3Events;
    private double currTime;
    
    
    static double lastUpdateTime;
    
    //Constructor
    Game(){
        super();
        scoreString = "Score: ";
        gameWorld = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        gameBack = new Background();
        mainMenu = new MainMenu(this);
        gameObjects = new ArrayList<>();
        level2Objects = new ArrayList<>();
        level3Objects = new ArrayList<>();
        gameEvents = new ArrayList<>();
        level2Events = new ArrayList<>();
        level3Events = new ArrayList<>();
        init();
    }
    //Thread method
    @Override
    public void run(){
        new Window(SCREEN_WIDTH, SCREEN_HEIGHT ,"Rainbow Reef", this);
        this.isRunning = true;
        gameLoop();       
    }
    //Game loop, rendering, and updating methods
    private void gameLoop(){
        currTime = System.nanoTime()/1000000000.0;
        final double TIME_BTWN_UPDATES = 1000000000 / GAME_HERTZ;

        final int MAX_UPDATES_TO_RENDER = 1;

        lastUpdateTime = System.nanoTime();

        double lastRenderTime = System.nanoTime();

        final double TARGET_FPS = 60;
        final double TARGET_RENDER_TIME = 1000000000 / TARGET_FPS;

        int lastSecondTime = (int) (lastUpdateTime / 1000000000);

        while (isRunning) {

            double now = System.nanoTime();
            int updateCount = 0;

            while (now - lastUpdateTime > TIME_BTWN_UPDATES
                    && updateCount < MAX_UPDATES_TO_RENDER) {
                update();
                lastUpdateTime += TIME_BTWN_UPDATES;
                updateCount++;
            }

            if (now - lastUpdateTime > TIME_BTWN_UPDATES) {
                lastUpdateTime = now - TIME_BTWN_UPDATES;
            }
             
            this.repaint();
            this.setFocusable(isRunning);
            this.requestFocusInWindow(isRunning);

            lastRenderTime = now;

            int thisSecond = (int) (lastUpdateTime / 1000000000);
            if (thisSecond > lastSecondTime) {
                //System.out.println("New Second " + thisSecond + " " + frameCount);
                fps = frameCount;
                //System.out.println("FPS is :" + fps);
                frameCount = 0;
                lastSecondTime = thisSecond;
            }

            while (now - lastRenderTime < TARGET_RENDER_TIME
                    && now - lastUpdateTime < TIME_BTWN_UPDATES) {
                Thread.yield();
                try {
                    Thread.sleep(1000 / 144);
                } catch (Exception e) {

                }

                now = System.nanoTime();
            }
            if(!pop.getState()){
                isRunning = false;
                mainMenu.addScore(score);
                repaint();
            }
            if(!isRunning){
             File scoreFile = new File("rrresources/highscore.txt");
             Path path = scoreFile.toPath();
            BufferedReader scoreReader = null;
            try{
            scoreReader = Files.newBufferedReader(path);
            String line = null;
            while((line = scoreReader.readLine()) != null){
                 int temp = Integer.parseInt(line);
                 if (temp > highScore){
                     highScore = temp;
                 }
            }
            }catch(IOException e){
            System.out.println(e.getMessage());
        }
            if(score >= highScore){
               JOptionPane.showMessageDialog(this,"CONGRATS!\n You have the High Score!!!\n Your Score is: " + score); 
            } else{
             JOptionPane.showMessageDialog(this,"Your Score is: " + score + "\n" + "High Score is: " + highScore);
            }
                System.exit(0);
            }
        }
    }
    
    private void init(){
        this.addMouseListener(new MouseInput(mainMenu));
        soundEffects = new SoundPlayer();
        numBigLegs = 0;
        level1 = new TiledMap("rrresources/level1.txt");
        level2 = new TiledMap("rrresources/level2.txt");
        level3 = new TiledMap("rrresources/level3.txt");
        initializeLevel(level1);
        initializeLevel(level2);
        initializeLevel(level3);   
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        //If the game is running, render the main menu or one of the levels
        if(this.pop.getLives() != 0){
            Graphics2D buffer = (Graphics2D) gameWorld.createGraphics();
            
            //Render the main menu
            if(atMenu){
                mainMenu.render(buffer);
            }else if(atLevel1 || atLevel2 || atLevel3){
                
                //Render background to buffer
                gameBack.render(buffer);
                //Render all game objects to buffer
                for(int i = 0; i < gameObjects.size(); i++){
                    GameObject temp = gameObjects.get(i);
                    temp.render(buffer);
                }
            
                //Render score, double points if so, and pop lives to buffer
                buffer.scale(3, 3);
                if(isDoublePoints())
                    buffer.drawString("Double Points!", 200, 229);
                buffer.drawString(displayScore(), 15, 229);
                buffer.scale(.33 + .01,.33 + .01);
                renderPopLives(buffer);
            }
            BufferedImage displayImage = gameWorld.getSubimage(0, 0, 
                Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
            g2.drawImage(displayImage, null, 0, 0);
        }else{
            //This clause is entered if the game ends
            Graphics2D buffer = (Graphics2D) gameWorld.createGraphics();
            gameBack.render(buffer);
        
            for(int i = 0; i < gameObjects.size(); i++){
                GameObject temp = gameObjects.get(i);
                temp.render(buffer);
                }
            BufferedImage displayImage = gameWorld.getSubimage(0, 0, 
            Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
            System.out.println("Game Over");
            g2.drawImage(displayImage, null, 0, 0);
            g2.setColor(Color.red);
            g2.scale(5, 5);
            g2.drawString(gameOver, Game.SCREEN_WIDTH/30, Game.SCREEN_HEIGHT/10);
        }
        g2.dispose();
    }
        
    private void update(){
        //Update menu choices
        if(atMenu){
            mainMenu.update();
        }
        //Update only when on one of the playable levels
        if(atLevel1 || atLevel2 || atLevel3){
            for(int i = 0; i< gameObjects.size(); i++){
                GameObject temp = gameObjects.get(i);
                GameEvents tempE = gameEvents.get(i);
            
                if(!temp.getState()){
                   gameObjects.remove(temp);
                   gameEvents.remove(tempE);
                }else{
                    tempE.addObserver(temp);
                    tempE.setChanged();
                    tempE.notifyObservers();
                } 
            }
        }
        checkLevel();
    }
    
    public double getTime(){
        return currTime;
    }
    
    public int getObjectListSize(){
        return gameObjects.size();
    }
        
    public GameObject getObject(int i){
        return gameObjects.get(i);
    }

    public Pop getPop() {
        return pop;
    }

    public void setPop(Pop pop) {
        this.pop = pop;
    }

    public Player getKatch() {
        return katch;
    }

    public void setKatch(Player katch) {
        this.katch = katch;
    }
    
    public void decrementBigLeg(){
        numBigLegs--;
    }
    public int getNumBigLegs(){
        return numBigLegs;
    }
    
    public int getScore(){
        return score;
    }
    
    public static void setHighScore(int x){
        highScore = x;
    }
    
    public SoundPlayer getSoundEffects() {
        return soundEffects;
    }

    public void setSoundEffects(SoundPlayer soundEffects) {
        this.soundEffects = soundEffects;
    }
    
    public boolean isDoublePoints() {
        if(doublePoints == true){
            if(checkCounter() == 0){
                doublePoints = false;
            }else{
                decrementDPCounter();
            }
        }
        return doublePoints;
    }

    public void setDoublePoints(boolean doublePoints) {
        if(doublePoints == true){
            doublePointsTicks = 300;
        }
        this.doublePoints = doublePoints;
    }
    private void decrementDPCounter(){
        doublePointsTicks--;
    }
    private int checkCounter(){
        return doublePointsTicks;
    }
    public void updateScore(int x){
        score = score + x;
    }
    
    public void resetScore(){
        score = 0;
    }
    
    public boolean isAtMenu() {
        return atMenu;
    }

    public void setAtMenu(boolean atMenu) {
        this.atMenu = atMenu;
    }

    public boolean isAtLevel1() {
        return atLevel1;
    }

    public void setAtLevel1(boolean atLevel1) {
        this.atLevel1 = atLevel1;
    }

    public boolean isAtLevel2() {
        return atLevel2;
    }

    public void setAtLevel2(boolean atLevel2) {
        this.atLevel2 = atLevel2;
    }

    public boolean isAtLevel3() {
        return atLevel3;
    }

    public void setAtLevel3(boolean atLevel3) {
        this.atLevel3 = atLevel3;
    }
    
    public void setisRunning(boolean isRunning){
        this.isRunning = isRunning;
    }
    
    private void renderPopLives(Graphics g){
        
        Graphics2D g2 = (Graphics2D) g;
        int x = Game.SCREEN_WIDTH / 2 - 50;
        int y = 658;
        
        g2.scale(2,2);
        g2.drawString("Lives: ", x / 2 - 40, y / 2 + 10);
        g2.scale(.5,.5);
        for(int i = 0; i < pop.getLives(); i++){
            g2.drawImage(pop.getSprite(), x, y, null);
            x += 30;
        }
    }
    
    private void checkLevel(){
        if(numBigLegs == 12 && atLevel2 == false){
            gameObjects.clear();
            gameEvents.clear();
            gameObjects.addAll(level2Objects);
            gameEvents.addAll(level2Events);
            atLevel2 = true;
        }
        if(numBigLegs == 8 && atLevel3 == false){
            gameObjects.clear();
            gameEvents.clear();
            gameObjects.addAll(level3Objects);
            gameEvents.addAll(level3Events);
            atLevel3 = true;
        }
    }
    
    private void initializeLevel(TiledMap level){
        int y = 0;
        int x = 0;
        for(int i = 0; i < TiledMap.NUM_ROWS; i++){
            for(int j = 0; j < TiledMap.NUM_COLS; j++){
                char fileValue = level.getTile(i, j);
                //System.out.println(fileValue);
                switch(fileValue){
                    case 'k':
                        if(level.equals(level1)){
                            katch = new Player(x, 625, "Katch", this);
                            GameEvents playerE = new GameEvents();
                            Controls playerControl = new Controls (katch, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT);
                            this.addKeyListener(playerControl);
                            gameObjects.add(katch);
                            gameEvents.add(playerE);
                        }else if(level.equals(level2)){
                            katch.setX(x);
                            GameEvents playerE = new GameEvents();
                            level2Objects.add(katch);
                            level2Events.add(playerE);
                        }else if(level.equals(level3)){
                            katch.setX(x);
                            GameEvents playerE = new GameEvents();
                            level3Objects.add(katch);
                            level3Events.add(playerE);
                        }
                        break;
                    case 'p':
                        if(level.equals(level1)){
                            pop = new Pop(x, y, "Pop", this);
                            GameEvents popE = new GameEvents();
                            gameObjects.add(pop);
                            gameEvents.add(popE);
                        }else if(level.equals(level2)){
                            pop.setX(x);
                            pop.setY(y);
                            GameEvents popE = new GameEvents();
                            level2Objects.add(pop);
                            level2Events.add(popE);
                        }else if(level.equals(level3)){
                            pop.setX(x);
                            pop.setY(y);
                            GameEvents popE = new GameEvents();
                            level3Objects.add(pop);
                            level3Events.add(popE);
                        }
                        break;
                    case 'b':
                        if(level.equals(level1)){
                            BigLeg bigLeg = new BigLeg(x, y, "BigLeg", 
                                this, numBigLegs);
                            GameEvents blE = new GameEvents();
                            gameObjects.add(bigLeg);
                            gameEvents.add(blE);
                            numBigLegs++;
                        }else if(level.equals(level2)){
                            BigLeg bigLeg = new BigLeg(x, y, "BigLeg", 
                                this, numBigLegs);
                            GameEvents blE = new GameEvents();
                            level2Objects.add(bigLeg);
                            level2Events.add(blE);
                            numBigLegs++;
                        }else if(level.equals(level3)){
                            BigLeg bigLeg = new BigLeg(x, y, "BigLeg", 
                                this, numBigLegs);
                            GameEvents blE = new GameEvents();
                            level3Objects.add(bigLeg);
                            level3Events.add(blE);
                            numBigLegs++;
                        }
                        break;
                    case '1':
                        if(level.equals(level1)){
                            Bricks brick = new Bricks(x, y, "Block_solid", this);
                            GameEvents brickEvent = new GameEvents();
                            gameObjects.add(brick);
                            gameEvents.add(brickEvent);
                        }else if(level.equals(level2)){
                            Bricks brick = new Bricks(x, y, "Block_solid", this);
                            GameEvents brickEvent = new GameEvents();
                            level2Objects.add(brick);
                            level2Events.add(brickEvent);
                        }else if(level.equals(level3)){
                            Bricks brick = new Bricks(x, y, "Block_solid", this);
                            GameEvents brickEvent = new GameEvents();
                            level3Objects.add(brick);
                            level3Events.add(brickEvent);
                        }
                        break;
                    case '2':
                        if(level.equals(level1)){
                            Bricks brick = new Bricks(x, y, "Block1", this);
                            GameEvents brickEvent = new GameEvents();
                            gameObjects.add(brick);
                            gameEvents.add(brickEvent);
                        }else if(level.equals(level2)){
                            Bricks brick = new Bricks(x, y, "Block1", this);
                            GameEvents brickEvent = new GameEvents();
                            level2Objects.add(brick);
                            level2Events.add(brickEvent);
                        }else if(level.equals(level3)){
                            Bricks brick = new Bricks(x, y, "Block1", this);
                            GameEvents brickEvent = new GameEvents();
                            level3Objects.add(brick);
                            level3Events.add(brickEvent);
                        }
                        break;
                    case '3':
                        if(level.equals(level1)){
                            Bricks brick = new Bricks(x, y, "Block2", this);
                            GameEvents brickEvent = new GameEvents();
                            gameObjects.add(brick);
                            gameEvents.add(brickEvent);
                        }else if(level.equals(level2)){
                            Bricks brick = new Bricks(x, y, "Block2", this);
                            GameEvents brickEvent = new GameEvents();
                            level2Objects.add(brick);
                            level2Events.add(brickEvent);
                        }else if(level.equals(level3)){
                            Bricks brick = new Bricks(x, y, "Block2", this);
                            GameEvents brickEvent = new GameEvents();
                            level3Objects.add(brick);
                            level3Events.add(brickEvent);
                        }
                        break;
                    case '4':
                        if(level.equals(level1)){
                            Bricks brick = new Bricks(x, y, "Block3", this);
                            GameEvents brickEvent = new GameEvents();
                            gameObjects.add(brick);
                            gameEvents.add(brickEvent);
                        }else if(level.equals(level2)){
                            Bricks brick = new Bricks(x, y, "Block3", this);
                            GameEvents brickEvent = new GameEvents();
                            level2Objects.add(brick);
                            level2Events.add(brickEvent);
                        }else if(level.equals(level3)){
                            Bricks brick = new Bricks(x, y, "Block3", this);
                            GameEvents brickEvent = new GameEvents();
                            level3Objects.add(brick);
                            level3Events.add(brickEvent);
                        }
                        break;
                    case '5':
                        if(level.equals(level1)){
                            Bricks brick = new Bricks(x, y, "Block4", this);
                            GameEvents brickEvent = new GameEvents();
                            gameObjects.add(brick);
                            gameEvents.add(brickEvent);
                        }else if(level.equals(level2)){
                            Bricks brick = new Bricks(x, y, "Block4", this);
                            GameEvents brickEvent = new GameEvents();
                            level2Objects.add(brick);
                            level2Events.add(brickEvent);
                        }else if(level.equals(level3)){
                            Bricks brick = new Bricks(x, y, "Block4", this);
                            GameEvents brickEvent = new GameEvents();
                            level3Objects.add(brick);
                            level3Events.add(brickEvent);
                        }
                        break;
                    case '6':
                        if(level.equals(level1)){
                            Bricks brick = new Bricks(x, y, "Block5", this);
                            GameEvents brickEvent = new GameEvents();
                            gameObjects.add(brick);
                            gameEvents.add(brickEvent);
                        }else if(level.equals(level2)){
                            Bricks brick = new Bricks(x, y, "Block5", this);
                            GameEvents brickEvent = new GameEvents();
                            level2Objects.add(brick);
                            level2Events.add(brickEvent);
                        }else if(level.equals(level3)){
                            Bricks brick = new Bricks(x, y, "Block5", this);
                            GameEvents brickEvent = new GameEvents();
                            level3Objects.add(brick);
                            level3Events.add(brickEvent);
                        }
                        break;
                    case '7':
                        if(level.equals(level1)){
                            Bricks brick = new Bricks(x, y, "Block6", this);
                            GameEvents brickEvent = new GameEvents();
                            gameObjects.add(brick);
                            gameEvents.add(brickEvent);
                        }else if(level.equals(level2)){
                            Bricks brick = new Bricks(x, y, "Block6", this);
                            GameEvents brickEvent = new GameEvents();
                            level2Objects.add(brick);
                            level2Events.add(brickEvent);
                        }else if(level.equals(level3)){
                            Bricks brick = new Bricks(x, y, "Block6", this);
                            GameEvents brickEvent = new GameEvents();
                            level3Objects.add(brick);
                            level3Events.add(brickEvent);
                        }
                        break;
                    case '8':
                        if(level.equals(level1)){
                            Bricks brick = new Bricks(x, y, "Block7", this);
                            GameEvents brickEvent = new GameEvents();
                            gameObjects.add(brick);
                            gameEvents.add(brickEvent);
                        }else if(level.equals(level2)){
                            Bricks brick = new Bricks(x, y, "Block7", this);
                            GameEvents brickEvent = new GameEvents();
                            level2Objects.add(brick);
                            level2Events.add(brickEvent);
                        }else if(level.equals(level3)){
                            Bricks brick = new Bricks(x, y, "Block7", this);
                            GameEvents brickEvent = new GameEvents();
                            level3Objects.add(brick);
                            level3Events.add(brickEvent);
                        }
                        break;
                    case '9':
                        if(level.equals(level1)){
                            Bricks brick = new Bricks(x, y, "Block_life", this);
                            GameEvents brickEvent = new GameEvents();
                            gameObjects.add(brick);
                            gameEvents.add(brickEvent);
                        }else if(level.equals(level2)){
                            Bricks brick = new Bricks(x, y, "Block_life", this);
                            GameEvents brickEvent = new GameEvents();
                            level2Objects.add(brick);
                            level2Events.add(brickEvent);
                        }else if(level.equals(level3)){
                            Bricks brick = new Bricks(x, y, "Block_life", this);
                            GameEvents brickEvent = new GameEvents();
                            level3Objects.add(brick);
                            level3Events.add(brickEvent);
                        }
                        break;
                    case '?':
                        if(level.equals(level1)){
                            Bricks brick = new Bricks(x, y, "Block_split", this);
                            GameEvents brickEvent = new GameEvents();
                            gameObjects.add(brick);
                            gameEvents.add(brickEvent);
                        }else if(level.equals(level2)){
                            Bricks brick = new Bricks(x, y, "Block_split", this);
                            GameEvents brickEvent = new GameEvents();
                            level2Objects.add(brick);
                            level2Events.add(brickEvent);
                        }else if(level.equals(level3)){
                            Bricks brick = new Bricks(x, y, "Block_split", this);
                            GameEvents brickEvent = new GameEvents();
                            level3Objects.add(brick);
                            level3Events.add(brickEvent);
                        }
                        break;
                    default:
                        break;
                }
                x += 40;
            }
            x = 0;
            y += 20;
        }
    }
    
    private String displayScore(){
        scoreString = "Score: ";
        String stScore = Integer.toString(score);
        String displayedString = scoreString + stScore;
        return displayedString;
    }
    
    
    public static void main(String [] args){
        Game glt = new Game();
        SoundPlayer gameMusic = new SoundPlayer("rrresources/Music.wav");
        Thread game = new Thread(glt);
        Thread music = new Thread(gameMusic);
        music.start();
        game.start();
    }   
}
