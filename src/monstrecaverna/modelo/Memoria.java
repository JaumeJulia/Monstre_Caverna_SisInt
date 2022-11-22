/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monstrecaverna.modelo;

import java.util.Arrays;
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
                    if(casilla.getNivelBrisa() == 0){
                        casilla.incrementaNivelHedor();
                    } else {
                        casilla.setSegura();
                    }
                }
            }
            if(estado[2]){ //En la casilla ocupada se percibe BRISA
                for(Casilla casilla : entorno){
                    if(casilla.getNivelHedor()== 0){
                        casilla.incrementaNivelBrisa();
                    } else {
                        casilla.setSegura();
                    }
                }
            }
        }
    }
    
    public void recuerda(int[] posicionActual, Direcciones direccion){ //Recuerda el camino SEGURO de vuelta
        System.out.println("La antecesora de " + Arrays.toString(posicionActual) + "Se encuentra hacia el " + direccion.toString());
        Casilla casillaOcupada = getCasilla(posicionActual);
        casillaOcupada.setAntecesora(direccion);
    }
    
    public Casilla getCasilla(int[] posicion, Direcciones direccion){ //Para mirar las casillas cercanas
        int[] aux = {posicion[0], posicion[1]};
        aux[0] += direccion.X;
        aux[1] += direccion.Y;
        return getCasilla(aux);     
    }
    
    public Casilla getCasilla(int[] posicion){ //Para mirar la casilla ocupada
        System.out.println("Recordando la casilla " + Arrays.toString(posicion));
        HashMap <Integer, Casilla> memoriaAux = memoria.get(posicion[0]);
        Casilla casilla;
        if(memoriaAux != null){
            System.out.println("Existe");
            casilla = memoriaAux.get(posicion[1]);
            if(casilla == null){
                System.out.println("No existe");
                casilla = new Casilla();
                memoriaAux.put(posicion[1], casilla);
                //casilla = ampliaMemoria(posicion);
            }
        } else {
            System.out.println("No existe");
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
