/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowReef.rrgame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author jmendo12
 */
public class MouseInput implements MouseListener{
    private MainMenu control;
    
    public MouseInput(MainMenu menu){
        control = menu;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        control.setClickX(e.getX());
        control.setClickY(e.getY());
        System.out.println("Mouse clicked" + "x: " + e.getX() + " y: " + e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseClicked(e);
        System.out.println("Mouse pressed");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("Mouse entered");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("Mouse exited");
    }
    
}
