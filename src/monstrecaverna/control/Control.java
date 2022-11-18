/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package robot.control;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import robot.vista.Vista;

/**
 *
 * @author bertu
 */
public class Control implements Runnable {
    Vista vista;
    Agente agente;
    private boolean simulacion = false;
    
    public void setAgente(Agente agente){
        this.agente = agente;
    }
    
    public void setVista(Vista vista){
        this.vista = vista;
    }
    
    public void setSimulacion(boolean simulacion){
        this.simulacion = simulacion;
        System.out.println("Simulacion: "+ simulacion);
    }
    
    @Override
    public void run() {
        while(simulacion){        
            vista.moverAgente(agente.moverAgente());
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
