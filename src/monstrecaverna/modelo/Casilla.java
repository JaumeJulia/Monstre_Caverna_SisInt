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
public class Casilla {
    private boolean segura;
    private int nivelHedor;
    private int nivelBrisa;
    private boolean pared;
    
    Direcciones antecesora; //permitir√° al agente recular hasta la entrada
    
    public Casilla(){
        segura = false;
        nivelHedor = 0;
        nivelBrisa = 0;
        pared = false;
        antecesora = null;
    }

    public boolean isSegura() {
        return segura;
    }

    public void setSegura() { //Una vez la casilla se considera segura, permanecer√° en este estado para siempre ya que los monstruos no se mueven
        this.segura = true;
        nivelHedor = 0;
        nivelBrisa = 0;
    }

    public int getNivelHedor() {
        return nivelHedor;
    }

    public void incrementaNivelHedor() {
        if(!isSegura() && nivelHedor < 4){
            this.nivelHedor += 1;
        }
    }

    public int getNivelBrisa() {
        return nivelBrisa;
    }

    public void incrementaNivelBrisa() {
        if(!isSegura() && nivelBrisa < 4){
            this.nivelBrisa += 1;
        }
    }

    public boolean isPared() {
        return pared;
    }

    public void setPared(boolean pared) {
        this.pared = pared;
    }

    public Direcciones getAntecesora() {
        return antecesora;
    }

    public void setAntecesora(Direcciones antecesora) {
        this.antecesora = antecesora;
    }    
}
