package monstrecaverna.modelo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;

/**
 *
 * @author bertu
 */
public class MapaAgente extends JPanel {

    JPanel gamePanel = new JPanel();
    Image bgImage = Toolkit.getDefaultToolkit().createImage("src/monstrecaverna/modelo/mapa.jpg");
    Image map_monstruo = Toolkit.getDefaultToolkit().createImage("src/monstrecaverna/modelo/monstruo.png");
    Image map_abismo = Toolkit.getDefaultToolkit().createImage("src/monstrecaverna/modelo/abismo.png");
    Image map_confuso = Toolkit.getDefaultToolkit().createImage("src/monstrecaverna/modelo/pregunta.png");
    Image map_seguro = Toolkit.getDefaultToolkit().createImage("src/monstrecaverna/modelo/segura.png");
    Image map_agente = Toolkit.getDefaultToolkit().createImage("src/monstrecaverna/modelo/amogus_ESTE.png");
    //Casilla[][] matrizCasillasConocidas;
    NodoCasilla nodosAbiertos;
    int X = 27 * 9, Y = 28 * 6, W = 28, H = 30;
    int limiteEste = 480, limiteSur = 500, limiteNorte = 28, limiteOeste = 27;
    
    
    public NodoCasilla getNodosAbiertos(){
        return nodosAbiertos;
    }
    
    public void setX(int X){
        this.X = X;
        System.out.println("***********************************************************************************************");
        System.out.println("X ahora vale " + this.X);
    }
    
    public void setY(int Y){
        this.Y = Y;
        System.out.println("***********************************************************************************************");
        System.out.println("Y ahora vale " + this.Y);
    }
    
    public void actualizaMapa(Casilla[] casillas, int[] posiciones){
        int i = 0;
        int aux = 0;
        if(nodosAbiertos == null){
            nodosAbiertos = new NodoCasilla(casillas[0], posiciones[0], posiciones[1]);
            i++;
            aux += 2;
        }
        for(; i < casillas.length; i++){
            NodoCasilla lista = nodosAbiertos;
            int[] posicionesAux = new int[2];
            posicionesAux[0] = posiciones[aux];
            posicionesAux[1] = posiciones[aux + 1];
            while(lista.getNextNodo() != null){
                if(lista.getPosicion() == posicionesAux){
                    aux += 2;
                    continue;
                }
                lista = lista.getNextNodo();
            }
            if(lista.getPosicion() != posicionesAux){
                NodoCasilla nodoCasilla = new NodoCasilla(casillas[i], posiciones[aux], posiciones[aux + 1]);
                lista.setNextNodo(nodoCasilla);
                //lista = lista.getNextNodo();
            }
            aux += 2;
        }
        repaint();
    }
    
//    public void actualizaMapa(Memoria mem) {
//        try {
//            if (mem.memoria.size() > 0) {
//                int max = 0;
//                for (int i = 0; i < mem.memoria.size() - 1; i++) {
//                    if (mem.memoria.get(i).size() > max) {
//                        max = mem.memoria.get(i).size();
//                    }
//                }
//                matrizCasillasConocidas = new Casilla[mem.memoria.size()][max];
//            }
//            for (int i = 0; i < matrizCasillasConocidas[0].length - 1; i++) {
//                for (int j = 0; j < matrizCasillasConocidas[0].length - 1; j++) {
//                    matrizCasillasConocidas[j][i] = (Casilla) mem.memoria.get(i).get(j);
//                }
//            }
//        } catch (NullPointerException e) {
//            System.out.println("Aun no se ha creado");
//        }
//    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, null);
        NodoCasilla nodoCasilla = getNodosAbiertos();
        while(nodoCasilla != null){
            int j = nodoCasilla.getPosicion()[0];
            int i = nodoCasilla.getPosicion()[1];
            if(!nodoCasilla.getCasilla().isPared()){
                if(nodoCasilla.getCasilla().isSegura()){
                   g.drawImage(map_seguro, (W * j) + X, (H * i) + Y, W, H, this); 
                } else if(!nodoCasilla.getCasilla().getConfusa()){
                    if (nodoCasilla.getCasilla().getNivelBrisa() >= 3) {
                        g.drawImage(map_abismo, (W * j) + X, (H * i) + Y, W, H, this);
                    } else if (nodoCasilla.getCasilla().getNivelHedor() >= 3) {
                        g.drawImage(map_monstruo, (W * j) + X, (H * i) + Y, W, H, this);
                    } else {
                        g.drawImage(map_confuso, (W * j) + X, (H * i) + Y, W, H, this);
                    }
                } else {
                    g.drawImage(map_confuso, (W * j) + X, (H * i) + Y, W, H, this);
                }
            }
            nodoCasilla = nodoCasilla.getNextNodo();
            if((W * j) + X  >= limiteEste){
                setX(X - limiteOeste);
            } else if((W * j + X) <= limiteOeste){
                setX(X + limiteOeste);
            }
            if((H * i + Y) >= limiteSur){                
                setY(Y - limiteNorte);
            } else if((H * i + Y)<= limiteNorte){                
                setY(Y + limiteNorte);
            }
        }
        
        
//        
//        for (int i = 0; i < matrizCasillasConocidas[0].length - 1; i++) {
//            for (int j = 0; j < matrizCasillasConocidas[0].length - 1; j++) {
//                if (matrizCasillasConocidas[i][j] != null && !matrizCasillasConocidas[i][j].isPared()) {
//                    if (matrizCasillasConocidas[i][j].isSegura()) {
//                        g.drawImage(map_seguro, (W * j) + X, (H * i) + Y, W, H, this);
//                    } else if (!matrizCasillasConocidas[i][j].getConfusa()) {
//                        if (matrizCasillasConocidas[i][j].getNivelBrisa() >= 3) {
//                            g.drawImage(map_abismo, (W * j) + X, (H * i) + Y, W, H, this);
//                        } else if (matrizCasillasConocidas[i][j].getNivelHedor() >= 3) {
//                            g.drawImage(map_monstruo, (W * j) + X, (H * i) + Y, W, H, this);
//                        } else {
//                            g.drawImage(map_confuso, (W * j) + X, (H * i) + Y, W, H, this);
//                        }
//                    }else {
//                            g.drawImage(map_confuso, (W * j) + X, (H * i) + Y, W, H, this);
//                        }
//                }
//
//            }
//        }
    }
}
