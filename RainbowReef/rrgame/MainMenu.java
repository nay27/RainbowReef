/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;

/**
 *
 * @author jmendo12
 */
public class MainMenu{
    
    private Game game;
    private BufferedImage backImage;
    private BufferedImage helpImage;
    private BufferedImage scoreImage;
    private final BufferedImage[] options;
    private ArrayList <Rectangle> optionsBox = new ArrayList<>();
    private ArrayList<File> fileList = new ArrayList<>(4);
    private ArrayList<String> scores;
    private int currentSelection;
    private int clickX, clickY;
    
    /**
     *
     * @param currentGame
     */
    public MainMenu(Game currentGame){
        game = currentGame;
        fileList.add(new File("rrresources/Button_start.gif"));
        fileList.add(new File("rrresources/Button_help.gif"));
        fileList.add(new File("rrresources/Button_scores.gif"));
        fileList.add(new File("rrresources/Button_quit.gif"));
        fileList.add(new File("rrresources/highscore.txt"));
        options = new BufferedImage[fileList.size() - 1];
        try{
           backImage = ImageIO.read(new File("rrresources/Title.png")); 
           helpImage = ImageIO.read(new File("rrresources/helpPage.png"));
           scoreImage = ImageIO.read(new File("rrresources/scoreImage.png"));
           for(int i = 0; i < options.length; i++){
               options[i] = ImageIO.read(fileList.get(i));
           }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        scores = new ArrayList<>();
        currentSelection = 5;
    }
    
    public void render(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(backImage, 0, 0, null);
        
        int x = 190;
        for (BufferedImage option : options) {
            g2.drawImage(option, x, 650, null);
            setRectangle(option, x, 650);
            x += 125;
        }
        if(currentSelection == 2){
            renderScores(g);
        }
    }
    
    public void update(){
        
        
        for (int i = 0; i < optionsBox.size(); i++){
            Rectangle boxRect = optionsBox.get(i);
            Rectangle clickRect = new Rectangle(clickX, clickY, 1, 1);
            if(boxRect.intersects(clickRect)){
                currentSelection = i;
                break;
            }
        }
        
        switch(currentSelection){
            case 0:
                game.setAtMenu(false);
                game.setAtLevel1(true);
                break;
            case 1:
                this.backImage = helpImage;
                break;
            case 2:
                this.backImage = scoreImage;
                break;
            case 3:
                game.setisRunning(false);
                break;
            default:
                break;
        }
    }
    private void setRectangle(BufferedImage img, int x, int y){
        optionsBox.add(new Rectangle(x, y, img.getWidth(), img.getHeight()));
    }

    public int getClickX() {
        return clickX;
    }

    public void setClickX(int clickX) {
        this.clickX = clickX;
    }

    public int getClickY() {
        return clickY;
    }

    public void setClickY(int clickY) {
        this.clickY = clickY;
    }
    
    public void addScore(int score){
        scores.add(Integer.toString(score));
        sortScores();
        writeScore();
    }
    private void sortScores(){
        Collections.sort(scores);
        Collections.reverse(scores);
    }
    private void writeScore(){
        File scoreFile = fileList.get(fileList.size() - 1);
        Path path = scoreFile.toPath();
        BufferedWriter scoreWriter = null;
        OpenOption option = StandardOpenOption.APPEND;
        try{
            scoreWriter = Files.newBufferedWriter(path, option);
            for(int i = 0; i < scores.size(); i++){
                String scoreString = scores.get(i);
                scoreWriter.write(scoreString, 0, scoreString.length());
                scoreWriter.newLine();
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        
        if(scoreWriter != null){
            try{
                scoreWriter.close();
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
    private void renderScores(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        File scoreFile = fileList.get(fileList.size() - 1);
        Path path = scoreFile.toPath();
        BufferedReader scoreReader = null;
        g2.setColor(Color.BLACK);
        g2.scale(2, 2);
        int y = 50;
        int x = Game.SCREEN_WIDTH/4 - 30;
        int i = 1;
        
        try{
            scoreReader = Files.newBufferedReader(path);
            String line = null;
            while((line = scoreReader.readLine()) != null){
                String renderLine = i + "." + " " + line;
                g2.drawString(renderLine, x, y);
                y += 10;
                i++;
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        
        if(scoreReader != null){
            try{
                scoreReader.close();
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
   
}