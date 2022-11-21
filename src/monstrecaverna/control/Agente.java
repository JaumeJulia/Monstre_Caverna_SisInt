/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monstrecaverna.control;

import java.util.HashMap;
import java.util.Random;
import monstrecaverna.modelo.Casilla;
import monstrecaverna.modelo.Direcciones;
import monstrecaverna.modelo.Memoria;
import monstrecaverna.vista.Vista;

/**
 *
 * @author Daxmaster
 */
public class Agente {
    
    Vista vista;
    Direcciones direcciones[] = {Direcciones.NORTE, Direcciones.ESTE, Direcciones.SUR, Direcciones.OESTE};
    int direccionActual;
    int rotacion; // +1 cuando se rota en sentido horario, -1 cuando es antihorario
    int[] posicionActual = new int[2];
    boolean[] estadoCasillaActual; 
    Memoria memoria;
    boolean saliendo;
    
    public Agente(int rotacion, Vista vista){
        Random ran = new Random();
        direccionActual = ran.nextInt(20) % 4;
        this.rotacion = rotacion;
        posicionActual[0] = 0;
        posicionActual[1] = 0;
        memoria = new Memoria(posicionActual);
        this.vista = vista;
        saliendo = false;
    }
    
    private Casilla[] reconocerEntorno(){
        System.out.println("Iba hacia el:" + direcciones[direccionActual].toString());
        Casilla[] entorno = new Casilla[4];
        rotar(-rotacion);
        for(int i = 0 ; i < 4 ; i++){
            System.out.println("Miro al:" + direcciones[direccionActual].toString());
            entorno[i] = memoria.getCasilla(posicionActual, direcciones[direccionActual]);
            int[] casillaCercana = {posicionActual[0] + direcciones[direccionActual].X, posicionActual[1] + direcciones[direccionActual].Y};
            if(vista.getCasilla(casillaCercana).isPared()){
                entorno[i].setPared(true);
            }
            rotar(rotacion);
        }
        rotar(rotacion);
        System.out.println("Salgo mirando al:" + direcciones[direccionActual].toString());
        
        boolean[] deduccionCasillasAdyacentes = {false, false, false};
        if(!estadoCasillaActual[0] && !estadoCasillaActual[1]){ //no hay ni hedor ni brisa
            deduccionCasillasAdyacentes[0] = true; //casilla segura
        } else {
            deduccionCasillasAdyacentes[1] = estadoCasillaActual[0]; //hay hedor
            deduccionCasillasAdyacentes[2] = estadoCasillaActual[1]; //hay brisa
        }
        memoria.defineEntorno(entorno, deduccionCasillasAdyacentes);
        return entorno;        
    }
    
    public Direcciones moverAgente(){
        estadoCasillaActual = vista.getCasilla(posicionActual).getEstado(); //esto deberia devolver el array de booleanas que dice si hay hedor, brisa o resplandor
        if(saliendo){
            return memoria.getCasilla(posicionActual).getAntecesora();//se dirige a la casilla antecesora
        }
        if(estadoCasillaActual[2]){
            //coge el tesoro
            saliendo = true; //se marca como que está saliendo, por lo que solo va a buscar la casilla antecesora en lugar de seguir explorando
        }
        Casilla[] entorno = reconocerEntorno();
        
        rotar(-rotacion);
        for(Casilla casilla: entorno){ //al salir del for, la rotación dará como resultado que el agente mire hacia atrás, siendo este el movimiento por defecto incluso cuando todo falla. 
            if(casilla.isSegura() && !casilla.isPared()){
                break;
            }
            rotar(rotacion);
        }
        
        posicionActual[0] += direcciones[direccionActual].X;
        posicionActual[1] += direcciones[direccionActual].Y;
        memoria.recuerda(posicionActual, inversorDireccion(direcciones[direccionActual]));
        System.out.println("Me dirijo al:" + direcciones[direccionActual].toString());
        return direcciones[direccionActual];
    }
    
    private Direcciones inversorDireccion(Direcciones direccion){
        switch(direccion){
            case ESTE:
                return Direcciones.OESTE;
            case NORTE:
                return Direcciones.SUR;
            case OESTE:
                return Direcciones.ESTE;
            case SUR:
                return Direcciones.NORTE;
            default:
                return direccion;
        }
    }
    
    public void rotar(int sentido){
        direccionActual = Math.floorMod((direccionActual + sentido), 4);
    }
    
}
