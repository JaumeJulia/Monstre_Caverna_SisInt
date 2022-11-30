/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monstrecaverna.modelo;

/**
 *
 * @author Daxmaster-PC
 */
public class NodoCasilla {
    
    private Casilla casilla;
    private NodoCasilla nextNodo;
    private int[] posicion;
    
    public NodoCasilla(){
        
    }
    
    public NodoCasilla(Casilla casilla, int posicionX, int posicionY){
        this.casilla = casilla;
        this.posicion = new int[2];
        this.posicion[0] = posicionX;
        this.posicion[1] = posicionY;
    }

    public Casilla getCasilla() {
        return casilla;
    }

    public void setCasilla(Casilla casilla) {
        this.casilla = casilla;
    }

    public NodoCasilla getNextNodo() {
        return nextNodo;
    }

    public void setNextNodo(NodoCasilla nextNodo) {
        this.nextNodo = nextNodo;
    }

    public int[] getPosicion() {
        return posicion;
    }

    public void setPosicion(int[] posicion) {
        this.posicion = posicion;
    }
    
    
}
