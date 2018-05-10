/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author jmendo12
 * Will be used to determine spawn points and whats on the map
 */
public class TiledMap {
    
    private File inputFile;
    private char [][] tiles;
    private String line;
    private BufferedReader reader;
    public static final int NUM_COLS = 24;
    public static final int NUM_ROWS = 48;
    
    TiledMap(String file){
        inputFile = new File(file);
        try{
           reader = new BufferedReader(new FileReader(inputFile)); 
        }catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        tiles = new char[NUM_ROWS][NUM_COLS];
        initTiles();
    }
    
    private void initTiles(){
        for(int i = 0; i < NUM_ROWS; i++){
            try{
                  line = reader.readLine();  
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
            for(int j = 0; j < NUM_COLS; j++){
                char value = line.charAt(j);
                tiles[i][j] = value;
            }
        }
    }

    public char getTile(int i , int j) {
        return tiles[i][j];
    }

    public void setTiles(char[][] tiles) {
        this.tiles = tiles;
    }
}
