/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monstrecaverna.modelo;

/**
 *
 * @author Daxmaster
 */
public class PosicionAgente {
    private int[] posicionActual;
    private final int[] posicionInicial;
    
    public PosicionAgente(int[] posicion){
        this.posicionInicial = new int[2];
        this.posicionInicial[0] = posicion[0];
        this.posicionInicial[1] = posicion[1];
        this.posicionActual = new int[2];
        this.posicionActual[0] = posicion[0];
        this.posicionActual[1] = posicion[1];
        
    }
    
    public int[] getPosicionInicial(){
        return posicionInicial;
    }

    public int[] getPosicionActual() {
        return posicionActual;
    }

    public void setPosicionActual(int[] posicionActual) {
        this.posicionActual = posicionActual;
    }
    
}
