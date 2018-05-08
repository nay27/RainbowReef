/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.event.KeyEvent;
import java.util.Observable;

/**
 *
 * @author christinemalicdem
 */
public class GameEvents extends Observable{
     Object source, target;
    int Damage;
    int type; //distinguish b/w colliosion event or keypressed
    final int key = 1;
    final int collision = 2;
    
     public GameEvents() {
    }

    @Override
    protected synchronized void setChanged() {
        super.setChanged();
    }
    
    public void setKey(KeyEvent e){
       type = key;
       this.target = e;
       
       //mark the GameEvent as changed 
       setChanged();
       //noify observer about the change and call the clearChanged method to 
       //indicate that this object has no longer changed.
       notifyObservers(this);
       
    }
    
}
