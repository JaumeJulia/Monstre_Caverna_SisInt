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
public class MovimientoAgenteWrapper {
    int identificador;
    Direcciones direccion;
    
    public MovimientoAgenteWrapper(int id, Direcciones direccion){
        this.identificador = id;
        this.direccion = direccion;
    }
    
    public int getIdentificador(){
        return identificador;
    }
    
    public Direcciones getDireccion(){
        return direccion;
    }
            
}
