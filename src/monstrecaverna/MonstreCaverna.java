/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monstrecaverna;

import monstrecaverna.control.Control;
import monstrecaverna.vista.Vista;

/**
 *
 * @author Daxmaster
 */
public class MonstreCaverna {
    
    //private Control control;
    private Vista vista;
    
    public MonstreCaverna(){
        //this.control = new Control();
        this.vista = new Vista("Cueva del monstruo");
        //control.setVista(vista);
        
        this.vista.mostrar();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MonstreCaverna m = new MonstreCaverna();
    }
    
}