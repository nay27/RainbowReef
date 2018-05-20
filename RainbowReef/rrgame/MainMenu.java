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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author jmendo12
 */
public class MainMenu{
    
    private Game game;
    private BufferedImage backImage;
    private final BufferedImage[] options;
    private ArrayList <Rectangle> optionsBox = new ArrayList<>();
    private ArrayList<File> fileList = new ArrayList<>(4);
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
        options = new BufferedImage[fileList.size()];
        try{
           backImage = ImageIO.read(new File("rrresources/Title.png")); 
           for(int i = 0; i < options.length; i++){
               options[i] = ImageIO.read(fileList.get(i));
           }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
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
}
