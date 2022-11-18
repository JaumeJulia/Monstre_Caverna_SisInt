/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.modelo;

/**
 *
 * @author Daxmaster
 */
public enum Direcciones {
    NORTE(0, -1, "src/robot/modelo/amogus_NORTE.png"),
    SUR(0, 1, "src/robot/modelo/amogus_SUR.png"),
    ESTE(1, 0, "src/robot/modelo/amogus_ESTE.png"),
    OESTE(-1, 0, "src/robot/modelo/amogus_OESTE.png");
    
    public final int X;
    public final int Y;
    public final String LINKFOTO;
    
    private Direcciones(int x, int y, String linkFoto){
        X = x;
        Y = y;
        LINKFOTO = linkFoto;
    }
    
    public int[] getMotionValues(){
        int[] motionValues = new int[2];
        motionValues[0] = X;
        motionValues[1] = Y;
        return motionValues;        
    }
}
