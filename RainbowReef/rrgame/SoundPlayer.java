/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Jmendo12
 */
public class SoundPlayer implements Runnable{
    
    private String fileName;
    
    public SoundPlayer(){
        fileName = "";
    }
    
    public SoundPlayer(String file){
        fileName = file;
    }
    
    private void playSound(){
        try{
            File soundFile = new File(fileName);
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat format = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(ais);
            clip.start();
        } catch (LineUnavailableException | IOException | 
                UnsupportedAudioFileException e) {
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
    
    public String getFile(){
        return fileName;
    }
    
    public void setFile(String file){
        fileName = file;
    }
    
    @Override
    public void run() {
        playSound();
    }
    
}
