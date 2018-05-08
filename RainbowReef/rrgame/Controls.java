/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

/**
 *
 * @author christinemalicdem
 */
public class Controls extends Observable implements KeyListener{
    
    private final Player controller;
    private final int right;
    private final int left;
    
    Controls(Player player, int r, int l){
        controller = player;
        right = r;
        left = l;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyPressed = e.getKeyCode();
        if(keyPressed == right){

            this.controller.toggleRightPressed();
        }
        else if(keyPressed == left){
            this.controller.toggleLeftPressed();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyReleased = e.getKeyCode();
        if(keyReleased == right){
            this.controller.unToggleRightPressed();
        }
        else if(keyReleased == left){
            this.controller.unToggleLeftPressed();
        }
    }
}
