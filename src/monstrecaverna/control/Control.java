/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monstrecaverna.control;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import monstrecaverna.modelo.MovimientoAgenteWrapper;
import monstrecaverna.vista.Vista;

/**
 *
 * @author bertu
 */
public class Control implements Runnable {
    Vista vista;
    Agente agente;
    private boolean stop = true;
    //private boolean simulacion = false;
    
    public Control(Vista vista){
        this.vista = vista;
    }
    
    public void setAgente(Agente agente){
        this.agente = agente;
        stop = false;
    }
    
//    public void setVista(Vista vista){
//        this.vista = vista;
//    }
    
    public void stop(){
        stop = true;
    }
    
    @Override
    public void run() {
        while(vista.simulacion && !stop){
                MovimientoAgenteWrapper movimiento = agente.moverAgente();
                if(movimiento == null){
                    vista.salir(agente.getIdentificador(), agente.getTesoros());
                    agente = null;
                    break;
                }else{
                    vista.moverAgente(movimiento);
                }
            try {
                sleep(vista.getVelocidad());
            } catch (InterruptedException ex) {
                Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(stop){
            agente = null;
        }
    }
}
