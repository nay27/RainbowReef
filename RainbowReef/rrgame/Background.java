/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author jmendo12
 */
public class Background {
    
    private BufferedImage image;
    
    Background(){
        try{
            image = ImageIO.read(new File("rrresources/Background1.png"));
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    public void render(Graphics g){
        
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(image, null, 0, 0);
    }
}
