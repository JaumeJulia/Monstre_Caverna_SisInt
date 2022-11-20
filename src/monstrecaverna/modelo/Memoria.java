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
    
    HashMap<Integer, HashMap> memoria = new HashMap<>();
    
    public Memoria(int[] posicion){
        HashMap<Integer, Casilla> memoriaAux = new HashMap<>();
        Casilla casilla = new Casilla();
        casilla.setSegura();
        memoriaAux.put(posicion[1], casilla);
        memoria.put(posicion[0], memoriaAux);
    }
    
    public void explora(Casilla[] entorno, boolean[] estado){ //recuerda las casillas de su entorno basandose en la información extraida de la casilla que está ocupando
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
        Casilla antecesora = getCasilla(posicionActual);
        Casilla predecesora = getCasilla(posicionActual, direccion);
        predecesora.setAntecesora(antecesora);
    }
    
    public void recuerda(Casilla antecesora, Casilla predecesora){ //Recuerda el camino SEGURO de vuelta
        predecesora.setAntecesora(antecesora);
    }
    
    public Casilla getCasilla(int[] posicion, Direcciones direccion){ //Para mirar las casillas cercanas
        HashMap <Integer, Casilla> memoriaAux = memoria.get(posicion[0] + direccion.X);
        return memoriaAux.get(posicion[1] + direccion.Y);        
    }
    
    public Casilla getCasilla(int[] posicion){ //Para mirar la casilla ocupada
        HashMap <Integer, Casilla> memoriaAux = memoria.get(posicion[0]);
        return memoriaAux.get(posicion[1]);        
    }
    
}
