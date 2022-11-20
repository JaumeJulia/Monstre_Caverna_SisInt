/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monstrecaverna.modelo;

import java.util.HashMap;

/**
 *
 * @author Daxmaster-PC
 */
public class Memoria {
    
    private HashMap<Integer, HashMap> memoria = new HashMap<>(); //No, no puede ser final, netbeans es estúpido. Si fuese final, no podriamos ampliar el hashmap nunca
    
    public Memoria(int[] posicion){
        Casilla casilla = ampliaMemoria(posicion);
        casilla.setSegura();
    }
    
    public void defineEntorno(Casilla[] entorno, boolean[] estado){ //define el nivel de peligrosidad de las casillas de su entorno basandose en la información extraída de la casilla que está ocupando
        if(estado[0]){ //La casilla ocupada es totalmente SEGURA
            for(Casilla casilla : entorno){
                casilla.setSegura();
            }
        } else {
            if(estado[1]){ //En la casilla ocupada se percibe un HEDOR
                for(Casilla casilla : entorno){
                    casilla.incrementaNivelHedor();
                }
            }
            if(estado[2]){ //En la casilla ocupada se percibe BRISA
                for(Casilla casilla : entorno){
                    casilla.incrementaNivelBrisa();
                }
            }
        }
    }
    
    public void recuerda(int[] posicionActual, Direcciones direccion){ //Recuerda el camino SEGURO de vuelta
        Casilla casillaOcupada = getCasilla(posicionActual);
        casillaOcupada.setAntecesora(direccion);
    }
    
    public Casilla getCasilla(int[] posicion, Direcciones direccion){ //Para mirar las casillas cercanas
        int[] aux = posicion;
        aux[0] += direccion.X;
        aux[1] += direccion.Y;
        return getCasilla(aux);     
    }
    
    public Casilla getCasilla(int[] posicion){ //Para mirar la casilla ocupada
        HashMap <Integer, Casilla> memoriaAux = memoria.get(posicion[0]);
        Casilla casilla;
        if(memoriaAux != null){
            casilla = memoriaAux.get(posicion[1]);
            if(casilla == null){
                casilla = ampliaMemoria(posicion);
            }
        } else {
            casilla = ampliaMemoria(posicion);
        }
        return casilla;
    }
    
    private Casilla ampliaMemoria(int[] posicion){
        HashMap<Integer, Casilla> memoriaAux = new HashMap<>();
        Casilla casilla = new Casilla();
        memoriaAux.put(posicion[1], casilla);
        memoria.put(posicion[0], memoriaAux);
        return casilla;
    }    
}
