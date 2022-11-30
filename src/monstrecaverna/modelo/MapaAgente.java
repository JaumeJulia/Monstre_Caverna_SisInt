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
    Casilla[][] matrizCasillasConocidas;
    int X = 22, Y = 47, W = 28, H = 30;

    public void actualizaMapa(Memoria mem) {
        try {
            if (mem.memoria.size() > 0) {
                int max = 0;
                for (int i = 0; i < mem.memoria.size() - 1; i++) {
                    if (mem.memoria.get(i).size() > max) {
                        max = mem.memoria.get(i).size();
                    }
                }
                matrizCasillasConocidas = new Casilla[mem.memoria.size()][max];
            }
            for (int i = 0; i < matrizCasillasConocidas[0].length - 1; i++) {
                for (int j = 0; j < matrizCasillasConocidas[0].length - 1; j++) {
                    matrizCasillasConocidas[j][i] = (Casilla) mem.memoria.get(i).get(j);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Aun no se ha creado");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, null);
        for (int i = 0; i < matrizCasillasConocidas[0].length - 1; i++) {
            for (int j = 0; j < matrizCasillasConocidas[0].length - 1; j++) {
                if (matrizCasillasConocidas[i][j] != null && !matrizCasillasConocidas[i][j].isPared()) {
                    if (matrizCasillasConocidas[i][j].isSegura()) {
                        g.drawImage(map_seguro, (W * j) + X, (H * i) + Y, W, H, this);
                    } else if (!matrizCasillasConocidas[i][j].getConfusa()) {
                        if (matrizCasillasConocidas[i][j].getNivelBrisa() >= 3) {
                            g.drawImage(map_abismo, (W * j) + X, (H * i) + Y, W, H, this);
                        } else if (matrizCasillasConocidas[i][j].getNivelHedor() >= 3) {
                            g.drawImage(map_monstruo, (W * j) + X, (H * i) + Y, W, H, this);
                        } else {
                            g.drawImage(map_confuso, (W * j) + X, (H * i) + Y, W, H, this);
                        }
                    }else {
                            g.drawImage(map_confuso, (W * j) + X, (H * i) + Y, W, H, this);
                        }
                }

            }
        }
    }
}
