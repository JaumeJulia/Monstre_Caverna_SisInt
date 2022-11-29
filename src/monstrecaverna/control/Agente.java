/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monstrecaverna.control;

import java.util.Arrays;
import java.util.Random;
import monstrecaverna.modelo.Casilla;
import monstrecaverna.modelo.Direcciones;
import monstrecaverna.modelo.Memoria;
import monstrecaverna.modelo.MovimientoAgenteWrapper;
import monstrecaverna.vista.Vista;

/**
 *
 * @author Daxmaster
 */
public class Agente {
    
    private Vista vista;
    private Direcciones direcciones[] = {Direcciones.NORTE, Direcciones.ESTE, Direcciones.SUR, Direcciones.OESTE};
    private int direccionActual;
    private int rotacion; // +1 cuando se rota en sentido horario, -1 cuando es antihorario
    private int[] posicionActual = new int[2];
    private boolean[] estadoCasillaActual; 
    private Memoria memoria;
    private boolean saliendo;
    private final int identificador;
    private int tesoros;
    
    public Agente(int identificador, int rotacion, Vista vista){
        this.identificador = identificador;
        Random ran = new Random();
        direccionActual = ran.nextInt(20) % 4;
        this.rotacion = rotacion;
        posicionActual[0] = 0;
        posicionActual[1] = 0;
        memoria = new Memoria(posicionActual);
        this.vista = vista;
        saliendo = false;
        tesoros = 0;
    }
    
    private Casilla[] reconocerEntorno(){
        System.out.println("Iba hacia el:" + direcciones[direccionActual].toString());
        Casilla[] entorno = new Casilla[4];
        rotar(-rotacion);
        for(int i = 0 ; i < 4 ; i++){
            System.out.println("Miro al:" + direcciones[direccionActual].toString());
            entorno[i] = memoria.getCasilla(posicionActual, direcciones[direccionActual]);
            System.out.println("Posicion actual del agente: " + Arrays.toString(posicionActual));
            int[] casillaCercana = {posicionActual[0] + direcciones[direccionActual].X, posicionActual[1] + direcciones[direccionActual].Y};
            System.out.println("Investigo la casilla " + Arrays.toString(casillaCercana));
            if(vista.getCasilla(identificador, casillaCercana).isPared()){
                System.out.println("PARED!");
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
    
    public MovimientoAgenteWrapper moverAgente(){
        System.out.println("------------------------ MOVIMIENTO AGENTE ------------------------");
        memoria.getCasilla(posicionActual).visitada();
        if(vista.getCantidadTesoro() == 0){
            saliendo = true;
        }
        
        System.out.println("comprobacion de que se ha guardado: " + memoria.getCasilla(posicionActual).getVisitada());
        estadoCasillaActual = vista.getCasilla(identificador, posicionActual).getEstado(); //esto deberia devolver el array de booleanas que dice si hay hedor, brisa o resplandor
        if(estadoCasillaActual[2]){
            //coge el tesoro
            if(vista.cogerTesoro(identificador, vista.getCasilla(identificador, posicionActual))){
                tesoros += 1;
            }
            if(vista.getCantidadTesoro() == 0 || !vista.getAvaricioso()){
                saliendo = true; //se marca como que está saliendo, por lo que solo va a buscar la casilla antecesora en lugar de seguir explorando
            }
        }
        if(saliendo){
            if(posicionActual[0] == 0 && posicionActual[1] == 0){
                return null;
            }
            Direcciones direccion = memoria.getCasilla(posicionActual).getAntecesora();
            posicionActual[0] += direccion.X;
            posicionActual[1] += direccion.Y;
            MovimientoAgenteWrapper maw = new MovimientoAgenteWrapper(identificador, direccion);
            return maw;
            //return direccion;//se dirige a la casilla antecesora
        }
        Casilla[] entorno = reconocerEntorno();
        
        int visitaCasillaSeleccionada = 999;
        int marcadorDireccion = direccionActual;
        rotar(-rotacion);
        System.out.println("Estoy mirando la casilla del " + direcciones[direccionActual]);
        for(Casilla casilla: entorno){ //al salir del for, la rotación dará como resultado que el agente mire hacia atrás, siendo este el movimiento por defecto incluso cuando todo falla. 
            System.out.println("es una pared? " + casilla.isPared());
            System.out.println("es segura? " + casilla.isSegura());
            System.out.println("Cuantas veces ha sido visitada? " + casilla.getVisitada());
            if(casilla.isSegura() && !casilla.isPared() && casilla.getVisitada() < visitaCasillaSeleccionada){
                visitaCasillaSeleccionada = casilla.getVisitada();
                marcadorDireccion = direccionActual;
            }
            rotar(rotacion);
        }
        
        direccionActual = marcadorDireccion;
        
        posicionActual[0] += direcciones[direccionActual].X;
        posicionActual[1] += direcciones[direccionActual].Y;
        if(memoria.getCasilla(posicionActual).getVisitada() < 1){
            memoria.recuerda(posicionActual, inversorDireccion(direcciones[direccionActual]));
        }
        System.out.println("Me dirijo al:" + direcciones[direccionActual].toString());
        System.out.println("------------------------ LISTO! ------------------------");
        MovimientoAgenteWrapper maw = new MovimientoAgenteWrapper(identificador, direcciones[direccionActual]);
        return maw;
        //return direcciones[direccionActual];
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
    
    public int getTesoros(){
        return tesoros;
    }
    
    public int getIdentificador(){
        return identificador;
    }
}
