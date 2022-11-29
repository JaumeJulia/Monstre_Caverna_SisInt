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
    private int visitada;
    private boolean confusa;
    
    Direcciones antecesora; //permitirá al agente recular hasta la entrada
    
    public Casilla(){
        confusa = false;
        segura = false;
        nivelHedor = 0;
        nivelBrisa = 0;
        pared = false;
        antecesora = null;
        visitada = 0;
    }

    public boolean isSegura() {
        return segura;
    }

    public void setSegura() { //Una vez la casilla se considera segura, permanecerá en este estado para siempre ya que los monstruos no se mueven
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
            if(confusa){
                this.nivelHedor += 1;
                confusa = false;
            }
        }
    }

    public int getNivelBrisa() {
        return nivelBrisa;
    }

    public void incrementaNivelBrisa() {
        if(!isSegura() && nivelBrisa < 4){
            this.nivelBrisa += 1;
            if(confusa){
                this.nivelBrisa += 1;
                confusa = false;
            }
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

    public void visitada(){
        visitada += 1;
    }
    
    public int getVisitada(){
        return visitada;
    }
    
    public void confusionMaxima(){
        if(nivelBrisa == 0 && nivelHedor == 0){
            confusa = true;
        } else if(nivelBrisa != 0){
            incrementaNivelBrisa();
            confusa = false;
        } else if(nivelHedor != 0){
            incrementaNivelHedor();
            confusa = false;
        }
    }
    
    public boolean getConfusa(){
        return confusa;
    }
    
    public void setConfusa(){
        
    }
}
