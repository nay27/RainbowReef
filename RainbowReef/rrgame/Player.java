package RainbowReef.rrgame;

import java.util.Observable;

/**
 *
 * @author jmendo12
 */
public class Player extends GameObject{
    
    Player(int x, int y, String img){
        super(x,y,img);
    }
    
    Player(){
        
    }
    
    @Override
    public void update(Observable obv, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean checkCollision() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
